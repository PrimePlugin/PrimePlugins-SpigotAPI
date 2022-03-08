package de.primeapi.primeplugins.spigotapi.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@AllArgsConstructor
@Deprecated
public class DatabaseTask<T> {

    private final CompletableFuture<T> future;

    /**
     * Runs the consumer async as soon as the Database returns a value
     *
     * @param consumer The consumer which should be run
     */
    public void submit(Consumer<T> consumer) {
        future.handle((unused, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return null;
            }
            return unused;
        }).thenAcceptAsync(consumer);
    }


    /**
     * Used to handle the SQL request synchronised<br>
     * Use this only if needed! This slows down the hole runtime!
     *
     * @return The Object which is returned by the Database
     */
    public T complete() {
        return future.join();
    }


    public <U> DatabaseTask<U> map(Function<? super T, ? extends U> fn) {
        return new DatabaseTask<>(future.thenApply(fn));
    }

    @Override
    public String toString() {
        return complete().toString();
    }
}
