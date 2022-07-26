@echo off

echo Terminal windows will be opened for debug. VAIM is loading: with the default options a browser window should open automatically, otherwise navigate with your browser to localhost:3000. If database is not yet ready (connection error on the client) please reload the page until a connection can be established.

start /B neo4j console :: change to "start" if neo4j is installed as a service

start /min cmd /k java -jar ..\server\target\vaim-1.0.0.jar
start /min cmd /k npm --prefix=..\client\ start