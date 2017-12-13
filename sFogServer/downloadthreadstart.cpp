#include "downloadthreadstart.h"
#include <QDebug>

DownloadThreadStart::DownloadThreadStart(QObject *parent) :
    QThread(parent)
{
    //stopped = false;
}

void DownloadThreadStart::run()
{
    //while(!stopped){
    msleep(10);
    //qDebug()<<"download thread";

    emit done(i);

}

void DownloadThreadStart::stop()
{
    stopped = true;
}
