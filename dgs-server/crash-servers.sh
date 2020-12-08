#!/bin/bash

# script to shutdown all servers in dgs-server/tmp
# each {repId}.txt has value {pidOfReplica}
for file in $(ls tmp )
do
	# kill each replica
	kill `cat "tmp/"${file}` 2>/dev/null
done
# time to print signal handling msgs
sleep 0.2
