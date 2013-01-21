/**
 * Copyright (c) 2012-2012 Malhar, Inc. All rights reserved.
 */
package com.malhartech.lib.math;

import com.malhartech.engine.TestCountAndLastTupleSink;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Performance tests for {@link com.malhartech.lib.math.QuotientMap}<p>
 *
 */
public class QuotientBenchmark
{
  private static Logger LOG = LoggerFactory.getLogger(Quotient.class);

  /**
   * Test node logic emits correct results
   */
  @Test
  @SuppressWarnings("SleepWhileInLoop")
  @Category(com.malhartech.annotation.PerformanceTestCategory.class)
  public void testNodeProcessing() throws Exception
  {
    testNodeProcessingSchema(new QuotientMap<String, Integer>());
    testNodeProcessingSchema(new QuotientMap<String, Double>());
  }

  public void testNodeProcessingSchema(QuotientMap oper) throws Exception
  {

    TestCountAndLastTupleSink quotientSink = new TestCountAndLastTupleSink();

    oper.quotient.setSink(quotientSink);
    oper.setMult_by(2);

    oper.beginWindow(0); //
    HashMap<String, Number> input = new HashMap<String, Number>();
    int numtuples = 100000000;
    for (int i = 0; i < numtuples; i++) {
      input.clear();
      input.put("a", 2);
      input.put("b", 20);
      input.put("c", 1000);
      oper.numerator.process(input);
      input.clear();
      input.put("a", 2);
      input.put("b", 40);
      input.put("c", 500);
      oper.denominator.process(input);
    }

    oper.endWindow();
    // One for each key
    LOG.debug(String.format("Processed %d tuples", numtuples * 6));

    HashMap<String, Number> output = (HashMap<String, Number>)quotientSink.tuple;
    for (Map.Entry<String, Number> e: output.entrySet()) {
      LOG.debug(String.format("Key, value is %s,%f", e.getKey(), e.getValue().doubleValue()));
    }
  }
}
