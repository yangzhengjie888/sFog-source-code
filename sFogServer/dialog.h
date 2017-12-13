#ifndef DIALOG_H
#define DIALOG_H

#include <QDialog>
#include <QNetworkInterface>
#include <QDateTime>
#include <QStringList>
#include <QHostInfo>
#include <QTcpServer>
#include <QTcpSocket>
#include <stdio.h>

#include <QWidget>
#include <QtNetwork>

#include "packet.h"
#include "downloadthread.h"
#include "downloadthreadstart.h"
#include "sendtoapthread.h"
#include "sendtoapthreadstart.h"
#include "receivetaskthread.h"
#include "computetaskthread.h"
#include "fakedownloaddelay.h"
#include "fakemigrationdelay.h"


namespace Ui {
class Dialog;
}

class Dialog : public QDialog
{
    Q_OBJECT

public:
    explicit Dialog(QWidget *parent = 0);
    ~Dialog();

private slots:
    void on_startpushButton_clicked();
    void updateStatus();
    void serverReadMessage();
//    void serverSendResult(Packet *tempPacket);
    void serverCompute();
    void updateSendStatus();
    void on_stoppushButton_clicked();

    //做为server的
    //void connectAP_as_server();
    void newConnect_as_server();
    void readMessage_as_server();
    //做为client的
    void readMessage_as_client();
    void readMessage_as_client_start();
    //void connectAP_as_client();
    void sendMessage_as_client();
    void sendMessage_as_client_start();
    //server把处理结果发送给客户端
    void downloadService();
    void downloadService_start(int);
    void sendToApService();
    void sendToApService_start();

    void server_connect_client();
    //void downloadService1();
    void readMessage_from_client();
    void sendMessage_to_client();
    void delete_m_data_result();


    //server把处理的结果的start包发送给客户端
    void readMessage_from_client_start();
    void sendMessage_to_client_start();

    //做为server控制信息

    void newConnect_control();
    void readMessage_control();

    //时间同步
    void newConnect_timeSyn();
    void readMessage_timeSyn();



    void changeState();


    //各种service
    //void receiveTaskService();
    //void computeTaskService();




public:
    QList<Packet*> m_data_in;//用于存放接收的未处理的数据的第二个包,目前这个没有用到，只是存起来了。
    QList<Packet*> m_data_in_start;//用于存放接收的未处理的数据的第一个包
    QList<Packet*> m_data_result;//用于存放手机在本机上传后处理的结果，并在手机在state3之前发送数据,即在本机上传本机下载的结果
                                 //是先判断requestold是否完成，requestold queue里的东西优先
                                 //如果requestold完成（即判断QStringList是否为空），清零m_data_request_old和requestold的QSTringList
                                 //然后再发送m_data_result里的东西，
                                 //这个queue，每发送一个东西以后，清除一个
                                 //(OK)在手机断开之后，清空
    QList<Packet*> m_data_result_start;
    QStringList m_data_request_old;//这个是queue
                                    //用于在ap2上存放premigration的时候从ap1到ap2的结果，记住需要清零
                                   //在手机从state 2 到state 3之间给ap发送东西
                                   //当手机requestold的时候，这里面的东西肯定有requestold的东西，但是有可能大于requestold的东西
                                   //这个queue发送的时候不需要清空，在requestold为O的时候，清空
    QStringList m_data_request_old_start;//第一个包
    QStringList m_data_request_old_send;//这个用来在作为client的server发送处理好的包的queue，第二个包
    QStringList m_data_request_old_send_start;
    QStringList requestold;//这个是用来接收requestold的seqnum的

    QString data_result_temp;//这个OK了
                             //用于存放临时需要从ap传递到ap的结果
                             //这个是专门用于从ap到ap传数据的，即state2到state3之间给另一个ap传数据
    QString data_result_temp_start;
    QString data_result_temp_to_client;//这个是用于临时存放下载给client的
    QString data_result_temp_to_client_start;
    //volatile表示任何时候这个值都是最新的
    volatile int state =1;//记录手机状态，现在只有一个手机
                 //0:断开，1：连接，2：premigration
    volatile int ifstart = 0;
    volatile int ifcompute =0;//判断是否进入模拟compute
    volatile int ifdownload =0;//判断是否进入模拟下载
    volatile int ifsend =0;//判断是否进入传给AP
    volatile int uploadflag =0;//暂时没用到
    volatile int sendToApThreadCount =0;
    volatile int downloadThreadCount =0;
    //boolean ifconnected = false;//记录手机是否连接
    volatile int resendToAPFlag =0;//判断ap1是否要重新发一下下载的包头给ap2


    //用一个线程来判断是否连接，每次下载完一个包都要start一下thread，thread设计成只执行一次的那种。

    //全局变量：
    QString hostIP = "192.168.12.1";
    quint16 timeSyn = 9997;
    quint16 apServerControlPort = 8888;// 做为server让client连接，并接收控制信息

    //如果是三个node的话，node1只发送不接收，node2既发送又接收，node3只接收不发送

    QString apServerIP = "10.66.30.222";// 做为server让另一个ap连接，并接收数据
    quint16 apServerPort = 7777;

    QString apClientIP = "10.66.30.91";// 做为client连接另一个ap，并发送处理的结果start/end
    quint16 apClientPort = 9996;



    QString clientIP = "192.168.12.136";// 把处理结果的start/end发给client
    quint16 clientPort = 20001;



private:
    Ui::Dialog *ui;
    QTcpServer *tcpServer;
    QList<QTcpSocket *>mytcpsocket;//连接成功的套接字

    QTcpServer *tcpServer_as_server;//用于连接AP的套接字,做为server让另一个ap来连
    QTcpSocket *tcpSocket_as_server;//用于连接AP的成功的套接字，做为server接收数据，并发送ack

    QTcpSocket *tcpSocket_as_client;//用于连接AP，做为client发送数据给另一个ap

    QTcpSocket *tcpSocket_send_to_client;//用于连接client，把处理的结果发送给client，即下载

    QTcpServer *tcpServer_control;//供client发送控制信息
    QTcpSocket *tcpSocket_control;//供client发送控制信息，做为server接收控制信息，并通过该socket发送ack

    QTcpServer *tcpServer_timeSyn;//时间同步服务器
    QTcpSocket *tcpSocket_timeSyn;//用于时间同步连接成功的套接字，做为server接收数据并发送ack;

    void info_init();//信息初始化

    //公有函数，不是槽函数
    void connectAP_as_client();
    void connectAP_as_client_start();
    void connectAP_as_server();
    void connectAP_timeSyn();
    void connectAP_control();

    DownloadThread downloadThread;//用来控制check是否下载服务的
    DownloadThread downloadThread_start;//用来控制下载的第一个包的
    SendToApThread sendToApThread;//用来控制做为client发送东西给另一个ap用的
    SendToApThread sendToAPThread_start;//用来控制ap给ap的第一个包

    //ReceiveTaskThread receiveTaskThread;这个不需要
    ComputeTaskThread computeTaskThread;//用来模拟compute延迟的
    FakeDownloadDelay fakeDownloadDelay;//用来模拟download延迟的
    FakeMigrationDelay fakeMigrationDelay;//用来模拟migrate延迟的


    void server_connect_client_start();
    void checkReMigration();







};

#endif // DIALOG_H
