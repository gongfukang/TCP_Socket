package Socket;

import java.io.*;
import java.net.Socket;

/**
 * 1、创建客户端   必须指定服务器+端口    此时就在连接
 * Socket(String host, int port)
 * 2、接收数据 +发送数据
 *
 * @Auther gongfukang
 * @Date 2017/11/10 10:50
 */
public class Client {
    public static void main(String[] args) throws IOException {
        System.out.println("请输入用户名");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String name = bufferedReader.readLine();
        if (name.equals("")) {
            return;
        }
        Socket client = new Socket("localhost", 9999);
        new Thread(new Send(client,name)).start();
        new Thread(new Receive(client)).start();
    }
}
