#include "fakemigrationdelay.h"

#include<QDebug>

FakeMigrationDelay::FakeMigrationDelay(QObject *parent) :
    QThread(parent)
{

}

void FakeMigrationDelay::run()
{
    msleep(667);
    //预设：是download delay的三分之一
    //qDebug()<<"FAKE DOWNLOAD";

    emit done();

}

void FakeMigrationDelay::stop()
{
    stopped = true;
}
