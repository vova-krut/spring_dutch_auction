package eu.attempto.dutch_auction.files;

import eu.attempto.dutch_auction.exceptions.BadRequestException;
import eu.attempto.dutch_auction.exceptions.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Component
public class FilesService {
  @Value("${upload.path}")
  private String uploadPath;

  @Value("${upload.host}")
  private String host;

  public String saveImage(MultipartFile image) {
    validateImage(image);

    try {
      var uploadDir = new File(uploadPath);
      System.out.println(uploadDir.getAbsolutePath());
      if (!uploadDir.exists()) {
        uploadDir.mkdirs(); // Use mkdirs() to create parent directories if necessary
      }

      var imageName = UUID.randomUUID() + getFileExtension(image.getOriginalFilename());
      var imagePath = uploadDir.getAbsolutePath() + File.separator + imageName;
      image.transferTo(new File(imagePath));

      System.out.println(imagePath);
      return host + imageName;
    } catch (IOException e) {
      System.out.println(e.getMessage());
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
