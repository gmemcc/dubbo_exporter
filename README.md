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

    # HELP dubbo_provider_success_count Total successful invocation count, provider view
    # TYPE dubbo_provider_success_count counter
    dubbo_provider_success_count{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 20673
    # HELP dubbo_provider_failure_count Total failed invocation count, provider view
    # TYPE dubbo_provider_failure_count counter
    dubbo_provider_failure_count{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 0
    # HELP dubbo_provider_input_bytes Total input message size, provider view
    # TYPE dubbo_provider_input_bytes counter
    dubbo_provider_input_bytes{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 4962095
    # HELP dubbo_provider_output_bytes Total output message size, provider view
    # TYPE dubbo_provider_output_bytes counter
    dubbo_provider_output_bytes{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 0
    # HELP dubbo_provider_elapsed_ms Total elapsed milliseconds during invocation, provider view
    # TYPE dubbo_provider_elapsed_ms counter
    dubbo_provider_elapsed_ms{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 8333309
    # HELP dubbo_provider_concurrent_requests Instant concurrent request, provider view
    # TYPE dubbo_provider_concurrent_requests gauge
    dubbo_provider_concurrent_requests{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 9
    # HELP dubbo_provider_input_bytes_max Max input message size, provider view
    # TYPE dubbo_provider_input_bytes_max gauge
    dubbo_provider_input_bytes_max{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 243
    # HELP dubbo_provider_output_bytes_max Max output message size, provider view
    # TYPE dubbo_provider_output_bytes_max gauge
    dubbo_provider_output_bytes_max{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 0
    # HELP dubbo_provider_elapsed_ms_max Max elapsed milliseconds during invocation, provider view
    # TYPE dubbo_provider_elapsed_ms_max gauge
    dubbo_provider_elapsed_ms{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 819
    # HELP dubbo_provider_concurrent_requests_max Max instant concurrent request, provider view
    # TYPE dubbo_provider_concurrent_requests_max gauge
    dubbo_provider_concurrent_requests_max{dubbo_application="dubbo-greeting-service",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 10
    
    
    
    
    # HELP dubbo_consumer_success_count Total successful invocation count, consumer view
    # TYPE dubbo_consumer_success_count counter
    dubbo_consumer_success_count{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 20670
    # HELP dubbo_consumer_failure_count Total failed invocation count, consumer view
    # TYPE dubbo_consumer_failure_count counter
    dubbo_consumer_failure_count{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 0
    # HELP dubbo_consumer_input_bytes Total input message size, consumer view
    # TYPE dubbo_consumer_input_bytes counter
    dubbo_consumer_input_bytes{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 0
    # HELP dubbo_consumer_output_bytes Total output message size, consumer view
    # TYPE dubbo_consumer_output_bytes counter
    dubbo_consumer_output_bytes{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 724023
    # HELP dubbo_consumer_elapsed_ms Total elapsed milliseconds during invocation, consumer view
    # TYPE dubbo_consumer_elapsed_ms counter
    dubbo_consumer_elapsed_ms{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 8506017
    # HELP dubbo_consumer_concurrent_requests Instant concurrent request, consumer view
    # TYPE dubbo_consumer_concurrent_requests gauge
    dubbo_consumer_concurrent_requests{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 9
    # HELP dubbo_consumer_input_bytes_max Max input message size, consumer view
    # TYPE dubbo_consumer_input_bytes_max gauge
    dubbo_consumer_input_bytes_max{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 0
    # HELP dubbo_consumer_output_bytes_max Max output message size, consumer view
    # TYPE dubbo_consumer_output_bytes_max gauge
    dubbo_consumer_output_bytes_max{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 38
    # HELP dubbo_consumer_elapsed_ms_max Max elapsed milliseconds during invocation, consumer view
    # TYPE dubbo_consumer_elapsed_ms_max gauge
    dubbo_consumer_elapsed_ms{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 855
    # HELP dubbo_consumer_concurrent_requests_max Max instant concurrent request, consumer view
    # TYPE dubbo_consumer_concurrent_requests_max gauge
    dubbo_consumer_concurrent_requests_max{dubbo_application="dubbo-consumer",service="cc.gmem.dubbo.greeting.service.GreetingService",method="sayHelloTo",server="172.27.155.32:20880",client="172.27.252.188"} 10
