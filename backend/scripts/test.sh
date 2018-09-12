#!/bin/bash

CURL_DATA_USER='{"mode": "USER", "id": "jan", "avatarUrl": "string", "exists": true, "fullname": "Jan Kowalski", "requestedIpDetails": true, "requestedMacDetails": true, "senderRoles": ["string"], "username": "string"}'
CURL_DATA_IP='{"mode": "IP", "id": "1.2.3.4", "avatarUrl": "string", "exists": true, "fullname": "Jan Kowalski", "requestedIpDetails": true, "requestedMacDetails": true, "senderRoles": ["string"], "username": "string"}'
CURL_DATA_MAC='{"mode": "MAC", "id": "01-02-03-04-05-06", "avatarUrl": "string", "exists": true, "fullname": "Jan Kowalski", "requestedIpDetails": true, "requestedMacDetails": true, "senderRoles": ["string"], "username": "string"}'

CURL_DATA_USERS1='[{"location": "1"}, {"location": "2"}, {"location": "3"}]'
CURL_DATA_USERS2='{"location": "9"}'

# curl -v -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d "$CURL_DATA_USER" http://localhost:8080/api/whereis
curl -v -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d "$CURL_DATA_IP" http://localhost:8080/api/whereis


rem 2>/dev/null

exit
curl -v -X GET --header 'Content-Type: application/json' --header 'Accept: */*' -d "$CURL_DATA_USERS1" http://localhost:8081/users_random/jan 2>/dev/null | jq
# curl -X GET --header 'Content-Type: application/json' --header 'Accept: */*' -d "$CURL_DATA_USERS1" http://localhost:8081/users_random/adam 2>/dev/null | jq
# curl -X GET --header 'Content-Type: application/json' --header 'Accept: */*' -d "$CURL_DATA_USERS1" http://localhost:8081/users_random/house 2>/dev/null | jq

exit
curl -X GET --header 'Content-Type: application/json' --header 'Accept: */*' -d "$CURL_DATA_USERS1" http://localhost:8081/users/jan 2>/dev/null
curl -X GET --header 'Content-Type: application/json' --header 'Accept: */*' -d "$CURL_DATA_USERS1" http://localhost:8081/users/jan 2>/dev/null | jq
# curl -X GET --header 'Content-Type: application/json' --header 'Accept: */*' -d "$CURL_DATA_USERS1" http://localhost:8081/users/adam 2>/dev/null | jq
# curl -X GET --header 'Content-Type: application/json' --header 'Accept: */*' -d "$CURL_DATA_USERS1" http://localhost:8081/users/ 2>/dev/null | jq
exit

curl -X POST --header 'Content-Type: application/json' --header 'Accept: */*' -d '{ \
   "avatarUrl": "string", \
   "command": "string", \
   "exists": true, \
   "fullname": "string", \
   "requestedIpDetails": true, \
   "requestedMacDetails": true, \
   "senderRoles": [ \
     "string" \
   ], \
   "username": "string" \
 }' 'http://localhost:8080/whereis' 2>/dev/null
