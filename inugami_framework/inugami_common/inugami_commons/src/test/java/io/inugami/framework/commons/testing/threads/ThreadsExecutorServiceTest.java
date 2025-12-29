package io.inugami.framework.commons.testing.threads;

import io.inugami.framework.commons.threads.ThreadsExecutorService;
import io.inugami.framework.interfaces.exceptions.TechnicalException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
@SuppressWarnings({"java:S2925"})
class ThreadsExecutorServiceTest {
    private              ThreadsExecutorService service;
    private static final int                    MAX_THREADS = 4;

    @BeforeEach
    void setUp() {
        service = new ThreadsExecutorService("TestExecutor", MAX_THREADS, true, 1000L);
    }

    @AfterEach
    void tearDown() {
        service.shutdown();
    }

    @Test
    void should_execute_tasks_and_return_futures() throws Exception {
        // GIVEN
        Callable<String> task1 = () -> "Result 1";
        Callable<String> task2 = () -> "Result 2";

        // WHEN
        List<CompletableFuture<String>> futures = service.run(Arrays.asList(task1, task2));

        // THEN
        assertThat(futures).hasSize(2);
        assertThat(futures.get(0).get()).isEqualTo("Result 1");
        assertThat(futures.get(1).get()).isEqualTo("Result 2");
    }

    @Test
    void should_run_and_grab_results() throws TechnicalException {
        // GIVEN
        List<Callable<Integer>> tasks = Arrays.asList(() -> 1, () -> 2, () -> 3);

        // WHEN
        List<Integer> results = service.runAndGrab(tasks);

        // THEN
        assertThat(results).containsExactlyInAnyOrder(1, 2, 3);
    }


    @Test
    void should_handle_errors_with_onError_callback() throws Exception {
        // GIVEN
        AtomicBoolean    errorTriggered = new AtomicBoolean(false);
        Callable<String> failingTask    = () -> {
            throw new RuntimeException("Boom");
        };

        // WHEN
        CompletableFuture<String> future = service.buildFuture(failingTask, null, (ex, t) -> errorTriggered.set(true));

        Thread.sleep(200);

        // THEN
        assertThat(errorTriggered.get()).isTrue();
    }

    @Test
    void should_handle_timeout() {
        ThreadsExecutorService quickService = new ThreadsExecutorService("Quick", 1, true, 50L);
        Callable<String> longTask = () -> {
            Thread.sleep(500);
            return "too late";
        };

        // WHEN
        List<CompletableFuture<String>> futures = quickService.run(Arrays.asList(longTask));

        // THEN
        assertThatCode(() -> {
            String res = futures.get(0).get();
            assertThat(res).isNull();
        }).doesNotThrowAnyException();

        quickService.shutdown();
    }

    @Test
    void should_wait_for_futures_manually() throws TechnicalException {
        // GIVEN
        CompletableFuture<String> f1 = CompletableFuture.completedFuture("ok");
        CompletableFuture<String> f2 = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            f2.complete("delayed");
        });

        // WHEN & THEN
        assertThatCode(() -> service.waitting(Arrays.asList(f1, f2), 500L))
                .doesNotThrowAnyException();
    }

    @Test
    void should_submit_with_listeners() throws Exception {
        // GIVEN
        AtomicBoolean started  = new AtomicBoolean(false);
        AtomicBoolean finished = new AtomicBoolean(false);

        // WHEN
        service.submit("taskWithListeners",
                       () -> "ok",
                       (time, delay, name, res, err) -> finished.set(true),
                       (time, name) -> started.set(true)
                      ).get();

        // THEN
        assertThat(started.get()).isTrue();
        assertThat(finished.get()).isTrue();
    }

    @Test
    void should_handle_shutdown_gracefully() {
        ThreadsExecutorService tempService = new ThreadsExecutorService("Temp", 2);
        assertThat(tempService.getExecutor().isShutdown()).isFalse();

        tempService.shutdown();
        assertThat(tempService.getExecutor().isShutdown()).isTrue();
    }
}