# Big Data Playground

This is a personal repo for experimenting with big data technologies including

- [Aerospike](https://www.aerospike.com/)
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

### List of containers

#### Aerospike

- `aerospike-1`
- `aeropsike-2`
- `aeropsike-2`

#### Apache Cassandra cluster

- `cassandra-1`
- `cassandra-2`
- `cassandra-3`

#### Graphite - Grafana

- `graphite`

#### Jupyter

- `jupyter`

#### Apache Kafka cluster

- `kafka-1`
- `kafka-2`
- `kafka-3`
- `zookeeper-1`
- `zookeeper-2`
- `zookeeper-3`

#### Probe

- `probe`

Used for running applications from the command line from within the docker network.

#### PostgreSQL

- `postgres`

#### Apache Spark cluster

- `spark-master`
- `spark-worker` (scaled to 3 workers in `spark-up` script)

### Superset

- `superset`

#### Apache Zeppelin

- `zeppelin`

## Addresses on the host

Whenever suitable I have bound ports to the host so that for example access to various UI's is possible.

- Grafana (UI): [localhost:9180](http://localhost:9180) (username: `admin`, password: `admin`)
- Graphite (web): [localhost:9181](http://localhost:9181)
- Graphite (data): [localhost:9103](http://localhost:9103)
- Jupyter (UI): [localhost:9999](http://localhost:9999)
- Kafka Manager (UI): [localhost:9000](http://localhost:9000)
- PostgreSQL: [localhost:6432](http://localhost:6432)
- Probe (ssh): [localhost:52022](http://localhost:52022)
- Spark Cluster (UI): [localhost:8080](http://localhost:8080)
- StatsD (UDP): [localhost:9125](http://localhost:9125)
- StatsD (admin): [localhost:9126](http://localhost:9126)
- Superset: [localhost:8188](http://localhost:8188)
- Zeppelin (UI): [localhost:9080](http://localhost:9080)

## Access Spark workers

There are links on the Spark Cluster UI that lead you to spark workers.

To access these from the host you will need to install [sshuttle](https://github.com/sshuttle/sshuttle) and run the `scripts/ssh/sshuttle-via-probe` script. The password is `root`.

## Using the probe

- Start the probe container
- SSH to it using the `scripts/ssh/ssh-probe` script. The password is `root`.
- Use command line tools and access other containers using their hostname

## Run an application/main written in Scala

### Prerequisites

- Install [sbt](http://www.scala-sbt.org/) (Scala Built Tool).

### Run them on Probe container

- Build a fat jar `big-data-playground.jar` with `sbt assembly`. This is placed under `/target/big-data-playground/`. (Directory `/target/big-data-playground/ is mount at `/playground/` on the `probe` container.)
- SSH to the probe container (see above)
- Run a main with `java -cp /playground/big-data-playground.jar com.codiply.bdpg.SomeClassWithMain`

## Inspect a Kafka topic

SSH to the probe container and then inspect a topic with `kafkacat`

    kafkacat -C -b kafka-1 -t topic-name -f 'Topic %t[%p], offset: %o, key: %k, payload: %S bytes: %s\n'

replacing `topic-name` with the actual topic name.
