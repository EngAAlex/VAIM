# VAIM Supplemental material

This code is supplied as supplementary material for the TVCG paper submission "Influence Maximization with Visual Analytics". The paper presents _VAIM_, a visual analytics solution for studying information diffusion processes and to tackle the problem of influence maximization.

Paper is published as gold open access, and is available on [IEEE Xplore](https://www.computer.org/csdl/journal/tg/5555/01/09829321/1EYxoEPe9eU).

## System Architecture

VAIM is a client-server application supported by a graph database (Neo4J). The repository has therefore two folders: one with usage and building details for the server and one for the client.

## System requirements

VAIM uses software that is available on all the three major operating systems, specifically:

* [GIT](https://git-scm.com/) for downloading the code
* [Neo4J](https://neo4j.com/) graph database
* [NPM](https://www.npmjs.com/) javascript package manager for the client
* [OpenJDK](https://openjdk.org/) java implemenetation and [Maven](https://maven.apache.org/) for building and running the server
* [GraphViz](https://graphviz.org/) graph drawing library 

System as been tested on __Windows 10 & 11__ and __Ubuntu 22.04__, but should be supported on Mac too. 

## Installation and available Scripts

Detailed information about the installation of both server and client are available on the respective folders. However, a script for a complete default installation is available for Linux in the ```scripts``` folder of this repository. This installs all the required packages and sets up the database. For Windows, the steps are the same, but dependencies need to be individually installed using msi packages or portable installations, making it very difficult to provide an automated installation script.

Once installation is complete, navigate to the script folder of VAIM and run ```VAIM_start.sh``` script on Linux or ```VAIM_start.bat``` on Windows or run the client and server independently with the instructions in the readme files of their respective folders.

## Live Demo

A live demo of the system can be accessed [here](http://vaim.cvast.tuwien.ac.at).

# License

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
