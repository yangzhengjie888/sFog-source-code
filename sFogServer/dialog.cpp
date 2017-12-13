#include "dialog.h"
#include "ui_dialog.h"
#include <sys/types.h>


#include <QDebug>
#include <sys/socket.h>

#include "unistd.h"
#include <unistd.h>


Dialog::Dialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::Dialog)
{
    ui->setupUi(this);
    info_init();

}
Dialog::~Dialog()
{
    delete ui;
}


///////////////////////////////////////////////////////////////////////////////////////////


void Dialog::connectAP_timeSyn()// 时间同步
{
    tcpServer_timeSyn = new QTcpServer(this);
    tcpSocket_timeSyn = new QTcpSocket(this);
    tcpServer_timeSyn->listen(QHostAddress(hostIP),timeSyn);
    connect(tcpServer_timeSyn,SIGNAL(newConnection()),this,SLOT(newConnect_timeSyn()));

}

void Dialog::newConnect_timeSyn()
{
    //qDebug() << "111";
    tcpSocket_timeSyn = tcpServer_timeSyn->nextPendingConnection();
    //qDebug() << "222";

    connect(tcpSocket_timeSyn,SIGNAL(readyRead()),this,SLOT(readMessage_timeSyn()));
    connect(tcpSocket_timeSyn,SIGNAL(disconnected()),tcpSocket_timeSyn,SLOT(deleteLater()));

}

void Dialog::readMessage_timeSyn()
{
    QByteArray in = tcpSocket_timeSyn->readAll();
    //QString message(in);
    qint64 curTime = QDateTime::currentMSecsSinceEpoch();
    QString curTimeString = QString::number(curTime,10);

    //开始转换编码
    QTextCodec *utf8codec = QTextCodec::codecForName("UTF-8");
    QString timemsg = utf8codec->toUnicode(in.mid(2));

    qDebug()<<curTime<<": client time: "<<timemsg;

    QByteArray datasend;
    datasend.append(curTimeString);
    tcpSocket_timeSyn->write(datasend);
    qDebug()<<": client time response: "<<curTime;


}

//////////////////////////////////////////////////////////////////////////////////////////

void Dialog::checkReMigration()
{

    //qDebug()<<"Re Migration check start!";

    if(!requestold.isEmpty()){
        //qDebug()<<"Re Migration checklist is not empty !!";
        for(int i =0; i<m_data_request_old.size();i++){
            QStringList dataStrTemp = m_data_request_old[i].split("==");//0:mark,1:seqNum,2:data,3:mac
            //Packet *tempPacket = new Packet(dataStr[0],dataStr[1],dataStr[2]);//0:seqNum,1:data,2:mac
            //m_data_request_old[i]
            if(QString::compare(requestold[0],dataStrTemp[1],Qt::CaseSensitive)==0){

                m_data_request_old_send_start.push_back(m_data_request_old_start[i]+"|re");
                m_data_request_old_send.push_back(m_data_request_old[i]+"|re");



                    if(ifsend ==0){
                        ifsend =1;
            //            if(resendToAPFlag ==0){
            //                ifresend = 1;
            //            }
                        qDebug()<<"Re Migration start!!!";
                        connectAP_as_client_start();
                    }

//                    //download开始时间
//                    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
//                    QStringList dataStrTemp2 = data_result_temp_to_client_start.split("==");
//                    //添加开始下载时间戳

//                    qDebug()<<dataStrTemp2[0]<<"=="<<dataStrTemp2[1]<<"=="<<dataStrTemp2[2]<<" ==== download starts: "<<curTime;

            }
        }
    }
}





//////////////////////////////////////////////////////////////////////////////////////////

void Dialog::connectAP_control()// 做为server让client连接，并接收控制信息
{
    tcpServer_control = new QTcpServer(this);
    tcpSocket_control = new QTcpSocket(this);
    tcpServer_control->listen(QHostAddress(hostIP),apServerControlPort);
    connect(tcpServer_control,SIGNAL(newConnection()),this,SLOT(newConnect_control()));

}

void Dialog::newConnect_control()
{
    state =2;
    //这边加一个判断然后执行一系列操作，并触发一次sent to ap service
    checkReMigration();

    if(ifsend ==1){
        resendToAPFlag =1;
    }

    //判断包在download的过程中接收到premigration信号以后需要把这个包传给node2
    if(ifdownload ==1){
        m_data_request_old_send.push_back(m_data_result[0]->getMark()+"=="+
                m_data_result[0]->getSeqNum()+"=="+m_data_result[0]->getData()+"=="+m_data_result[0]->getMac());
        m_data_request_old_send_start.push_back(m_data_result_start[0]->getMark()+"=="+m_data_result_start[0]->getSeqNum()+"=="+
                m_data_result_start[0]->getData()+"=="+m_data_result_start[0]->getMac());

        if(ifsend ==0){
            ifsend =1;
            qDebug()<<"connect AP as client start";
            connectAP_as_client_start();
        }

    }





    //qDebug() << "111";
    tcpSocket_control = tcpServer_control->nextPendingConnection();
    //qDebug() << "222";

    connect(tcpSocket_control,SIGNAL(readyRead()),this,SLOT(readMessage_control()));
    connect(tcpSocket_control,SIGNAL(disconnected()),tcpSocket_control,SLOT(deleteLater()));

}

void Dialog::readMessage_control()//这个目前只是发送一个信号，node直接切换下就好。
                                  //requestold的信息是通过之前上传的端口传过来的，因为程序设计是先requestold,然后再重新开启上传任务
{
    QByteArray in = tcpSocket_control->readAll();


    //记录时间
    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
//    int curtime = QTime::currentTime().msecsSinceStartOfDay();


    //开始转换编码
    QTextCodec *utf8codec = QTextCodec::codecForName("UTF-8");
    QString controlmsg = utf8codec->toUnicode(in.mid(2));
    qDebug()<<"\n";
    qDebug()<<"hex:["<<in.toHex().toUpper()<<"]";
//    qDebug()<<"receive time is: "<<curTime;
    qDebug()<<curTime<<": utf-8 ["<< (controlmsg) << "]";

    //control message格式为：【  "Switching"+"=="+ssid  】


    qDebug() << "control message: " << controlmsg;
    //message = "hi" + message + "\n";
    //这边要处理下收到的数据，添加数组啥的

}

//////////////////////////////////////////////////////////////////////////////////////

void Dialog::connectAP_as_server()// 做为server让另一个ap连接，并接收数据,并发送返回
{
    tcpServer_as_server = new QTcpServer(this);
    tcpSocket_as_server = new QTcpSocket(this);
    tcpServer_as_server->listen(QHostAddress(apServerIP),apServerPort);
    connect(tcpServer_as_server,SIGNAL(newConnection()),this,SLOT(newConnect_as_server()));

}

void Dialog::newConnect_as_server()
{
    //qDebug() << "111";
    tcpSocket_as_server = tcpServer_as_server->nextPendingConnection();
    //qDebug() << "222";

    connect(tcpSocket_as_server,SIGNAL(readyRead()),this,SLOT(readMessage_as_server()));
    connect(tcpSocket_as_server,SIGNAL(disconnected()),tcpSocket_as_server,SLOT(deleteLater()));

}

void Dialog::readMessage_as_server()
{
    QByteArray in = tcpSocket_as_server->readAll();
    QString message(in);
    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    qDebug() <<curTime<< "server receive packet from another ap: " << message;
    //message = "hi" + message + "\n";
    //这边要处理下收到的数据，添加数组啥的
    QStringList dataStrTemp1 = message.split("==");//0:mark,1:seqNum,2:data,3:mac
    //当是start，放到start里，当是end,放到end里
    if(QString::compare(dataStrTemp1[0],"start",Qt::CaseSensitive)==0){
        m_data_request_old_start.append(message);
    }

    if(QString::compare(dataStrTemp1[0],"end",Qt::CaseSensitive)==0){
        m_data_request_old.append(message);
        downloadThread_start.start();
        if(!requestold.isEmpty()){
            checkReMigration();
        }
    }


    QByteArray datasend;
    datasend.append("OK1");
    tcpSocket_as_server->write(datasend);


}

//////////////////////////////////////////////////////////////////////////////////

void Dialog::connectAP_as_client_start()// 做为client连接另一个ap，并发送处理的结果start
{
    tcpSocket_as_client=new QTcpSocket(this);
    tcpSocket_as_client->abort();
    tcpSocket_as_client->connectToHost(QHostAddress(apClientIP),apClientPort); //连接到主机

    connect(tcpSocket_as_client,SIGNAL(connected()),this,SLOT(sendMessage_as_client_start()));
    connect(tcpSocket_as_client,SIGNAL(readyRead()),this,SLOT(readMessage_as_client_start()));




}

void Dialog::readMessage_as_client_start()
{
    QByteArray data=tcpSocket_as_client->readAll();
    //QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    //qDebug()<<curTime<<"ap receive ack from another ap:"<<data;
    if(QString::compare(QString(data),"OK1")==0){
        //sendToApThread.start();
        fakeMigrationDelay.start();
    }
}

void Dialog::sendMessage_as_client_start()//这边也许需要加变量
{
    QByteArray data;
    //qDebug()<<"send requestold packet to another ap: "<< data_result_temp;

    data.append(m_data_request_old_send_start[0]);//这个地方是send已经处理好的结果

    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    qDebug()<<curTime<<"send requestold packet to another ap [start]: "<< m_data_request_old_send_start[0];
    //m_data_request_old_send_start.removeFirst();
    tcpSocket_as_client->write(data);


}

///////////////////////////////////////////////////////////////////////////////////




void Dialog::connectAP_as_client()// 做为client连接另一个ap，并发送处理的结果end
{
    tcpSocket_as_client=new QTcpSocket(this);
    tcpSocket_as_client->abort();
    tcpSocket_as_client->connectToHost(QHostAddress(apClientIP),apClientPort); //连接到主机

    connect(tcpSocket_as_client,SIGNAL(connected()),this,SLOT(sendMessage_as_client()));
    connect(tcpSocket_as_client,SIGNAL(readyRead()),this,SLOT(readMessage_as_client()));




}

void Dialog::readMessage_as_client()
{
    QByteArray data=tcpSocket_as_client->readAll();
    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    qDebug()<<curTime<<"ap receive ack from another ap [end]:"<<data;
    if(QString::compare(QString(data),"OK1")==0){
        ifsend =0;
        sendToAPThread_start.start();//对吗？
    }
}

void Dialog::sendMessage_as_client()//这边也许需要加变量
{
    QByteArray data;
    //qDebug()<<"send requestold packet to another ap: "<< data_result_temp;

    data.append(m_data_request_old_send[0]);//这个地方是send已经处理好的结果

    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    qDebug()<<curTime<<"send requestold packet to another ap: "<< m_data_request_old_send[0];
    m_data_request_old_send_start.removeFirst();
    m_data_request_old_send.removeFirst();
    tcpSocket_as_client->write(data);


}

///////////////////////////////////////////////////////////////////////////////////


void Dialog::info_init()//信息初始化，主要是获取本地IP
{
    QList<QHostAddress>address=QNetworkInterface::allAddresses();
    foreach(QHostAddress add,address)
    {
        if(add.protocol()==QAbstractSocket::IPv4Protocol)//这边添加ip address
        {
             ui->comboBox->addItem(add.toString());
        }
    }
    ui->portlineEdit->setText("12345");
    QString strTcpInfo="";
    QList<QNetworkInterface>list = QNetworkInterface::allInterfaces();
    //获取所有网络接口的列表
    foreach(QNetworkInterface interface,list)
    {
        //遍历每一个网络接口
        //qDebug() <<"Device:"<<interface.name();
        strTcpInfo+="Device:"+interface.name()+"\n";
        //设备名
        //qDebug() <<"HardwareAddress: "<<interface.hardwareAddress();
        strTcpInfo+="HardwareAddress: "+interface.hardwareAddress()+"\n";
        //硬件地址
        QList<QNetworkAddressEntry>entryList = interface.addressEntries();
        //获取IP地址条目列表，每个条目中包含一个IP地址，一个子网掩码和一个广播地址
        foreach(QNetworkAddressEntry entry,entryList)
        {
            if(entry.ip().protocol()==QAbstractSocket::IPv4Protocol && entry.ip().toString()!="127.0.0.1")
            {
                //遍历每一个IP地址条目
                //qDebug()<<"IP Address: "<<entry.ip().toString();
                strTcpInfo+="IP Address: "+entry.ip().toString()+"\n";
                //IP地址
                //qDebug()<<"Netmask: "<<entry.netmask().toString();
                strTcpInfo+="Netmask: "+entry.netmask().toString()+"\n";
                //子网掩码
                //qDebug()<<"Broadcast: "<<entry.broadcast().toString();
                strTcpInfo+="Broadcast: "+entry.broadcast().toString()+"\n";
                //广播地址
            }
        }
    }
    QDateTime time = QDateTime::currentDateTime();//获取系统现在的时间
    QString strTime = time.toString("yyyy-MM-dd\nhh:mm:ss"); //设置显示格式
    strTime="System Time:\n"+strTime;
    ui->timelabel->setText(strTime);//在标签上显示时间
    ui->textBrowser->setAlignment( Qt::AlignCenter );
    ui->textBrowser->setText(strTcpInfo);
    //pushButton按钮初始化
    ui->stoppushButton->setEnabled(false);
    ui->startpushButton->setEnabled(true);
    //treeWidget初始化
//    ui->treeWidget->setColumnCount(4);
//    ui->treeWidget->setHeaderLabels(QStringList()<<"Client IP"<<"Client Port"<<"Descriptor"<<"Recive Message");
    ui->treeWidget->setColumnCount(2);
    ui->treeWidget->setHeaderLabels(QStringList()<<"               Client IP              "<<"Recive Message");
    ui->treeWidget->header()->setSectionResizeMode(QHeaderView::ResizeToContents);
    ui->treeWidget->header()->setStretchLastSection(true);

    //计算结束触发开始下载第一个包
    connect(&downloadThread_start,SIGNAL(done(int)),this,SLOT(downloadService_start(int)));//这边暂时slot先写这个，
                                                                    //在downloadservice里要根据不同的包进行不同的线程
    //关联下载线程和开始下载槽函数
    //connect(&downloadThread,SIGNAL(done(int)),this,SLOT(downloadService(int)));
    //关联node之间通信线程和发送槽函数
    //connect(&sendToApThread,SIGNAL(done(int)),this,SLOT(sendToApService(int)));
    //计算结束触发开始传给另一个AP的第一个包
    connect(&sendToAPThread_start,SIGNAL(done()),this,SLOT(sendToApService_start()));

    //关联receive task槽函数,突然发现这个不需要只需要加一个compute函数
    //connect(&receiveTaskThread,SIGNAL(done()),this,SLOT(receiveTaskService()));
    //关联compute 槽函数
    connect(&computeTaskThread,SIGNAL(done()),this,SLOT(serverCompute()));
    //关联模拟download下载时间槽函数
    connect(&fakeDownloadDelay,SIGNAL(done()),this,SLOT(downloadService()));
    //关联模拟migrationdelay和传递第二个包
    connect(&fakeMigrationDelay,SIGNAL(done()),this,SLOT(sendToApService()));
    //connect(&downloadThread,SIGNAL(finished()),this,SLOT(downloadService1()));
    //connect(&thread, SIGNAL(stringChanged(QString)), this, SLOT(changeString(QString)));







}

void Dialog::on_startpushButton_clicked()//开始（Start）按钮单击，开启服务
{
    connectAP_as_server();//开启ap和ap之间的传输链路
    connectAP_control();//开启AP和手机的控制链路
    connectAP_timeSyn();//开启时间同步控制链路

    //以上是添加的东西
    tcpServer=new QTcpServer(this);
    if(!tcpServer->listen(QHostAddress(ui->comboBox->currentText()),ui->portlineEdit->text().toInt()))
    {
        //QMessageBox::warning (this, tr("Warnning"), tcpServer->errorString ());
        QString error="listening failed: "+tcpServer->errorString();
        ui->statuslabel->setText(error);
        return;
    }
    ui->stoppushButton->setEnabled(true);
    ui->startpushButton->setEnabled(false);
    QString strTmp="IP:"+ui->comboBox->currentText()+" is listening...";
    ui->statuslabel->setText(strTmp);
    //到时候有可能会把所有的type变成QueuedConnection
    connect(tcpServer, SIGNAL(newConnection()),this, SLOT(updateStatus()),Qt::QueuedConnection);//有新客户连接

}

void Dialog::updateStatus()//有客户来连接了
{
    QTcpSocket *clientConnection=tcpServer->nextPendingConnection ();//获取socket
    QString clientInfo="Client "+clientConnection->peerAddress().toString()+" connected successfully";
    ui->statuslabel->setText(clientInfo);
    //qDebug()<<"peerAddress: "<<clientConnection->peerAddress()<<" peerName: "<<clientConnection->peerName()<<" peerPort: "<<clientConnection->peerPort();

    mytcpsocket.append(clientConnection);


    connect (clientConnection, SIGNAL(disconnected()), this, SLOT(updateSendStatus()));
    connect (clientConnection, SIGNAL(disconnected()), clientConnection, SLOT(deleteLater()));
    //connect (clientConnection, SIGNAL(disconnected()), this,SLOT(changeState()));
    connect (clientConnection, SIGNAL(readyRead()), this, SLOT(serverReadMessage()));
}


void Dialog::changeState(){
    state =1;
    //ifconnected = false;
}


////////////////////////////////////////////////////////////////////////////////////////////

void Dialog::serverReadMessage()//接受客户发送的信息
{
    for(int i=0;i<mytcpsocket.length();i++)
    {
        //qDebug()<<"bytesAvailable"<<mytcpsocket[i]->bytesAvailable()<<"    ";
        if(mytcpsocket[i]->bytesAvailable()>0)
        {
            QByteArray datarcv=mytcpsocket[i]->readAll();

            //记录时间
            QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
//            int curtime = QTime::currentTime().msecsSinceStartOfDay();


            //开始转换编码
            QTextCodec *utf8codec = QTextCodec::codecForName("UTF-8");
            QString clientmsg = utf8codec->toUnicode(datarcv.mid(2));
            qDebug()<<"\n";
            qDebug()<<"receive time is: "<<curTime<<"   ==== utf-8 ["<< (clientmsg) << "]";



            QStringList columItemList;
            QTreeWidgetItem *A;
            //columItemList<<mytcpsocket[i]->peerAddress().toString()<<QString::number(mytcpsocket[i]->peerPort())<<QString::number(mytcpsocket[i]->socketDescriptor())<<clientmsg;
            columItemList<<mytcpsocket[i]->peerAddress().toString()<<clientmsg;
            A = new QTreeWidgetItem(columItemList);

            ui->treeWidget->addTopLevelItem(A);

            //这边要加处理requestold字符串的方法
            //requestold字符串格式为： 【  1??2??3??4 】用split忽略空字符串的那个
            //把处理过后的信息放到QStringList里面，便于循环处理
            //需要加一个case，用来判断是从哪个queue里下载，
            //也许需要两个queue，一个是正常处理完的结果的queue，一个是requestold的结果的queue
            //逻辑是，balabalabalabala
            if(!clientmsg.isEmpty()){
            if(clientmsg.contains("??")){
                requestold = clientmsg.split("??",QString::SkipEmptyParts);
                ifdownload =0;
                downloadThread_start.start();//对吗

            }else{
                //把数据放入数组
                //            if(clientmsg.size()!= 0){
                QStringList dataStr = clientmsg.split("==");

                //Packet里是没有Mark的，只有在下载给手机的时候加上Mark
                Packet *tempPacket = new Packet(dataStr[0],dataStr[1],dataStr[2],dataStr[3]);//0:mark(start/end),1:seqNum,2:data,3:mac
                //m_data_in.push_back(tempPacket);
                //            }

                //发送ack给client
                //clientmsg="hi "+clientmsg+"\n";
                QByteArray datasend;
                if(QString::compare(dataStr[0],"start",Qt::CaseSensitive)==0){
                    m_data_in_start.push_back(tempPacket);
                    datasend.append("start");
                    mytcpsocket[i]->write(datasend);
                    //QString curTime1 = QTime::currentTime().toString("hh:mm:ss.zzz");
                    //int curtime1 = QTime::currentTime().msecsSinceStartOfDay();
                   //qDebug()<<dataStr[0]+dataStr[1]+dataStr[2]<<" ==== compute starts: "<<curTime1;

                }else{
                    m_data_in.push_back(tempPacket);
                    datasend.append("end");
                    mytcpsocket[i]->write(datasend);
                    QString curTime1 = QTime::currentTime().toString("hh:mm:ss.zzz");
                    //int curtime1 = QTime::currentTime().msecsSinceStartOfDay();
                   qDebug()<<dataStr[0]<<"=="<<dataStr[1]<<"=="<<dataStr[2]<<" ==== compute starts: "<<curTime1;

                    //测试上传的时候关掉
                    computeTaskThread.start();

                }



                //QTimer *timer = new QTimer(this);
                //connect(timer,SIGNAL(timeout()),this,SLOT(serverSendResult(tempPacket)));

                //connect(timer,&QTimer::timeout,this,&Dialog::serverSendResult(clientmsg));
                //处理数据并写入

              //  if(ifstart ==0){
//                    QString curTime1 = QTime::currentTime().toString("hh:mm:ss.zzz");
//                    //            int curtime1 = QTime::currentTime().msecsSinceStartOfDay();
//                    qDebug()<<dataStr[0]<<"=="<<dataStr[1]<<" ==== compute starts: "<<curTime1;
//                    computeTaskThread.start();
                  //  ifstart =1;

            }

            }
        }
        //qDebug()<<mytcpsocket[i]->socketDescriptor();//<<mytcpsocket[i]->SocketState;
    }
}

//#include <QCoreApplication>


void Dialog::serverCompute()
{


    //记录时间
//    QString curTime = QTime::currentTcomputeTaskThreadime().toString("hh:mm:ss.zzz");
//    int curtime = QTime::currentTime().msecsSinceStartOfDay();

    if(!m_data_in.isEmpty()){
        m_data_result.clear();
        m_data_result_start.clear();
        m_data_result.push_back(m_data_in[0]);
        m_data_result_start.push_back(m_data_in_start[0]);
        m_data_in.removeFirst();
        m_data_in_start.removeFirst();
        //QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
        //qDebug()<<m_data_result[0]->getSeqNum()<<"=="<<m_data_result[0]->getData()<<" ==== compute finishes: "<<curTime;
        //qDebug()<<"state:"<< state;

        //data_result_temp = m_data_result[0]->getSeqNum()+"==?"+m_data_result[0]->getData()+"==?"+m_data_result[0]->getMac();
    }

    ifcompute =0;//这个好像没啥用。


    if(ifcompute ==0){
        if(!m_data_in.isEmpty()){
        //QString curTime1 = QTime::currentTime().toString("hh:mm:ss.zzz");
        //            int curtime1 = QTime::currentTime().msecsSinceStartOfDay();
        //qDebug()<<m_data_in[0]->getSeqNum()<<"=="<<m_data_in[0]->getData()<<" ==== compute starts: "<<curTime1;
        computeTaskThread.start();

        ifcompute =1;
        }
    }
    //判断是否要发送给手机或者既发送给手机也发送给node
    if(state ==1){
               downloadThread_start.start();//启动下载第一个包线程
               //downloadThread.start();//启动下载线程
    }
    if(state ==2){

        m_data_request_old_send.push_back(m_data_result[0]->getMark()+"=="+
                m_data_result[0]->getSeqNum()+"=="+m_data_result[0]->getData()+"=="+m_data_result[0]->getMac());
        m_data_request_old_send_start.push_back(m_data_result_start[0]->getMark()+"=="+m_data_result_start[0]->getSeqNum()+"=="+
                m_data_result_start[0]->getData()+"=="+m_data_result_start[0]->getMac());

        downloadThread_start.start();
        //downloadThread.start();//启动下载线程

              //测试单个node的时候关掉
            sendToAPThread_start.start();

    }
}

//void Dialog::serverSendResult(Packet *tempPacket)//发送处理结果
//{
//    tcpSocket_send_to_client
//}


//////////////////////////////////////////////////////////////////////////////

//download service

void Dialog::sendToApService_start(){

//    if (start ==0){

//    }
    //此时state已经是2了，每次sent to AP 之前都要check下来自上一个Node的requestold
    checkReMigration();

    if(!m_data_request_old_send_start.isEmpty()){

        if(ifsend ==0){
            ifsend =1;
//            if(resendToAPFlag ==0){
//                ifresend = 1;
//            }
            qDebug()<<"connect AP as client start";
            connectAP_as_client_start();
        }
    }else{

    }

}


void Dialog::sendToApService(){

//    if (start ==0){

//    }
    if (resendToAPFlag ==1 && ifsend ==1){
        resendToAPFlag =0;
        connectAP_as_client_start();
    }else{

        if(!m_data_request_old_send.isEmpty()){
            connectAP_as_client();
        }else{

        }
    }

}

/////////////////////////////////////////////////////////////////////////////////

void Dialog::downloadService_start(int start){
    //qDebug()<<"start download one packet...";
    int flag =0;

    if (start ==0){

    }
    if(state ==1 || state ==2){
        if(!requestold.isEmpty()){
            for(int i =0; i<m_data_request_old.size();i++){
                QStringList dataStrTemp = m_data_request_old[i].split("==");//0:mark,1:seqNum,2:data,3:mac
                //Packet *tempPacket = new Packet(dataStr[0],dataStr[1],dataStr[2]);//0:seqNum,1:data,2:mac
                //m_data_request_old[i]
                if(QString::compare(requestold[0],dataStrTemp[1],Qt::CaseSensitive)==0){
                    flag =1;
                    data_result_temp_to_client.clear();
                    data_result_temp_to_client = "pre|"+m_data_request_old[i];
                    data_result_temp_to_client_start.clear();
                    data_result_temp_to_client_start = "pre|"+m_data_request_old_start[i];

//                    //download开始时间
//                    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
//                    QStringList dataStrTemp2 = data_result_temp_to_client_start.split("==");
//                    //添加开始下载时间戳

//                    qDebug()<<dataStrTemp2[0]<<"=="<<dataStrTemp2[1]<<"=="<<dataStrTemp2[2]<<" ==== download starts: "<<curTime;



                    if(ifdownload ==0){//确保是否能下载，某种程度上确保是否下载成功
                                       //目前来说逻辑没有问题，因为我从node1到node2就不会走了，直到全部下载完成

                        //requestold.removeFirst();
                        m_data_request_old.removeAt(i);
                        m_data_request_old_start.removeAt(i);
                        ifdownload =1;
                        server_connect_client_start();
                        //fakeDownloadDelay.start();//start不需要经历下载延迟，下载start完成后再延迟
                        //开始download start,ifdownload一直是1，直到download end,ifdownload才变成0


                    }
                }
            }
        }

        if(flag ==0){
//            if(requestold.isEmpty()){
//                m_data_request_old.clear();
//            }
            if(!m_data_result_start.isEmpty()){
                data_result_temp_to_client_start.clear();
                data_result_temp_to_client_start = m_data_result_start[0]->getMark()+"=="+m_data_result_start[0]->getSeqNum()+"=="+m_data_result_start[0]->getData()+"=="+m_data_result_start[0]->getMac();


//                //download开始时间
//                QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
//                //QStringList dataStrTemp2 = data_result_temp_to_client.split("==");
//                //添加开始下载时间戳

//                qDebug()<<m_data_result_start[0]->getMark()<<"=="<<m_data_result_start[0]->getSeqNum()<<"=="<<m_data_result_start[0]->getData()<<" ==== download starts: "<<curTime;

                //qDebug()<<ifdownload<< "\n\n";
                if(ifdownload ==0){//某种程度上逻辑没有问题，因为从Node1离开以后就不会再回来。
                    //qDebug()<<"hahahahdddd";
                    //m_data_result.removeFirst();
                    //qDebug()<<"hahahah";
                    //fakeDownloadDelay.start();//start不需要经历下载延迟，下载start完成后再延迟
                    ifdownload =1;
                    server_connect_client_start();//直接开始下载start包


                }
                //server_connect_client();
                //downloadThread.start();
            }else{
                //downloadThread_start.start();//对吗？
            }
        }

    }


    //downloadThread.start();//这一句写在下载完了，删除完了的最后一句，要在每个里面写，不能在这个method里写，
                            //因为下载是在另一个线程里（建立了socket），要保证那个线程完成了才行因为要保证已经传完了
}




void Dialog::downloadService(){
    //qDebug()<<"start download one packet...";
    int flag =0;

//    if (start ==0){

//    }
    if(state ==1 || state ==2){
        if(!requestold.isEmpty()){
            //for(int i =0; i<m_data_request_old.size();i++){
                //QStringList dataStrTemp = m_data_request_old[i].split("==");//0:mark,1:seqNum,2:data,3:mac
                //Packet *tempPacket = new Packet(dataStr[0],dataStr[1],dataStr[2]);//0:seqNum,1:data,2:mac
                //m_data_request_old[i]
                //if(QString::compare(requestold[0],dataStrTemp[0],Qt::CaseSensitive)==0){
                    flag =1;
                    //data_result_temp_to_client.clear();
                    //data_result_temp_to_client = "pre|"+m_data_request_old[i];

//                    //download开始时间
//                    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
//                    QStringList dataStrTemp2 = data_result_temp_to_client.split("==");
//                    //添加开始下载时间戳

//                    qDebug()<<dataStrTemp2[0]<<"=="<<dataStrTemp2[1]<<"=="<<dataStrTemp2[2]<<" ==== download finishes: "<<curTime;



                    //if(ifdownload ==0){//确保是否能下载，某种程度上确保是否下载成功
                                       //目前来说逻辑没有问题，因为我从node1到node2就不会走了，直到全部下载完成

                        requestold.removeFirst();
                        server_connect_client();
                        //m_data_request_old.removeAt(i);
                        //m_data_request_old_start.removeAt(i);

                       // fakeDownloadDelay.start();
                        //ifdownload =1;
                    //}
               //}
            //}
        }

        if(flag ==0){
            if(requestold.isEmpty()){
                m_data_request_old.clear();
                m_data_request_old_start.clear();
            }
            if(!m_data_result.isEmpty()){
                data_result_temp_to_client.clear();
                data_result_temp_to_client = m_data_result[0]->getMark()+"=="+m_data_result[0]->getSeqNum()+"=="+m_data_result[0]->getData()+"=="+m_data_result[0]->getMac();


//                //download开始时间
//                QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
//                //QStringList dataStrTemp2 = data_result_temp_to_client.split("==");
//                //添加开始下载时间戳

//                qDebug()<<m_data_result[0]->getMark()<<"=="<<m_data_result[0]->getSeqNum()<<"=="<<m_data_result[0]->getData()<<" ==== download finish: "<<curTime;

                //qDebug()<<ifdownload<< "\n\n";
                //if(ifdownload ==0){//某种程度上逻辑没有问题，因为从Node1离开以后就不会再回来。
                    //qDebug()<<"hahahahdddd";
                    m_data_result.removeFirst();
                    m_data_result_start.removeFirst();
                    server_connect_client();
                    //qDebug()<<"hahahah";
                    //fakeDownloadDelay.start();
                    //ifdownload =1;
                //}
                //server_connect_client();
                //downloadThread.start();
            }else{
                //downloadThread_start.start();
            }
        }

    }


    //downloadThread.start();//这一句写在下载完了，删除完了的最后一句，要在每个里面写，不能在这个method里写，
                            //因为下载是在另一个线程里（建立了socket），要保证那个线程完成了才行因为要保证已经传完了
}

//void Dialog::downloadService1(){
//    qDebug()<<"finish";
//}

////////////////////////////////////////////////////////////////////////////////////

void Dialog::
server_connect_client_start()// 把处理结果的start发给client
{
    tcpSocket_send_to_client=new QTcpSocket(this);
    tcpSocket_send_to_client->abort();
    tcpSocket_send_to_client->connectToHost(QHostAddress(clientIP),clientPort); //连接到client

    connect(tcpSocket_send_to_client,SIGNAL(connected()),this,SLOT(sendMessage_to_client_start()));
    connect(tcpSocket_send_to_client,SIGNAL(readyRead()),this,SLOT(readMessage_from_client_start()));

}

void Dialog::readMessage_from_client_start()
{
    QByteArray data=tcpSocket_send_to_client->readAll();
    //qDebug()<<"ap receive ack from client:"<<data;

    //开始转换编码
    QTextCodec *utf8codec = QTextCodec::codecForName("UTF-8");
    QString clientmsg = utf8codec->toUnicode(data.mid(2));
    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    qDebug()<<curTime<<"=="<<"fakeDown Start";
    if(QString::compare(clientmsg,"OK",Qt::CaseSensitive)==0){
        //if(state ==2){//这边不知道对不对
        //ifdownload=0;
        //downloadThread.start();
        //}
        fakeDownloadDelay.start();

    }

}

void Dialog::sendMessage_to_client_start()//这边也许需要加变量
{

    QByteArray data;
    data.append(data_result_temp_to_client_start);//这个地方是send已经处理好的结果
    //download开始时间
    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    QStringList dataStrTemp2 = data_result_temp_to_client_start.split("==");
    //添加开始下载时间戳

    qDebug()<<dataStrTemp2[0]<<"=="<<dataStrTemp2[1]<<"=="<<dataStrTemp2[2]<<" ==== download starts: "<<curTime;

    //qDebug()<<"send start"<<data_result_temp_to_client_start;
    tcpSocket_send_to_client->write(data);
    //fakeDownloadDelay.start();

}



//////////////////////////////////////////////////////////////////////////////////


void Dialog::server_connect_client()// 把处理结果的end发给client
{
    tcpSocket_send_to_client=new QTcpSocket(this);
    tcpSocket_send_to_client->abort();
    tcpSocket_send_to_client->connectToHost(QHostAddress(clientIP),clientPort); //连接到client

    connect(tcpSocket_send_to_client,SIGNAL(connected()),this,SLOT(sendMessage_to_client()));
    connect(tcpSocket_send_to_client,SIGNAL(readyRead()),this,SLOT(readMessage_from_client()));

}

void Dialog::readMessage_from_client()
{
    QByteArray data=tcpSocket_send_to_client->readAll();
    //qDebug()<<"ap receive ack from client:"<<data;

    //开始转换编码
    QTextCodec *utf8codec = QTextCodec::codecForName("UTF-8");
    QString clientmsg = utf8codec->toUnicode(data.mid(2));
    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    qDebug()<<curTime<<"=="<<"fakeDown finish";
    if(QString::compare(clientmsg,"OK",Qt::CaseSensitive)==0){
        //if(state ==2){//这边不知道对不对
        ifdownload=0;
        downloadThread_start.start();//对吗？
        //}

    }

}

void Dialog::sendMessage_to_client()//这边也许需要加变量
{

    QByteArray data;
    data.append(data_result_temp_to_client);//这个地方是send已经处理好的结果
    //download开始时间
    QString curTime = QTime::currentTime().toString("hh:mm:ss.zzz");
    QStringList dataStrTemp2 = data_result_temp_to_client.split("==");
    //添加开始下载时间戳

    qDebug()<<dataStrTemp2[0]<<"=="<<dataStrTemp2[1]<<"=="<<dataStrTemp2[2]<<" ==== download finishes: "<<curTime;

    tcpSocket_send_to_client->write(data);
    //downloadThread_start.start();

}


/////////////////////////////////////////////////////////////////////////////////////////

void Dialog::delete_m_data_result()//这个不知道对不对，现在先这么着
{
    m_data_result.clear();
}




void Dialog::updateSendStatus()//客户断开连接
{
    for(int i=0;i<mytcpsocket.length();i++)
    {
        //qDebug()<<"bytesAvailable"<<mytcpsocket[i]->bytesAvailable()<<"    ";
        if(mytcpsocket[i]->state()==QAbstractSocket::UnconnectedState)
        {
            ui->statuslabel->setText(mytcpsocket[i]->peerAddress().toString()+" is disconnected!");
            mytcpsocket.removeAt(i);
        }
        //qDebug()<<mytcpsocket[i]->isReadable()<<mytcpsocket[i]->peerPort()<<mytcpsocket[i]->socketDescriptor()<<mytcpsocket[i]->state();
    }
}


void Dialog::on_stoppushButton_clicked()//停止按钮单击
{
    tcpServer->close();
    //ui->treeWidget->clear();
    mytcpsocket.clear();
    ui->stoppushButton->setEnabled(false);
    ui->startpushButton->setEnabled(true);
    ui->statuslabel->setText("Server is Stopped");
}
