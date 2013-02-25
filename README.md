# JAlphaNode - Clustered cron job scheduler

JAlphaNode is a clustered timer that can survive to node failures, written entirely in Java. The propose of JAlphaNode is to provide a reliable mechanism of task scheduling, which can be deployed on several nodes connected to each other. When JAlphaNode starts, one of the nodes is elected as the master, and only that node can execute the scheduled tasks. If this node fails, another one is elected as the master, which continues the work. Using a cluster ensures that as long as one of the node in the cluster is available, the scheduled task has a high-availability characteristic and will be executed.

JAlphaNode provides powerful characteristics such as:

 - Configuration mechanism. JAlphaNode not only can be configured declaratively through a XML file, but can also be configured programmatically.
 - Listener API, where clients can be notified when events take place.
 - UI module which includes a CLI and a GUI.

Requirements: JDK 6 compliant JVM.

