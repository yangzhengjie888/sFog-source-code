#include "receivetaskthread.h"
#include <QDebug>

ReceiveTaskThread::ReceiveTaskThread(QObject *parent) :
    QThread(parent)
{

}

void ReceiveTaskThread::run()
{
    msleep(50);
    //qDebug()<<"send to client or ap once...";

    emit done();

}

void ReceiveTaskThread::stop()
{
    stopped = true;
}






