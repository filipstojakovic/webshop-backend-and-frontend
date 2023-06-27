package org.etf.webshopbackend;

import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.service.FileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@SpringBootApplication
public class WebshopBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebshopBackendApplication.class, args);
  }

  @Bean
  public CommandLineRunner loadData(FileService fileService) {
    return args -> {
      log.info("Backend started");
      Path avatarsDirPath = fileService.getAvatarDirPath();
      Path productsDirPath = fileService.getProductsDirPath();
      fileService.createDirIfNotExists(List.of(avatarsDirPath, productsDirPath));
    };
  }
}
