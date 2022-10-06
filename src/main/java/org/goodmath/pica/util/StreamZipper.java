package org.goodmath.pica.util;

import org.goodmath.pica.ast.Pair;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Given two streams <code>Stream<A> a<code> and <code>Stream<B> b</code>
 * returns a new stream <Code>Stream<Pair<A, B>></Code>.
 */
public class StreamZipper {
    public static<A,B> Stream<Pair<A, B>> zip (Stream<A> a, Stream<B> b) {
        Stream.Builder<Pair<A, B>> builder = Stream.builder();
        var aIter = a.iterator();
        var bIter = b.iterator();
        while (aIter.hasNext() && bIter.hasNext()) {
            var aVal = aIter.next();
            var bVal = bIter.next();
            builder.add(new Pair<>(aVal, bVal));
        }
        return builder.build();
    }
}
