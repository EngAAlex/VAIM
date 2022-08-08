# VAIM Server source code

VAIM application server is responsible for parsing the new graph files, interact with the database, and respond to the client queries.

### Requirements

* [GIT-LFS](https://git-lfs.github.com/) for downloading the large database dumps.
* Maven and Java software (version >=11) 
* Neo4J server running - please refer to the [official documentation](https://neo4j.com/docs/operations-manual/current/installation/) to do so. On Windows, VAIM is recommended with a portable Neo4J installation, using ```Neo4J console``` as a start command.
* [GraphVIZ](https://graphviz.org/) library for creating layouts - optional.

### Build

First, clone the repository using Git.

Building and installation can be done automatically using the install script in the ```scripts``` folder located in the root of this repository or manually as follows.

**First**, get into the repository root and run ```git lfs pull```

If necessary, this will pull the larger files from the remote repository. At this point, switch to the server folder: ```cd server```

In the following, command line statements work interchangeably on both Windows and Linux unless explicitly stated otherwise. They also assume the working directory to be ```/path/to/VAIM/server```.

Before building, modify the ```application.properties``` file to match your Neo4J configuration and GraphVIZ configuration. VAIM looks for the SFDP GraphVIZ function from the command line. The default is "sfdp", meaning that GraphVIZ is run from PATH.  These configurations are not necessary if you installed VAIM using the installation script.

To build it, get into the server folder of the repository and type the following:

```
mvn clean package -DskipTests
```

It might take a few minutes, depending on the network speed and computer capabilities.

The server will be packaged in the newly created ```target``` folder as ```vaim-1.0.0.jar```. 

### Database restore (optional)

The available dumps can be restored to make use of the system right away. Otherwise, a clean database will be created automatically. To restore a dump, according to Neo4J documentation, use the following (with administrator priviledges):

```neo4j-admin load --force --database=neo4j --from=dumps/livedemodump.dump```

Please note that the existing database will be wiped out. You might want to dump the existing database for backup following the [official documentation](https://neo4j.com/docs/operations-manual/current/backup-restore/offline-backup/). Dumps have been created with Neo4J 4.4.9 - use of database versions prior to this might lead the dumps to malfunction.

### Start-up

Client can be started using the start script in the ```scripts``` folder located in the root of this repository or manually as follows.

First, make sure that the Neo4J server is up and running.

To start the server is sufficient to run the following:

```
java -jar target/vaim-1.0.0.jar
```

### New Graph Load

To load a new graph in the system, run the following:

```
java -jar target/vaim-1.0.0.jar -f <path to folder with GML files> [--redraw]
```

The system will load all the files in the specified folder in sequence, but it will not go recursively if other folders are present. At the moment of writing, the system only accepts GML files. The command can be launched with the server either running or not. Database server on the other hand **must** be running.

Use the ```--redraw``` flag to compute a layout at the moment the system is loaded. Otherwise, the node coordinates indicated in the GML file will be used. 

**IMPORTANT**: the system presumes the input graph to be directed, otherwise edge direction will be randomized. The operation might take a while, especially on larger graphs.

### Known Issues

The JDBC driver and Spring in general produce a lot of low-level logging. Neo4j released a new Driver class that gives more freedom in log level control, but requires a revision of the whole database access strategy used in this code (planned for future release). In particular, the used JDBC driver expects a JDK 1.8, and therefore harmless debug exceptions might appear in the logs (i.e., Reflection.newIllegalAccessException - and alike).

