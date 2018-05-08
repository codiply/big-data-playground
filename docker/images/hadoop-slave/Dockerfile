FROM java:openjdk-8-jdk

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get -y upgrade && apt-get install -y  \
    dnsutils \
    openssh-server \
    rsync \
    supervisor \
    wget \
 && rm -rf /var/lib/apt/lists/*

# Define Apache Mirror
ARG APACHE_MIRROR=www-eu.apache.org/dist

# Download Hadoop
ARG HADOOP_VERSION="3.1.0"

ENV HADOOP_SHA256SUM 670D2CED595FA42D9FA1A93C4E39B39F47002CAD1553D9DF163EE828CA5143E7

RUN cd /tmp && \
       wget -q ${APACHE_MIRROR}/hadoop/core/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz && \
       echo "${HADOOP_SHA256SUM} *hadoop-${HADOOP_VERSION}.tar.gz" | sha256sum -c - && \
       tar xzf hadoop-${HADOOP_VERSION}.tar.gz -C /usr/local && \
       mv /usr/local/hadoop-${HADOOP_VERSION} /usr/local/hadoop && \
       rm hadoop-${HADOOP_VERSION}.tar.gz
ENV HADOOP_HOME /usr/local/hadoop/

# Update path
ENV PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin

CMD ["/usr/bin/supervisord"]
