#ifndef RECEIVETASKTHREAD_H
#define RECEIVETASKTHREAD_H
#include <QThread>

class ReceiveTaskThread : public QThread
{
    Q_OBJECT
public:
    explicit ReceiveTaskThread(QObject *parent =0);
    void stop();

protected:
    void run();

private:
    volatile bool stopped;

signals:
    void done();

};

#endif // RECEIVETASKTHREAD_H








