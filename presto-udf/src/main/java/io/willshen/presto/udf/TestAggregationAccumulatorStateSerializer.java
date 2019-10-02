package io.willshen.presto.udf;

import static com.facebook.presto.spi.type.VarbinaryType.VARBINARY;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jol.info.ClassLayout;

import com.facebook.presto.spi.block.Block;
import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.function.AccumulatorStateSerializer;
import com.facebook.presto.spi.type.Type;
import io.airlift.slice.SliceInput;
import io.airlift.slice.SliceOutput;
import io.airlift.slice.Slices;

public class TestAggregationAccumulatorStateSerializer implements AccumulatorStateSerializer<TestAggregationAccumulatorState> {
    private static final int LONG_SIZE = ClassLayout.parseClass(Long.class).instanceSize();

    @Override
    public Type getSerializedType()
    {
        return VARBINARY;
    }

    @Override
    public void serialize(TestAggregationAccumulatorState state, BlockBuilder out)
    {
        List<Metric> metrics = state.getMetrics();
        int n = metrics.size();
        int size = Integer.BYTES + (3 * n * Long.BYTES);

        SliceOutput sliceOut = Slices.allocate(size).getOutput();

        // number of entries followed by triples of (type, count, value)
        sliceOut.appendInt(n);
        metrics.forEach(metric -> {
            sliceOut.appendLong(metric.getType());
            sliceOut.appendLong(metric.getCount());
            sliceOut.appendLong(metric.getValue());
        });

        VARBINARY.writeSlice(out, sliceOut.slice());
    }

    @Override
    public void deserialize(Block block, int index, TestAggregationAccumulatorState state)
    {
        SliceInput input = VARBINARY.getSlice(block, index).getInput();

        if (!input.isReadable()) {
            return;
        }

        int n = input.readInt();
        List<Metric> metrics = new ArrayList<>(n);

        for (int i = 0; i < n; ++i) {
            long type = Long.valueOf(input.readLong());
            long count = Long.valueOf(input.readLong());
            long revenue = Long.valueOf(input.readLong());

            metrics.add(new Metric(type, revenue, count));
        }

        state.getMetrics().addAll(metrics);
        state.addMemoryUsage(3 * n * LONG_SIZE);
    }
}
