package fa.hust.utils;

import fa.hust.enums.ETypeFile;
import fa.hust.req.FileUpload;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
public class FileUtil {
    public FileUpload checkFile(MultipartFile file, ETypeFile type) throws IOException {
        StringBuilder uploadDir = new StringBuilder("src/main/resources/static/files/");
        String name = Objects.requireNonNull(file.getOriginalFilename());
        if (type == ETypeFile.THUMBNAIL && ((name.endsWith(".jpg") || name.endsWith(".png")))) {
            uploadDir.append("images/");
        } else if (type == ETypeFile.CHAPTER && name.endsWith(".txt")) {
            uploadDir.append("chapter/");
        } else if (type == ETypeFile.INTRODUCTION && name.endsWith(".txt")) {
            uploadDir.append("introduction/");
        } else if (type == ETypeFile.AVATAR && ((name.endsWith(".jpg") || name.endsWith(".png")))) {
            uploadDir.append("avatar/");
        } else {
            throw new IOException("Upload không đúng file");
        }
        return FileUpload.builder()
                .fileName(name)
                .uploadDir(uploadDir.toString())
                .file(file)
                .build();
    }

    public void saveFile(FileUpload fileUpload, Long id) throws IOException {
        String uploadDir = id == null ? fileUpload.getUploadDir() : fileUpload.getUploadDir() + id;
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        try (InputStream inputStream = fileUpload.getFile().getInputStream()) {
            Path pathFile = path.resolve(fileUpload.getFileName());
            Files.copy(inputStream, pathFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Đéo luu được đâu");
        }
    }

    public List<String> loadText(String name, Long id, ETypeFile type) throws IOException {
        StringBuilder loadDir = new StringBuilder("src/main/resources/static/files/");
        if (type == ETypeFile.CHAPTER && name.endsWith(".txt")) {
            loadDir.append("chapter/").append(id).append("/").append(name);

        } else if (type == ETypeFile.INTRODUCTION && name.endsWith(".txt")) {
            loadDir.append("introduction/").append(id).append("/").append(name);
        } else {
            throw new IOException("Upload không đúng file");
        }
        List<String> lines = new ArrayList<>();
        try (BufferedReader inputStream = new BufferedReader(new FileReader(loadDir.toString()))) {
            String line = "";
            while ((line = inputStream.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IOException("Đéo luu được đâu");
        }
        return lines;
    }


}