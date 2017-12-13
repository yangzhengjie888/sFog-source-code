#include "downloadthread.h"
#include <QDebug>

DownloadThread::DownloadThread(QObject *parent) :
    QThread(parent)
{
    //stopped = false;
}

void DownloadThread::run()
{
    //while(!stopped){
    msleep(10);
    //qDebug()<<"download thread";

    emit done(i);

}

void DownloadThread::stop()
{
    stopped = true;
}


