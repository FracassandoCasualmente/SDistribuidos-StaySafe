#!/bin/bash
# runs 3 servers in background

color_reset="\u001B[0m"
color_purple="\u001B[1;94m"
color_green="\033[1;92m"
color_yellow="\033[1;93m"
color_cyan="\033[1;96m"
# chosen colour
color=$color_yellow

pidList=""

for repId in $(seq 1 3);
do
	./execute.sh ${repId} &
	pid=$!
	pidList="${pidList} ${pid}"
	sleep 1
	#echo -e "${color}pidof ${repId} -> ${pid}${color_reset}"
	echo -e "${color}Replica ${repId} ${color_reset}"
done
# get time to "Server Started" appears
sleep 5
# print pid list
#echo -e "${color}PIDs : -> ${pidList} <- ${color_reset}"
# writes into /tmp/dgs/pids.txt the list of pids
#echo "${pidList}" > tmp/pids.txt



