package com.example.androidtemplate;

import android.os.Handler;
import android.os.Message;

import com.example.androidtemplate.common.Constants;
import com.example.androidtemplate.common.D;
import com.example.androidtemplate.manager.ManagerComm;
import com.example.androidtemplate.mo.Time;
import com.example.androidtemplate.utils.Utils;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class SocketManager {
    private ServerSocket serverFile;
    private ServerSocket serverString;
    private Handler handler = null;
    public SocketManager(Handler handler){
        this.handler = handler;
        int portFile = 9998;
        int portString = 9997;
        try {
            serverFile = new ServerSocket(portFile);
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
                    //ReceiveFile();
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

//    /**
//     * 图片接收
//     */
//    void ReceiveFile(){
//        try{
//
//            long startTime = System.currentTimeMillis();
//            // 接受流
//            Socket data = serverFile.accept();
//            InputStream dataStream = data.getInputStream();
//
//            // 前面4个字节是 序号 先读取
//            byte[] operaTimeBy = new byte[4];
//            dataStream.read(operaTimeBy, 0, 4);
//            int operaTime = parseBytesToInt32(operaTimeBy);
//
//            D.out("operaTime:"+operaTime);
//
//
//            byte[] countby = new byte[4];
//            dataStream.read(countby, 0, 4);
//            int count = parseBytesToInt32(countby);
//
//            D.out("downloadCount:"+count);
//            // 剩下的是图片流
//            String fileName = System.currentTimeMillis()+".jpg";
//            File filePath = new File(Constants.ReceiveImage);
//            filePath.mkdirs();
//            String savePath = Constants.ReceiveImage+fileName;
//            FileOutputStream file = new FileOutputStream(savePath, false);
//            byte[] buffer = new byte[1024];
//            int size = -1;
//            while ((size = dataStream.read(buffer)) != -1){
//                file.write(buffer, 0 ,size);
//            }
//            file.close();
//            dataStream.close();
//            data.close();
//
//            System.out.print("接受完成\n");
//
//            Message msg = new Message();
//            msg.obj = count;
//            msg.what = 1;
//            ManagerComm.handler.sendMessage(msg);
//
//            long endTime = System.currentTimeMillis();
//
//            String endStamp = Utils.getCurrentTimeStamp();
//            D.out("DownloadTime:"+(endTime-startTime));
//
//            // 计算下载时间
//            for(Time time: ManagerComm.timeList){
//                if(time.getCount()==count){
//                    time.setDownloadTime(endTime-startTime);
//                    time.setOperaTime(operaTime);
//                    time.setEndTime(endStamp);
//                    D.out("time.toString():"+time.toString());
//
//                    // 下载完成保存进文档
//                    String content = time.getCount() + "\t" + time.getStartTime() + "\t" + time.getEndTime() +"\n";
//                    writeFile(Constants.filePath+"upload.txt",content);
//
//                    break;
//                }
//            }
//
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }


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


//    public static byte[] getBytes(int value) {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
//        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
//        byteBuffer.putInt(value);
//        return byteBuffer.array();
//    }

//    public void SendFile(int count, String path, String ipAddress, int port){
//        try {
//
//            long startTime = System.currentTimeMillis();
//
//            String startStamp = Utils.getCurrentTimeStamp();
//
//            double currentLenght = 0.00;
//            double fileSize = 0.00;
//
//            Socket data = new Socket(ipAddress, port);
//            OutputStream outputData = data.getOutputStream();
//
//            File file = new File(path);
//            fileSize = file.length();
//
//            FileInputStream fileInput = new FileInputStream(path);
//            int size = -1;
//
//
//
//            // 最前面的4个字节是序号
//            outputData.write(getBytes(count));
//
//            byte[] buffer = new byte[1024];
//            while((size = fileInput.read(buffer, 0, 1024)) != -1){
//                outputData.write(buffer, 0, size);
//                outputData.flush();
//                currentLenght = currentLenght + size;
//
//                double progress = Double.parseDouble(String.format("%.2f", (currentLenght/fileSize))) * 100;
//
//                D.out(progress);
////                Message message = new Message();
////                message.what=1000;
////                message.obj = progress;
////                handler.sendMessage(message);
//                //
//
////                dialogProgress.getProgressBar().post(new Runnable() {
////                    @Override
////                    public void run() {
////                        DT.out("progress:"+progress);
////                        dialogProgress.getProgressBar().setProgress((int)progress);
////                    }
////                });
//            }
//
//            Message message = new Message();
//            message.what=0;
//            message.obj = count;
//            ManagerComm.handler.sendMessage(message);
//
//            outputData.close();
//            fileInput.close();
//            data.close();
//
//            long endTime = System.currentTimeMillis();
//            D.out("time :"+count+"=="+(endTime-startTime));
//


                // 【这边重写】


//            // 计算上传时间
//            if(ManagerComm.timeList==null){
//                ManagerComm.timeList = new ArrayList<Time>();
//            }
//            boolean isExit = false;
//            for(Time time: ManagerComm.timeList){
//                if(time.getCount()==count){
//                    time.setUploadTime(endTime-startTime);
//                    time.setStartTime(startStamp);
//                    isExit = true;
//                    break;
//                }
//            }
//            if(!isExit){
//                ManagerComm.timeList.add(new Time(count,endTime-startTime,startStamp));
//            }
//
//            // SendMessage(0, fileName + "  发送完成");
//        } catch (Exception e) {
//            D.out(e);
//            //SendMessage(0, "发送错误:\n" + e.getMessage());
//        }
//    }


    public void SendString(String ipAddress, int port,String msg){

        System.out.println("客户端启动...");
        //while (true) {
        Socket socket = null;
        try {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket(ipAddress, port);

            //读取服务器端数据
            //DataInputStream input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //String str = new BufferedReader(new InputStreamReader(System.in)).readLine();
            //out.writeUTF("5==111==E8-33");
            out.writeUTF("发送的消息");
            //String ret = input.readUTF();
            //System.out.println("服务器端返回过来的是: " + ret);
            // 如接收到 "OK" 则断开连接

            out.close();
            //input.close();

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