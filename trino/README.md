# Trino on Multipass
## Multipass
Download and install from https://multipass.run

## Docker
### Create a Docker Environment
multipass launch docker

## Portainer
### Access UI
Port 9000 on the docker host

## Trino
### Create a single node container
multipass exec docker -- docker run -p 8080:8080 --name trino trinodb/trino

### Accessing Trino UI
Port 8080 on the docker host

### Querying Trino
multipass exec docker -- docker container exec -it trino trino

