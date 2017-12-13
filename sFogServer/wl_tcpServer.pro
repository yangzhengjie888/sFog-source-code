#-------------------------------------------------
#
# Project created by QtCreator 2014-10-28T15:23:22
#
#-------------------------------------------------

QT       += core gui network

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = wl_tcpServer
TEMPLATE = app


SOURCES += main.cpp\
        dialog.cpp \
    packet.cpp \
    downloadthread.cpp \
    sendtoapthread.cpp \
    receivetaskthread.cpp \
    computetaskthread.cpp \
    fakedownloaddelay.cpp \
    downloadthreadstart.cpp \
    sendtoapthreadstart.cpp \
    fakemigrationdelay.cpp

HEADERS  += dialog.h \
    packet.h \
    downloadthread.h \
    sendtoapthread.h \
    receivetaskthread.h \
    computetaskthread.h \
    fakedownloaddelay.h \
    downloadthreadstart.h \
    sendtoapthreadstart.h \
    fakemigrationdelay.h

FORMS    += dialog.ui
