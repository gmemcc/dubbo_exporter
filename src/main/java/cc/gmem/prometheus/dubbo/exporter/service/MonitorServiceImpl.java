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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service( interfaceClass = MonitorService.class )
@Component
public class MonitorServiceImpl implements MonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger( MonitorServiceImpl.class );

    private static final String N_A = "N/A";

    @Value( "${dubbo.exporter.discard.empty.metrics}" )
    private boolean discardEmptyMetrics;

    @Value( "${dubbo.exporter.pre.aggregate.masking.level}" )
    private String preAggreateMaskingLevel;

    private final String MASKING_LV_NO = "no";

    private final String MASKING_LV_CS = "cs";

    private final String MASKING_LV_CSA = "csa";


    private static final int LENGTH = 20;

    private final ConcurrentMap<Statistics, AtomicReference<long[]>> statisticsMapConsumer = new ConcurrentHashMap<Statistics, AtomicReference<long[]>>();

    private final ConcurrentMap<Statistics, AtomicReference<long[]>> statisticsMapProvider = new ConcurrentHashMap<Statistics, AtomicReference<long[]>>();

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
        boolean sendFromProvider = url.getParameter( MonitorService.CONSUMER ) == null ? false : true;
        ConcurrentMap<Statistics, AtomicReference<long[]>> statisticsMap = sendFromProvider ? statisticsMapProvider : statisticsMapConsumer;
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

    public Map<Statistics, AtomicReference<long[]>> consumerStatistics() {
        return preAggregate( statisticsMapConsumer );
    }

    public Map<Statistics, AtomicReference<long[]>> providerStatistics() {
        return preAggregate( statisticsMapProvider );
    }

    private Map<Statistics, AtomicReference<long[]>> preAggregate( ConcurrentMap<Statistics, AtomicReference<long[]>> statistics ) {
        if ( noPreAggregate() ) return statistics;

        return statistics.entrySet().stream().collect( Collectors.groupingBy( statAndMetrics -> {
            return mask( statAndMetrics.getKey() );
        } ) ).entrySet().stream().collect(
            Collectors.toMap( Map.Entry::getKey, e -> e.getValue().stream().map( Map.Entry::getValue ).reduce( ( m1r, m2r ) -> {
                long[] m1 = m1r.get();
                long[] m2 = m2r.get();
                long[] m3 = new long[LENGTH];
                m3[0] = m1[0] + m2[0];
                m3[1] = m1[1] + m2[1];
                m3[2] = m1[2] + m2[2];
                m3[3] = m1[3] + m2[3];
                m3[4] = m1[4] + m2[4];
                m3[5] = m1[5] + m2[5];
                m3[6] = m1[6] > m2[6] ? m1[6] : m2[6];
                m3[7] = m1[7] > m2[6] ? m1[7] : m2[7];
                m3[8] = m1[8] > m2[6] ? m1[8] : m2[8];
                m3[9] = m1[9] > m2[6] ? m1[9] : m2[9];
                m3[10] = m1[10] + m2[10];
                return new AtomicReference<long[]>( m3 );
            } ).get() )
        );
    }

    private Statistics mask( Statistics statistics ) {
        Statistics maskedStatistics = new Statistics( statistics.getUrl() );
        if ( needMaskClientAndServer() ) {
            maskedStatistics.setClient( N_A );
            maskedStatistics.setServer( N_A );
        }
        if ( needMaskDubboApplication() ) {
            maskedStatistics.setApplication( N_A );
        }
        return maskedStatistics;
    }

    private boolean needMaskClientAndServer() {
        return !MASKING_LV_NO.equals( preAggreateMaskingLevel );
    }

    private boolean needMaskDubboApplication() {
        return MASKING_LV_CSA.equals( preAggreateMaskingLevel );
    }

    private boolean noPreAggregate() {
        return MASKING_LV_NO.equals( preAggreateMaskingLevel );
    }

}
