# Big Data Playground

This is a personal repo for experimenting with big data technologies including

- [Apache Kafka](http://kafka.apache.org/)
- [Apache Cassandra](http://cassandra.apache.org/)

## Docker containers

### Prerequisites

You will need to install

- [Docker](https://www.docker.com/) and
- [Docker Compose](https://docs.docker.com/compose/) (if it is not part of the Docker installation on your platform).

### Run the containers

There are currently the following containers defined in `docker-compose.yml`

- `cassandra-1`, `cassandra-2`, `cassandra-3`: Apache Cassandra cluster
- `kafka-1`, `kafka-2`, `kafka-3`: Apache Kafka cluster
- `probe`: Used for running applications from the command line from within the docker network
- `zookeeper-1`, `zookeeper-2`, `zookeeper-3`: Apache Zookeper cluster

To start all containers

    docker-compose up -d

To stop all containers

    docker-compose stop

or completely remove them

    docker-compose down

## Addresses on the host

Whenever suitable I have bound ports to the host so that for example access to various UI's is possible.

- Kafka Manager (UI): [localhost:9000](http://localhost:9000)
- Probe (ssh): [localhost:52022](http://localhost:9000)

## Run an application/main written in Scala

### Prerequisites

- Install [sbt](http://www.scala-sbt.org/) (Scala Built Tool).

### Run them on Probe container

- Build a fat jar `big-data-playground.jar` with `sbt assembly`. This is placed under `/target/big-data-playground/`. (Directory `/target/big-data-playground/ is mount at `/playground/` on the `probe` container.)
- Ssh to the probe container using the `ssh-probe` script. The password is `root`.
- Run a main with `java -cp /playground/ com.codiply.bgdp.SomeClassWithMain`
