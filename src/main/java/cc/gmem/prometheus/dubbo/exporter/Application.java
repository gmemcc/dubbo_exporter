package cc.gmem.prometheus.dubbo.exporter;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class Application {

    public static void main( String[] args ) {
        SpringApplication.run( Application.class, args );
    }
}
