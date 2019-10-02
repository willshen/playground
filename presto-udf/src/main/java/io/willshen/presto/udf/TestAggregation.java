package io.willshen.presto.udf;

import static com.facebook.presto.spi.type.BigintType.BIGINT;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openjdk.jol.info.ClassLayout;

import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.function.*;
import com.facebook.presto.spi.type.StandardTypes;

@AggregationFunction("test_aggregate")
@Description("given metric type, value, and count, merge them with other rows, grouping by their type")
public class TestAggregation {
    private static final int METRIC_SIZE = ClassLayout.parseClass(Metric.class).instanceSize();

    @InputFunction
    public static void input(
            @AggregationState TestAggregationAccumulatorState state,
            @SqlType(StandardTypes.BIGINT) long type,
            @SqlType(StandardTypes.BIGINT) long value,
            @SqlType(StandardTypes.BIGINT) long count) {
        Metric metric = new Metric(type, value, count);

        TestAggregationAccumulatorState right = () -> Arrays.asList(metric);

        combine(state, right);
    }

    @InputFunction
    public static void input(
            @AggregationState TestAggregationAccumulatorState state,
            @SqlType(StandardTypes.BIGINT) long type,
            @SqlType(StandardTypes.DOUBLE) double value,
            @SqlType(StandardTypes.DOUBLE) double count) {
        input(state, type, (long) value, (long) count);
    }

    @InputFunction
    public static void input(
            @AggregationState TestAggregationAccumulatorState state,
            @SqlType(StandardTypes.BIGINT) long type,
            @SqlType(StandardTypes.DOUBLE) double value,
            @SqlType(StandardTypes.BIGINT) long count) {
        input(state, type, (long) value, count);
    }

    @InputFunction
    public static void input(
            @AggregationState TestAggregationAccumulatorState state,
            @SqlType(StandardTypes.BIGINT) long type,
            @SqlType(StandardTypes.BIGINT) long value,
            @SqlType(StandardTypes.DOUBLE) double count) {
        input(state, type, value, (long) count);
    }

    @CombineFunction
    public static void combine(TestAggregationAccumulatorState left, TestAggregationAccumulatorState right) {
        List<Metric> leftMetrics = left.getMetrics();
        List<Metric> rightMetrics = right.getMetrics();

        for (int i = 0; i < rightMetrics.size(); ++i) {
            Metric rightMetric = rightMetrics.get(i);

            Optional<Metric> metric = leftMetrics.stream()
                    .filter(m -> m.getType() == rightMetric.getType())
                    .findAny();

            if (metric.isPresent()) {
                Metric leftMetric = metric.get();

                leftMetric.merge(rightMetric);
            } else {
                Metric leftMetric = new Metric(rightMetric.getType(), rightMetric.getValue(), rightMetric.getCount());

                leftMetrics.add(leftMetric);
                left.addMemoryUsage(METRIC_SIZE);
            }
        }
    }

    @OutputFunction("array(row(metric_type bigint,metric_value bigint,metric_count bigint))")
    public static void output(@AggregationState TestAggregationAccumulatorState state, BlockBuilder out) {
        BlockBuilder arrayEntry = out.beginBlockEntry();
        state.getMetrics().forEach(metric -> {
            BlockBuilder rowBuilder = arrayEntry.beginBlockEntry();
            BIGINT.writeLong(rowBuilder, metric.getType());
            BIGINT.writeLong(rowBuilder, metric.getValue());
            BIGINT.writeLong(rowBuilder, metric.getCount());
            arrayEntry.closeEntry();
        });
        out.closeEntry();
    }
}
