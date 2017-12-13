#include "fakedownloaddelay.h"
#include<QDebug>

FakeDownloadDelay::FakeDownloadDelay(QObject *parent) :
    QThread(parent)
{

}

void FakeDownloadDelay::run()
{
    msleep(4000);
    //qDebug()<<"FAKE DOWNLOAD";

    emit done();

}

void FakeDownloadDelay::stop()
{
    stopped = true;
}







