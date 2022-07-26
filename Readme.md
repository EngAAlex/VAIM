# VAIM Supplemental material

This code is supplied as supplementary material for the TVCG paper submission "Influence Maximization with Visual Analytics". The paper presents _VAIM_, a visual analytics solution for studying information diffusion processes and to tackle the problem of influence maximization.

Paper is published as gold open access, and is available on [IEEE Xplore](https://www.computer.org/csdl/journal/tg/5555/01/09829321/1EYxoEPe9eU).

Please check the [Youtube](https://www.youtube.com/watch?v=LW3P4JCTDIM) video where the features of the system are shown and commented. 

## System Architecture

VAIM is a client-server application supported by a graph database (Neo4J). The repository has therefore two folders: one with usage and building details for the server and one for the client.

## System requirements

VAIM uses software that is available on all the three major operating systems, specifically:

* [GIT](https://git-scm.com/) and [GIT-LFS](https://git-lfs.github.com/) for downloading the code and data
* [Neo4J](https://neo4j.com/) graph database
* [NPM](https://www.npmjs.com/) javascript package manager for the client
* [OpenJDK](https://openjdk.org/) java implemenetation and [Maven](https://maven.apache.org/) for building and running the server
* [GraphViz](https://graphviz.org/) graph drawing library 

System as been tested on __Windows 10 & 11__ and __Ubuntu 22.04__, but should be supported on Mac too. 

### WARNING

The demo database dumps, given their size, are stored on git using the GIT-Large File Storage (LFS). To make sure those files are correctly downloaded, please check out the repository using git, and not by directly downloading the zip file. If so, the code would still work, but database dumps will malfunction.

## Installation and available Scripts

Detailed information about the installation of both server and client are available on the respective folders. However, a script for a complete default installation ```VAIM_install.sh``` is available for Linux in the ```scripts``` folder of this repository. This installs all the required packages and sets up the database. To run it, navigate to the ```scripts``` folder and run it as ```sh VAIM_install.sh```. Sudo access will be requested. 

For Windows, dependencies need to be individually installed using msi packages or portable installations, making it very difficult to provide an automated installation script. After dependencies have been installed, please refer to the individual server and client readmes to complete the installation.

Once installation is complete, navigate to the ```scripts``` folder of VAIM and run ```VAIM_start.sh``` script on Linux or ```VAIM_start.bat``` on Windows or run the client and server independently with the instructions in the readme files in their respective folders.

# License

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
