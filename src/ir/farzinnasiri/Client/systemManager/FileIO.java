package ir.farzinnasiri.Client.systemManager;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIO {



    private File getFile(String directory,String fileName) {
        Path path = Paths.get(".", "files", ".", directory, fileName + ".json");
        File file = new File(path.toUri());
        return file;

    }


    public void writeFile(String directory,String fileName,String json){
        File file = getFile(directory,fileName);

        try {

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(json);
            bufferedWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readFile(String directory,String fileName) {
        File file = getFile(directory,fileName);

        try {

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            return bufferedReader.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;



    }

    public File[] getAllFiles(String directory){
        Path path = Paths.get(".","files",".",directory);
        File folder = new File(path.toUri());

        return folder.listFiles();
    }

    public void deleteFile(String directory,String fileName){
        File file = getFile(directory,fileName);
        file.delete();
    }


}
