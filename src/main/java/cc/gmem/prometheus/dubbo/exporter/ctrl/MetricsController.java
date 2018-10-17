package cc.gmem.prometheus.dubbo.exporter.ctrl;

import cc.gmem.prometheus.dubbo.exporter.service.MonitorServiceImpl;
import com.alibaba.dubbo.monitor.dubbo.Statistics;
import com.fizzed.rocker.RockerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@EnableAutoConfiguration
public class MetricsController {

    @Autowired
    private MonitorServiceImpl monitorService;

    @RequestMapping( "/metrics" )
    @ResponseBody
    public RockerModel metrics() {
        ConcurrentMap<Statistics, AtomicReference<long[]>> statistics = monitorService.current();
        return templates.Metrics.template( statistics );
    }
}
