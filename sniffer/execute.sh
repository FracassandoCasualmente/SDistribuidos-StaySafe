#!/bin/bash
# script to execute sniffer executable

#host=$1
#port=$2
host=localhost
port=8080
otherArgs="$1 $2"

# Loop until all parameters are used up
while [ "$3" != "" ]; do
    otherArgs="${otherArgs} $3"
    shift
done

allArgs="${host} ${port} ${otherArgs}"
echo "${0} : arguments received: ${allArgs}"
./target/appassembler/bin/sniffer ${allArgs}