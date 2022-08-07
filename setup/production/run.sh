#!/bin/bash
set -e

case "$1" in 
start)
   docker-compose up -d
   ;;
stop)
   docker-compose down
   ;;
status)
   docker-compose ps
   ;;
restart)
   $0 stop
   $0 start
   ;;
reset)
   docker-compose down -v
   ;;
*)
   echo "Usage: $0 {start|stop|status|restart|reset}"
esac

exit 0 