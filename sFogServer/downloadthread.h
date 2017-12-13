#ifndef DOWNLOADTHREAD_H
#define DOWNLOADTHREAD_H

#include <QThread>

class DownloadThread : public QThread
{
    Q_OBJECT
public:
    explicit DownloadThread(QObject *parent = 0);
    void stop();

protected:
    void run();

private:
    volatile bool stopped;
    int i =1;
    //long int i = 0;


signals:
    void done(int);

public slots:


};

#endif // DOWNLOADTHREAD_H




