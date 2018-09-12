#!/bin/bash

DATE=$(date "+%Y-%m-%d %H;%M;%S")

echo $DATE >> /mnt/c/DATA/DEV/Projects/NCDC/deploy.log

if [ $1 == remote ]
then
    TARGET=REMOTE
else
    TARGET=LOCAL
fi

if [ $2 == install ]
then
    INSTALL=Y
else
    INSTALL=N
fi

if [ $TARGET == REMOTE ]
then
    # external docker @ ALPHA-2
    HOST=http://ALPHA-2:3000/
    USER=jan
    PASS=123jan
else
    # My docker @ ThinkPad
    HOST=http://192.168.99.100:3000/
    USER=jan
    PASS=123jan
fi

if [ $INSTALL != Y ]
then
    MODE=--update
fi


# update deploy
# rc-apps deploy --url=$HOST --id=$USER --password=$PASS --update

# fresh deploy
# rc-apps deploy --url=$HOST --id=$USER --password=$PASS

# echo mode = $MODE
# echo install = $INSTALL
# echo host = $HOST
# echo whereis = $USER
# echo pass = $PASS

rc-apps deploy --url=$HOST --username=$USER --password=$PASS $MODE

LAST_FILE=$(ls --format single-column ./dist/ | tail -n 1)
cp ./dist/$LAST_FILE "/mnt/c/DATA/DEV/Projects/NCDC/rwb.deploy/$LAST_FILE-$TARGET-$DATE" 2>/dev/null
