#!/bin/sh -e

if [ $# -ne 1 ]
then
  echo "Usage: $(basename $0) <number of instances>"
  exit 1
fi

INTANCES=$1

SCRIPT_FOLDER=$(dirname $0)
CONFIG_FILE=$SCRIPT_FOLDER/../etc/jalphanode-config.xml
DEMO_JAR=$SCRIPT_FOLDER/../jalphanode-demo.jar
TARGET=$SCRIPT_FOLDER/../inst
NODE_NAME_PLACEHOLDER='${NODE_NAME}'

mkdir -p $TARGET

for i in $(seq 1 $INTANCES)
do
  name=inst$i
  inst=$TARGET/$name

  mkdir -p $inst
  rsync -aq --exclude=demo $SCRIPT_FOLDER/../../ $inst

  # copy jobs
  cp $DEMO_JAR $inst/lib/

  # copy configuration file and replace placeholder
  cat $CONFIG_FILE | sed "s/$NODE_NAME_PLACEHOLDER/$name/" > $inst/etc/$(basename $CONFIG_FILE)
done
