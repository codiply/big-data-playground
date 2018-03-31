FROM ubuntu:18.04

RUN apt-get update && apt-get -y upgrade && apt-get install -y  \
    build-essential \
    curl \
    dnsutils \
    iputils-ping \
    kafkacat \ 
    nano \
    openjdk-8-jdk \
    openssh-server \
    scala \
    software-properties-common \
    supervisor \
    wget \
 && rm -rf /var/lib/apt/lists/* \
 && apt-get clean

ARG APACHE_MIRROR=www-eu.apache.org/dist
ARG SCALA_VERSION=2.12

# Install Kafka and bin folder to the path
ENV KAFKA_VERSION 1.1.0
ENV KAFKA_HOME /usr/local/kafka

RUN wget -q http://${APACHE_MIRROR}/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz \
      -O /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz \
 && tar -xzf /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz -C /usr/local/ && \
    mv /usr/local/kafka_${SCALA_VERSION}-${KAFKA_VERSION} /usr/local/kafka/ && \
    rm /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz
RUN echo 'export PATH="$PATH:/usr/local/kafka/bin"' >> ~/.bash_profile

# Install Cassandra and bin folder to the path
ENV CASSANDRA_VERSION 3.11.2
ENV CASSANDRA_HOME /usr/local/cassandra

RUN wget -q http://${APACHE_MIRROR}/cassandra/${CASSANDRA_VERSION}/apache-cassandra-${CASSANDRA_VERSION}-bin.tar.gz \
      -O /tmp/apache-cassandra-${CASSANDRA_VERSION}-bin.tgz \
 && tar -xzf /tmp/apache-cassandra-${CASSANDRA_VERSION}-bin.tgz -C /usr/local/ && \
    mv /usr/local/apache-cassandra-${CASSANDRA_VERSION} /usr/local/cassandra && \
    rm /tmp/apache-cassandra-${CASSANDRA_VERSION}-bin.tgz
RUN echo 'export PATH="$PATH:/usr/local/cassandra/bin"' >> ~/.bash_profile
RUN echo 'export PATH="$PATH:/usr/local/cassandra/tools/bin"' >> ~/.bash_profile

# Setup SSH
EXPOSE 22
RUN echo "\nPermitRootLogin yes\n" >> /etc/ssh/sshd_config
RUN echo "root\nroot" | passwd

CMD ["/usr/bin/supervisord"]
