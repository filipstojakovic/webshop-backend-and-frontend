package org.etf.webshopbackend;

import org.etf.webshopbackend.service.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.util.List;

@SpringBootApplication
public class WebshopBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebshopBackendApplication.class, args);
  }

  @Bean
  public CommandLineRunner loadData(FileService fileService) {
    return args -> {
      Path avatarsDirPath = fileService.getAvatarDirPath();
      Path productsDirPath = fileService.getProductsDirPath();
      fileService.createDirIfNotExists(List.of(avatarsDirPath, productsDirPath));
    };
  }
}
