# ./bin/sh

##### THIS SCRIPT INSTALLS THE WHOLE VAIM INFRASTRUCTURE
##### IF YOU WANT TO INSTALL JAVA AND NEO4J YOURSELF COMMENT FROM HERE
echo Starting VAIM script installer for Linux
echo

echo Installing Prerequisites...

sudo apt-get update -qq
sudo apt-get install -y maven git git-lfs graphviz npm openjdk-11-jdk

echo DONE!
echo 

echo Installing Neo4J

wget -O - https://debian.neo4j.com/neotechnology.gpg.key | sudo apt-key add -
echo 'deb https://debian.neo4j.com stable latest' | sudo tee -a /etc/apt/sources.list.d/neo4j.list
sudo apt-get update -qy

sudo apt-get install -y neo4j=1:4.4.8

echo Installed Neo4J!

##### TO HERE. 

echo Setting Neo4J default password and loading default database

sudo neo4j-admin set-initial-password neo4j

echo Retrieving code and creating git environment

cd ..

echo Building server

cd server

mvn clean package -DskipTests

echo Loading Dump into Neo4j

sudo neo4j-admin load --database=neo4j --force --from="dumps/livedemodump.dump"

echo Done! Installing client

cd ../client

npm install

echo All done! Start Neo4J and the client/server to get VAIM running.
