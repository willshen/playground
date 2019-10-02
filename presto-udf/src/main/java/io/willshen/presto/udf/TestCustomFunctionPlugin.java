package io.willshen.presto.udf;

import java.util.Set;

import com.facebook.presto.spi.Plugin;
import com.google.common.collect.ImmutableSet;

public class TestCustomFunctionPlugin implements Plugin {
    @Override
    public Set<Class<?>> getFunctions(){
        return ImmutableSet.<Class<?>>builder().add(TestAggregation.class).build();
    }
}
