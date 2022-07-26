# GRSI Verification 

In this folder are included the files and information required by the GRSI to verification of the eligibility of the replicability stamp for the paper **Influence Maximization with Visual Analytics** and its system **VAIM**. 

Specifically, the provided data reproduces Figure 1 of the paper. If necessary, it is possible to include the data to reproduce the other figures of the paper.

VAIM, the proposed system, is an interactive application. Moreover, the single simulations are stochastic processes, meaning that each new realization would be different from the previous, and it would be extremely hard to reproduce the same conditions which generated the same data showed in the figure in the paper. Therefore, as reported in the [GRSI submission requirements](http://www.replicabilitystamp.org/requirements.html), we include a short video that shows all the steps on how to reproduce the figure - once the system is installed and run.

This folder contains the following data:

* A database dump containing the database used for generating the figure (verification_db.dump)
* A short video showing step-by-step how to reproduce the figure (replicability_video.mp4)
* The figure reported in the paper without labels (representative_image.png)
* Liability Form

## Setup

The system must be first installed -- please refer to the readme file in the repository root. It is provided a fully automated install script for Linux alone - as the same for Windows would be much more complex to prepare. However, full instructions are given to install VAIM on Windows and a start script in the form of a batch file is provided.

Once installed, please continue with the following instructions.

### Linux (tested on vanilla Ubuntu 22.04)

**First**, navigate to the repository root folder and open a terminal there. load the database dump using the following command.

```# neo4j-admin load --database=neo4j --force --from=GRSI/verification_db.dump```

Please note that ```--force``` will erase all existing data in ```neo4j``` database.

**Second**, navigate to the ```scripts``` folder and start the system using the ```VAIM_start.sh``` script in the ```scripts``` folder.

```$ sh VAIM_start.sh```

Root priviledges are needed to start Neo4J server (if it's not running already).

A web page should open in the default browser. If not, navigate to ```localhost:3000``` (assuming a default local installation such as the one obtained using the provided ```VAIM_install.sh``` script).

### Windows (10 & 11)

**First**, open a command line and navigate to the root folder of the repository. There, load the dump in Neo4J.

```neo4j-admin load --database=neo4j --force --from=GRSI\verification_db.dump```

Please note that ```--force``` will erase all existing data in ```neo4j``` database.

**Second** start the system using the ```VAIM_start.bat``` script in the ```scripts``` folder.

A web page should open in the default browser. If not, navigate to ```localhost:3000``` (assuming a local default installation).

## Execution

Follow the instructions in the attached video to replicate the figure. 

