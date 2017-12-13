#ifndef SENDTOAPTHREADSTART_H
#define SENDTOAPTHREADSTART_H

#include <QThread>
class SendToApThreadStart : public QThread
{
    Q_OBJECT
public:
    explicit SendToApThreadStart(QObject *parent =0);
    void stop();

protected:
    void run();

private:
    volatile bool stopped;
    int i =0;

signals:
    void done(int);

};
#endif // SENDTOAPTHREADSTART_H
