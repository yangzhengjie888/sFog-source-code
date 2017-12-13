#ifndef FAKEDOWNLOADDELAY_H
#define FAKEDOWNLOADDELAY_H

#include <QThread>
class FakeDownloadDelay : public QThread
{
    Q_OBJECT
public:
    explicit FakeDownloadDelay(QObject *parent =0);
    void stop();

protected:
    void run();

private:
    volatile bool stopped;

signals:
    void done();

};

#endif // FAKEDOWNLOADDELAY_H












