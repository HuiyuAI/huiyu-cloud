FROM alpine

RUN apk add --no-cache openjdk8-jre

ENV TZ=Asia/Shanghai
RUN set -eux; \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime; \
    echo $TZ > /etc/timezone

WORKDIR /huiyu

COPY ./docker-entrypoint.sh /huiyu/docker-entrypoint.sh
RUN chmod +x /huiyu/docker-entrypoint.sh

COPY ./huiyu-gateway/target/huiyu-gateway-1.0.0.jar /huiyu/huiyu-gateway-1.0.0.jar
COPY ./huiyu-service/service-core/target/service-core-1.0.0.jar /huiyu/huiyu-service-1.0.0.jar
COPY ./huiyu-auth/target/huiyu-auth-1.0.0.jar /huiyu/huiyu-auth-1.0.0.jar

ENV PATH=$PATH:/bin:/usr/bin:/usr/local/bin

EXPOSE 8000 8010 8100

ENTRYPOINT ["/huiyu/docker-entrypoint.sh"]
