package code_clone;

import IO.ProjectReader;
import console.Command;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class CloneCheck {

    private ArrayList<String> projectFileName1 = new ArrayList<>();
    private ArrayList<String> projectFileName2 = new ArrayList<>();
    private String path1;
    private String path2;

    // Utility method to generate project path
    private String pathGenerate(String projectName) {
        String currentpath = Command.currentPath;
        currentpath = new Command().pathGenerate(currentpath);
        String current = currentpath.replaceAll("\\\\", "-").replace(":", "");
        return "H:\\2-1\\project\\ProcessAllFiles\\ProcessFile$" + current + "-" + projectName;
    }

    // Method to list files for a project
    private void getFileListForProject(String projectName, String projectPath, ArrayList<String> projectFileNames) throws IOException {
        ProjectReader.getFileList(projectName, projectPath, projectFileNames);
    }

    // Helper method to ensure directory creation
    private void createDirectoryIfNotExist(String path) throws IOException {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            Files.createDirectories(p);
        }
    }

    // Process files for a project and generate the necessary directories and files
    private void processProjectFiles(String projectName, String path, HashMap<String, String> projectData) throws IOException {
        createDirectoryIfNotExist(path);
        for (HashMap.Entry<String, String> entry : projectData.entrySet()) {
            new PreProcessing().ProcessFile(entry.getKey(), entry.getValue(), path);
        }
    }

    // Main method to handle code cloning logic
    public void codeClone(String project1, String project2) throws IOException {
        String path1 = pathGenerate(project1);
        String path2 = pathGenerate(project2);

        // Ensure project directories exist and process files
        processProjectIfNeeded(project1, path1, ProjectReader.projectOne);
        processProjectIfNeeded(project2, path2, ProjectReader.projectTwo);

        // File list processing
        getFileListForProject(project1, path1, projectFileName1);
        getFileListForProject(project2, path2, projectFileName2);

        // Initialize and perform calculations
        TfIdfCalculate tfIdfCalculator = new TfIdfCalculate();
        tfIdfCalculator.getUniqueWordProject1(path1);
        tfIdfCalculator.getUniqueWordProject2(path2);
        tfIdfCalculator.IdfCal();
        tfIdfCalculator.tfIdfVectorProject1();
        tfIdfCalculator.tfIdfVectorProject2();

        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        cosineSimilarity.getCosinesimilarity();

        // Display the results using Box and Whisker chart
        new BoxAndWhiskerChart()

        // Clean up static data to prevent memory leaks
        cleanUpStaticData();
    }

    // Process project if directory doesn't exist
    private void processProjectIfNeeded(String projectName, String projectPath, HashMap<String, String> projectData) throws IOException {
        File projectFolder = new File(projectPath);
        if (!projectFolder.exists()) {
            ProjectReader.fileRead(Command.currentPath + "//" + projectName, projectData.equals(ProjectReader.projectOne) ? 0 : 1);
            processProjectFiles(projectName, projectPath, projectData);
        }
    }

    // Cleanup static data (ensure clearing only when necessary)
    private void cleanUpStaticData() {
        CosineSimilarity.similarArray.clear();
        ProjectReader.projectOne.clear();
        ProjectReader.projectTwo.clear();
        TfIdfCalculate.tfidfvectorProject1.clear();
        TfIdfCalculate.tfidfvectorProject2.clear();
        projectFileName1.clear();
        projectFileName2.clear();
    }
}
