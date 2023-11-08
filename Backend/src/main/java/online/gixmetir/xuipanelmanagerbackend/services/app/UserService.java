package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
import online.gixmetir.xuipanelmanagerbackend.exceptions.CustomException;
import online.gixmetir.xuipanelmanagerbackend.exceptions.DuplicateException;
import online.gixmetir.xuipanelmanagerbackend.exceptions.ForbiddenException;
import online.gixmetir.xuipanelmanagerbackend.exceptions.UsernameOrPasswordWrongException;
import online.gixmetir.xuipanelmanagerbackend.filters.UserFilter;
import online.gixmetir.xuipanelmanagerbackend.models.*;
import online.gixmetir.xuipanelmanagerbackend.repositories.*;
import online.gixmetir.xuipanelmanagerbackend.security.jwt.JwtService;
import online.gixmetir.xuipanelmanagerbackend.utils.Helper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final AuthenticationService authenticationService;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationRepository authenticationRepository;
    private final UserRenewLogRepository userRenewLogRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionRenewLogRepository subscriptionLogRepository;

    public UserService(UserRepository repository, AuthenticationService authenticationService, PasswordEncoder encoder, JwtService jwtService, AuthenticationRepository authenticationRepository, UserRenewLogRepository userRenewLogRepository, SubscriptionRepository subscriptionRepository, SubscriptionRenewLogRepository subscriptionLogRepository) {
        this.repository = repository;
        this.authenticationService = authenticationService;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authenticationRepository = authenticationRepository;
        this.userRenewLogRepository = userRenewLogRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionLogRepository = subscriptionLogRepository;
    }

    public Page<UserDto> getAll(UserFilter filter, Pageable pageable) {
        return repository.findAll(filter, pageable).map(UserDto::new);
    }

    public UserDto create(UserRequest request) throws Exception {
        if (authenticationRepository.findByUsername(request.getUsername()).orElse(null) != null) {
            throw new DuplicateException("User with username: " + request.getUsername() + " already exists");
        }
        UserEntity userEntity = request.toEntity();
        userEntity.setStartDateTime(LocalDateTime.now());
        if (!userEntity.getIsIndefiniteExpirationTime())
            userEntity.setExpirationDateTime(LocalDateTime.now().plusDays(request.getPeriodLength()));
        repository.save(userEntity);
        AuthenticationEntity authenticationEntity = AuthenticationEntity.builder()
                .password(encoder.encode(request.getPassword()))
                .username(request.getUsername())
                .userId(userEntity.getId())
                .build();

        authenticationRepository.save(authenticationEntity);
        userEntity.setAuthenticationId(authenticationEntity.getId());

        repository.save(userEntity);
        createRenewLog(userEntity.getId(), request, UserLogType.CREATE);
        return new UserDto(userEntity);
    }

    public UserDto update(Long id, UserRequest request) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + "not found"));
        entity = request.toEntity(entity);
        repository.save(entity);
        return new UserDto(entity);
    }

    public void delete(Long id) {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + "not found"));
        List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAllByUserId(entity.getId());
        if (!subscriptionEntities.isEmpty())
            throw new ForbiddenException("you can't delete user witch has subscriptions");
        authenticationRepository.deleteById(entity.getAuthenticationId());
        repository.delete(entity);
    }

    @Transactional
    public AuthDto login(LoginModel loginModel) throws UsernameOrPasswordWrongException {
        UserEntity entity = authenticationService.loadUserByUsername(loginModel.getUsername());
        if (encoder.matches(loginModel.getPassword(), entity.getPassword())) {
            return AuthDto.builder()
                    .token(jwtService.generateToken(entity))
                    .role(entity.getRole())
                    .build();

        } else {
            throw new UsernameOrPasswordWrongException("username or password is wrong");
        }
    }


    public UserDto changeStatus(Boolean newStatus, Long id) throws Exception {
        UserEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + "not found"));
        entity.setEnabled(newStatus);
        repository.save(entity);
        return new UserDto(entity);
    }

    public void changePassword(ChangePasswordModel changePasswordModel) throws Exception {
        UserEntity entity = new Helper().getUserFromContext();
        AuthenticationEntity authenticationEntity = null;
        if (changePasswordModel.getUserId() != null && changePasswordModel.getUserId() > 0) {
            if (entity.getRole() == Role.Admin) {
                authenticationEntity = authenticationRepository.findByUserId(changePasswordModel.getUserId()).orElseThrow(() -> new EntityNotFoundException("user not found"));
            } else {
                throw new ForbiddenException("only admin can change password");
            }
        } else {
            if (encoder.matches(changePasswordModel.getOldPassword(), entity.getPassword())) {
                authenticationEntity = authenticationRepository.findByUserId(entity.getId()).orElseThrow(() -> new EntityNotFoundException("user not found"));
            } else throw new IllegalArgumentException("old password doesnt matched.");
        }
        authenticationEntity.setPassword(encoder.encode(changePasswordModel.getNewPassword()));
        authenticationRepository.save(authenticationEntity);
    }

    public void renew(Long id, UserRequest request) {
        Helper helper = new Helper();
        UserEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + "not found"));
        if (entity.getRole() == Role.SuperCustomer) {
            entity.setPricePerGb(request.getPricePerGb());
            if (request.getPeriodLength() != null && request.getPeriodLength() > 0)
                if (entity.getExpirationDateTime() == null || entity.getExpirationDateTime().isBefore(LocalDateTime.now())) {
                    entity.setExpirationDateTime(LocalDateTime.now().plusDays(request.getPeriodLength()));
                } else if (entity.getExpirationDateTime().isAfter(LocalDateTime.now()))
                    entity.setExpirationDateTime(entity.getExpirationDateTime().plusDays(request.getPeriodLength()));
            if (request.getTotalFlow() != null) {
                if (entity.getTotalFlow() == null)
                    entity.setTotalFlow(helper.GBToByte(request.getTotalFlow()));
                else
                    entity.setTotalFlow(entity.getTotalFlow() + helper.GBToByte(request.getTotalFlow()));
            }
            createRenewLog(entity.getId(), request, UserLogType.RENEW);
            repository.save(entity);
        }
//        long differenceBetweenTotalUsedAndTotalFlow = entity.getTotalFlow() - entity.getTotalUsed();
//        // if all of total flow used this section add renew amount to total flow
//        // else  all total flow not complete used this section add renew amount to total used
//        if (differenceBetweenTotalUsedAndTotalFlow <= 0)
//            entity.setTotalFlow(entity.getTotalFlow() + helper.GBToByte(request.getTotalFlow()));
//        else
//            entity.setTotalUsed(entity.getTotalUsed() + helper.GBToByte(request.getTotalFlow()));
//
//        // Calculate the duration between the two LocalDateTime instances
//        Duration duration = Duration.between(entity.getStartDateTime(), LocalDateTime.now());
//        int days = (int) duration.toDays();
//
//        if (entity.getPeriodLength() <= days)
//            entity.setPeriodLength(entity.getPeriodLength() + request.getPeriodLength());
//        else
//            entity.setPeriodLength(days + request.getPeriodLength());
//
//        entity.setExpirationDateTime(entity.getStartDateTime().plusDays(entity.getPeriodLength()));
//        repository.save(entity);
//
//        UserRenewLogEntity userRenewLogEntity = UserRenewLogEntity.builder()
//                .userId(entity.getId())
//                .periodLength(request.getPeriodLength())
//                .totalFlow(request.getTotalFlow())
//                .build();
//        userRenewLogRepository.save(userRenewLogEntity);
//        return new UserDto(entity);
    }

    private void createRenewLog(Long userId, UserRequest request, UserLogType logType) {
        UserRenewLogEntity userRenewLogEntity = UserRenewLogEntity.builder()
                .userId(userId)
                .totalFlow(request.getTotalFlow())
                .periodLength(request.getPeriodLength())
                .build();
        userRenewLogRepository.save(userRenewLogEntity);

    }

    public ResponseEntity<InputStreamResource> getUserReport(Long userId) throws FileNotFoundException {
        List<SubscriptionEntity> subscriptionEntities = subscriptionRepository.findAllByUserId(userId);
//        double balance = 0;
        List<Long> ids = subscriptionEntities.stream().map(SubscriptionEntity::getId).toList();
        List<SubscriptionLogEntity> allBySubscriptionIdInAndMarkAsPaid = subscriptionLogRepository.findAllBySubscriptionIdInAndMarkAsPaid(ids, false);
//        for (SubscriptionLogEntity subscriptionLogEntity : allBySubscriptionIdInAndMarkAsPaid) {
//            balance += subscriptionLogEntity.getPrice();
//        }

        String filePath = createCsvFile(allBySubscriptionIdInAndMarkAsPaid);
        String fileName = new File(filePath).getName();

        // Open the file as an InputStream
        InputStream stream = new FileInputStream(filePath);

        // Set the HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Wrap the InputStream in an InputStreamResource
        InputStreamResource resource = new InputStreamResource(stream);

        // Return the ResponseEntity with the InputStreamResource and headers
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(new File(filePath).length())
                .body(resource);
//        return createCsvFile(allBySubscriptionIdInAndMarkAsPaid);
    }

    private String createCsvFile(List<SubscriptionLogEntity> logs) {

        String csvFileName = "./report.csv";

        try {
            // Remove the existing file if it exists
            File existingFile = new File(csvFileName);
            if (existingFile.exists()) {
                existingFile.delete();
            }
            // Create a FileWriter and specify the CSV file name
            FileWriter csvWriter = new FileWriter(csvFileName);


            csvWriter.append("حجم");
            csvWriter.append(" , ");
            csvWriter.append("طول بازه زمانی");
            csvWriter.append(" , ");
            csvWriter.append("تاریخ");
            csvWriter.append(" , ");
            csvWriter.append("مبلغ");
            csvWriter.append("\n");


            BigDecimal sumOfPrices = new BigDecimal(0);
            Helper helper = new Helper();
            // Write the data to the CSV file
            for (SubscriptionLogEntity log : logs) {
                csvWriter.append(String.valueOf(helper.byteToGB(log.getTotalFlow()))).append(" GB");
                csvWriter.append(" , ");
                csvWriter.append(log.getPeriodLength().toString()).append(" روز ");
                csvWriter.append(" , ");
                csvWriter.append(log.getCreateDate().toLocalDate().toString());
                csvWriter.append(" , ");
                csvWriter.append(log.getPrice().toString());
                csvWriter.append("\n");
                sumOfPrices = sumOfPrices.add(BigDecimal.valueOf(log.getPrice()));
            }
            csvWriter.append("\n");
            csvWriter.append(sumOfPrices.toString());
            csvWriter.append(" تومان ");
            csvWriter.append(" , ");
            csvWriter.append("جمع کل : ");


            // Close the FileWriter
            csvWriter.close();
            return csvFileName;

        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }
    }

    public UserSelfDetails getSelfDetails() throws Exception {
        Helper helper = new Helper();
        UserEntity entity = helper.getUserFromContext();
        UserSelfDetails userSelfDetails = new UserSelfDetails();
        userSelfDetails.setFirstName(entity.getFirstName());
        userSelfDetails.setLastName(entity.getLastName());
        userSelfDetails.setIsIndefiniteExpirationTime(entity.getIsIndefiniteExpirationTime());
        userSelfDetails.setIsIndefiniteFlow(entity.getIsIndefiniteFlow());
        userSelfDetails.setStartDateTime(entity.getStartDateTime());
        userSelfDetails.setExpirationDateTime(entity.getExpirationDateTime());
        userSelfDetails.setTotalFlow(helper.byteToGB(entity.getTotalFlow()));
        userSelfDetails.setTotalUsed(helper.byteToGB(entity.getTotalUsed()));
//        if (entity.getRole() == Role.SuperCustomer) {
//            Long debtAmount = (long) (helper.byteToGB(entity.getTotalUsed() == null ? 0 : entity.getTotalUsed()) * (entity.getPricePerGb() == null ? 0 : entity.getPricePerGb()) - (entity.getPayedAmount() == null ? 0 : entity.getPayedAmount()));
//            userSelfDetails.setDebtAmount(debtAmount);
//        } else if (entity.getRole() == Role.Customer) {
        List<SubscriptionLogEntity> subscriptionLogEntities = subscriptionLogRepository.findAllBySubscriptionUserIdAndMarkAsPaid(entity.getId(), false);
        double debitAmount = 0;
        for (SubscriptionLogEntity log : subscriptionLogEntities
        ) {
            debitAmount += log.getPrice();
        }
        userSelfDetails.setDebtAmount((long) debitAmount);
//        }
        return userSelfDetails;
    }
}
