#!/bin/bash -e
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

SCRIPT_FOLDER=$(dirname $0)

UI_JAR="jalphanode-ui.jar"

LOG_DIR="$SCRIPT_FOLDER/../log"
MAIN_CLASS="org.jalphanode.ui.JalphaNodeCli"

# JGroups bind address -Djgroups.bind_addr=1.2.3.4.
JVM_PARAMS="$JVM_PARAMS -Djava.net.preferIPv4Stack=true -Dlog4j.logDir=$LOG_DIR -Xdock:name=JAlphaNode"

CP="$SCRIPT_FOLDER/../etc"
CP="$CP:$SCRIPT_FOLDER/../$UI_JAR"
for lib in $SCRIPT_FOLDER/../lib/*.jar ; do
    CP="$CP:$lib"
done

mkdir -p $LOG_DIR

if [ -d "$JAVA_HOME" -a -x "$JAVA_HOME/bin/java" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD=$(which java)
fi

if [ ! -x "$JAVACMD" ]; then
    echo "Error: java not found"
    exit 1
fi

$JAVACMD -cp $CP $JVM_PARAMS $MAIN_CLASS "$@"
