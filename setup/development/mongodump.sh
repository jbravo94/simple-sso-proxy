#!/bin/sh
docker-compose -f mongodb-docker-compose.yml exec mongo mongodump --username root --password example --authenticationDatabase admin --db test --out /backup
docker cp $(docker-compose -f mongodb-docker-compose.yml ps -q mongo):/backup .
