package de.primeapi.primeplugins.spigotapi.sql;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Getter @AllArgsConstructor
public class DatabaseTask<T> {

    private CompletableFuture<T> future;

    public void submit(Consumer<T> consumer){
        future.thenAccept(consumer);
    }



    public T complete(){
        return future.join();
    }
}
