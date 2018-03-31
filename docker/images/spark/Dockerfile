FROM java:openjdk-8-jdk

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get -y upgrade && apt-get install -y  \
    dnsutils \
    supervisor \
    wget \
 && rm -rf /var/lib/apt/lists/* \
 && apt-get clean

ARG APACHE_MIRROR=www-eu.apache.org/dist

ARG SPARK_VERSION=2.3.0
ARG HADOOP_VERSION=2.7

RUN wget -q http://$APACHE_MIRROR/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz \
      -O /tmp/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz \
 && tar -xzf /tmp/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz -C /usr/local/ && \
    mv /usr/local/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} /usr/local/spark/ && \
    rm /tmp/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz

CMD ["/usr/bin/supervisord"]
