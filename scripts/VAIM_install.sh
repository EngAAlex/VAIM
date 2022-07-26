# ./bin/sh

##### THIS SCRIPT INSTALLS THE WHOLE VAIM INFRASTRUCTURE
##### IF YOU WANT TO INSTALL JAVA AND NEO4J YOURSELF COMMENT FROM HERE
echo Starting VAIM script installer for Linux
echo

echo Installing Prerequisites...

sudo apt-get update -qq
sudo apt-get install -y maven graphviz git-lfs npm openjdk-11-jdk

echo
echo DONE!
echo 
echo
echo Installing Neo4J
echo
wget -O - https://debian.neo4j.com/neotechnology.gpg.key | sudo apt-key add -
echo 'deb https://debian.neo4j.com stable latest' | sudo tee -a /etc/apt/sources.list.d/neo4j.list
sudo apt-get update -qy

sudo apt-get install -y neo4j=1:4.4.8
echo
echo Installed Neo4J!
echo
##### TO HERE. 

echo Setting Neo4J default password and loading default database

sudo neo4j-admin set-initial-password neo4j
echo
echo Retrieving files in LFS
echo
cd ..

git lfs pull

echo
echo Building server

echo
cd server

mvn clean package -DskipTests

echo
echo Loading Dump into Neo4j
echo

sudo neo4j-admin load --database=neo4j --force --from="dumps/livedemodump.dump"

echo
echo Done! Installing client
echo

cd ../client

npm install

echo
echo
echo
echo All done! Start Neo4J and the client/server to get VAIM running.
