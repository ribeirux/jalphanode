#!/bin/bash -e
#*******************************************************************************
# JAlphaNode: Java Clustered Timer
# Copyright (C) 2011 Pedro Ribeiro
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 2.1 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
# License along with this library; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
#
# $Id$
#*******************************************************************************

SCRIPT_FOLDER=$(dirname $0)

CORE_JAR="jalphanode-core.jar"
UI_JAR="jalphanode-ui.jar"

LOG_DIR="$SCRIPT_FOLDER/../log"
MAIN_CLASS="org.jalphanode.ui.JalphaNodeCli"
JVM_PARAMS="$JVM_PARAMS -Djava.net.preferIPv4Stack=true -Dlog4j.logDir=$LOG_DIR -Xdock:name=JAlphaNode"

CP="$SCRIPT_FOLDER/../etc"
CP="$CP:$SCRIPT_FOLDER/../$CORE_JAR"
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