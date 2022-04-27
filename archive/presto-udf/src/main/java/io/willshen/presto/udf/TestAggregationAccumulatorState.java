package io.willshen.presto.udf;

import java.util.List;

import com.facebook.presto.spi.function.AccumulatorState;
import com.facebook.presto.spi.function.AccumulatorStateMetadata;

@AccumulatorStateMetadata(
        stateSerializerClass =  TestAggregationAccumulatorStateSerializer.class,
        stateFactoryClass = TestAggregationAccumulatorStateFactory.class)
@FunctionalInterface
public interface TestAggregationAccumulatorState extends AccumulatorState {
    default void addMemoryUsage(int memory) {}

    @Override
    default long getEstimatedSize() { return 0; }

    List<Metric> getMetrics();
}
