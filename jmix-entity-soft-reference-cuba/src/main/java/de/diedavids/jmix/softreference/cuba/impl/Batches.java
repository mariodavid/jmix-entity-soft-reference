package de.diedavids.jmix.softreference.cuba.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Batches {

    private Batches() {}

    public static void doInBatches(int total, int batchSize, Consumer<Batch> sliceHandler) {
        calculate(total, batchSize)
            .forEach(sliceHandler);
    }

    public static List<Batch> calculate(int total, int batchSize) {

        double batchCount = Math.ceil((double) total / batchSize);

        final ArrayList<Batch> batches = new ArrayList<>();

        for (int n = 0; n < batchCount; n++) {

            final int start = n * batchSize;
            final int end = Math.min(((n + 1) * batchSize) - 1, total);

            batches.add(new Batch(start, end));
        }

        return batches;
    }

    public static class Batch {
        private final int start;
        private final int end;

        private Batch(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getEnd() {
            return end;
        }

        public int getStart() {
            return start;
        }
    }
}
