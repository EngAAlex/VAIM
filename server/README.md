# VAIM Server source code

This code is supplied as supplementary material for the TVCG paper submission "Influence Maximization with Visual Analytics". What follows is the README file of the server code.

## Overview  
VAIM application server is responsible for parsing the new graph files, interact with the database, and respond to the client queries.

### Requirements

* Maven and Java software (version 14) 
* Neo4J server running 
* [GraphVIZ](https://graphviz.org/) library for creating layouts - optional.

### Build

Before building, modify the "application.properties" function to match your Neo4J configuration and GraphVIZ configuration. VAIM looks for the SFDP GraphVIZ function from the command line. The default is "wsl sfdp", meaning that GraphVIZ is run on the Windows Subsystem for Linux. Remove "wsl" if you run it from PATH.

To build it, get into the root folder of the project and type the following:

```
$ mvn clean package -DskipTests
```

It might take a few minutes, depending on the network speed and computer capabilities.

The server will be packaged in the newly created "target" folder as "vaim-1.0.0.jar". 

### Database restore (optional)

The available dumps can be restored to make use of the system right away. Otherwise, a clean database will be created automatically. 

### Start-up

First, make sure that the Neo4J server is up and running.

To start the server is sufficient to run the following:

```
$ java -jar vaim-1.0.0.jar
```

### New Graph Load

To load a new graph in the system, run the following:

```
$ java -jar vaim-1.0.0.jar -f <path to folder with GML files> [--redraw]
```

The system will load all the files in the specified folder in sequence, but it will not go recursively if other folders are present. At the moment of writing, the system only accepts GML files. The command can be launched with the server either running or not. Database server must be running.

Use the ``--redraw`` flag to compute a layout at the moment the system is loaded. 

**IMPORTANT**: the system presumes the input graph to be directed, otherwise edge direction will be randomized.

### Known Issues

The JDBC driver and Spring in general produce a lot of low-level logging. Neo4j released a new Driver class that gives more freedom in log level control, but requires a revision of the whole database access strategy used in this code (planned for future release). In particular, the used JDBC driver expects a JDK 1.8, and therefore harmless debug exceptions might appear in the logs (i.e., Reflection.newIllegalAccessException - and alike).

