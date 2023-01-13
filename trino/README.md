# Trino on Multipass
## Multipass
Download and install from https://multipass.run

## Docker
### Create a Docker Environment
```
multipass launch docker
```

## Portainer
### Access UI
Port 9000 on the docker host

## Trino
### Create a single node test cluster
#### Run container
```
multipass exec docker -- docker run -p 8080:8080 --name trino trinodb/trino
```

#### Accessing Trino UI
Port 8080 on the docker host

#### Querying Trino
```
multipass exec docker -- docker container exec -it trino trino
```

### Create a single node Trino cluster with CDH 5.12 Hive (2 nodes)
#### Download Docker files
```
$ multipass exec docker -- sudo apt install unzip
$ multipass exec docker -- curl https://codeload.github.com/willshen/playground/zip/refs/heads/master -o playground-master.zip
$ multipass exec docker -- unzip playground-master.zip
```

#### Start up cluster
```
$ multipass shell docker
Welcome to Ubuntu 21.10 (GNU/Linux 5.13.0-35-generic x86_64)

 * Documentation:  https://help.ubuntu.com
 * Management:     https://landscape.canonical.com
 * Support:        https://ubuntu.com/advantage

  System information as of Wed Apr 27 12:20:50 CDT 2022

  System load:                      0.0
  Usage of /:                       27.5% of 39.76GB
  Memory usage:                     8%
  Swap usage:                       0%
  Processes:                        125
  Users logged in:                  0
  IPv4 address for br-788ebf0da290: 172.18.0.1
  IPv4 address for docker0:         172.17.0.1
  IPv4 address for enp0s2:          192.168.64.3
  IPv6 address for enp0s2:          fdea:e64a:59a7:bef:a833:ffff:fe6d:fc7a


0 updates can be applied immediately.


Last login: Wed Apr 27 11:41:58 2022 from 192.168.64.1
ubuntu@docker:~$ cd playground-master/trino/trino-cdh-5.12/
ubuntu@docker:~/playground-master/trino/trino-cdh5.12-hive$ docker-compose up -d
Creating network "trino-cdh-512_trino-network" with driver "bridge"
Creating trino-cdh-512_trino-coordinator_1 ... done
Creating trino-cdh-512_hadoop-node_1       ... done
ubuntu@docker:~/playground-master/trino/trino-cdh5.12-hive$ exit
logout
```

#### Querying Trino
```
$ multipass exec docker -- docker container exec -it trino-cdh512_trino-coordinator_1 trino
trino> 
```

#### Accessing HDFS
NameNode UI: Port 50070 on docker host
```
$ multipass exec docker -- docker container exec -it trino-cdh512_hadoop-node_1 hadoop fs -ls /
Found 5 items
drwxrwxrwx   - hdfs  supergroup          0 2021-01-18 20:43 /benchmarks
drwxr-xr-x   - hbase supergroup          0 2021-01-18 20:43 /hbase
drwxrwxrwt   - hdfs  supergroup          0 2022-04-27 23:07 /tmp
drwxr-xr-x   - hdfs  supergroup          0 2021-01-18 20:44 /user
drwxr-xr-x   - hdfs  supergroup          0 2021-01-18 20:44 /var
```

#### Querying Hive
```
multipass exec docker -- docker container exec -it trino-cdh512_hadoop-node_1 hive
```
