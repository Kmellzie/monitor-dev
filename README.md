# Monitor-Dev

In this Scala project I'm using a library named ```Kamon``` to get system metrics when my application is running.  These metrics are sent and stored in ```InfluxDB``` and visualized using ```Grafana```.

InfluxDB and Grafana are run in a ```Docker``` container while the main application runs in local mode (using IntelliJ).  The final dashboard looks like this:

![alt text](https://raw.githubusercontent.com/Kmellzie/monitor-dev/master/project/resources/KamonMetrics-Memory.png)

## HowTo

### Start InfluxDB and Grafana

* Create a folder named ```monitor-dev``` and put the file ```docker-compose.yml``` in it

* Open the console there and start the docker containers with the command ```docker-compose up```

* You can stop the containers with ```docker-compose stop```

### Create the Database in InfluxDB

* Access to the InfluxDB container and create a database named ```mydb```.  To do so, you must:
```
// Get access to the container
docker exec -it monitordev_influxdb_1 /bin/bash

// Get access the InfluxDB's console with
influx

// Create the database
create database mydb

// Exit InfluxDB and container
exit
CTRL + P
CTRL + Q
```

### Connect Grafana to InfluxDB

* Login to Grafana's UI
```
http://localhost:3000/login
user: admin
password: admin
```

* Create a new Datasource

![alt text](https://raw.githubusercontent.com/Kmellzie/monitor-dev/master/project/resources/KamonMetrics-Datasource.png)

* Connect it to the InfluxDB container using port 8086

![alt text](https://raw.githubusercontent.com/Kmellzie/monitor-dev/master/project/resources/KamonMetrics-DatasourceInflux.png)


## Application

### Main

It's built in Scala 2.11.8. Basically it executes until you press ENTER in the console and while active it sends system metrics to InfluxDB to a data base called "mydb"

```scala
object MainApp {
  def main(args: Array[String]) {
    Kamon.start()
    StdIn.readLine()
    Kamon.shutdown()
  }
}
```

### Kamon

It's properties are configured in the file ```application.conf```
```
kamon {
  log-reporter {
    requires-aspectj = no
    auto-start = yes
  }

  system-metrics {
    requires-aspectj = no
    auto-start = yes
  }

  influxdb {
    hostname = "localhost"
    port = "8086"
    authentication.user = "admin"
    authentication.password = "admin"
    database = "mydb"

    max-packet-size = 1024
    application-name = "kamon"
  }
}
```

## Docker Compose

I'm using docker-compose to start a Grafana image and an InfluxDB image. Here's the content of the file ```docker-compose.yml```.

```
version: '2'

services:

  grafana:
    image: grafana/grafana
    ports:
      - 3000:3000
    networks:
      mynet:
        ipv4_address: 172.25.0.101

  influxdb:
    image: influxdb
    ports:
      - 8086:8086
    networks:
      mynet:
        ipv4_address: 172.25.0.102

networks:
  mynet:
    driver: bridge
    ipam:
      config:
      - subnet: 172.25.0.0/24
```


## References:

* Kamon System Metrics Module: http://kamon.io/documentation/kamon-system-metrics/0.6.6/overview/

* Grafana additional plugins: https://grafana.com/plugins