#!/bin/bash
# cd /storage/emulated/0/ACode/Projects/Coding/Plugin/Item-Caster

COMMAND=$1
TARGET=$2

case $COMMAND in
	build)
		mvn clean package -pl $TARGET
		;;
	test)
		mvn test -pl $TARGET
		;;
	push)
		sh push.sh
		;;
	*)
		echo "Unknown command: $COMMAND"
		echo "Usage:"
		echo "  sh run.sh build <module>"
		echo "  sh run.sh test <module>"
		echo "  sh run.sh push"
		exit 1
		;;
esac