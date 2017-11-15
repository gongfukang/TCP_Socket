package Socket;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 必须先启动服务器 后连接
 * 1、创建服务器  指定端口   ServerSocket(int port)
 * 2、接收客户端连接
 * 3、发送数据+接收数据
 *
 * @Auther gongfukang
 * @Date 2017/11/10 10:50
 */
public class Server {
    private List<MyChannel> all = new ArrayList<MyChannel>();

    public static void main(String[] args) throws IOException {
        new Server().start();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket client = serverSocket.accept();
            MyChannel channel = new MyChannel(client);
            all.add(channel);
            new Thread(channel).start();
        }
    }

    /**
     * @Auther gongfukang
     * @Date 2017/11/15
     * 一个客户端，一条道路
     * 输入流+输出流
     * 输入数据+输出数据
     */
    private class MyChannel implements Runnable {
        private DataInputStream dataInputStream;
        private DataOutputStream dataOutputStream;
        private boolean isRunning = true;
        private String name;

        public MyChannel(Socket client) throws IOException {
            try {
                dataInputStream = new DataInputStream(client.getInputStream());
                dataOutputStream = new DataOutputStream(client.getOutputStream());

                this.name = dataInputStream.readUTF();
                this.send("欢迎进入聊天室");
                sendOthers(this.name + "进入聊天室", true);
                //System.out.println("test"+this.name);
            } catch (IOException e) {
                //e.printStackTrace();
                CloseUtil.closeAll(dataInputStream, dataOutputStream);
                isRunning = false;
            }
        }

        /**
         * @Auther gongfukang
         * @Date 2017/11/15
         * 读取数据
         */
        private String receive() throws IOException {
            String msg = "";
            try {
                msg = dataInputStream.readUTF();
            } catch (IOException e) {
                //e.printStackTrace();
                CloseUtil.closeAll(dataInputStream, dataOutputStream);
                isRunning = false;
                all.remove(this);
            }
            return msg;
        }

        /**
         * @Auther gongfukang
         * @Date 2017/11/15
         * 发送数据
         */
        private void send(String msg) throws IOException {
            if (msg == null || msg.equals("")) {
                return;
            }
            dataOutputStream.writeUTF(msg);
            try {
                dataOutputStream.flush();
            } catch (IOException e) {
                CloseUtil.closeAll(dataInputStream, dataOutputStream);
                isRunning = false;
                all.remove(this);
            }
        }

        /**
         * @Auther gongfukang
         * @Date 2017/11/15
         * 发送给其他客户端 私聊
         */
        private void sendOthers(String msg, boolean sys) throws IOException {
            if (msg.startsWith("@") && msg.indexOf(":") > -1) {
                String name = msg.substring(1, msg.indexOf(":"));
                String content = msg.substring(msg.indexOf(":") + 1);
                for (MyChannel other : all) {
                    if (other.name.equals(name)) {
                        other.send(this.name + "私聊你说：" + content);
                    }
                }
            } else {
                for (MyChannel other : all) {
                    if (other == this) {
                        continue;
                    }
                    if (sys) {
                        other.send("系统消息:" + msg);
                    } else {
                        other.send(this.name + "群聊:" + msg);
                    }
                }
            }
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    sendOthers(receive(), false);
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
