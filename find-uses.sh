#!/bin/bash


MESSAGESFILE="./conf/messages"
NOTFOUNDFILE="messages_not_directly_used.log"
SURENOTUSED="sure_not_used.log"
MAYBENOTUSED="maybe_not_used.log"
MESSAGE_DELIMITER="."
MINIMUM_MESSAGE_SECTIONS=2

MESSAGECOUNT=0
NOTFOUNDCOUNTER=0
STRINGCOUNT=0
IFS="="

echo Logging messages that are not directly referenced in the source code \(htm/html and scala\) to :  $NOTFOUNDFILE
if [ -e $NOTFOUNDFILE ]
then
    rm $NOTFOUNDFILE
fi

while read -r NAME VALUE
do
    if [[ ! -z "${NAME// }" ]] && [[ $NAME != \#* ]]; then
        MESSAGECOUNT=$[$MESSAGECOUNT +1]
        echo -en "\rCounting records to process: $MESSAGECOUNT"

        FOUND=$(grep -c "^$NAME=" $MESSAGESFILE)
        if [[ $FOUND -gt 1 ]]; then
          echo
          echo Found a duplicate message: $NAME
        fi
    fi
done < $MESSAGESFILE
echo




while read -r NAME VALUE
do
    if [[ ! -z "${NAME// }" ]] && [[ $NAME != \#* ]]; then
       STRINGCOUNT=$[$STRINGCOUNT +1]
       XNAME="${NAME//+([[:space:]])/}"
       echo -en "\rRecords processed: $STRINGCOUNT / $MESSAGECOUNT. Looking for $XNAME                                                   "
       FOUND=$(grep -r --include=*.{html,htm,scala} "$XNAME")
       if [[ -z "${FOUND// }" ]]; then
          echo $NAME >> $NOTFOUNDFILE
          NOTFOUNDCOUNTER=$[$NOTFOUNDCOUNTER +1]
       fi
    fi
done < $MESSAGESFILE



echo
echo total not found: $NOTFOUNDCOUNTER
echo out of $STRINGCOUNT messages
echo
echo
echo Checking messages which were not directly referenced in the source code, to see if the key may be created at execution time.
echo Messages that I really think are not used any more will be logged to $SURENOTUSED.
echo Messages that aren\'t used, but their parent is \(i.e. might be concatenated at run time\) are logged to $MAYBENOTUSED.
echo I assume that the delimiter between key sections is a full stop. e.g. site.new.invalidDate has three sections.
echo If a not directly referenced key has $MINIMUM_MESSAGE_SECTIONS or fewer sections, I will not check for dynamically concatenated keys, and will just assume that it isn\'t used.
echo if you have dynamic keys that are based on a grandparent \(two or more dynamic sections\), then I will not find them. Please check them yourself. :oP
echo

MAYBENOTUSEDCOUNTER=0
SURENOTUSEDCOUNTER=0

if [ -e $SURENOTUSED ]
then
    rm $SURENOTUSED
fi

if [ -e $MAYBENOTUSED ]
then
    rm $MAYBENOTUSED
fi

while read -r NAME
do
   IFS=$MESSAGE_DELIMITER
   TOKENS=( $NAME )
   IFS='='
   TOKEN_COUNT=${#TOKENS[@]}
   LAST_TOKEN=${TOKENS[$[$TOKEN_COUNT-1]]}
   LEN_LAST_TOKEN=${#LAST_TOKEN}
   NAME_LEN=${#NAME}
   REMAINING_LEN=$[$NAME_LEN-$LEN_LAST_TOKEN-1]
   REMAINING=${NAME:0:$REMAINING_LEN}
   SEARCHSTRING="$REMAINING.\""

   if [ "$TOKEN_COUNT" -gt $MINIMUM_MESSAGE_SECTIONS ]; then
      FOUND=$(grep -r --include=*.{html,htm,scala} "$SEARCHSTRING")
      if [[ -z "${FOUND// }" ]]; then
         SURENOTUSEDCOUNTER=$[$SURENOTUSEDCOUNTER +1]
         echo $NAME >> $SURENOTUSED
      else
         MAYBENOTUSEDCOUNTER=$[$MAYBENOTUSEDCOUNTER +1]
         echo $NAME >> $MAYBENOTUSED
      fi
   else
      SURENOTUSEDCOUNTER=$[$SURENOTUSEDCOUNTER +1]
      echo $NAME >> $SURENOTUSED
   fi
    echo -en "\rNot used: $SURENOTUSEDCOUNTER   Might not be used: $MAYBENOTUSEDCOUNTER  [ $[$SURENOTUSEDCOUNTER+$MAYBENOTUSEDCOUNTER] / $NOTFOUNDCOUNTER ]"
done < $NOTFOUNDFILE


echo
echo Checking for duplicates in $SURENOTUSED....
while read -r NAME
do
    FOUND=$(grep -x "\<$NAME\>" $SURENOTUSED)
    if [[ ! -z "${FOUND// }" ]] && [[ $FOUND != $NAME ]]; then
       echo Found a duplicate: $NAME
    fi
done < $SURENOTUSED


echo
echo
echo Total that really look like they\'re not used: $SURENOTUSEDCOUNTER
echo Total that might not be used: $MAYBENOTUSEDCOUNTER


