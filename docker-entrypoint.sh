#!/bin/sh

set -ex;

TIME=$(date "+%Y%m%d%H%M%S")

java -jar -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/huiyu/huiyu-gateway-${TIME}.hprof -XX:+ExitOnOutOfMemoryError -Dspring.profiles.active=test /huiyu/huiyu-gateway-1.0.0.jar &

sleep 10

java -jar -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/huiyu/huiyu-service-${TIME}.hprof -XX:+ExitOnOutOfMemoryError -Dspring.profiles.active=test /huiyu/huiyu-service-1.0.0.jar &

sleep 10

java -jar -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/huiyu/huiyu-auth-${TIME}.hprof -XX:+ExitOnOutOfMemoryError -Dspring.profiles.active=test /huiyu/huiyu-auth-1.0.0.jar
