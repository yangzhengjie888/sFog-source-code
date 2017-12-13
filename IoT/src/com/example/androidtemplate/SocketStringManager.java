package com.example.androidtemplate;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.example.androidtemplate.common.Constants;
import com.example.androidtemplate.common.D;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SocketStringManager {
    private ServerSocket serverString;
    private Handler handler = null;
    public SocketStringManager(Handler handler){
        this.handler = handler;
        int portString = 9997;
        try {;
            serverString = new ServerSocket(portString);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        while(port > 9000){
//            try {
//                server = new ServerSocket(port);
//                break;
//            } catch (Exception e) {
//                port--;
//            }
//        }
//        SendMessage(1, port);
        Thread receiveFileThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    ReceiveString();
                }
            }
        });
        receiveFileThread.start();
    }
    void SendMessage(int what, Object obj){
        if (handler != null){
            Message.obtain(handler, what, obj).sendToTarget();
        }
    }

    public static int parseBytesToInt32(byte[] value) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(value);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.asIntBuffer().get();
    }




    void ReceiveString(){
        try {
            // 读取客户端数据
            Socket socket = serverString.accept();
            DataInputStream input = new DataInputStream(socket.getInputStream());
            String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
            // 处理客户端数据
            System.out.println("收到的内容:" + clientInputStr);

            // 向客户端回复信息
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // 发送键盘输入的一行
            //String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
            out.writeUTF("OK");

            out.close();
            input.close();

        } catch (Exception e) {
            System.out.println("服务器 run 异常: " + e.getMessage());
        }
    }

    //在文件末追加写：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
    public static void writeFile(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





    public void SendString(String ipAddress, int port,String msg){

        long uploadTime = System.currentTimeMillis();
        String uploadTimeString = Long.toString(uploadTime);
        System.out.println("客户端启动...");
        //while (true) {
        Socket socket = null;
        try {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket(ipAddress, port);

            //读取服务器端数据
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
            //out.writeUTF("5==111==E8-33");
            out.writeUTF(uploadTimeString);

            //通过数组接收来自服务器的东西，设置为1024，
            byte[] by = new byte[1024];
            input.read(by);

            String ret = new String(by).trim();


            System.out.println("服务器端返回过来的是: " + ret);
            // 如接收到 "OK" 则断开连接
            long sysTime = 0;
            if(!TextUtils.isEmpty(ret)){
                sysTime = Long.valueOf(ret);
            }

            long downloadTime = System.currentTimeMillis();

            // 计算时间
            long calculateTime = (downloadTime+uploadTime)/2;
            D.out("calculateTime:"+calculateTime);

            D.out("result:"+(calculateTime-sysTime));


            writeFile(Constants.filePath+"upload.txt",(calculateTime-sysTime)+"" +"\n");

            out.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("客户端异常:" + e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                    System.out.println("客户端 finally 异常:" + e.getMessage());
                }
            }
            //  }
        }

    }

}