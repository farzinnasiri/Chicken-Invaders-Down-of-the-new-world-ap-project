package ir.farzinnasiri.Client.systemManager;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class ConfigsManager {
    private Properties properties;



    private FileInputStream loadConfigFile(String name){
        Path path = Paths.get(".","files",".","configs",name+".properties");
        try {
            return new FileInputStream(new File(path.toUri()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    private FileOutputStream saveConfigFile(String name){
        Path path = Paths.get(".","files",".","configs",name+".properties");
        try {
            return new FileOutputStream(new File(path.toUri()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String ,String> getAllProperties(String configFile){
        HashMap<String,String> hm = new HashMap<>();
        InputStream in = loadConfigFile(configFile);
        properties = new Properties();
        try {
            properties.load(in);
            in.close();
            Enumeration<?> e = properties.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = properties.getProperty(key);
                hm.put(key,value);
            }
            return hm;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    public void saveProperty(String configFile,String key,String value){
        InputStream in = loadConfigFile(configFile);
        properties = new Properties();
        try {
            properties.load(in);
            in.close();
            OutputStream out = saveConfigFile(configFile);
            properties.setProperty(key,value);
            properties.store(out,null);
            out.close();



        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
