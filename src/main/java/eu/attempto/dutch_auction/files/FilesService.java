package eu.attempto.dutch_auction.files;

import eu.attempto.dutch_auction.exceptions.BadRequestException;
import eu.attempto.dutch_auction.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FilesService {
  private final Logger logger = LoggerFactory.getLogger(FilesService.class);

  @Value("${upload.path}")
  private String uploadPath;

  @Value("${upload.host}")
  private String host;

  public String saveImage(MultipartFile image) {
    validateImage(image);

    try {
      var uploadDir = new File(uploadPath);
      if (!uploadDir.exists()) {
        uploadDir.mkdirs(); // Use mkdirs() to create parent directories if necessary
      }

      var originalFileName = image.getOriginalFilename();
      if (originalFileName == null) {
        throw new InternalServerException("Error while writing a file");
      }

      var imageName = UUID.randomUUID() + getFileExtension(originalFileName);
      var imagePath = uploadDir.getAbsolutePath() + File.separator + imageName;
      image.transferTo(new File(imagePath));

      return host + imageName;
    } catch (IOException e) {
      logger.error(e.getMessage());
      throw new InternalServerException("Error while writing a file");
    }
  }

  private void validateImage(MultipartFile image) {
    if (image == null || image.isEmpty()) {
      throw new BadRequestException("Auction requires an image");
    }

    var allowedTypes = Arrays.asList("image/jpeg", "image/png");
    if (!allowedTypes.contains(image.getContentType())) {
      throw new BadRequestException(
          String.format("Invalid file type. Only %s are allowed", allowedTypes));
    }
  }

  private String getFileExtension(String fileName) {
    int dotIndex = fileName.lastIndexOf(".");
    return (dotIndex != -1) ? fileName.substring(dotIndex) : "";
  }
}
