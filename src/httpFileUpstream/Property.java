package httpFileUpstream;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.util.Properties;  
  
public class Property {  
    private static Properties properties = new Properties();  
    static {  
        File file = new File(System.getProperty("user.dir")+"setting.property");  
        if (!file.exists())  
        {  
            file = new File(Property.class.getResource("/").getPath()+"setting.property");  
        }  
           
        try (FileInputStream fis =   
                new FileInputStream(file)){  
            properties.load(fis);  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
              
            e.printStackTrace();  
        }  
    }  
      
    private Property(){  
    }  
     
    public static String getProperty(String key) {  
        return properties.getProperty(key);  
    }  
      
    public static String getSaveFileDir(){  
        return System.getProperty("user.dir") + File.separator;  
    }  
}  