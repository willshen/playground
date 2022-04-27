package io.willshen.presto.udf;

public class Metric {
    private final long type;
    private long value;
    private long count;

    public Metric(long type, long value, long count) {
        this.type = type;
        this.value = value;
        this.count = count;
    }

    public Metric merge(Metric that) {
        if (that == null) {
            return this;
        }

        if (this.type != that.type) {
            throw new IllegalArgumentException("Invalid types");
        }

        this.value += that.value;
        this.count += that.count;

        return this;
    }

    public long getType() {
        return type;
    }

    public long getValue() {
        return value;
    }

    public long getCount() {
        return count;
    }
}
