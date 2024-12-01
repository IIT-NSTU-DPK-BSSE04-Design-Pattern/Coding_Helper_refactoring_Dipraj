package searching;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodFind {
    private final GrepContent grepContent;
    private final ProcessSearchFile processSearchFile;

    public MethodFind() {
        this.grepContent = new GrepContent();
        this.processSearchFile = new ProcessSearchFile();
    }

    public void getMethod(String filename, String fileContent, String path, String processfilePath) 
            throws FileNotFoundException, IOException {
        String file = extractFileContent(fileContent);
        Pattern methodPattern = createMethodPattern();
        processMatches(methodPattern, file, filename, path, processfilePath);
    }

    private String extractFileContent(String fileContent) {
        Scanner scan = new Scanner(fileContent);
        return scan.useDelimiter("\\Z").next().trim();
    }

    private Pattern createMethodPattern() {
        return Pattern.compile(
            "(public|void|protected|private|static|final|public static|private static|protected static|" +
            "public final|private final|protective final)+\\s*(\\<.*\\>)*\\s*[a-zA-Z]*\\s*\\b([_$a-zA-Z1-9]+)\\b\\s*" +
            "\\(.*\\)\\s*[^;].*?$",
            Pattern.MULTILINE
        );
    }

    private void processMatches(Pattern pattern, String content, String filename, 
                              String path, String processfilePath) throws IOException {
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            processMatch(matcher, content, filename, path, processfilePath);
        }
    }

    private void processMatch(Matcher matcher, String content, String filename, 
                            String path, String processfilePath) throws IOException {
        String method = grepContent.findBetweenBraces(matcher.start(), content);
        String methodName = cleanMethodName(matcher.group());
        int lineNumber = grepContent.getLineNumber(methodName, path, 0);
        
        if (lineNumber != 0) {
            String processFilename = createProcessFilename(matcher.group(3), lineNumber, filename);
            processSearchFile.processMethod(processFilename, method, path, processfilePath);
        }
    }

    private String cleanMethodName(String methodName) {
        return methodName.replaceAll("\\{", "").replaceAll("[\r\n]+", " ").trim();
    }

    private String createProcessFilename(String name, int lineNumber, String filename) {
        return String.format("%s-%d-%s", name, lineNumber, filename);
    }
}

