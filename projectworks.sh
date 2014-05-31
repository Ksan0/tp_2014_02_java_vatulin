if [ "$1" == "start" ]; then
	if ! [ -f daemonpid ]; then
		eval "sudo -u ksan start-stop-daemon -d ~/www/tp_2014_02_java_vatulin/ -Sbmp daemonpid -x /usr/share/maven/bin/mvn -- exec:java -Dexec.mainClass=Main"
		if [ $? -eq 0 ]; then
			echo "server started on port 8080"
		fi
		exit $?
	fi
	echo "server is working. Try stop or restart"
	exit 1
fi
if [ "$1" == "stop" ]; then
	eval "sudo start-stop-daemon -Kp daemonpid"
	if [ $? -eq 0 ]; then
		echo "server stopped"
		eval "sudo rm -f daemonpid"
		exit 0
	fi
	exit $?
fi
if [ "$1" == "restart" ]; then
	eval "bash projectworks.sh stop"
	if [ $? -eq 0 ]; then
		eval "bash projectworks.sh start"
	fi
	exit $?
fi
if [ "$1" == "compile" ]; then
	eval "mvn compile"
	exit $?
fi
if [ "$arg1" == "" ]; then
	echo "Help"
	echo "daemonworks.sh {start | stop | restart | compile}"
	echo "work with tp_java_123game daemon"
	exit 0
fi
