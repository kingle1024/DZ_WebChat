package Controller;

import java.io.File;

public class AllServlet  {
    public static void main(String[] args) {
        String javaPath = "./src/main/java/";
        File f = new File(javaPath);
        File[] list = f.listFiles();
        for(File folder : list){
            System.out.println(folder.getName() +" " +javaPath+folder.getName());

            File folderInFiles = new File(javaPath+folder.getName());
            File[] fileList = folderInFiles.listFiles();
            if(fileList != null) {
                for (File flist : fileList) {
                    String fileInName = flist.getName();
                    if (fileInName.contains("Action.java") || fileInName.contains("ActionPost.java")) {
                        System.out.println(" > " + folder.getName() + "/" + flist.getName());
                    }
                }
            }
        }
    }
}
