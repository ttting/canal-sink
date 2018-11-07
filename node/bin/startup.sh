#!/usr/bin/env bash

jarName=$(basename ../lib/canal-sink-* )
echo $basename

current_path=`pwd`
case "`uname`" in
    Linux)
		bin_abs_path=$(readlink -f $(dirname $0))
		;;
	*)
		bin_abs_path=`cd $(dirname $0); pwd`
		;;
esac

base=${bin_abs_path}/..
canal_sink_conf=$base/conf/canal-sink.yaml
log4j_configurationFile=$base/conf/log4j2.xml
jar_abs_path=${bin_abs_path}/../lib/$jarName

echo $bin_abs_path

if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
fi


str=`file -L $JAVA | grep 64-bit`
if [ -n "$str" ]; then
	JAVA_OPTS="-server -Xms2048m -Xmx3072m -Xmn1024m -XX:SurvivorRatio=2 -XX:PermSize=96m -XX:MaxPermSize=256m -Xss256k -XX:-UseAdaptiveSizePolicy -XX:MaxTenuringThreshold=15 -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError"
else
	JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:MaxPermSize=128m "
fi

CANAL_SINK_OPTS="-DappName=canal-sink -Dlogging.config=$log4j_configurationFile -Dcanal-sink.conf=$canal_sink_conf"


$JAVA  $JAVA_OPTS $CANAL_SINK_OPTS  -jar  $jar_abs_path 1>>$base/logs/canal-sink.log 2>&1 &
echo $! > $base/bin/canal-sink.pid