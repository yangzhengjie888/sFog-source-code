#ifndef COMPUTETASKTHREAD_H
#define COMPUTETASKTHREAD_H
#include <QThread>

class ComputeTaskThread : public QThread
{
    Q_OBJECT
public:
    explicit ComputeTaskThread(QObject *parent =0);
    void stop();

protected:
    void run();

private:
    volatile bool stopped;

signals:
    void done();

};

#endif // COMPUTETASKTHREAD_H










