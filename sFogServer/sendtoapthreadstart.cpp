#include "sendtoapthreadstart.h"
#include <QDebug>

SendToApThreadStart::SendToApThreadStart(QObject *parent) :
    QThread(parent)
{

}


void SendToApThreadStart::run()
{
    msleep(50);
    qDebug()<<"SENDTOAP ！！！！start";

    emit done(i);

}

void SendToApThreadStart::stop()
{
    stopped = true;
}
