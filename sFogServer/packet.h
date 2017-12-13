#ifndef PACKET_H
#define PACKET_H

#include <QString>


class Packet
{
public:
    Packet();
    Packet(QString mark,QString seqNum, QString data, QString mac);

    QString getMark() const;
    QString getSeqNum() const;
    QString getData()const;
    QString getMac() const;

private:
    QString _mark;
    QString _seqNum;
    QString _data;
    QString _mac;
};

#endif // PACKET_H
