# ./bin/sh

echo Launching VAIM

sudo neo4j start

cd ..

gnome-terminal -- java -jar 'server/target/vaim-1.0.0.jar' & 
gnome-terminal --wait -v -- npm --prefix=client/ start  &

echo Terminal windows have been opened for debug. VAIM is loading, with the default options, navigate with your browser to localhost:3000.	
