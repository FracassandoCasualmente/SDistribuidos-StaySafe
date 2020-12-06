#!/bin/bash
# script to execute server executable
# -------
# ./execute.sh {RepId}
# -------
repId=$1
zooHost=localhost
zooPort=2181
serverPath=/grpc/staysafe/dgs/${repId}
host=localhost
port=$((8080+${repId}))

allArgs="${zooHost} ${zooPort} ${serverPath} ${repId} ${host} ${port}"
echo "${0} : arguments received: ${allArgs}"
./target/appassembler/bin/dgs-server ${allArgs}