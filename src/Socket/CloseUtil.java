package Socket;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Auther gongfukang
 * @Date 2017/11/14 20:34
 */
public class CloseUtil {
    public static void closeAll(Closeable... io) throws IOException {
        for(Closeable temp:io){
            if(null !=temp){
                temp.close();
            }
        }
    }
}
