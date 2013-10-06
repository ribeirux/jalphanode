#!/bin/sh -e
#
#    Copyright 2011 Pedro Ribeiro
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#

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
