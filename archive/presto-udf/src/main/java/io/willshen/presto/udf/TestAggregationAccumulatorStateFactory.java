package io.willshen.presto.udf;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jol.info.ClassLayout;

import com.facebook.presto.array.ObjectBigArray;
import com.facebook.presto.spi.function.AccumulatorStateFactory;
import com.facebook.presto.spi.function.GroupedAccumulatorState;

public class TestAggregationAccumulatorStateFactory implements AccumulatorStateFactory<TestAggregationAccumulatorState> {

    private static final long ARRAY_LIST_SIZE = ClassLayout.parseClass(ArrayList.class).instanceSize();

    public TestAggregationAccumulatorState createSingleState() {
        return new SingleTestAggregationAccumulatorState();
    }

    public Class<? extends TestAggregationAccumulatorState> getSingleStateClass() {
        return SingleTestAggregationAccumulatorState.class;
    }

    public TestAggregationAccumulatorState createGroupedState() {
        return new GroupedTestAggregationAccumulatorState();
    }

    public Class<? extends TestAggregationAccumulatorState> getGroupedStateClass() {
        return GroupedTestAggregationAccumulatorState.class;
    }

    public static class SingleTestAggregationAccumulatorState implements TestAggregationAccumulatorState {
        private List<Metric> metrics = new ArrayList<>();
        private int memory;
        @Override
        public long getEstimatedSize() {
            return memory + ARRAY_LIST_SIZE;
        }

        @Override
        public void addMemoryUsage(int memory) {
            this.memory += memory;
        }

        @Override
        public List<Metric> getMetrics() {
            return metrics;
        }
    }

    public static class GroupedTestAggregationAccumulatorState implements GroupedAccumulatorState, TestAggregationAccumulatorState {
        private final ObjectBigArray<List<Metric>> metrics = new ObjectBigArray<>();
        private long groupId;
        private long memory;

        @Override
        public long getEstimatedSize() {
            return memory + metrics.sizeOf();
        }

        @Override
        public void addMemoryUsage(int memory) {
            this.memory += memory;
        }

        @Override
        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }

        @Override
        public void ensureCapacity(long size) {
            metrics.ensureCapacity(size);
        }

        @Override
        public List<Metric> getMetrics() {
            if (metrics.get(groupId) == null) {
                metrics.set(groupId, new ArrayList<>());
                memory += ARRAY_LIST_SIZE;
            }
            return metrics.get(groupId);
        }
    }
}
