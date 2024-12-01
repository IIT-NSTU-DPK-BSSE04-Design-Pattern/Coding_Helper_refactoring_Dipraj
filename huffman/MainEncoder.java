package huffman;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class MainEncoder {
    private static final String[] VALID_INPUT_EXTENSIONS = {".java", ".txt"};
    private static final String ZIP_EXTENSION = ".zip";
    
    private final FileOperations fileOps;
    private final UserInterface ui;
    private final PathResolver pathResolver;
    
    public MainEncoder() {
        this.fileOps = new FileOperations();
        this.ui = new UserInterface();
        this.pathResolver = new PathResolver();
    }

    public void compress(String basePath) throws IOException {
        try {
            String normalizedPath = pathResolver.normalizePath(basePath);
            String sourceFilePath = getSourceFilePath(normalizedPath);
            String compressedFilePath = getCompressedFilePath(normalizedPath);
            
            validateCompression(sourceFilePath, compressedFilePath);
            
            try (Compress encoder = new Compress(sourceFilePath, compressedFilePath)) {
                encoder.compressFile();
            }
        } catch (CompressionException e) {
            ui.showError(e.getMessage());
            throw e;
        }
    }

    private String getSourceFilePath(String basePath) throws IOException {
        String filename = ui.promptForInput("Enter a filename:");
        validateSourceFilename(filename);
        
        String fullPath = pathResolver.buildPath(basePath, filename);
        validateSourceFile(fullPath);
        
        return fullPath;
    }

    private String getCompressedFilePath(String basePath) throws IOException {
        String filename = ui.promptForInput("Enter compress filename:");
        validateCompressedFilename(filename);
        
        String fullPath = pathResolver.buildPath(basePath, filename);
        validateCompressedFile(fullPath);
        
        return fullPath;
    }

    private void validateSourceFilename(String filename) throws CompressionException {
        if (filename.isEmpty()) {
            throw new CompressionException("Filename cannot be empty");
        }
        if (!hasValidInputExtension(filename)) {
            throw new CompressionException("Invalid file extension. Must be .java or .txt");
        }
    }

    private void validateCompressedFilename(String filename) throws CompressionException {
        if (filename.isEmpty()) {
            throw new CompressionException("Filename cannot be empty");
        }
        if (!filename.endsWith(ZIP_EXTENSION)) {
            throw new CompressionException("Compressed file must have .zip extension");
        }
    }

    private void validateSourceFile(String path) throws IOException {
        if (!fileOps.exists(path)) {
            throw new CompressionException("Source file does not exist");
        }
        if (fileOps.isEmpty(path)) {
            throw new CompressionException("Source file is empty");
        }
    }

    private void validateCompressedFile(String path) throws IOException {
        if (fileOps.exists(path)) {
            throw new CompressionException("Compressed file already exists");
        }
    }

    private void validateCompression(String sourcePath, String compressedPath) throws IOException {
        if (!fileOps.exists(sourcePath)) {
            throw new CompressionException("Source file not found");
        }
        if (fileOps.exists(compressedPath)) {
            throw new CompressionException("Destination file already exists");
        }
    }

    private boolean hasValidInputExtension(String filename) {
        return Arrays.stream(VALID_INPUT_EXTENSIONS)
                    .anyMatch(filename::endsWith);
    }
}

class FileOperations {
    public boolean exists(String path) throws IOException {
        Path filePath = Paths.get(path);
        return Files.exists(filePath) && !Files.isDirectory(filePath);
    }

    public boolean isEmpty(String path) throws IOException {
        return Files.size(Paths.get(path)) == 0;
    }
}

class UserInterface implements AutoCloseable {
    private final Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public String promptForInput(String message) {
        System.out.print("\t" + message + " ");
        return scanner.nextLine().trim();
    }

    public void showError(String message) {
        System.out.println("\t" + message);
    }

    @Override
    public void close() {
        scanner.close();
    }
}

class PathResolver {
    public String normalizePath(String path) {
        return path.replace("\\", File.separator);
    }

    public String buildPath(String basePath, String filename) {
        return Paths.get(basePath, filename).toString();
    }
}

class CompressionException extends IOException {
    public CompressionException(String message) {
        super(message);
    }
}
