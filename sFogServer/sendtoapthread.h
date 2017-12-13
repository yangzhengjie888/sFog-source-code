#ifndef SENDTOAPTHREAD_H
#define SENDTOAPTHREAD_H

#include <QThread>
class SendToApThread : public QThread
{
    Q_OBJECT
public:
    explicit SendToApThread(QObject *parent =0);
    void stop();

protected:
    void run();

private:
    volatile bool stopped;
    int i =1;

signals:
    void done();

};

#endif // SENDTOAPTHREAD_H







