package store.custom.service.filehandler;

import static store.custom.validator.CustomErrorMessages.READING_FAIL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import store.custom.validator.Validator;

public class FileReader {
    public static List<String> run(String filePath) {
        Validator.validateFilePath(filePath);

        try {
            return Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException(READING_FAIL + e);
        }
    }
}