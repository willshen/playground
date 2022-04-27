package io.willshen.presto.udf;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jol.info.ClassLayout;

import com.facebook.presto.array.ObjectBigArray;
import com.facebook.presto.spi.function.AccumulatorState;

public class TestAggregationMetricAccumulatorState implements AccumulatorState {
    private static final long ARRAY_LIST_SIZE = ClassLayout.parseClass(ObjectBigArray.class).instanceSize();

    private ObjectBigArray<Metric> metrics = new ObjectBigArray<>();
    private int memory;
    @Override
    public long getEstimatedSize() {
        return memory + ARRAY_LIST_SIZE;
    }

    public void addMemoryUsage(int memory) {
        this.memory += memory;
    }

    public void setMetric(Metric m ) {
        metrics.set(m.getType(), m);
    }

    public Metric getMetric(long type) {
        return metrics.get(type);
    }
}
