#ifndef FAKEMIGRATIONDELAY_H
#define FAKEMIGRATIONDELAY_H


#include <QThread>
class FakeMigrationDelay : public QThread
{
    Q_OBJECT
public:
    explicit FakeMigrationDelay(QObject *parent =0);
    void stop();

protected:
    void run();

private:
    volatile bool stopped;

signals:
    void done();

};

#endif // FAKEMIGRATIONDELAY_H
