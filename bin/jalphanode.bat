@rem ***************************************************************************
@rem JAlphaNode: Java Clustered Timer
@rem Copyright (C) 2011 Pedro Ribeiro
@rem
@rem This library is free software; you can redistribute it and/or
@rem modify it under the terms of the GNU Lesser General Public
@rem License as published by the Free Software Foundation; either
@rem version 2.1 of the License, or (at your option) any later version.
@rem
@rem This library is distributed in the hope that it will be useful,
@rem but WITHOUT ANY WARRANTY; without even the implied warranty of
@rem MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
@rem Lesser General Public License for more details.
@rem
@rem You should have received a copy of the GNU Lesser General Public
@rem License along with this library; if not, write to the Free Software
@rem Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
@rem
@rem $Id$
@rem ***************************************************************************
@echo off

set SCRIPT_FOLDER=%~dp0

set CORE_JAR=jalphanode-core.jar
set UI_JAR=jalphanode-ui.jar

set LOG_DIR=%SCRIPT_FOLDER%\..\log
set MAIN_CLASS=org.jalphanode.ui.JalphaNodeCli

@rem JGroups bind address -Djgroups.bind_addr=1.2.3.4.
set JVM_PARAMS=-Djava.net.preferIPv4Stack=true -Dlog4j.logDir=%LOG_DIR%

set CP=%SCRIPT_FOLDER%\..\etc
set CP=%CP%;%SCRIPT_FOLDER%\..\%CORE_JAR%
set CP=%CP%;%SCRIPT_FOLDER%\..\%UI_JAR%
for %%i in (%SCRIPT_FOLDER%\..\lib\*.jar) do call set CP=%%CP%%;%%i

if not exist %LOG_DIR% (
	mkdir %LOG_DIR%
)

java -cp %CP% %JVM_PARAMS% %MAIN_CLASS% %*
