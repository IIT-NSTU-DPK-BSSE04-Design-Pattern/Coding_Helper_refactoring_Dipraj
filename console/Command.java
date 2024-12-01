package console;

import code_clone.CloneCheck;
import huffman.mainDecode;
import huffman.mainEncode;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import metrices.Average_LOC;
import metrices.FileCount;
import metrices.LineOfCode;
import metrices.MethodCount;
import searching.Search;

public class Command {
    private CommandProcessor processor;
    
    public void command() throws IOException {
        initializeCurrentPath();
        
        while (true) {
            displayPrompt();
            String choice = getUserInput();
            processCommand(choice);
        }
    }

    private void processCommand(String choice) throws IOException {
        if (isHelpCommand(choice)) {
            displayHelpMenu();
        } else if (isCloneCommand(choice)) {
            handleCloneCommand();
        } else if (isFileOperationCommand(choice)) {
            handleFileOperation(choice);
        } else if (isMetricsCommand(choice)) {
            handleMetricsCommand(choice);
        } else if (isNavigationCommand(choice)) {
            handleNavigation(choice);
        } else {
            displayInvalidCommand(choice);
        }
    }

    public interface CommandExecutor {
        void execute() throws IOException;
    }
    
    public class CommandFactory {
        private Map<String, CommandExecutor> commands = new HashMap<>();
        
        public CommandFactory() {
            commands.put("help", new HelpCommand());
            commands.put("clone", new CloneCommand());
            commands.put("fcom", new CompressCommand());
            commands.put("dcom", new DecompressCommand());
            // ... other commands
        }
        
        public CommandExecutor getCommand(String choice) {
            return commands.getOrDefault(choice.toLowerCase(), new InvalidCommand());
        }
    }
    public class Command {
        private String forwardDir;
        private String currentPath;
        private String directoryName;
        private final Scanner scanner;
        
        public Command() {
            this.scanner = new Scanner(System.in);
            this.currentPath = null;
            this.directoryName = null;
        }
    }
    public class FileOperationUtil {
        public static boolean isValidPath(Path path) {
            return Files.exists(path) && !Files.isDirectory(path);
        }
        
        public static boolean isValidDirectory(Path path) {
            return Files.exists(path) && Files.isDirectory(path);
        }
        
        public static String pathGenerate(String currentPath) {
            // Common path generation logic
            return currentPath;
        }
    }
    public class FileOperationException extends RuntimeException {
        public FileOperationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    public void getMethod(String path) {
        try {
            validatePath(path);
            String projectPath = buildProjectPath(path);
            executeMethodCount(projectPath);
        } catch (IOException e) {
            throw new FileOperationException("Failed to process method count", e);
        }
    }
    public class CommandConstants {
        public static final String HELP_COMMAND = "help";
        public static final String CLONE_COMMAND = "clone";
        public static final String COMPRESS_COMMAND = "fcom";
        public static final String DECOMPRESS_COMMAND = "dcom";
        public static final String PROMPT_SUFFIX = ">";
    }
    public class ConsoleIO {
        private final Scanner scanner;
        
        public ConsoleIO() {
            this.scanner = new Scanner(System.in);
        }
        
        public String readInput() {
            return scanner.nextLine().trim();
        }
        
        public void displayPrompt(String currentPath) {
            System.out.print(currentPath + ">");
        }
        
        public void displayMessage(String message) {
            System.out.println(message);
        }
    }
    public class MetricsProcessor {
        public void processMethodCount(String path) throws IOException {
            // Method count logic
        }
        
        public void processLineCount(String path) throws IOException {
            // Line count logic
        }
    }
    
    public class FileNavigator {
        public String navigateToParent(String currentPath) {
            // Navigation logic
        }
        
        public String navigateToPath(String path) {
            // Path navigation logic
        }
    }
    public String Input() {
        try (Scanner scanner = new Scanner(System.in)) {
            return scanner.nextLine().trim();
        }
    }
    public enum FileType {
        DIRECTORY,
        FILE
    }
    
    public class PathValidator {
        public boolean isValid(String path, FileType type) {
            Path filePath = Paths.get(path);
            return switch (type) {
                case DIRECTORY -> Files.exists(filePath) && Files.isDirectory(filePath);
                case FILE -> Files.exists(filePath) && !Files.isDirectory(filePath);
            };
        }
    }
    