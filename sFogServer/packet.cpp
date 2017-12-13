#include "packet.h"

Packet::Packet()
{

}

Packet::Packet(QString mark,QString seqNum, QString data,QString mac)
{
    _mark = mark;
    _seqNum = seqNum;
    _data = data;
    _mac = mac;
}

QString Packet::getMark() const
{
    return _mark;
}

QString Packet::getSeqNum() const
{
    return _seqNum;
}

QString Packet::getData() const
{
    return _data;
}

QString Packet::getMac() const
{
    return _mac;
}


