#!/bin/bash

FOLDER="."
FILE_PREFIX="file_"
NUM_FILES=0

if [ "$#" -eq 2 ]; then
    FOLDER="$1"
    NUM_FILES=$2
fi

echo "We will start generating $NUM_FILES random files in $FOLDER";

for (( c=0; c<$NUM_FILES; c++ ))
do
    echo "Generating file $c"
    rngCount=$RANDOM
    rngCount=$(( rngCount / 128))
    echo "$rngCount"
	dd if=/dev/urandom of=$FOLDER$FILE_PREFIX$c  bs=1K  count=$rngCount
done
