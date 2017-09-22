#!/bin/bash


MESSAGESFILE="./conf/messages"
NOTFOUNDFILE="all_messages_not_directly_used.log"
PARENTNOTUSED="parent_not_used.log"
GRANDPARENTNOTUSED="grandparent_not_used.log"
GRANDPARENTUSED="grandparent_used.log"
PARENTUSED="parent_used.log"
SHORTNOTUSED="short_not_used.log"
MESSAGE_DELIMITER="."
MINIMUM_MESSAGE_SECTIONS=4

MESSAGECOUNT=0
FOUNDCOUNTER=0
NOTFOUNDCOUNTER=0
STRINGCOUNT=0
DUPLICATES=""
DUPLICATECOUNT=0
IFS="="

# Tidy up old files....
if [ -e $NOTFOUNDFILE ]
then
    rm $NOTFOUNDFILE
fi

if [ -e $PARENTNOTUSED ]
then
    rm $PARENTNOTUSED
fi

if [ -e $PARENTUSED ]
then
    rm $PARENTUSED
fi

if [ -e $SHORTNOTUSED ]
then
    rm $SHORTNOTUSED
fi

if [ -e $GRANDPARENTNOTUSED ]
then
    rm $GRANDPARENTNOTUSED
fi

if [ -e $GRANDPARENTUSED ]
then
    rm $GRANDPARENTUSED
fi


echo Logging messages that are not directly referenced in the source code \(htm/html and scala\) to :  $NOTFOUNDFILE
# Count records in messages file....
while read -r NAME VALUE
do
    if [[ ! -z "${NAME// }" ]] && [[ $NAME != \#* ]]; then
        MESSAGECOUNT=$[$MESSAGECOUNT +1]
        echo -en "\rCounting records to process: $MESSAGECOUNT"

        FOUND=$(grep -c "^$NAME=" $MESSAGESFILE)
        if [[ $FOUND -gt 1 ]]; then
#          echo
#          echo Found a duplicate message: $NAME
          DUPLICATES="${DUPLICATES}
          ${NAME}"
          DUPLICATECOUNT=$[$DUPLICATECOUNT +1]
        fi
    fi
done < $MESSAGESFILE



# Check if message key is referenced in the codebase...
while read -r NAME VALUE
do
    if [[ ! -z "${NAME// }" ]] && [[ $NAME != \#* ]]; then
       STRINGCOUNT=$[$STRINGCOUNT +1]
       NAME_TRIMMED="$(echo -e "${NAME}" | sed -e 's/[[:space:]]*$//')"
       PC=$[$STRINGCOUNT*100/$MESSAGECOUNT]
       echo -en "\rRecords processed: $STRINGCOUNT / $MESSAGECOUNT  [$PC%]"

       FOUND=$(grep -r --exclude-dir=target --include=*.{html,htm,scala} "$NAME_TRIMMED")
       if [[ -z "${FOUND// }" ]]; then
          echo $NAME_TRIMMED >> $NOTFOUNDFILE
          NOTFOUNDCOUNTER=$[$NOTFOUNDCOUNTER +1]
       else
          FOUNDCOUNTER=$[$FOUNDCOUNTER +1]
       fi
    fi
done < $MESSAGESFILE



echo
echo total not found: $NOTFOUNDCOUNTER \(logged to $NOTFOUNDFILE \)
echo out of $STRINGCOUNT messages
echo
echo
echo Checking for message keys which were not directly referenced in the source code, to see if the key may be created at execution time.
echo I assume that the delimiter between key sections is a full stop. e.g. site.new.invalidDate has three sections.
echo If a key has $MINIMUM_MESSAGE_SECTIONS sections or fewer and is not directly referenced, I will not check for dynamically concatenated keys, and will log it to $SHORTNOTUSED.
echo if there are dynamic keys that are based on a great grandparent \(three or more dynamic sections\), then sorry, I will not find them,
echo the messages will be assumed to be unused and so logged to $PARENTNOTUSED. :\(
echo I.e.
echo If message key is: page.tcs.personal.change_telephone_number.landing_page_definition
echo Then parent key is: page.tcs.personal.change_telephone_number
echo Grandparent key is: page.tcs.personal
echo Greatgrandparent key is: page.tcs
echo

PARENTUSEDCOUNTER=0
PARENTNOTUSEDCOUNTER=0
SHORTNOTUSEDCOUNTER=0


# Check if parent of message key is referenced in the codebase...
while read -r NAME
do
   IFS=$MESSAGE_DELIMITER
   TOKENS=( $NAME )
   IFS='='
   TOKEN_COUNT=${#TOKENS[@]}
   LAST_TOKEN=${TOKENS[$[$TOKEN_COUNT-1]]}
   LEN_LAST_TOKEN=${#LAST_TOKEN}
   NAME_LEN=${#NAME}
   PARENT_LEN=$[$NAME_LEN-$LEN_LAST_TOKEN-1]
   PARENT=${NAME:0:$PARENT_LEN}
   SEARCHSTRING="$PARENT."



   if [ "$TOKEN_COUNT" -gt $MINIMUM_MESSAGE_SECTIONS ]; then
      FOUND=$(grep -r --exclude-dir=target --include=*.{html,htm,scala} "$SEARCHSTRING")
      if [[ -z "${FOUND// }" ]]; then
         PARENTNOTUSEDCOUNTER=$[$PARENTNOTUSEDCOUNTER +1]
         echo $NAME >> $PARENTNOTUSED
      else
         PARENTUSEDCOUNTER=$[$PARENTUSEDCOUNTER +1]
         echo $NAME >> $PARENTUSED
      fi
   else
      SHORTNOTUSEDCOUNTER=$[$SHORTNOTUSEDCOUNTER +1]
      echo $NAME >> $SHORTNOTUSED
   fi
    echo -en "\rParent not found: $PARENTNOTUSEDCOUNTER   Parent found: $PARENTUSEDCOUNTER   Short and not found: $SHORTNOTUSEDCOUNTER [ $[$PARENTNOTUSEDCOUNTER+$PARENTUSEDCOUNTER+$SHORTNOTUSEDCOUNTER] / $NOTFOUNDCOUNTER ]"
done < $NOTFOUNDFILE




echo
# Check if grandparent of message key is referenced in the codebase...
GRANDPARENTUSEDCOUNTER=0
GRANDPARENTNOTUSEDCOUNTER=0
while read -r NAME
do
   IFS=$MESSAGE_DELIMITER
   TOKENS=( $NAME )
   IFS='='
   TOKEN_COUNT=${#TOKENS[@]}
   LAST_TOKEN=${TOKENS[$[$TOKEN_COUNT-1]]}
   LEN_LAST_TOKEN=${#LAST_TOKEN}
   PENULTIMATE_TOKEN=${TOKENS[$[$TOKEN_COUNT-2]]}
   LEN_PENULTIMATE_TOKEN=${#PENULTIMATE_TOKEN}
   NAME_LEN=${#NAME}
   GRANDPARENT_LEN=$[$NAME_LEN-$LEN_LAST_TOKEN-$LEN_PENULTIMATE_TOKEN-1]
   GRANDPARENT=${NAME:0:$GRANDPARENT_LEN}
   SEARCHSTRING="$GRANDPARENT."



   FOUND=$(grep -r --exclude-dir=target --include=*.{html,htm,scala} "$SEARCHSTRING")
   if [[ -z "${FOUND// }" ]]; then
      GRANDPARENTNOTUSEDCOUNTER=$[$GRANDPARENTNOTUSEDCOUNTER +1]
      echo $NAME >> $GRANDPARENTNOTUSED
   else
      GRANDPARENTUSEDCOUNTER=$[$GRANDPARENTUSEDCOUNTER +1]
      echo $NAME >> $GRANDPARENTUSED
   fi

    echo -en "\rGrandparent not found: $GRANDPARENTNOTUSEDCOUNTER   Grandparent found: $GRANDPARENTUSEDCOUNTER   [ $[$GRANDPARENTNOTUSEDCOUNTER+$GRANDPARENTUSEDCOUNTER] / $PARENTNOTUSEDCOUNTER ]"
done < $PARENTNOTUSED




echo
echo
echo Total that look like they are still being used: $FOUNDCOUNTER
echo Total that are not directly referenced, but their parent key is: $PARENTUSEDCOUNTER   \(logged to $PARENTUSED\)
echo Total that are not directly referenced, but their grandparent key is: $GRANDPARENTUSEDCOUNTER      \(logged to $GRANDPARENTUSED\)
echo Total that neither they, their parent, nor their grandparent key is referenced: $GRANDPARENTNOTUSEDCOUNTER  \(logged to $GRANDPARENTNOTUSED\)
echo Total that have short keys that are not referenced: $SHORTNOTUSEDCOUNTER   \(logged to $SHORTNOTUSED\)
echo
echo Recommendations...
if [ "$DUPLICATECOUNT" -gt 0 ]; then
    echo Resolve $DUPLICATECOUNT duplicate messages:
    echo $DUPLICATES
    echo
fi
if [ "$SHORTNOTUSEDCOUNTER" -gt 0 ]; then
    echo Check and remove the $SHORTNOTUSEDCOUNTER messages in $SHORTNOTUSED
fi
if [ "$GRANDPARENTNOTUSEDCOUNTER" -gt 0 ]; then
    echo It is also worth checking the $GRANDPARENTNOTUSEDCOUNTER messages in $GRANDPARENTNOTUSED
fi
if [ "$PARENTUSEDCOUNTER" -gt 0 ]; then
    echo Some messages in $PARENTUSED \($PARENTUSEDCOUNTER\) could still be unused, but it is less likely.
fi
if [ "$GRANDPARENTUSEDCOUNTER" -gt 0 ]; then
    echo Some messages in $GRANDPARENTUSED \($GRANDPARENTUSEDCOUNTER\) could still be unused, but it is less likely.
fi
echo
echo
