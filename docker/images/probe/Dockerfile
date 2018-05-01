FROM ubuntu:18.04

RUN apt-get update && apt-get -y upgrade && apt-get install -y  \
    build-essential \
    ca-certificates \
    curl \
    dnsutils \
    iputils-ping \
    kafkacat \ 
    logrotate \
    nano \
    openjdk-8-jdk \
    openssh-server \
    openssl \
    python \
    python-argparse \
    python-bcrypt \
    python-dev \
    python-openssl \
    python-setuptools \
    scala \
    software-properties-common \
    supervisor \
    wget \
 && rm -rf /var/lib/apt/lists/* \
 && apt-get clean

ARG APACHE_MIRROR=www-eu.apache.org/dist
ARG SCALA_VERSION=2.12

# Install Kafka and add bin folder to the path
ENV KAFKA_VERSION 1.1.0
ENV KAFKA_HOME /usr/local/kafka

RUN wget -q http://${APACHE_MIRROR}/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz \
      -O /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz \
 && tar -xzf /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz -C /usr/local/ && \
    mv /usr/local/kafka_${SCALA_VERSION}-${KAFKA_VERSION} /usr/local/kafka/ && \
    rm /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz
RUN echo 'export PATH="$PATH:/usr/local/kafka/bin"' >> ~/.bash_profile

# Install Cassandra and add bin folder to the path
ENV CASSANDRA_VERSION 3.11.2
ENV CASSANDRA_HOME /usr/local/cassandra

RUN wget -q http://${APACHE_MIRROR}/cassandra/${CASSANDRA_VERSION}/apache-cassandra-${CASSANDRA_VERSION}-bin.tar.gz \
      -O /tmp/apache-cassandra-${CASSANDRA_VERSION}-bin.tgz \
 && tar -xzf /tmp/apache-cassandra-${CASSANDRA_VERSION}-bin.tgz -C /usr/local/ && \
    mv /usr/local/apache-cassandra-${CASSANDRA_VERSION} /usr/local/cassandra && \
    rm /tmp/apache-cassandra-${CASSANDRA_VERSION}-bin.tgz
RUN echo 'export PATH="$PATH:/usr/local/cassandra/bin"' >> ~/.bash_profile
RUN echo 'export PATH="$PATH:/usr/local/cassandra/tools/bin"' >> ~/.bash_profile

# Install Spark and add bin folder to the path
ARG SPARK_VERSION=2.3.0
ARG HADOOP_VERSION=2.7

RUN wget -q http://$APACHE_MIRROR/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz \
      -O /tmp/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz \
 && tar -xzf /tmp/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz -C /usr/local/ && \
    mv /usr/local/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION} /usr/local/spark/ && \
    rm /tmp/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz
RUN echo 'export PATH="$PATH:/usr/local/spark/bin"' >> ~/.bash_profile

# Install Aerospike Tools
ENV AEROSPIKE_VERSION 3.15.3.6

RUN wget "https://www.aerospike.com/artifacts/aerospike-tools/${AEROSPIKE_VERSION}/aerospike-tools-${AEROSPIKE_VERSION}-ubuntu16.04.tgz" \
      -O /tmp/aerospike-tools.tgz \
  && mkdir /tmp/aerospike \
  && tar xzf /tmp/aerospike-tools.tgz --strip-components=1 -C /tmp/aerospike

RUN ls /tmp/ && dpkg -i /tmp/aerospike/aerospike-tools-*.ubuntu16.04.x86_64.deb \
  && rm -rf /tmp/aerospike-tools.tgz /tmp/aerospike

# Setup SSH
EXPOSE 22
RUN echo "\nPermitRootLogin yes\n" >> /etc/ssh/sshd_config
RUN echo "root\nroot" | passwd

CMD ["/usr/bin/supervisord"]
