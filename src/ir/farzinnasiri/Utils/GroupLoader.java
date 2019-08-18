package ir.farzinnasiri.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GroupLoader extends ClassLoader {

    public Class<?> loadClassWithUrl(URL url, String className) {
        byte[] classData;

        try {
            classData = getClassData(url);
            try {
                return loadClass(className);
            } catch (ClassNotFoundException e) {
                return defineClass(className, classData, 0, classData.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }


    public byte[] getClassData(URL url) throws IOException {
        URLConnection connection = url.openConnection();

        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int data = inputStream.read();

        while (data != -1) {
            bos.write(data);
            data = inputStream.read();
        }
        inputStream.close();
        return bos.toByteArray();

    }
}
