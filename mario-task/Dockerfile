# Version 1.0.0
FROM java:8

MAINTAINER Qiujw "o.awang@qq.com"

# 定义时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo 'Asia/Shanghai' >/etc/timezone

# 环境变量
# JAR运行目录
ENV WORK_PATH /home/app
# 日志数据目录
ENV WORK_DATA /home/tomcat
#ENV APP_NAME @project.build.finalName@.@project.packaging@
#ENV APP_VERSION @project.version@

EXPOSE 8080

#USER
#useradd tomcat

#VOLUME
VOLUME $WORK_DATA $WORK_PATH

#ADD
#-server -Xms8G -Xmx8G -Xmn4G -Xss256k -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:$WORK_DATA/logs/gc_%p.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=30m -XX:+HeapDumpOnOutOfMemoryError
ARG JAVA_OPTS='-server -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:$WORK_DATA/logs/gc_%p.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=5 -XX:GCLogFileSize=30m -XX:+HeapDumpOnOutOfMemoryError'
ADD target/mario-task-*.jar $WORK_PATH/app.jar

ENV MEM_OPTS=""
ENV APOLLO_ENV=""
ENV APOLLO_CONFIG_SERVICE=""
ENV SPRING_PROFILES_ENV=""
ENV JOB_IP=""
ENV JOB_PORT=""


# 输出JAVA_OPTS
RUN echo $JAVA_OPTS
#RUN echo $APOLLO_ENV
#RUN echo $APOLLO_CONFIG_SERVICE

#COPY
#COPY $APP_NAME $WORK_PATH/

#LABEL
#STOPSIGNAL
#ARG
#ONBUILD

# WORKDIR
WORKDIR $WORK_PATH

#ENV JAVA_OPTS=""

# ENTRYPOINT java ${JAVA_OPTS} -Denv=${APOLLO_ENV} -Dapollo.configService=${APOLLO_CONFIG_SERVICE} -Djava.security.egd=file:/dev/./urandom -jar app.jar
# ENTRYPOINT ["/usr/bin/java", "${JAVA_OPTS}", "-jar", "-Djava.security.egd=file:/dev/./urandom", "app.jar"]
ENTRYPOINT java ${JAVA_OPTS} ${MEM_OPTS} -Denv=${APOLLO_ENV} -Dapollo.configService=${APOLLO_CONFIG_SERVICE} -Dxxl.job.executor.ip=${JOB_IP} -Dxxl.job.executor.port=${JOB_PORT} -jar -Djava.security.egd=file:/dev/./urandom app.jar