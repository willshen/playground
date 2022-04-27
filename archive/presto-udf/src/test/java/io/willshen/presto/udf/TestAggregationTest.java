package io.willshen.presto.udf;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.facebook.presto.tests.DistributedQueryRunner;
import com.facebook.presto.tests.H2QueryRunner;
import com.facebook.presto.tests.QueryAssertions;
import com.facebook.presto.tests.tpch.TpchQueryRunnerBuilder;
import io.willshen.presto.udf.TestCustomFunctionPlugin;

public class TestAggregationTest {
    private DistributedQueryRunner queryRunner;
    private H2QueryRunner h2QueryRunner;

    @BeforeClass
    public void setUp() throws Exception {
        h2QueryRunner = new H2QueryRunner();
        queryRunner = TpchQueryRunnerBuilder.builder().setNodeCount(0).build();
        queryRunner.installPlugin(new TestCustomFunctionPlugin());
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        queryRunner.close();
        queryRunner = null;
    }

    @Test
    public void testAggregation() {
        QueryAssertions.assertQuery(queryRunner, queryRunner.getDefaultSession(),
                "SELECT " +
                        "orderdate, " +
                        "TEST_AGGREGATE(custkey, CAST(totalprice AS BIGINT), CAST(1 AS BIGINT))[1].metric_value, " +
                        "TEST_AGGREGATE(custkey, CAST(totalprice AS BIGINT), CAST(1 AS BIGINT))[1].metric_count " +
                        "FROM orders " +
                        "WHERE custkey = 643 " +
                        "GROUP BY orderdate",
                h2QueryRunner,
                "SELECT " +
                        "orderdate, " +
                        "SUM(CAST(totalprice AS BIGINT)), " +
                        "COUNT(1) " +
                        "FROM orders " +
                        "WHERE custkey = 643 " +
                        "GROUP BY orderdate",
                false, false);
    }
}
