package Socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @Auther gongfukang
 * @Date 2017/11/14 21:10
 */
public class Receive implements Runnable {
    private DataInputStream dataInputStream;
    private boolean isRunning = true;

    public Receive() {}

    public Receive(Socket client) throws IOException {
        try {
            dataInputStream = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            isRunning = false;
            CloseUtil.closeAll(dataInputStream);
        }
    }

    public String receive() throws IOException {
        String msg="";
        try {
            msg=dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            isRunning=false;
            CloseUtil.closeAll(dataInputStream);
        }
        return msg;
    }

    @Override
    public void run(){
        while(isRunning){
            try {
                System.out.println(receive());
            } catch (IOException e) {}
        }
    }
}
