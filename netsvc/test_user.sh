#!/bin/bash

# Test script for WhereIs netsvc low level network service

CheckRequirements() {
    for tool in "$@"
	do
	    # echo "$tool"
	    command -v $tool >/dev/null 2>&1 || { echo "I require $tool but it's not installed (please install it by \"apt install $tool\"). Aborting." >&2; exit 1; }
	done
}

CheckRequirements curl jq

echo curl -v -X GET --header 'Content-Type: application/json' --header 'Accept: */*' http://localhost:8081/whereis/jan
echo
curl -v -X GET --header 'Content-Type: application/json' --header 'Accept: application/json' http://localhost:8081/whereis/jan 2>/dev/null | jq
