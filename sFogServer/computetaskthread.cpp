#include "computetaskthread.h"
#include <QDebug>

ComputeTaskThread::ComputeTaskThread(QObject *parent) :
    QThread(parent)
{

}

void ComputeTaskThread::run()
{
    msleep(3000);
    //qDebug()<<"send to client or ap once...";

    emit done();

}

void ComputeTaskThread::stop()
{
    stopped = true;
}








