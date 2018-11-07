#!/usr/bin/env bash

cygwin=false;
linux=false;
case "`uname`" in
    CYGWIN*)
        cygwin=true
        ;;
    Linux*)
    	linux=true
    	;;
esac


base=`dirname $0`/..
pidfile=$base/bin/canal-sink.pid


if [ ! -f "$pidfile" ];then
	echo "canal is not running. exists"
	exit
fi


pid=`cat $pidfile`

echo -e "`hostname`: stopping canal $pid ... "
kill $pid


