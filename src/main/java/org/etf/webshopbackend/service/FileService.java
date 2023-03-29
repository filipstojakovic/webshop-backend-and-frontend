package org.etf.webshopbackend.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.etf.webshopbackend.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

@Slf4j
@Getter
@Service
public class FileService {

  @Value("${app.imagesDir}")
  private String imagesDir;
  @Value("${app.image.avatarDir}")
  private String avatarsDirString;
  @Value("${app.image.productsDir}")
  private String productsDirString;

  public static final String ENCODED_IMAGE_PREFIX = "data:image/png;base64,";

  private static final List<String> IMAGE_TYPES = Arrays.asList(
      IMAGE_JPEG.getMimeType(),
      IMAGE_PNG.getMimeType());

  public String saveImageToFilesystem(Path path, String username, MultipartFile multipartFile) throws IOException, URISyntaxException {
    isFileEmpty(multipartFile);
    isFileImage(multipartFile);
    String parentDir = path.toFile().getAbsolutePath();
    Path filePath = Paths.get(parentDir, username + "-" + multipartFile.getOriginalFilename());

    try (OutputStream os = new FileOutputStream(filePath.toFile())) {
      os.write(multipartFile.getBytes());
    }

    return filePath.toString();
  }

  private void isFileEmpty(MultipartFile file) {
    if (file.isEmpty()) {
      log.error("Cannot upload empty file [ " + file.getSize() + "]");
      throw new BadRequestException("Cannot upload empty file [ " + file.getSize() + "]");
    }
  }

  private void isFileImage(MultipartFile file) {
    if (!IMAGE_TYPES.contains(file.getContentType())) {
      log.error("File must be an image [" + file.getContentType() + "]");
      throw new BadRequestException("File must be an image [" + file.getContentType() + "]");
    }
  }

  public void createDirIfNotExists(List<Path> paths) {
    paths.forEach((path) -> {
      if (!Files.exists(path)) {
        try {
          Files.createDirectories(path);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  public void deleteFile(String path) throws IOException {
    Path filePath = Paths.get(path);
    if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
      Files.delete(filePath);
    }
  }

  public Path getAvatarDirPath() {
    return Paths.get(getRootDirPath(), getImagesDir(), getAvatarsDirString());
  }

  public Path getProductsDirPath() {
    return Paths.get(getRootDirPath(), getImagesDir(), getProductsDirString());
  }

  public String getRootDirPath() {
    return System.getProperty("user.dir");
  }

  public String downloadUserProfileImage(String path) throws IOException {
    if (path == null) {
      return null;
    }
    Path filePath = Paths.get(path);
    byte[] imageBytes = Files.readAllBytes(filePath);
    //TODO: maybe encoded image prefix handle on frontend
    return ENCODED_IMAGE_PREFIX + Base64.getEncoder().encodeToString(imageBytes);
  }
}
