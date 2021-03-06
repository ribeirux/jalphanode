#!/bin/bash
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

SCRIPT_FOLDER=`dirname $0`

JMX_ENABLED=false
JMX_LOCAL_ONLY=false

UI_JAR="jalphanode-ui.jar"

LOG_DIR="$SCRIPT_FOLDER/../log"
MAIN_CLASS="org.jalphanode.ui.JalphaNodeCli"

# JGroups bind address -Djgroups.bind_addr=1.2.3.4.
JVM_PARAMS="$JVM_PARAMS -Djava.net.preferIPv4Stack=true -Dlog4j.logDir=$LOG_DIR"

JALPHANODE_CONF="$SCRIPT_FOLDER/../etc/jalphanode-config.xml"
PID_FILE="$SCRIPT_FOLDER/../var/jalphanode.pid"
DAEMON_OUT="$LOG_DIR/jalphanode.out"

case $1 in
    start)
        if [ -f $PID_FILE ]; then
            if kill -0 $(cat $PID_FILE) > /dev/null 2>&1; then
                 echo "jalphanode is already running with PID: $(cat $PID_FILE)"
                 exit 0
              fi
        fi

        if $JMX_ENABLED; then
            JVM_PARAMS="$JVM_PARAMS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.local.only=$JMX_LOCAL_ONLY"
        fi

        CP="$SCRIPT_FOLDER/../etc"
        CP="$CP:$SCRIPT_FOLDER/../$UI_JAR"
        for lib in $SCRIPT_FOLDER/../lib/*.jar ; do
            CP="$CP:$lib"
        done

        mkdir -p $LOG_DIR
        mkdir -p $(dirname $PID_FILE)

        if [ -d "$JAVA_HOME" -a -x "$JAVA_HOME/bin/java" ]; then
            JAVACMD="$JAVA_HOME/bin/java"
        else
            JAVACMD=$(which java)
        fi

        if [ ! -x "$JAVACMD" ]; then
            echo "Error: java not found"
            exit 1
        fi

        nohup $JAVACMD -cp $CP $JVM_PARAMS $MAIN_CLASS -s $JALPHANODE_CONF > $DAEMON_OUT 2>&1 < /dev/null &

        if [ $? -eq 0 ]; then
            if /bin/echo -n $! > "$PID_FILE"; then
                sleep 1
                echo "jalphanode started with PID: $(cat $PID_FILE)"
            else
                echo "Error: failed to write PID"
                exit 2
            fi
        else
            echo "Error: jalphanode did not start"
            exit 3
        fi
        ;;
    stop)
        if [ ! -f "$PID_FILE" ]; then
            echo "Error: could not find file: $PID_FILE"
            exit 4
        else
            kill $(cat "$PID_FILE")
            rm "$PID_FILE"
            echo "jalphanode stopped"
            exit 0
        fi
        ;;
    restart)
        "$0" stop ${@}
        sleep 3
        "$0" start ${@}
        ;;
    status)
        if [ -f "$PID_FILE" ]; then
            # Read PID
            read PID < $PID_FILE
            if kill -0 $PID > /dev/null 2>&1; then
                echo "jalphanode is running with PID: $PID"
                exit 0
            else
                echo "jalphanode is stopped"
                exit 5
            fi
        else
            echo "jalphanode is stopped"
            exit 5
        fi
        ;;
    *)
        echo "Usage: $(basename $0) {start|stop|restart|status}"
esac

