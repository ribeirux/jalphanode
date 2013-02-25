# Quick Start

JAlphaNode is a clustered cron job scheduler that can survive to node failures, written entirely in Java. The propose of JAlphaNode is to provide a reliable mechanism of task scheduling, which can be deployed on several nodes connected to each other.  
When JAlphaNode starts, one of the nodes is elected as the master, and only that node can execute the scheduled tasks. If this node fails, another one is elected as the master, which continues the work. Using a cluster ensures that as long as one of the node in the cluster is available, the scheduled task has a high-availability characteristic and will be executed.

JAlphaNode provides powerful characteristics such as:

 - Configuration mechanism. JAlphaNode not only can be configured declaratively through a XML file, but can also be configured programmatically.
 - Listener API, where clients can be notified when events take place.
 - UI module which includes a CLI and a GUI.

**Requirements: JDK 6 compliant JVM.**

## Download

Grab the latest binary distribution or checkout the last source code from the repository. Note that jalphanode requires JDK 1.6 or higher.

Unzip the contents of the binary distribuition into a new folder.

The most important contents of this folder are:
* bin: Contains administration scripts.
* etc: Contains the configuration files
* jalphanode-core.jar: Core functionality
* jalphanode-ui.jar: User interface functionality
* schema: Contains the configuration file schema

## Configuration

Jalphanode offers both declaratively and programmatically configuration approaches. Declarative configuration comes in a form of XML document (the XML structure is define on schema: schema/jalphanode-config.xsd). Every aspect of jalphanode that can be configured declaratively can also be configured programmatically (through the class JAlphaNodeConfigBuilder).

There are four main configuration abstractions in jalphanode:
* tasks: You should specify here all the tasks to run (the classes should implement the Task interface) and a cron expression.
* taskScheduler: Configuration of the scheduler thread pool used to execute the tasks.
* asyncExecutor: Configuration of the thread pool used for asynchronous notifications
* membership: In this configuration you can define the name of the cluster to connect, the current node name, and an optional JGroups configuration file (currently, JGroups is used for reliable multicast communication)

To run jalphanode, you can start with the sample configuration file etc/jalphanode-config.xml and change it. If you not specify a configuration file, the default configuration will be used.

To reload the configuration, you should restart jalphanode.

## Show me what I like

You can launch jalphanode as a service, or for testing proposes, from a command line interface (CLI) or a graphical user interface (GUI).

When the application is started, a log folder is automatically created with all log files. You can check through the log files, the current members of the cluster, the running tasks, etc...

If you want to start several instances on the same machine, you should copy the distribution folder, and use this copy to start a new instance.

### Service

To run as a service, just execute the script **bin/jalphanode-service**. This script by default uses the sample configuration file (etc/jalphanode-config.xml). You can change some configurations of this script, for instance, change the path of the configuration file, enable JMX (disabled by default), add JVM parameters, etc..

### CLI

CLI should be used only for testing proposes. To launch CLI, just execute the script **bin/jalphanode**. By default the script shows all available options.

To start jalphanode through the CLI using the sample configuration file, just run **bin/jalphanode -s etc/jalphanode-config.xml** (configuration file is optional)

### GUI

GUI should be used only for testing proposes. To launch GUI, run *bin/jalphanode -g*

You can select an optional configuration file before starting jalpahnode. To use the sample configuration file, just select the file **etc/jalphanode-config.xml** using the browse button, and push start.

The GUI provides an efficient way to check the current members of the group.

## Mailing lists

JAlphaNode Users List: http://groups.google.com/group/jalphanode

JAlphaNde Developers List: http://groups.google.com/group/jalphanode-dev
