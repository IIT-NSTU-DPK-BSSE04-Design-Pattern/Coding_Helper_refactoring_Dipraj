package searching;

import IO.ProjectReader;
import IO.Filereader;
import console.Command;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;



import IO.ProjectReader;
import IO.Filereader;
import console.Command;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class Search {
    public static ArrayList<String> ProjectFileName = new ArrayList<>();
    public static void ProjectRead(String Projectpath, String processfilePath) throws IOException {
        ArrayList<String> filename = new ArrayList<>();
        Path folderToWalk = Paths.get(Projectpath);
        //   ProjectPath = fullPath;
        Files.walkFileTree(folderToWalk, new SimpleFileVisitor<Path>() {
            // int count = 0;

            @Override
            public FileVisitResult visitFile(Path f, BasicFileAttributes attr) throws IOException {

                if (f.getFileName().toString().endsWith(".java")) {
                    String fileNamewithPackage = "";
                    //count++;
                    byte[] p = Files.readAllBytes(f);
                    String content = new String(p, StandardCharsets.UTF_8).trim();
                    String filePath = f.getParent() + "\\" + f.getFileName();
                    //   System.out.println("pa="+filePath);
                    String dir = f.getParent().toString().substring(f.getParent().toString().lastIndexOf(File.separator) + 1);
                    //  String fileNamewithPackage = f.getFileName().toString() + "$" + dir+".java";
                    fileNamewithPackage = dir + "$" + f.getFileName().toString(); //packagename$filename.java
                    // System.out.println("name=" + fileNamewithPackage);
                    // System.out.println("path="+filePath);
                    //  System.out.println("content="+content);
                    filename.add(fileNamewithPackage);
                    
                    if (!fileNamewithPackage.isEmpty() && !content.isEmpty()) {
                        
                        new MethodFind().getMethod(fileNamewithPackage, content, filePath, processfilePath);
                        new MethodFind().getConstructor(fileNamewithPackage, content, filePath, processfilePath);
                    }
                }
                //     System.out.println("(" + attr.size() + "bytes)");
                return FileVisitResult.CONTINUE;

            }
        }
        );
        if (filename.isEmpty()) {
            System.out.println("\tProject doesnot have any java file");
        }
    }

    public void processProject(String projectpath, String Projectname) throws IOException {
        String current = projectpath.replaceAll("\\\\", "-").replace(":", "");
        //System.out.println("current="+current);
        String processfilePath = "H:\\2-1\\project\\ProcessAllFiles" + "\\ProcessMethod$" + current;
        File f1 = new File(processfilePath);
        if (!f1.exists()) {
            Path p1 = Paths.get(processfilePath);
            Files.createDirectories(p1);
            ProjectRead(projectpath, processfilePath);
          //  System.out.println("not exist");
        }
        getProjectFile(Projectname);
    }

    /* public void getFileList(String projectname) {
        String currentpath = Command.currentPath;
        String path=new Command().pathGenerate(currentpath);
        String current = path.replaceAll("\\\\", "-").replace(":", "");
        String Pathname = "H:\\coding_helper" + "\\ProcessMethod$" + current + "-" + projectname;
        File folder = new File(Pathname);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].toString().endsWith(".txt")) {
                count++;
                // String newFilename = listOfFiles[i].getName().replaceAll(".{3}$", "java");
                ProjectFileName.add(listOfFiles[i].getName().replaceAll(".{3}$", "java"));
            }
        }
    }
     */
    public String getProcessFilepath(String projectname){
       String currentpath = Command.currentPath;
    String path = new Command().pathGenerate(currentpath);    
    String current = path.replaceAll("\\\\", "-").replace(":", "");
    String Pathname = "H:\\2-1\\project\\ProcessAllFiles" + "\\ProcessMethod$" + current + "-" + projectname;
    return Pathname;
    
    }
    public void getProjectFile(String projectname) throws IOException {
     //   String currentpath = Command.currentPath;
        //String path = new Command().pathGenerate(currentpath);
       // String current = path.replaceAll("\\\\", "-").replace(":", "");
       // String Pathname = "H:\\coding_helper" + "\\ProcessMethod$" + current + "-" + projectname;
       String Pathname=getProcessFilepath(projectname);
        ProjectReader.getFileList(projectname, Pathname, ProjectFileName);

    }

    public void getFile(String projectPath, String fileName) throws IOException {
        String currentPath = Command.currentPath;
      String processfilePath=getProcessFilepath(fileName);
        File f1 = new File(processfilePath);
          File file = new File(currentPath + "\\" + fileName);
        //    if (f1.exists()) {
        //        System.out.println("file exist");
        //    }

        Path p = Paths.get(currentPath + "\\" + fileName);
        if (file.getName().endsWith(".java") && !Files.isDirectory(p)) {
            boolean fileEmpty=new Filereader().fileEmpty(p.toString());
             byte[] s = Files.readAllBytes(p);
            String fileContent = new String(s, StandardCharsets.UTF_8).trim();
            if(fileEmpty){
                System.out.println("File is Empty");
            }
          
         
             else {
                if (!f1.exists()) {
                    Path p1 = Paths.get(processfilePath);
                    Files.createDirectories(p1);
                    new MethodFind().getMethod(file.getName(), fileContent, file.getAbsolutePath(), processfilePath);
                    new MethodFind().getConstructor(file.getName(), fileContent, file.getAbsolutePath(), processfilePath);
                    // for(int i=0;i<MethodFind.Methodbody.size();i++) {
                }
                // }
//System.out.println(filePath);
            }
            getProjectFile(file.getName());
            //  for (int i = 0; i < ProjectFileName.size(); i++) {
            //     System.out.println("" + ProjectFileName.get(i));
            //  }
        }
    }

    public void SearchingResult(String query, String projectPath) throws IOException {
        String current = projectPath.replaceAll("\\\\", "-").replace(":", "");
        //System.out.println("current="+current);
        String processfilePath = "H:\\2-1\\project\\ProcessAllFiles" + "\\ProcessMethod$" + current;
        String processquery = new ProcessSearchFile().queryProcess(query);
        TfIdfCalculate ob = new TfIdfCalculate();
        ob.fileRead(processfilePath);
        ob.Idfcal();
        ob.UniqueQueryTerms(processquery);
        ob.queryTfIdfCal(processquery);
        ob.ProjectTfIdfCal();

//new Similarity().p();
        new Similarity().getCosine();
        new Similarity().getResult();
        ProjectFileName.clear();
        TfIdfCalculate.queryTerms.clear();
        TfIdfCalculate.queryTfIdfVector.clear();
        TfIdfCalculate.tfidfvectorProject.clear();

    }

}
