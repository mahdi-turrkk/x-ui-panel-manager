package online.gixmetir.xuipanelmanagerbackend.services.app;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import online.gixmetir.xuipanelmanagerbackend.clients.models.LoginModel;
import online.gixmetir.xuipanelmanagerbackend.entities.*;
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
import java.time.Duration;
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
            throw new Exception("User with username: " + request.getUsername() + " already exists");
        }
        UserEntity userEntity = request.toEntity();
        userEntity.setStartDateTime(LocalDateTime.now());
        if (!userEntity.getIsIndefiniteExpirationTime())
            userEntity.setExpirationDateTime(LocalDateTime.now().plusDays(userEntity.getPeriodLength()));
        repository.save(userEntity);
        AuthenticationEntity authenticationEntity = AuthenticationEntity.builder()
                .password(encoder.encode(request.getPassword()))
                .username(request.getUsername())
                .userId(userEntity.getId())
                .build();
        authenticationRepository.save(authenticationEntity);
        userEntity.setAuthenticationId(authenticationEntity.getId());
        repository.save(userEntity);
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
        repository.delete(entity);
    }

    @Transactional
    public AuthDto login(LoginModel loginModel) {
        UserEntity entity = authenticationService.loadUserByUsername(loginModel.getUsername());
        if (encoder.matches(loginModel.getPassword(), entity.getPassword())) {
            return AuthDto.builder()
                    .token(jwtService.generateToken(entity))
                    .role(entity.getRole())
                    .build();

        } else {
            throw new IllegalArgumentException("username or password is wrong");
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
                throw new Exception("only admin can change password");
            }
        } else {
            if (encoder.matches(changePasswordModel.getOldPassword(), entity.getPassword())) {
                authenticationEntity = authenticationRepository.findByUserId(entity.getId()).orElseThrow(() -> new EntityNotFoundException("user not found"));
            } else throw new Exception("old password doesnt.");
        }
        authenticationEntity.setPassword(encoder.encode(changePasswordModel.getNewPassword()));
        authenticationRepository.save(authenticationEntity);
    }

    public UserDto renew(Long id, UserRequest request) {
        Helper helper = new Helper();
        UserEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + "not found"));
        long differenceBetweenTotalUsedAndTotalFlow = entity.getTotalFlow() - entity.getTotalUsed();
        // if all of total flow used this section add renew amount to total flow
        // else  all total flow not complete used this section add renew amount to total used
        if (differenceBetweenTotalUsedAndTotalFlow <= 0)
            entity.setTotalFlow(entity.getTotalFlow() + helper.GBToByte(request.getTotalFlow()));
        else
            entity.setTotalUsed(entity.getTotalUsed() + helper.GBToByte(request.getTotalFlow()));

        // Calculate the duration between the two LocalDateTime instances
        Duration duration = Duration.between(entity.getStartDateTime(), LocalDateTime.now());
        int days = (int) duration.toDays();

        if (entity.getPeriodLength() <= days)
            entity.setPeriodLength(entity.getPeriodLength() + request.getPeriodLength());
        else
            entity.setPeriodLength(days + request.getPeriodLength());

        entity.setExpirationDateTime(entity.getStartDateTime().plusDays(entity.getPeriodLength()));
        repository.save(entity);

        UserRenewLogEntity userRenewLogEntity = UserRenewLogEntity.builder()
                .userId(entity.getId())
                .periodLength(request.getPeriodLength())
                .totalFlow(request.getTotalFlow())
                .build();
        userRenewLogRepository.save(userRenewLogEntity);
        return new UserDto(entity);
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


            Double sumOfPrices = 0D;
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
                sumOfPrices += log.getPrice();
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
            throw new RuntimeException(e);
        }
    }

}
