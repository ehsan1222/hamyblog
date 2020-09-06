package ir.hamyblog.services.io;

import ir.hamyblog.exceptions.FileException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Log4j2
public class StorageService {

    private final String BASE_PATH = new File(".")
            .getAbsolutePath().concat(File.separator).concat("files").concat(File.separator);

    public UUID store(MultipartFile file) {
        UUID filename = UUID.randomUUID();
        try {
            String extension = getFileExtension(file.getOriginalFilename());
            String target = BASE_PATH.concat(filename.toString()).concat(
                            ((!extension.equals("")) ? ("." + extension) : "")
                    );

            createDirectoryIfNotExists(BASE_PATH);

            Files.copy(file.getInputStream(), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);

        }catch (NullPointerException | IOException e) {
            log.warn("invalid file, error={}", e.getMessage());
            throw new FileException();
        }
        return filename;
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
