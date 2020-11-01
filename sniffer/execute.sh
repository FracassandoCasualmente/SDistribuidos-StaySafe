#!/bin/bash
# script to execute sniffer executable
host=$1
port=$2
otherArgs=""

# Loop until all parameters are used up
while [ "$3" != "" ]; do
    otherArgs="${otherArgs} $3"
    echo "otherArgs="$otherArgs
    shift
done

allArgs="${host} ${port}${otherArgs}"
echo -e "${0} : arguments received: ${allArgs}\n"
./target/appassembler/bin/sniffer ${allArgs}