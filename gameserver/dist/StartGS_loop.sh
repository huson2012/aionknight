#!/bin/bash

err=1
until [ $err == 0 ];
do
	[ -d log/ ] || mkdir log/
	[ -f log/console.log ] && mv log/console.log "log/console/`date +%Y-%m-%d_%H-%M-%S`_console.log"
	java -Xms128m -Xmx1536m -Xbootclasspath/p:./libs/jsr166.jar -cp ./libs/*:ak-server.jar gameserver.GameServer > log/console.log 2>&1
	err=$?
	gspid=$!
	echo ${gspid} > gameserver.pid
	sleep 10
done
