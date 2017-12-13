package com.example.androidtemplate;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.androidtemplate.common.BaseActivity;
import com.example.androidtemplate.common.Constants;
import com.example.androidtemplate.common.D;
import com.example.androidtemplate.common.T;
import com.example.androidtemplate.manager.ManagerComm;
import com.example.androidtemplate.mo.Time;
import com.example.androidtemplate.utils.Utils;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.androidtemplate.manager.ManagerComm.countflag;
//import me.nereo.multi_image_selector.MultiImageSelectorActivity;






public class SocketActivity extends BaseActivity {

    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.title_ll)
    LinearLayout titleLl;
    @Bind(R.id.upload_btn)
    Button uploadBtn;
    @Bind(R.id.msg_tv)
    TextView msgTv;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;
    @Bind(R.id.download_btn)
    Button downloadBtn;
    @Bind(R.id.result_tv)
    TextView resultTv;
    private final static String split = "==";

    @Override
    protected void initData() {
        setContentView(R.layout.activity_socket);
        ButterKnife.bind(this);
        initHandler();

        File file = new File(Constants.filePath);
        if(!file.exists()){
            file.mkdirs();
        }

    }

    @Override
    protected void recycle() {
        mHandler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        uploadBtn.performClick();
        super.onResume();
    }
    //int count1 =1;
    int count = 1;//seqnumber


    // int countflag =0;//统计包的start和end

    public String mark ;
    //当upload是bootle neck的时候，congestion初始值要比bootle neck长一点，要不然会出现序号混乱问题
    long congestion =4100;//时间
    long uploadDelay =2000;

    long computeDelay =3000;

    private long waitTime1 = (long)2 * 1000;
    private long waitTime2 = (long)3 * 1000;
    private long waitTime3 = (long)4 * 1000;
    private double dev1 = 0.1;
    private double dev2 = 0.1;
    private double dev3 = 0.1;
    //countflag的奇偶来判断seqnumber是否++，countflag每上传一个包都会++

    // 控制值
    int limit = 5;
    boolean flag = true;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //String packageStr = UUID.randomUUID().toString();
            if(flag){
                downloadBtn.performClick();
                flag = false;
            }


            //if(count<=limit) {
                int max=1000;
                int min=100;
                Random random = new Random();
                int s = random.nextInt(max)%(max-min+1) + min;

//                if (countflag ==0){
//                    mark = "start";
//                }else if (countflag==1){
//                    mark = "end";
//                }
            //对
            if (countflag ==1){
                //count ++;
                mark = "end";
                //countflag =0;
            }
            if (countflag ==0){
                mark = "start";




                //记录上传开始时间并写入文档
                long uploadStart= System.currentTimeMillis();//写入arraylist用于计算
                String uploadStartStamp = Utils.getCurrentTimeStamp();//写入文档用于输出

                //包1上传开始时间等价于job上传时间
                //long endTime = System.currentTimeMillis();
                //D.out("time :"+count+"=="+(endTime-startTime));

                // 计算上传时间
                if(ManagerComm.timeList==null){
                    ManagerComm.timeList = new ArrayList<Time>();

                }

                if(ManagerComm.timeList.size() ==0){
                    ManagerComm.timeList.add(new Time());
                }

                int flag =0;
                for(Time time:ManagerComm.timeList){
                    if(time.getCount()==count) {
                        time.setUploadStart(uploadStart);
                        String content = time.getCount() + "\t" + uploadStartStamp + "\t";
                        writeFile(Constants.filePath + "upload.txt", content);
                        flag =1;
                    }
//                    }else{
//                        ManagerComm.timeList.add(new Time());
//                        time.setCount(count);
//                        time.setUploadStart(uploadStart);
//                        String content = time.getCount() + "\t" + uploadStartStamp+ "\t";
//                        writeFile(Constants.filePath+"upload.txt",content);
//
//                    }
                }

                if (flag !=1){
                    ManagerComm.timeList.add(new Time());
                    ManagerComm.timeList.get(ManagerComm.timeList.size()-1).setCount(count);
                    ManagerComm.timeList.get(ManagerComm.timeList.size()-1).setUploadStart(uploadStart);
                    String content = ManagerComm.timeList.get(ManagerComm.timeList.size()-1).getCount() + "\t" + uploadStartStamp+ "\t";
                    writeFile(Constants.filePath+"upload.txt",content);
                }




                //写入文档



//                boolean isExit = false;
//                for(Time time:ManagerComm.timeList){
//                    if(time.getCount()==count){
//                        //time.setUploadTime(endTime-startTime);
//                        time.setStartTime(startStamp);
//                        isExit = true;
//                        break;
//                    }
//                }
//                if(!isExit){
//                    ManagerComm.timeList.add(new Time());
//                }

                //countflag =1;
            }


                upload(mark + split + count + split + s + split + getMac());
            //格式是“start/end==seq==data==手机的mac地址”
                //countflag =
            if (mark.equals("start")) {
                mHandler.postDelayed(runnable, uploadDelay);

            }

            if (mark.equals("end")) {
                long temp = congestion -uploadDelay;
                if (temp <0){
                    temp =0;
                }
                mHandler.postDelayed(runnable, temp);

            }
                D.out(getMac());
           // }
        }
    };



    boolean isStart = false;
    @OnClick({R.id.left_tv, R.id.right_tv, R.id.upload_btn,R.id.download_btn,R.id.stop_btn,R.id.start_btn,R.id.switch_btn,R.id.situation_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_tv:
                mHandler.removeCallbacks(runnable);
                finish();
                break;
            case R.id.right_tv:
                break;
            case R.id.upload_btn:
                if(!isStart){
                    msgTv.setText("");
                    mHandler.postDelayed(runnable, 2000);
                    isStart = true;
                }

                break;
            case R.id.download_btn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        serverStart();
                    }
                }).start();
                T.showToast(this_,"Download Service Started");

                break;
            case R.id.stop_btn:
                //stopRun();
                break;
            case R.id.start_btn:
                //startRun();
                break;
            case R.id.switch_btn:
                //switchWifi();
                break;
            case R.id.situation_btn:
                situation();
                break;
        }
    }

    public static String IP_ADDR = "192.168.12.1";//服务器地址

    //public static String IP_ADDR = ManagerComm.gatewayIp;//自动获取网关地址

    public static final int UPLOAD_PORT = 12345;//上传端口号
    public static final int WINDOWN_SIZE = 5;//发送包的数量

    private void upload(final String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                // 上传写进文件
                ManagerComm.handler.sendEmptyMessage(200);
                try {
                    //创建一个流套接字并将其连接到指定主机上的指定端口号
                    socket = new Socket(IP_ADDR, UPLOAD_PORT);
                    //读取服务器端数据
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    
//                    Date date=new Date();
//                    DateFormat format= new SimpleDateFormat("HH : mm : ss : SSS");
//                    String time1 = format.format(date);
                    
                    //String content1 = str + split + time1 + "\n";
                   
                    
                    //向服务器端发送数据
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    out.writeUTF(str);
                    //writeFile(Constants.filePath+"upload.txt",content1);
                    //ArrayList<Byte> by = new ArrayList<Byte>(); //这种方法行不通，因为read是read数组不是List
                    
                    //通过数组接收来自服务器的东西，设置为1024，
                    byte[] by = new byte[1024];
                    input.read(by);
                   
                    String ret = new String(by).trim();

                    //String ret = input.readUTF();
                    D.out("Server Replied: " + ret);

                    String sendStr = str.split(split)[0]+ split +str.split(split)[1] + split ;
                    		//+ str.split(split)[1] ;



//                    if (ret.equals("OK")) {
//
//                        Message msg = new Message();
//                        msg.obj = sendStr;
//                        msg.what=0;
//                        ManagerComm.handler.sendMessage(msg);
//                        countflag ++;
//                    }
                    //这边系统端要改
                    if (ret.equals("start")) {

                        Message msg = new Message();
                        msg.obj = sendStr;
                        msg.what=0;
                        ManagerComm.handler.sendMessage(msg);
                        countflag =1;

                    }

                    if (ret.equals("end")) {

                        Message msg = new Message();
                        msg.obj = sendStr;
                        msg.what=0;
                        ManagerComm.handler.sendMessage(msg);
                        countflag =0;



                        long uploadEnd= System.currentTimeMillis();//写入arraylist用于计算
                        String uploadEndStamp = Utils.getCurrentTimeStamp();//写入文档用于输出

                        //包1上传开始时间等价于job上传时间
                        //long endTime = System.currentTimeMillis();
                        //D.out("time :"+count+"=="+(endTime-startTime));

                        // 计算上传时间


                        //记录上传完成时间并写入文档
                        for(Time time:ManagerComm.timeList){
                            if(time.getCount()==count){
                                time.setUploadEnd(uploadEnd);

                                //String content1 = str + split + time1 + "\n";
                                String content = time.getCount() + "\t" + uploadEndStamp + "\n";
                                writeFile(Constants.filePath+"upload.txt",content);
                            }
                        }

                        count ++;



                        //包2上传结束时间等价于T1 end time
//                        long endTime = System.currentTimeMillis();
//
//                        String endStamp = Utils.getCurrentTimeStamp();
//                        D.out("DownloadTime:"+(endTime-startTime));
//
//                        // 计算下载时间
//                        for(Time time:ManagerComm.timeList){
//                            if(time.getCount()==count){
//                                time.setDownloadTime(endTime-startTime);
//                                time.setOperaTime(computeDelay);
//                                time.setEndTime(endStamp);
//                                D.out("time.toString():"+time.toString());
//
//                                // 下载完成保存进文档
//                                //包2收完时间等价于job上传结束时间
//                                String content = time.getCount() + "\t" + time.getStartTime() + "\t" + time.getEndTime() +"\n";
//                                writeFile(Constants.filePath+"upload.txt",content);
//
//                                break;
//                            }
//                        }


                    }



                    out.close();
                    input.close();

                } catch (Exception e) {
                    D.out(e);
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            socket = null;
                            D.out(e);
                        }
                    }
                }
            }
        }).start();

    }


    //congestion control






    // 我要切换了使用新端口
    private int UPLOAD_SWITCH_PORT = 8888;
    // 发送我要切换了
    private void uploadSwitch(final String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    //创建一个流套接字并将其连接到指定主机上的指定端口号
                    socket = new Socket(IP_ADDR, UPLOAD_SWITCH_PORT);
                    //读取服务器端数据
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    //向服务器端发送数据
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    out.writeUTF(str);

                    //ArrayList<Byte> by = new ArrayList<Byte>(); //这种方法行不通，因为read是read数组不是List

                    //通过数组接收来自服务器的东西，设置为1024，
                    byte[] by = new byte[1024];
                    input.read(by);

                    String ret = new String(by).trim();

                    //String ret = input.readUTF();
                    D.out("Server Replied: " + ret);

                    String sendStr = str.split(split)[0] + split + str.split(split)[1] ;

                    if (ret.equals("OK")) {

                    }

                    out.close();
                    input.close();

                } catch (Exception e) {
                    D.out(e);
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            socket = null;
                            D.out(e);
                        }
                    }
                }
            }
        }).start();

    }

    // 下载的数据集合
    private List<String> downLoadStr = new ArrayList<String>();
    // 下载序号
    private int downloadCount = 0;
    int switchflag =0;
    private void initHandler(){
        ManagerComm.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:


                        // 等待最后有个上传成功了 停止上传与下载
//                        if(isStop){
//                            stopRun();
//
//                        }

                        msgTv.setText(msg.obj + " Uploaded\n" + msgTv.getText().toString());

                        break;
                    case 1:

//                        if(count>5){
//                            int max=1000;
//                            int min=100;
//                            Random random = new Random();
//                            int s = random.nextInt(max)%(max-min+1) + min;
//                            mHandler.postDelayed(runnable, 100);
//                            upload(count + split + s + split + getMac());
//                        }

//                        String msgStr = msg.obj+"";
//                        downLoadStr.add(msgStr);
//                        String[] strings = msgStr.split(split);
//                        // strings[0]是显示部分  strings[1] 是服务器mac地址
//                        downloadCount ++;
//                        resultTv.setText( strings[0] + split +" Downloaded\n"+ resultTv.getText().toString());
                        		
                        		//downloadCount+"、"+strings[0] + split + strings[1]+" Downloaded\n" + resultTv.getText().toString());


                        break;

                    // 我要切换了 通知
                    case 100:


                        String ssid = (String) msg.obj;
                        // 切换wifi 停止上传下载
                        if(switchflag ==0) {
                            switchWifi("Switching" + split + ssid);
                            D.out("Switching" + split + ssid);
                        }

                        break;

                    // 切换成功通知
                    case 101:
                        switchflag =0;


                        //判断切换以后是否要重传job的第一个包
                        //这边有一个问题一直没有解决，就是连接到新的node之后，虽然可以无视congestion control直接上传，但是上传之后的
                        //第一个包的upload delay有时候会非常小。
                        //发送比较情况
                        situation();
                        mHandler.removeCallbacks(runnable);
                        //如果是download是bottle neck,把下面这行注释掉
                        mHandler.post(runnable);
                        //投机取巧的方法，当移动到node2时，标签重新变成start，这个时候delay是uploaddelay，
                        //当移动到新的node之后，上传完end包时，进入congestion 这个时候congestion无限大，等同于停止，然后手动停止实验。
                        congestion =10000000;
                        // 开启上传下载服务
//                        startRun();
//                        if(countflag==1){
//                            countflag=0;
//                        }

                        break;
                    case 1000:


                        // 切换wifi 停止上传下载
                        switchWifi("我不切换了");

                        break;
                    case 1001:

                        break;

//                    case 200:
//
//                        Date date=new Date();
//                        DateFormat format= new SimpleDateFormat("yy-MM-dd   HH : mm : ss : SSS");
//                        String time = format.format(date);
//                        
//                        String content = count1 + split + time + "\n";
//                        writeFile(Constants.filePath+"upload.txt",content);
//                        count1++;
//                        
//                        break;
//                    case 201:
//
//                        Date date2=new Date();
//                        DateFormat format2= new SimpleDateFormat("yy-MM-dd   HH : mm : ss : SSS");
//                        String time2 = format2.format(date2);
//                        
//                        String content2 = downloadCount + split + time2 + "\n";
//                        writeFile(Constants.filePath+"download.txt",content2);
//                        break;
                }

            }
        };
    }




    private ServerSocket serverSocket;
    // 控制下载是否启动
    private boolean isDownloadRunning = true;
    public static final int DOWNLOAD_PORT = 20001;//下载端口号
    public void serverStart() {
        try {
            serverSocket = new ServerSocket(DOWNLOAD_PORT);
            while (true) {

                if(isDownloadRunning){
                    // D.out("接收程序");
                    // 一旦有堵塞, 则表示服务器与客户端获得了连接
                    Socket client = serverSocket.accept();
                    // 处理这次连接

                    new HandlerThread(client);
                }

            }
        } catch (Exception e) {
            D.out(e);
        }
    }


    // 下载等待接收
    private class HandlerThread implements Runnable {
        private Socket socket;

        public HandlerThread(Socket client) {
            socket = client;
            new Thread(this).start();
        }

        public void run() {
            try {
                // 读取客户端数据
                DataInputStream input = new DataInputStream(socket.getInputStream());
                //String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                //通过数组接收来自服务器的东西，设置为1024，
                byte[] by = new byte[1024];
                input.read(by);
               
                String clientInputStr = new String(by).trim();
                String[] strings = clientInputStr.split(split);
                // 处理客户端数据
                D.out("Downloaded from Server:" + clientInputStr);
                if(!TextUtils.isEmpty(clientInputStr)) {


//                    Message msg = new Message();
//                    msg.obj = clientInputStr;
//                    msg.what=1;
//                    ManagerComm.handler.sendMessage(msg);

                    if (strings[0].equals("start")||strings[0].equals("pre|start")) {

                        out.writeUTF("OK");
                        // 计算下载开始时间
                        long downloadStart = System.currentTimeMillis();//写入arraylist用于计算
                        String downloadStartStamp = Utils.getCurrentTimeStamp();//写入文档用于输出

                        //包1上传开始时间等价于job上传时间
                        //long endTime = System.currentTimeMillis();
                        //D.out("time :"+count+"=="+(endTime-startTime));


                        for (Time time : ManagerComm.timeList) {
                            if (time.getCount() == downloadCount+1) {
                                time.setDownloadStart(downloadStart);
                                String content = time.getCount() + "\t" + downloadStartStamp + "\t";
                                writeFile(Constants.filePath + "download.txt", content);
                            }
                        }


                        //String msgStr = msg.obj+"";
                        downLoadStr.add(clientInputStr);

                        //这边加解析包的东西，格式是“start/end==seq==data==手机的mac地址”
                        //upload的地方同一个seq要传两次，一次start，一次end


                        //compute time要传到congestion control里
                        //string[0]


                        // strings[3]是data  strings[4] 是服务器mac地址
                        //downloadCount++;
                       // out.writeUTF("OK");
                        resultTv.setText(strings[0] + split +strings[1] + split + " Downloaded\n" + resultTv.getText().toString());


                        // 下载成功写进文件
//                    ManagerComm.handler.sendEmptyMessage(201);
                    }

                    if (strings[0].equals("end")||strings[0].equals("pre|end")) {

                        out.writeUTF("OK");
                        // 计算下载开始时间
                        long downloadEnd = System.currentTimeMillis();//写入arraylist用于计算
                        String downloadEndStamp = Utils.getCurrentTimeStamp();//写入文档用于输出

                        //包1上传开始时间等价于job上传时间
                        //long endTime = System.currentTimeMillis();
                        //D.out("time :"+count+"=="+(endTime-startTime));


                        for (Time time : ManagerComm.timeList) {
                            if (time.getCount() == downloadCount+1) {
                                time.setDownloadEnd(downloadEnd);
                                String content = time.getCount() + "\t" + downloadEndStamp + "\n";//格式要改
                                writeFile(Constants.filePath + "download.txt", content);
                            }
                        }

                        long uploadtemp = ManagerComm.timeList.get(0).getUploadEnd()-ManagerComm.timeList.get(0).getUploadStart();

                        long downloadtemp = ManagerComm.timeList.get(0).getDownloadEnd()-ManagerComm.timeList.get(0).getDownloadStart();


                        congestionControl(uploadtemp,computeDelay,downloadtemp);
                        ManagerComm.timeList.remove(0);


                        //String msgStr = msg.obj+"";
                        downLoadStr.add(clientInputStr);

                        //这边加解析包的东西，格式是“start/end==seq==data==手机的mac地址”
                        //upload的地方同一个seq要传两次，一次start，一次end


                        //compute time要传到congestion control里
                        //string[0]


                        // strings[3]是data  strings[4] 是服务器mac地址
                        downloadCount++;
                        //out.writeUTF("OK");
                        resultTv.setText(strings[0]+ split +strings[1] + split + " Downloaded\n" + resultTv.getText().toString());


                        // 下载成功写进文件
//                    ManagerComm.handler.sendEmptyMessage(201);
                    }




                }

//                // 向客户端回复信息
//                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
//                // 发送键盘输入的一行
//                //String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
//                out.writeUTF("OK");
                out.close();
//

                input.close();

            } catch (Exception e) {
                D.out(e);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        socket = null;
                        D.out(e);
                    }
                }
            }
        }
    }

    public static String getMac() {
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            String line;
            while ((line = input.readLine()) != null) {
                macSerial += line.trim();
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return macSerial;
    }





    public void congestionControl(long uploadtemp,long computetemp,long downloadtemp){
        //Time time = ManagerComm.timeList.get(count-2);
        //long UpTime = time.getUploadTime();
        //long OprTime = time.getOperaTime();
        //long DlTime = time.getDownloadTime();
        long EsUpTime = (long)(0.125 * uploadtemp + 0.875 * waitTime1);
        double devT1 = 0.25 * (Math.abs(uploadtemp - waitTime1)) + 0.75 * dev1;
        long EsOprTime = (long)(0.125 * computetemp + 0.875 * waitTime2);
        double devT2 = 0.25 * (Math.abs(computetemp - waitTime2)) + 0.75 * dev2;
        long EsDlTime = (long)(0.125 * downloadtemp + 0.875 * waitTime3);
        double devT3 = 0.25 * (Math.abs(downloadtemp - waitTime3)) + 0.75 * dev3;
        //long waitTime;
        if ((EsUpTime + 2 * devT1) >= (EsOprTime + 2 * devT2) && (EsUpTime + 2 * devT1) >= (EsDlTime + 2 * devT3)) {
            congestion = (long)(EsUpTime + 2 * devT1);
        }
        else if ((EsOprTime + 2 * devT2) >= (EsUpTime + 2 * devT1) && (EsOprTime + 2 * devT2) >= (EsDlTime + 2 * devT3)) {
            congestion = (long)(EsOprTime + 2 * devT2);
        }
        else{
            congestion = (long)(EsDlTime + 2 * devT3);
        }

        waitTime1 = EsUpTime;
        waitTime2 = EsOprTime;
        waitTime3 = EsDlTime;
        dev1 = devT1;
        dev2 = devT2;
        dev3 = devT3;
    }



    // 是否要停止服务
    private boolean isStop = false;
    public void switchWifi(String message){
        uploadSwitch(message);
       // upload(message);
        switchflag =1;
        isStop = true;

//        ManagerComm.handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                stopRun();
//            }
//        },1000);

    }

    /**
     * 停止所有下载与上传
     */
    public void stopRun(){
        mHandler.removeCallbacks(runnable);
        isStart = false;
        //是否重置序号
        //count=1;
        //isDownloadRunning = false;
        //T.showToast(this_,"切换停止成功");
    }

    /**
     * 启动所有下载与上传
     */
    public void startRun(){
        if(!isStart){
           // msgTv.setText("");
            //limit = count + 5 -1;
            //mHandler.postDelayed(runnable, 10);
            isStart = true;
        }
        isStop = false;
        //isDownloadRunning = true;
        T.showToast(this_,"Restart upload and download successfully");
    }

    /**
     * 切换后比较上传下载情况
     */
    public void situation(){
        String uploadStr = "UploadSeq：";
        for(int i=1;i<count;i++){
            uploadStr = uploadStr + i + "、";
        }

        String downloadStr = "DownloadSeq：";
        for(int i=1;i<=downloadCount;i++){
            downloadStr = downloadStr + i + "、";
        }

        int diff = count-downloadCount;
        String diffStr = "";
        if(diff>0){
            //for(int i=count-1;i>count-diff;i--){
        	for(int i=count-diff+1;i<=count-1;i++){
                diffStr = diffStr + i + "??";
            }
            
            
            
            //diffStr = diffStr + "需要再次下载";
        }

        // 告诉服务器这些需要再次下载
        upload(diffStr);

        T.showToast(this_,uploadStr+"\n"+downloadStr+"\n"+diffStr);
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
}
