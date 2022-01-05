package de.primeapi.primeplugins.spigotapi.sql.clan;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
public class SQLPlayerAllocation {
    final int id;

    public static DatabaseTask<SQLPlayerAllocation> create(SQLPlayer player, SQLClan clan, Integer i) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Integer id = PrimeCore.getInstance().getDb().update("INSERT INTO prime_clan_players value (id,?,?,?)")
                    .parameters(player.retrieveUniqueId().complete().toString(), clan.getId(), i)
                    .returnGeneratedKeys()
                    .getAs(Integer.class)
                    .toBlocking().singleOrDefault(null);
            if (id != null) {
                return new SQLPlayerAllocation(id);
            }
            return null;
        }));
    }

    public static DatabaseTask<SQLPlayerAllocation> fromPlayer(SQLPlayer player) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Integer id = PrimeCore.getInstance().getDb().select("SELECT id FROM prime_clan_players WHERE uuid = ?")
                    .parameters(player.retrieveUniqueId().complete().toString())
                    .getAs(Integer.class)
                    .toBlocking().firstOrDefault(null);
            if (id != null) {
                return new SQLPlayerAllocation(id);
            }
            return null;
        }));
    }

    private <T> DatabaseTask<T> readDatabase(String column, Class<T> type) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> PrimeCore.getInstance().getDb().select(
                        "SELECT " + column + " FROM prime_clan_players WHERE id = ?"
                )
                .parameters(id).getAs(type).toBlocking().singleOrDefault(null)));
    }

    public DatabaseTask<UUID> getUUID() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> UUID.fromString(readDatabase("uuid", String.class).complete())));
    }

    public DatabaseTask<SQLClan> getClan() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> new SQLClan(readDatabase("clan", Integer.class).complete())));
    }

    public DatabaseTask<Integer> getRank() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> (readDatabase("rank", Integer.class).complete())));
    }

    public void delete() {
        PrimeCore.getInstance().getDb().update("DELETE FROM prime_clan_players WHERE id = ?")
                .parameters(id).execute();
    }

    public void updateRank(Integer i) {
        PrimeCore.getInstance().getDb().update("UPDATE prime_clan_players SET `rank` = ? WHERE id = ?")
                .parameters(i, id).execute();
    }
}
