# Big Data Playground

This is a personal repo for experimenting with big data technologies including

- [Apache Cassandra](http://cassandra.apache.org/)
- [Apache Kafka](http://kafka.apache.org/)
- [Apache Spark](http://spark.apache.org/)
- [Apache Zeppelin](http://zeppelin.apache.org/)

## Docker containers

### Prerequisites

You will need to install

- [Docker](https://www.docker.com/) and
- [Docker Compose](https://docs.docker.com/compose/) (if it is not part of the Docker installation on your platform).

### Build the docker images

You can build all docker images in advance by running

    scripts/docker-compose/build-all

### Run the containers

The following containers are defined in groups in `docker/compose/docker-compose.{group}.yml`.

To start a group of containers run the corresponding script

    scripts/docker-compose/{group}-up

To remove the containers for a group run the corresponding script

    scripts/docker-compose/{group}-down

### Apache Cassandra cluster

- `cassandra-1`
- `cassandra-2`
- `cassandra-3`

### Apache Kafka cluster

- `kafka-1`
- `kafka-2`
- `kafka-3`
- `zookeeper-1`
- `zookeeper-2`
- `zookeeper-3`

### Probe

- `probe`:

Used for running applications from the command line from within the docker network.

### Apache Spark cluster

- `spark-master`
- `spark-worker` (scaled to 3 workers in `spark-up` script)

### Apache Zeppelin

- `zeppelin`

## Addresses on the host

Whenever suitable I have bound ports to the host so that for example access to various UI's is possible.

- Kafka Manager (UI): [localhost:9000](http://localhost:9000)
- Spark Cluster (UI): [localhost:8080](http://localhost:8080)
- Probe (ssh): [localhost:52022](http://localhost:9000)
- Zeppelin (UI): [localhost:9080](http://localhost:9080)

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
