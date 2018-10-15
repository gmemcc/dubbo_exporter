package cc.gmem.prometheus.dubbo.exporter.service;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.monitor.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Service( interfaceClass = MonitorService.class )
@Component
public class MonitorServiceImpl implements MonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger( MonitorServiceImpl.class );

    @Override
    public void collect( URL statistics ) {
        LOGGER.debug( "Original statistics collected:\n{}", statistics.toString() );
    }

    @Override
    public List<URL> lookup( URL query ) {
        throw new UnsupportedOperationException();
    }
}
