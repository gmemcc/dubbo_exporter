# dubbo_exporter
Exposing dubbo statistics  via HTTP for Prometheus consumption

## Requirements
- Running Kubernetes cluster with Helm/Tiller installed
- JDK 1.8+, Maven 3.5, Docker, and Helm client for building this project

## Build
Execute the following script to build:
    git clone https://github.com/gmemcc/dubbo_exporter.git
    cd dubbo_exporter
    mvn clean package
    
Packaged helm chart will be populated into target/charts directory, docker image with repository **docker.gmem.cc/prometheus/dubbo_exporter** will be created, you may modify configuration of dockerfile-maven-plugin in pom.xml to change this default repository.

