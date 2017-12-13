#ifndef DOWNLOADTHREADSTART_H
#define DOWNLOADTHREADSTART_H

#include <QThread>

class DownloadThreadStart : public QThread
{
    Q_OBJECT
public:
    explicit DownloadThreadStart(QObject *parent = 0);
    void stop();

protected:
    void run();

private:
    volatile bool stopped;
    int i =0;
    //long int i = 0;


signals:
    void done(int);

public slots:


};

#endif // DOWNLOADTHREADSTART_H
