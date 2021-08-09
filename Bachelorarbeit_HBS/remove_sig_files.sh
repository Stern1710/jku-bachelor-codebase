#!/bin/sh

FOLDER="."

if [ "$#" -eq 1 ]; then
    FOLDER="$1"
fi

echo "The following files will be removed:"
find $FOLDER -name "*.sig" -type f

read -p "Do you wish to remove the listed files (y|N)? " yn
case $yn in
    [Yy]* ) 
        find $FOLDER -name "*.sig" -type f -delete;
        echo "Done deleting file";
        break;;
    * ) break;; # Just add this here so that it looks cleaner in case of reuse
esac
