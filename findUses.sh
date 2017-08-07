#!/bin/bash


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

while read -r name value
do
    if [[ ! -z "${name// }" ]] && [[ $name != \#* ]]; then
        MESSAGECOUNT=$[$MESSAGECOUNT +1]
        echo -en "\rCounting records to process: $MESSAGECOUNT"
    fi
done < ./conf/messages
echo




while read -r name value
do
    if [[ ! -z "${name// }" ]] && [[ $name != \#* ]]; then
       STRINGCOUNT=$[$STRINGCOUNT +1]
       xname="${name//+([[:space:]])/}"
       found=$(grep -r --include=*.{html,htm,scala} "$xname")
       if [[ -z "${found// }" ]]; then
          echo $name >> $NOTFOUNDFILE
          NOTFOUNDCOUNTER=$[$NOTFOUNDCOUNTER +1]
          #echo -e ".\c"
          echo -en "\rRecords processed: $STRINGCOUNT / $MESSAGECOUNT"
       fi
    fi
done < ./conf/messages



echo
echo total not found: $NOTFOUNDCOUNTER
echo out of $STRINGCOUNT messages
echo
echo
echo Checking messages which were not directly referenced in the source code, to see if the key may be created at execution time.
echo Messages that I really think are not used any more will be logged to $SURENOTUSED
echo Messages that arent used, but their parent is \(i.e. may be concatenated at run time\) are logged to $MAYBENOTUSED
echo I assume that the delimiter between key sections is a full stop. e.g. site.new.invalidDate has three sections
echo if you have dynamic keys that are based on a grandparent \(two dynamic sections\), then I will not find them. Please check them yourself. :oP
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

while read -r name
do
   IFS=$MESSAGE_DELIMITER
   tokens=( $name )
   IFS='='
   TOKEN_COUNT=${#tokens[@]}
   LAST_TOKEN=${tokens[$[$TOKEN_COUNT-1]]}
   LEN_LAST_TOKEN=${#LAST_TOKEN}
   NAME_LEN=${#name}
   REMAINING_LEN=$[$NAME_LEN-$LEN_LAST_TOKEN-1]
   REMAINING=${name:0:$REMAINING_LEN}
   SEARCHSTRING="$REMAINING.\""

   if [ "$TOKEN_COUNT" -gt $MINIMUM_MESSAGE_SECTIONS ]; then
      found=$(grep -r --include=*.{html,htm,scala} "$SEARCHSTRING")
      if [[ -z "${found// }" ]]; then
         SURENOTUSEDCOUNTER=$[$SURENOTUSEDCOUNTER +1]
         echo $name >> $SURENOTUSED
      else
         MAYBENOTUSEDCOUNTER=$[$MAYBENOTUSEDCOUNTER +1]
         echo $name >> $MAYBENOTUSED
      fi
   else
      SURENOTUSEDCOUNTER=$[$SURENOTUSEDCOUNTER +1]
      echo $name >> $SURENOTUSED
   fi
    echo -en "\rNot used: $SURENOTUSEDCOUNTER   Probably not used: $MAYBENOTUSEDCOUNTER  [ $[$SURENOTUSEDCOUNTER+$MAYBENOTUSEDCOUNTER] / $NOTFOUNDCOUNTER ]"
done < $NOTFOUNDFILE
echo
echo
echo total that really look like they\'re not used: $SURENOTUSEDCOUNTER
echo total that may not be used: $MAYBENOTUSEDCOUNTER