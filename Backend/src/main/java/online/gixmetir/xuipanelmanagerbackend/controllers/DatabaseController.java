package online.gixmetir.xuipanelmanagerbackend.controllers;

import online.gixmetir.xuipanelmanagerbackend.XUiPanelManagerBackEndApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/v1/databases")
public class DatabaseController {
    private final ApplicationContext applicationContext;

    public DatabaseController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
        String filePath = "./LocalDB.db";
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
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(MultipartFile file) {
        try {
            // Check if the uploaded file is not empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file.");
            }

            // Get the file's original name
            String fileName = "LocalDB.db";

            // Create a target path for the uploaded file
            Path targetPath = Path.of("./", fileName);

            // Copy the uploaded file to the target path
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            restartApplication();
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while uploading the file.");
        }
    }

    private void restartApplication() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Delay the restart to allow the response to be sent
                SpringApplication.exit(applicationContext, () -> 0);
                SpringApplication.run(XUiPanelManagerBackEndApplication.class);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        thread.setDaemon(false);
        thread.start();
    }
}
