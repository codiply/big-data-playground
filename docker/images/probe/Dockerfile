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

ENV SCALA_VERSION 2.12
ENV KAFKA_VERSION 1.0.1
ENV KAFKA_HOME /usr/local/kafka

RUN wget -q http://apache.mirror.anlx.net/kafka/${KAFKA_VERSION}/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz \
      -O /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz \
 && tar -xzf /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz -C /usr/local/ && \
    mv /usr/local/kafka_${SCALA_VERSION}-${KAFKA_VERSION} /usr/local/kafka/ && \
    rm /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz
RUN echo 'export PATH="$PATH:/usr/local/kafka/bin"' >> ~/.bash_profile

EXPOSE 22
RUN echo "\nPermitRootLogin yes\n" >> /etc/ssh/sshd_config
RUN echo "root\nroot" | passwd

CMD ["/usr/bin/supervisord"]
