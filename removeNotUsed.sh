#!/bin/bash

SURENOTUSED="sure_not_used.log"
OUTPUTFILE="new_messages"
MESSAGESFILE="./conf/messages"

KEEPCOUNT=0
REMOVECOUNT=0
IFS="="

echo Searching messages in ${PWD}/$MESSAGESFILE, for corresponding keys stored in ${PWD}/$SURENOTUSED.
echo Remaining messages will be output to ${PWD}/$OUTPUTFILE
if [ -e $OUTPUTFILE ]
then
    rm $OUTPUTFILE
fi

if [ ! -e $SURENOTUSED ]
then
    echo No messages to remove - can\'t find $SURENOTUSED
    exit 0
fi


while read -r NAME VALUE
do
    if [[ -z "${NAME// }" ]] || [[ $NAME == \#* ]]; then
        echo $NAME >> $OUTPUTFILE
    else
        echo >> temp.txt
        echo Name-: $NAME >> temp.txt
        FOUND=$(grep -x "\<$NAME\>" $SURENOTUSED)

        echo Found: $FOUND >> temp.txt
        if [ -z "${FOUND// }" ]; then
            KEEPCOUNT=$[$KEEPCOUNT +1]
            echo $NAME=$VALUE >> $OUTPUTFILE
            echo Keep >> temp.txt
        else
            REMOVECOUNT=$[$REMOVECOUNT +1]
            echo Remove >> temp.txt
        fi
    fi
    echo -en "\rKeep Count: $KEEPCOUNT    Remove Count: $REMOVECOUNT [ $[$KEEPCOUNT+$REMOVECOUNT] ]"
done < $MESSAGESFILE

echo




