package com.example.androidtemplate;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidtemplate.common.BaseActivity;
import com.example.androidtemplate.common.CommonAdapter;
import com.example.androidtemplate.common.CustomDialog;
import com.example.androidtemplate.common.D;
import com.example.androidtemplate.common.T;
import com.example.androidtemplate.common.ViewHolder;
import com.example.androidtemplate.manager.ManagerApp;
import com.example.androidtemplate.manager.ManagerComm;
import com.example.androidtemplate.manager.ManagerConf;
import com.example.androidtemplate.mo.WifiDesc;
import com.example.androidtemplate.mo.WifiInfoc;
import com.example.androidtemplate.utils.GsonUtil;
import com.example.androidtemplate.utils.ListUtil;
import com.example.androidtemplate.utils.Utils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {



    @Bind(R.id.left_tv)
    TextView leftTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_tv)
    TextView rightTv;
    @Bind(R.id.title_ll)
    LinearLayout titleLl;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.content_ll)
    LinearLayout contentLl;

    CommonAdapter adapter;
    @Bind(R.id.re_btn)
    Button reBtn;


    private String ap1 = "My1";
    private String ap2 = "My2";
    private String ap3 = "My3";
    //private ArrayList<Integer> ap0dbm;
   // private ArrayList<Integer> ap1dbm;
    
    Date date1;
    Long time1;
    

    /** 扫描完毕接收器 */
    private WifiReceiver receiverWifi;

    WifiManager wifiManager;

    ArrayList<ScanResult> list;   //存放周围wifi热点对象的列表
   // private List<ScanResult> list;

    private ProgressDialog dialog;


    WifiAutoConnectManager wac;

    @Override
    protected void initData() {
        setContentView(R.layout.b_activity_main);
        ButterKnife.bind(this);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        wac = new WifiAutoConnectManager(wifiManager);
        date1 = new Date();

        if(wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }

        list = (ArrayList<ScanResult>) wifiManager.getScanResults();
        sortByLevel(list);
        
        
        ManagerConf.saveToLocal("date_str", Utils.getCurrentDateStr());

        mHandler.postDelayed(runnable, 1);

        D.out("list num" + list.size());

        adapter = new CommonAdapter<ScanResult>(this_, list, R.layout.item_wifi) {
            @Override
            public void convert(ViewHolder helper, ScanResult item) {
                helper.setText(R.id.ssid_tv, item.SSID);
                helper.setText(R.id.leave_tv,item.level+"");
            }
        };
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               // startActivity(new Intent(this_,DescActivity.class).putExtra("wifi",list.get(position)));

                D.out(ManagerComm.wifiInfoList.toString());
                for(final WifiInfoc wifiInfoc:ManagerComm.wifiInfoList){
                    if(wifiInfoc.getSsid().equals(list.get(position).SSID)){
                        wac.connect(wifiInfoc.getSsid(), wifiInfoc.getPassword(),
                                TextUtils.isEmpty(wifiInfoc.getPassword())? WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS: WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                        D.out("Existed Wifi："+wifiInfoc.getSsid());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                WifiInfo wifiInfo = getConnectedWifi();
                                D.out(wifiInfo.toString() +wifiInfo.getIpAddress() );
                                if(wifiInfo!=null && wifiInfo.getIpAddress()!=0) {

                                    DhcpInfo di = wifiManager.getDhcpInfo();
                                    long getewayIpL=di.gateway;
                                    String getwayIpS=long2ip(getewayIpL);//网关地址
                                    D.out("getwayIpS:"+getwayIpS);
                                    if(!TextUtils.isEmpty(getwayIpS)){
                                        ManagerComm.gatewayIp = getwayIpS;
                                    }

                                    T.showToast(this_,"Connected: " +wifiInfo.getSSID());
                                    String wifiSSID = wifiInfo.getSSID().replace("\"","");
                                    D.out(wifiSSID);
                                    timeSync();
                                    ManagerComm.wifiInfoList.add(new WifiInfoc(wifiSSID,wifiInfoc.getPassword()));
                                    ManagerConf.saveToLocal("wifi", GsonUtil.getInstance().toJson(ManagerComm.wifiInfoList,new TypeToken<List<WifiInfoc>>(){}.getType()));
                                }else{

                                    T.showToast(this_,"Connection failed, try again");
                                }
                                if(dialog!=null){
                                    dialog.dismiss();
                                }
                            }
                        },3000);

                        return;
                    }
                }
                final CustomDialog customDialog = CustomDialog.getInstance(this_);
                customDialog.show();
                customDialog.setOkBtnOnClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String password = customDialog.getPassword();
                        final String ssid = list.get(position).SSID;
                        try {
                            D.out("ssid:"+ssid+" password:"+password);
                            wac.connect(ssid, password,
                                    TextUtils.isEmpty(password)? WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS: WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);

                            dialog = ProgressDialog.show(this_, "", "Connecting...");
                              new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    WifiInfo wifiInfo = getConnectedWifi();
                                    D.out(wifiInfo.toString() +wifiInfo.getIpAddress() );
                                    if(wifiInfo!=null && wifiInfo.getIpAddress()!=0) {
                                        DhcpInfo di = wifiManager.getDhcpInfo();
                                        long getewayIpL=di.gateway;
                                        String getwayIpS=long2ip(getewayIpL);//网关地址
                                        D.out("getwayIpS:"+getwayIpS);
                                        if(!TextUtils.isEmpty(getwayIpS)){
                                            ManagerComm.gatewayIp = getwayIpS;
                                        }


                                        T.showToast(this_,"Connected: " +wifiInfo.getSSID());
                                        String wifiSSID = wifiInfo.getSSID().replace("\"","");
                                        D.out(wifiSSID);
                                        timeSync();
                                        ManagerComm.wifiInfoList.add(new WifiInfoc(wifiSSID,password));
                                        ManagerConf.saveToLocal("wifi", GsonUtil.getInstance().toJson(ManagerComm.wifiInfoList,new TypeToken<List<WifiInfoc>>(){}.getType()));
                                    }else{
                                        T.showToast(this_,"Connection failed, try again");
                                    }
                                    if(dialog!=null){
                                        dialog.dismiss();
                                    }
                                }
                            },3000);



                        } catch (Exception e) {
                            D.out(e);
                        }
                        customDialog.dismiss();
                    }
                });



            }
        });

        receiverWifi = new WifiReceiver();//开始执行判断连接连接操作
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));// 注册广播


        wac.mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 操作界面
                T.showToast(this_,msg.obj+"");
                D.out(msg.obj+"");
                super.handleMessage(msg);
            }
        };

    }

    public WifiInfo getConnectedWifi(){
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo;
    }

    @Override
    protected void recycle() {
        if(receiverWifi!=null)
        unregisterReceiver(receiverWifi);
    }

    //将搜索到的wifi根据信号强度从强到弱进行排序
    private void sortByLevel(ArrayList<ScanResult> list) {
        for (int i = 0; i < list.size(); i++)
            for (int j = 1; j < list.size(); j++) {
                if (list.get(i).level > list.get(j).level)    //level属性即为强度
                {
                    ScanResult temp = null;
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        if (list.size() > 0) {
            ScanResult temp = list.get(0);
            list.remove(0);
            list.add(temp);
        }
    }


    @OnClick({R.id.left_tv, R.id.right_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_tv:
                onBackPressed();
                break;
            case R.id.right_tv:
            	startActivity(new Intent(this_,SocketActivity.class));
                break;
        }
    }


    private String long2ip(long ip){
        StringBuffer sb=new StringBuffer();
        sb.append(String.valueOf((int)(ip&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>8)&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>16)&0xff)));
        sb.append('.');
        sb.append(String.valueOf((int)((ip>>24)&0xff)));
        return sb.toString();
    }

    @OnClick(R.id.re_btn)
    public void onClick() {
        //scanWifi();
        timeSync();
    }


    private long exitTime = 0;

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            T.showToast(this_, "Click to exit");
            exitTime = System.currentTimeMillis();
        } else {

            super.onBackPressed();
            ManagerApp.exitApp();
        }

    }


    int ap1_dbm =-200;
    int ap2_dbm =-201;
    int ap3_dbm =-202;


    //判断三个wifi强度
    private String Change_AP (ArrayList<ScanResult> list){

        for (int i =0; i< list.size(); i++){
            if (list.get(i).SSID.equals(ap1)){
                ap1_dbm = list.get(i).level;
            }
            if (list.get(i).SSID.equals(ap2)){
                ap2_dbm = list.get(i).level;
            }
            if (list.get(i).SSID.equals(ap3)){
                ap3_dbm = list.get(i).level;
            }
        }

        ArrayList<WifiDesc> wifiDescs = new ArrayList<WifiDesc>();
        wifiDescs.add(new WifiDesc(ap1,ap1_dbm));
        wifiDescs.add(new WifiDesc(ap2,ap2_dbm));
        wifiDescs.add(new WifiDesc(ap3,ap3_dbm));

        Collections.sort(wifiDescs, new Comparator<WifiDesc>() {
            @Override
            public int compare(WifiDesc lhs, WifiDesc rhs) {
                return rhs.getLevel()-lhs.getLevel();
            }
        });

        D.out(wifiDescs.get(0).getLevel()+" " + wifiDescs.get(0).getSSID());


        return wifiDescs.get(0).getSSID();
    }


    List<String> changeAPTempList = new ArrayList<String>();
    boolean isTempCheck = false;
    String tempCheckWifi = null;
    boolean change_ap =false;

    //int switchflag =0;
    

    class WifiReceiver extends BroadcastReceiver {
    	Long time2;
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                //计算出从开始检测wifi到检测到wifi时间
                //这边时间是对的。
                Date date2 = new Date();
                time2 = date2.getTime();
                Long count_find_wifi = time2 -time1; 
                //Toast toast_scan = Toast.makeText(this_, "scan:"+ (count_find_wifi.toString()), Toast.LENGTH_SHORT);
                //toast_scan.show();

               // Toast.makeText(context, "扫描完毕", Toast.LENGTH_LONG).show();
//                Intent in = new Intent();
//                in.setClass(WifiTester.this, WifiListViewActivity.class);
//                WifiTester.this.startActivity(in);
                list.clear();
                list.addAll( wifiManager.getScanResults());
                sortByLevel(list);
                D.out("list num" + list.size());
                adapter.notifyDataSetChanged();
                //T.showToast(this_,"重新扫描成功");
                ManagerConf.saveToLocal("date_str",Utils.getCurrentDateStr());

                //T.showToast(this_,count_find_wifi.toString());     
                D.out(ManagerComm.wifiInfoList.toString());
                
             
				
					if (ListUtil.listIsNotNull(list)) {

                        // 得到信号较强的 ssid
						final String changeAP = Change_AP(list);
						String oldWifi = getConnectedWifi().getSSID().replace("\"", "");
						D.out("oldWifi:"+oldWifi);
						//boolean change_ap =false;
//						if (oldWifi == null){
//							change_ap = true;
//						}else {


                        // 比较三次判断，都是同一个最强的发送切换
                        if(changeAPTempList.size()<3) {
                            changeAPTempList.add(changeAP);
                        }
                        if(changeAPTempList.size()==3){
//                            if(oldWifi == null){
//                                change_ap =true;
//                            }else{


                                boolean temp = changeAPTempList.get(0).equals(changeAPTempList.get(1)) && changeAPTempList.get(1).equals(changeAPTempList.get(2));
                                if(!temp){
                                    change_ap = false;
                                }else {
                                    if (temp && oldWifi.equals(changeAPTempList.get(0))) {
                                        change_ap = false;
                                    } else {
                                        change_ap = true;
                                    }
                                }
                                //}
                            changeAPTempList.remove(0);
                            // 清空，重新进行三次判断
                            //changeAPTempList.clear();
                        }//else{
                            //if(change_ap){
                               // change_ap =false;
                            //}

                            //changeAPTempList.remove(0);
                        //}

                        D.out("changeap:" + change_ap);
                        // 发送完切换通知后重新扫描再判断一次
                        if(isTempCheck && !TextUtils.isEmpty(tempCheckWifi)){
                            // 最强的变化了 发送我不切换了
                            if(!changeAP.equals(tempCheckWifi)){
                                Message message = new Message();
                                message.what = 1000;
                                message.obj = changeAP;
                                ManagerComm.handler.sendMessage(message);
                            }
                            isTempCheck = false;
                            tempCheckWifi = null;
                        }

						//}
						
						//T.showToast(this_,changeAP);
						//T.showToast(this_, oldWifi);
						//boolean change_ap = ChangeAP(list,oldWifi);
						if (change_ap){
                         //change_ap = false;
                            //switchflag =1;
                            if(ManagerComm.handler!=null){
                                // 通知要切换了
                                Message message = new Message();
                                message.what = 100;
                                // 将ssid传过去
                                message.obj = changeAP;
                                ManagerComm.handler.sendMessage(message);

                                // 发送切换通知了
                                isTempCheck = true;
                                tempCheckWifi = changeAP;

                                // 延迟2S之后切换 连接成功 发送通知
                                ManagerComm.handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (final WifiInfoc wifiInfoc : ManagerComm.wifiInfoList) {
                                            if (wifiInfoc.getSsid().equals(changeAP)) {
                                                Toast toast_connect = Toast.makeText(this_, "hahahahahah", Toast.LENGTH_SHORT);
                                                toast_connect.show();
                                                D.out("AAAAAAAAAAAAAAAAAAAAA");
                                            	
                                            	
                                                wac.connect(wifiInfoc.getSsid(), wifiInfoc.getPassword(),
                                                        TextUtils.isEmpty(wifiInfoc.getPassword())
                                                                ? WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS
                                                                : WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                                                D.out("Existing wifi: " + wifiInfoc.getSsid());

                                                if(ManagerComm.countflag ==1){
                                                ManagerComm.countflag =0;
                                                 }




                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        WifiInfo wifiInfo = getConnectedWifi();
                                                        D.out(wifiInfo.toString() + wifiInfo.getIpAddress());
                                                        if (wifiInfo != null && wifiInfo.getIpAddress() != 0) {

                                                            DhcpInfo di = wifiManager.getDhcpInfo();
                                                            long getewayIpL=di.gateway;
                                                            String getwayIpS=long2ip(getewayIpL);//网关地址
                                                            D.out("getwayIpS:"+getwayIpS);
                                                            if(!TextUtils.isEmpty(getwayIpS)){
                                                                ManagerComm.gatewayIp = getwayIpS;
                                                                // 切换wifi连接成功通知
                                                                ManagerComm.handler.sendEmptyMessage(101);
                                                                //switchflag =0;
                                                                //if(ManagerComm.countflag ==1){
                                                                    //ManagerComm.countflag =0;
                                                               // }
                                                            }


                                                            String wifiSSID = wifiInfo.getSSID().replace("\"", "");
                                                            D.out(wifiSSID);
                                                            ManagerComm.wifiInfoList
                                                                    .add(new WifiInfoc(wifiSSID, wifiInfoc.getPassword()));
                                                            ManagerConf.saveToLocal("wifi", GsonUtil.getInstance()
                                                                    .toJson(ManagerComm.wifiInfoList, new TypeToken<List<WifiInfoc>>() {
                                                                    }.getType()));


                                                            //T.showToast(this_,count_connect_wifi.toString());
                                                        } else {
                                                            //T.showToast(this_, "Connection failed, try again");
                                                        }
                                                        if (dialog != null) {
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                },2000);
                                                break;
                                            }
                                        }
                                    }
                                },2000);
                            }
						}
					}
				
                //这里扫描时间1.2秒，migration2秒，handover2秒，但是实际测试出来发现Handover永远是3.2秒。。。这是一个问题
				// if(dialog!=null)
				// dialog.dismiss();
			}

		}
	}


    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
        	//这边是开始检测wifi的时间
        	date1 = new Date(); 
        	time1 = date1.getTime();
            scanWifi();
            mHandler.postDelayed(runnable, 1200);
        }
    };

    /**
     * 扫描WIFI 加载进度条
     */
    void scanWifi() {
        //T.showToast(this_,"Re-scanning wifi");
        //OpenWifi();
        if(wifiManager.isWifiEnabled()){
            wifiManager.startScan();
        }

//        dialog = ProgressDialog.show(this_, "", "重新扫描...");
//        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//             public void onDismiss(DialogInterface dialog) {
//                T.showToast(this_,"重新扫描成功");
//            }
//        });

    }

    /**
     * 打开WIFI
     */
    public void OpenWifi() {

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

    }

    /**
     * 关闭WIFI
     */
    public void CloseWifi() {

        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

    }

    public static String IP_ADDR = "192.168.12.1";//服务器地址
    public static int port = 9997;
    private SocketStringManager socketStringManager;
    private void timeSync() {
        if (socketStringManager == null) {
            socketStringManager = new SocketStringManager(ManagerComm.handler);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketStringManager.SendString(IP_ADDR, port, "时间戳");
            }
        }).start();
    }

}
