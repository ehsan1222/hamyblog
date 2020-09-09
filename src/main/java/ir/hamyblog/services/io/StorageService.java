package ir.hamyblog.services.io;

import ir.hamyblog.exceptions.FileException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Log4j2
public class StorageService {

    private final String BASE_PATH = new File(".")
            .getAbsolutePath().concat(File.separator).concat("files").concat(File.separator);

    public UUID store(MultipartFile file) {
        UUID filename = UUID.randomUUID();
        try {
            String extension = getFileExtension(file.getOriginalFilename());
            if (!(extension.contains("jpg") || extension.contains("jpeg") || extension.contains("png"))) {
                throw new FileException("invalid file extension, valid extensions = {jpg, jpeg, png} but send " + extension);
            }

            String target = BASE_PATH.concat(filename.toString()).concat("." + extension);

            createDirectoryIfNotExists(BASE_PATH);

            Files.copy(file.getInputStream(), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);

        }catch (NullPointerException | IOException e) {
            log.warn("invalid file, error={}", e.getMessage());
            throw new FileException();
        }
        return filename;
    }


    public File get(UUID uid) {
        createDirectoryIfNotExists(BASE_PATH);
        try {
            Optional<File> fileOptional = Stream.of(Objects.requireNonNull(new File(BASE_PATH).listFiles()))
                    .filter(file -> file.getName().contains(uid.toString()))
                    .findFirst();
            if (fileOptional.isPresent()) {
                return fileOptional.get();
            }
        }catch (NullPointerException e) {
            throw new RuntimeException();
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        int lastIndexOf = originalFilename.lastIndexOf(".");
        if (lastIndexOf > 0) {
            return originalFilename.substring(lastIndexOf + 1);
        }
        return "";
    }

    private void createDirectoryIfNotExists(String base_path) {
        if (!new File(base_path).isDirectory()) {
            new File(base_path).mkdirs();
        }
    }
}
