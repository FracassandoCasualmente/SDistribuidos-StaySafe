#!/bin/bash
# script to execute server executable
# -------
# ./execute.sh {RepId}
# -------
repId=$1
zooHost=localhost
zooPort=2181
host=localhost
port=$((8080+${repId}))

allArgs="${zooHost} ${zooPort} ${repId} ${host} ${port}"
# debug msg
echo "${0} : arguments received: ${allArgs}"
./target/appassembler/bin/dgs-server ${allArgs}