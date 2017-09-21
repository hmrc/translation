#!/bin/bash

if [ $# -lt 1 ]
  then
    echo "Not enough arguments supplied"
    echo "Usage: remove-not-used.sh [log-file]"
    echo "example: remove-not-used.sh short_not_used.log"
    exit 1
fi

MESSAGESTOREMOVE=$1
OUTPUTFILE="new_messages"
MESSAGESFILE="./conf/messages"

KEEPCOUNT=0
REMOVECOUNT=0
IFS="="

echo Searching messages in ${PWD}/$MESSAGESFILE, for corresponding keys stored in ${PWD}/$MESSAGESTOREMOVE.
echo Remaining messages will be output to ${PWD}/$OUTPUTFILE
if [ -e $OUTPUTFILE ]
then
    rm $OUTPUTFILE
fi

if [ ! -e $MESSAGESTOREMOVE ]
then
    echo No messages to remove - can\'t find $MESSAGESTOREMOVE
    exit 0
fi


while read -r NAME VALUE
do
    if [[ -z "${NAME// }" ]] || [[ $NAME == \#* ]]; then
        echo $NAME >> $OUTPUTFILE
    else
        FOUND=$(grep -x "\<$NAME\>" $MESSAGESTOREMOVE)
        if [ -z "${FOUND// }" ]; then
            KEEPCOUNT=$[$KEEPCOUNT +1]
            echo $NAME=$VALUE >> $OUTPUTFILE
        else
            REMOVECOUNT=$[$REMOVECOUNT +1]
        fi
    fi
    echo -en "\rKeep Count: $KEEPCOUNT    Remove Count: $REMOVECOUNT [ Total: $[$KEEPCOUNT+$REMOVECOUNT] ]"
done < $MESSAGESFILE

echo




