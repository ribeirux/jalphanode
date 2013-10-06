@REM
@REM    Copyright 2011 Pedro Ribeiro
@REM
@REM    Licensed under the Apache License, Version 2.0 (the "License");
@REM    you may not use this file except in compliance with the License.
@REM    You may obtain a copy of the License at
@REM
@REM        http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM    Unless required by applicable law or agreed to in writing, software
@REM    distributed under the License is distributed on an "AS IS" BASIS,
@REM    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM    See the License for the specific language governing permissions and
@REM    limitations under the License.
@REM

@echo off

set SCRIPT_FOLDER=%~dp0

set UI_JAR=jalphanode-ui.jar

set LOG_DIR=%SCRIPT_FOLDER%\..\log
set MAIN_CLASS=org.jalphanode.ui.JalphaNodeCli

@rem JGroups bind address -Djgroups.bind_addr=1.2.3.4.
set JVM_PARAMS=-Djava.net.preferIPv4Stack=true -Dlog4j.logDir=%LOG_DIR%

set CP=%SCRIPT_FOLDER%\..\etc
set CP=%CP%;%SCRIPT_FOLDER%\..\%UI_JAR%
for %%i in (%SCRIPT_FOLDER%\..\lib\*.jar) do call set CP=%%CP%%;%%i

if not exist %LOG_DIR% (
	mkdir %LOG_DIR%
)

java -cp %CP% %JVM_PARAMS% %MAIN_CLASS% %*
