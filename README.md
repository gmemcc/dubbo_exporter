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
    
Docker image with repository **docker.gmem.cc/prometheus/dubbo_exporter** will be generated, you may modify configuration of dockerfile-maven-plugin in pom.xml to change this default repository, and run
    
    mvn dockerfile:push
    
to push the image to your private registry.

Packaged helm chart will be populated into target/charts directory.

## Deploy
Execute the following script to deploy dubbo_exporter on your kubernetes cluster:

    mvn bash:run

Also, you can invoke helm cli manually:

    helm delete dubbo-exporter --purge
    helm install target/charts/dubbo_exporter.tgz --name=dubbo-exporter --set zookeeper.host=zk,zookeeper.port=2181

## Metrics
All metrics are exposed by endpoint **http://$DUBBO_EXPORTER_POD_IP:9100/metrics**: 

    # HELP dubbo_success_count Total successful invocation count
    # TYPE dubbo_success_count counter
    dubbo_success_count{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 27462
    # HELP dubbo_failure_count Total failed invocation count
    # TYPE dubbo_failure_count counter
    dubbo_failure_count{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 0
    # HELP dubbo_input_bytes Total input message size
    # TYPE dubbo_input_bytes counter
    dubbo_input_bytes{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 6590885
    # HELP dubbo_output_bytes Total output message size
    # TYPE dubbo_output_bytes counter
    dubbo_output_bytes{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 0
    # HELP dubbo_elapsed_ms Total elapsed milliseconds during invocation
    # TYPE dubbo_elapsed_ms counter
    dubbo_elapsed_ms{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 13363138
    # HELP dubbo_concurrent_requests Instant concurrent request
    # TYPE dubbo_concurrent_requests gauge
    dubbo_concurrent_requests{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 9
    # HELP dubbo_input_bytes_max Max input message size
    # TYPE dubbo_input_bytes_max gauge
    dubbo_input_bytes_max{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 243
    # HELP dubbo_output_bytes_max Max output message size
    # TYPE dubbo_output_bytes_max gauge
    dubbo_output_bytes_max{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 0
    # HELP dubbo_elapsed_ms_max Max elapsed milliseconds during invocation
    # TYPE dubbo_elapsed_ms_max gauge
    dubbo_elapsed_ms{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 1052
    # HELP dubbo_concurrent_requests_max Max instant concurrent request
    # TYPE dubbo_concurrent_requests_max gauge
    dubbo_concurrent_requests_max{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 10
    
    
    
    # HELP dubbo_success_count Total successful invocation count
    # TYPE dubbo_success_count counter
    dubbo_success_count{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 27373
    # HELP dubbo_failure_count Total failed invocation count
    # TYPE dubbo_failure_count counter
    dubbo_failure_count{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 77
    # HELP dubbo_input_bytes Total input message size
    # TYPE dubbo_input_bytes counter
    dubbo_input_bytes{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 0
    # HELP dubbo_output_bytes Total output message size
    # TYPE dubbo_output_bytes counter
    dubbo_output_bytes{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 958098
    # HELP dubbo_elapsed_ms Total elapsed milliseconds during invocation
    # TYPE dubbo_elapsed_ms counter
    dubbo_elapsed_ms{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 13563399
    # HELP dubbo_concurrent_requests Instant concurrent request
    # TYPE dubbo_concurrent_requests gauge
    dubbo_concurrent_requests{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 8
    # HELP dubbo_input_bytes_max Max input message size
    # TYPE dubbo_input_bytes_max gauge
    dubbo_input_bytes_max{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 0
    # HELP dubbo_output_bytes_max Max output message size
    # TYPE dubbo_output_bytes_max gauge
    dubbo_output_bytes_max{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 38
    # HELP dubbo_elapsed_ms_max Max elapsed milliseconds during invocation
    # TYPE dubbo_elapsed_ms_max gauge
    dubbo_elapsed_ms{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 1037
    # HELP dubbo_concurrent_requests_max Max instant concurrent request
    # TYPE dubbo_concurrent_requests_max gauge
    dubbo_concurrent_requests_max{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.121.166:20880",client="172.27.252.186"} 10
