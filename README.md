# Quick Start

Jalphanode is a clustered cron job scheduler that can survive to node failures written entirely in Java. The propose of jalphanode is to provide a reliable mechanism of task scheduling which can be deployed on several nodes connected to each other.
When jalphanode starts, one of the nodes is elected as the master and only that node can execute the scheduled tasks. If this node fails, another one is elected as the master. Using a cluster ensures that as long as one of the nodes in the cluster is available, the scheduled task has a high-availability characteristic and will be executed.

Jalphanode provides powerful characteristics such as:

 - High-availability. If at least one node in the cluster is available the scheduled tasks will be executed.
 - Configuration mechanism. Jalphanode not only can be configured declaratively through a XML file, but also programmatically (org.jalphanode.config.JAlphaNodeConfigBuilder).
 - Listener API. Clients can be notified when a new view is installed and before/after a task is executed. 
 - UI module which includes a CLI and a GUI.

## Support Group

For general discussions please use the [jalphanode google group](https://groups.google.com/forum/#!forum/jalphanode).

## Building jalphanode distribution

### Requirements

* [Maven](http://maven.apache.org/) 2.1.0 or above
* Java 6 or above

To build jalphanode:

1. Download the source from [here](https://github.com/ribeirux/jalphanode/archive/master.zip).
2. Unzip the contents and in the project root directory execute: `mvn clean install -Pdistribution`. This will build jalphanode and run all tests. To skip the tests when building execute `mvn clean install -Pdistribution -DskipTests`
3. Grab the jalphanode distribution located in **dist/target/jalphanode-${version}-bin.zip** and unzip the contents into a new folder.

![folder structure](https://raw.github.com/wiki/ribeirux/jalphanode/img/folder-structure.png)

The most important contents of this folder are:
* bin: Contains administration scripts.
* etc: Contains the configuration files

## Configuration

Jalphanode offers both declaratively and programmatically configuration approaches. Declarative configuration comes in a form of XML document. Every aspect that can be configured declaratively can also be configured programmatically through class jalphanodeConfigBuilder.

There are four main configuration abstractions in jalphanode:
* **tasks**: You should specify here all tasks to run. Each task should contain the FQN of the class to run (should implement *org.jalphanode.task.Task*), an unique name used to identify the task a cron expression and optionally the time zone.
* **taskScheduler**: Configuration of the scheduler thread pool used to execute tasks.
* **asyncExecutor**: Configuration of the thread pool used for asynchronous notifications.
* **membership**: In this configuration you can define the name of the cluster to connect, the current node name and optionally JGroups configuration file (currently, JGroups is used for reliable multicast communication)

Since jalphanode was designed to be extensible, each configuration abstraction supports an additional list of properties useful for custom implementations. 

To run jalphanode, you can start with the sample configuration file **etc/jalphanode-config.xml** and change it according to your requirements. If you don't specify a configuration file, the default configuration will be used.

Currently, to reload the configuration you should restart jalphanode although this is highly likely to change in the future. 

## Show me what I like

You can launch jalphanode as a service, or for testing proposes, from a command line interface (CLI) or a graphical user interface (GUI).

When the application is started, a log folder is automatically created with all log files. You can check through the log files, the current members of the cluster, the running tasks, etc...

If you want to start several instances on the same machine, you should copy the distribution folder and use this copy to start a new instance.

### Service

To run as a service, just execute script `bin/jalphanode-service`. This script by default uses the sample configuration file (**etc/jalphanode-config.xml**). You can change some configurations of this script, for instance, change the path of the configuration file, enable JMX (disabled by default), add JVM parameters, etc..

![service](https://raw.github.com/wiki/ribeirux/jalphanode/img/service.png)

### CLI

To launch CLI, just execute script `bin/jalphanode`. By default the script shows all available options.

![ui](https://raw.github.com/wiki/ribeirux/jalphanode/img/ui.png)

To start jalphanode through the CLI using the sample configuration file, just run `bin/jalphanode -s etc/jalphanode-config.xml` (configuration file is optional)

### GUI

GUI should be used only for testing proposes. To launch GUI, run `bin/jalphanode -g`

You can select an optional configuration file before starting jalpahnode. To use the sample configuration file, just select the file **etc/jalphanode-config.xml** using the browse button, and push start.

![gui](https://raw.github.com/wiki/ribeirux/jalphanode/img/gui.png)

The GUI provides an efficient way to check the current members of the group.

### Demo

Jalphanode comes with a demo. This demo automatically creates a cluster of N instances with some predefined tasks. To create the cluster execute script `demo/bin/setup-environment.sh` with the number of instances to create.

Example: demo/bin/setup-environment.sh 10

The above command creates 10 instances in folder **demo/inst**. To start all instances you can run for example:
`for inst in $(find . -mindepth 1 -maxdepth 1 -type d); do $inst/bin/jalphanode.sh -s $inst/etc/jalphanode-config.xml & done`

This will start all 10 instances. Only one instance will execute the tasks (the master) while other the 9 are waiting (begging) for the master to fail.

If you kill the master, a new view will be installed and another instance will assume the lead of the group and execute the tasks.

