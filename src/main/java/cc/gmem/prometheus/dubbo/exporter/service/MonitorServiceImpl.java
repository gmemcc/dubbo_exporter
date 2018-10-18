package cc.gmem.prometheus.dubbo.exporter.service;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.monitor.MonitorService;
import com.alibaba.dubbo.monitor.dubbo.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

@Service( interfaceClass = MonitorService.class )
@Component
public class MonitorServiceImpl implements MonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger( MonitorServiceImpl.class );

    // If true, the first metric put into statisticsMap must have non-zero request(success + failure) count
    @Value( "dubbo.exporter.discard.empty.metrics" )
    private boolean discardEmptyMetrics;

    private static final int LENGTH = 11;

    private final ConcurrentMap<Statistics, AtomicReference<long[]>> statisticsMap = new ConcurrentHashMap<Statistics, AtomicReference<long[]>>();

    @Override
    public void collect( URL url ) {
        LOGGER.debug( "Original statistics collected:\n{}", url.toString() );
        int success = url.getParameter( MonitorService.SUCCESS, 0 );
        int failure = url.getParameter( MonitorService.FAILURE, 0 );
        int total = success + failure;
        int input = url.getParameter( MonitorService.INPUT, 0 );
        int output = url.getParameter( MonitorService.OUTPUT, 0 );
        int elapsed = url.getParameter( MonitorService.ELAPSED, 0 );
        int concurrent = url.getParameter( MonitorService.CONCURRENT, 0 );
        int maxInput = url.getParameter( MonitorService.MAX_INPUT, 0 );
        int maxOutput = url.getParameter( MonitorService.MAX_OUTPUT, 0 );
        int maxElapsed = url.getParameter( MonitorService.MAX_ELAPSED, 0 );
        int maxConcurrent = url.getParameter( MonitorService.MAX_CONCURRENT, 0 );
        Statistics statistics = new Statistics( url );
        AtomicReference<long[]> reference = statisticsMap.get( statistics );
        if ( reference == null ) {
            if ( discardEmptyMetrics && total == 0 ) {
                return;
            }
            statisticsMap.putIfAbsent( statistics, new AtomicReference<long[]>() );
            reference = statisticsMap.get( statistics );
        }
        long[] current;
        long[] update = new long[LENGTH];
        do {
            current = reference.get();
            if ( current == null ) {
                update[0] = success;
                update[1] = failure;
                update[2] = input;
                update[3] = output;
                update[4] = elapsed;
                update[5] = concurrent;
                update[6] = maxInput;
                update[7] = maxOutput;
                update[8] = maxElapsed;
                update[9] = maxConcurrent;
                update[10] = total;
            } else {
                update[0] = current[0] + success;
                update[1] = current[1] + failure;
                update[2] = current[2] + input;
                update[3] = current[3] + output;
                update[4] = current[4] + elapsed;
                update[5] = concurrent;
                update[6] = current[6] > maxInput ? current[6] : maxInput;
                update[7] = current[7] > maxOutput ? current[7] : maxOutput;
                update[8] = current[8] > maxElapsed ? current[8] : maxElapsed;
                update[9] = current[9] > maxConcurrent ? current[9] : maxConcurrent;
                update[10] = current[10] + total;
            }
        } while ( !reference.compareAndSet( current, update ) );
    }

    @Override
    public List<URL> lookup( URL query ) {
        throw new UnsupportedOperationException();
    }

    public ConcurrentMap<Statistics, AtomicReference<long[]>> current() {
        return statisticsMap;
    }
}
