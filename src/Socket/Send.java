package Socket;

import java.io.*;
import java.net.Socket;

/**
 * 发送数据线程
 *
 * @Auther gongfukang
 * @Date 2017/11/14 19:20
 */
public class Send implements Runnable {
    private BufferedReader console;
    private DataOutputStream dataOutputStream;
    private boolean isRunning = true;
    private String name;

    public Send() {
        console = new BufferedReader(new InputStreamReader(System.in));
    }

    public Send(Socket client,String name) throws IOException {
        this();
        try {
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            this.name=name;
            send(this.name);
        } catch (IOException e) {
            isRunning = false;
            CloseUtil.closeAll(dataOutputStream, console);
        }
    }

    private String getMsgFromConsole() {
        try {
            return console.readLine();
        } catch (IOException e) {

        }
        return "";
    }

    private void send(String msg) throws IOException {
        if (null != msg && !msg.equals("")) {
            try {
                dataOutputStream.writeUTF(msg);
                dataOutputStream.flush();
            } catch (IOException e) {
                isRunning = false;
                CloseUtil.closeAll(dataOutputStream, console);
            }
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                send(getMsgFromConsole());
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }
}
