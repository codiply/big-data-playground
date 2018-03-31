# Big Data Playground

This is a personal repo for experimenting with big data technologies including

- [Apache Cassandra](http://cassandra.apache.org/)
- [Apache Kafka](http://kafka.apache.org/)
- [Apache Spark](http://spark.apache.org/)

## Docker containers

### Prerequisites

You will need to install

- [Docker](https://www.docker.com/) and
- [Docker Compose](https://docs.docker.com/compose/) (if it is not part of the Docker installation on your platform).

### Run the containers

The following containers are defined in `docker-compose.yml`

- `cassandra-1`, `cassandra-2`, `cassandra-3`: Apache Cassandra cluster
- `kafka-1`, `kafka-2`, `kafka-3`: Apache Kafka cluster
- `probe`: Used for running applications from the command line from within the docker network
- `spark-master`, `spark-worker`'s: Apache Spark cluster
- `zookeeper-1`, `zookeeper-2`, `zookeeper-3`: Apache Zookeper cluster

To start all containers

    docker-compose up -d

To scale the number of spark workers (to 3 for example)

    docker-compose scale spark-worker=3

To stop all containers

    docker-compose stop

or completely remove them

    docker-compose down

## Addresses on the host

Whenever suitable I have bound ports to the host so that for example access to various UI's is possible.

- Kafka Manager (UI): [localhost:9000](http://localhost:9000)
- Spark Cluster (UI): [localhost:8080](http://localhost:8080)
- Probe (ssh): [localhost:52022](http://localhost:9000)

## Access Spark workers

There are links on the Spark Cluster UI that lead you to spark workers.

To access these from the host you will need to install [sshuttle](https://github.com/sshuttle/sshuttle) and run the `scripts/ssh/sshuttle-via-probe` script. The password is `root`.

## Run an application/main written in Scala

### Prerequisites

- Install [sbt](http://www.scala-sbt.org/) (Scala Built Tool).

### Run them on Probe container

- Build a fat jar `big-data-playground.jar` with `sbt assembly`. This is placed under `/target/big-data-playground/`. (Directory `/target/big-data-playground/ is mount at `/playground/` on the `probe` container.)
- Ssh to the probe container using the `scripts/ssh/ssh-probe` script. The password is `root`.
- Run a main with `java -cp /playground/ com.codiply.bgdp.SomeClassWithMain`
