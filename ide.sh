rm -rf "ide.stopped"
echo "Starting ide" >>"ide.started"
cd ide
yarn start "$1" --hostname 0.0.0.0 --port "$2"
rm -rf "ide.started"
echo "Stopped ide" >>"ide.stopped"
