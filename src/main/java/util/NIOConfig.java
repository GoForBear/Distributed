package util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.Properties;

public class NIOConfig {
    static public String  getValue(String key){
        FileInputStream fileInputStream = null;
        FileChannel fileChannel = null;
        String value = null;
        try{
            fileInputStream = new FileInputStream(new File("src/main/resources/config.properties"));
            fileChannel = fileInputStream.getChannel();
            Properties properties = new Properties();
            properties.load(fileInputStream);
            Enumeration em = properties.propertyNames();
            while(em.hasMoreElements()){
                String emKey = (String) em.nextElement();
                if(emKey != null &&  emKey.equals(key)){
                    value = properties.getProperty(emKey);
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }
    public static String SOCKET_SERVER_IP = NIOConfig.getValue("SOCKET_SERVER_IP") ;
    public static int SOCKET_SERVER_PORT = Integer.parseInt(NIOConfig.getValue("SOCKET_SERVER_PORT")) ;
    public static int capacity = Integer.parseInt(NIOConfig.getValue("capacity"));
}
