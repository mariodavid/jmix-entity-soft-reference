package de.diedavids.jmix.softreference.cuba.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import de.diedavids.jmix.softreference.cuba.impl.Batches.Batch;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class BatchesTest {


    private List<Batch> batches;

    @Test
    void calculate_createsFourBatchesFor_365_Entries_andBatchSizeOf_100() {


        final List<Batch> batches = Batches.calculate(356, 100);

        assertThat(batches)
            .hasSize(4);
    }

    @Test
    void withBatches_createsFourBatchesFor_365_Entries_andBatchSizeOf_100() {

        AtomicInteger invocations = new AtomicInteger();
        Batches.doInBatches(
            356,
            100,
            (Batch batch) -> invocations.getAndIncrement()
        );

        assertThat(invocations.get())
            .isEqualTo(4);
    }

    @Test
    void calculate_createsFourBatches_withTheCorrectStartAndEnd_For_365_Entries_andBatchSizeOf_100() {

        // when:
        batches = Batches.calculate(356, 100);

        // then:
        assertThat(batchStart(1))
            .isEqualTo(0);

        assertThat(batchEnd(1))
            .isEqualTo(99);

        // and:
        assertThat(batchStart(2))
            .isEqualTo(100);

        assertThat(batchEnd(2))
            .isEqualTo(199);

        // and:
        assertThat(batchStart(3))
            .isEqualTo(200);

        assertThat(batchEnd(3))
            .isEqualTo(299);

        // and:
        assertThat(batchStart(4))
            .isEqualTo(300);

        assertThat(batchEnd(4))
            .isEqualTo(356);
    }

    private int batchEnd(int batch) {
        return getBatch(batch).getEnd();
    }

    private Batch getBatch(int batch) {
        return batches.get(batch - 1);
    }

    private int batchStart(int batch) {
        return getBatch(batch).getStart();
    }
}