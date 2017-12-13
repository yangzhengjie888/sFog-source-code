#include "sendtoapthread.h"
#include <QDebug>

SendToApThread::SendToApThread(QObject *parent) :
    QThread(parent)
{

}


void SendToApThread::run()
{
    msleep(50);
    qDebug()<<"SENDTOAP ！！！！";

    emit done();

}

void SendToApThread::stop()
{
    stopped = true;
}





