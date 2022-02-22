package de.primeapi.primeplugins.spigotapi.sql.clan;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class SQLClanInvitation {
    final int id;

    public static DatabaseTask<SQLClanInvitation> create(SQLPlayer player, SQLClan clan) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Integer id = PrimeCore.getInstance().getDb().update("INSERT INTO prime_clan_requests value (id,?,?)")
                    .parameters(player.retrieveUniqueId().complete().toString(), clan.getId())
                    .returnGeneratedKeys()
                    .getAs(Integer.class)
                    .toBlocking().singleOrDefault(null);
            if (id != null) {
                return new SQLClanInvitation(id);
            }
            return null;
        }));
    }

    public static DatabaseTask<SQLClanInvitation> fromPlayer(SQLPlayer player, SQLClan clan) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Integer id = PrimeCore.getInstance().getDb().select(
                    "SELECT id FROM prime_clan_requests WHERE uuid = ? AND clan = ?"
            ).parameters(player.retrieveUniqueId().complete().toString(), clan.getId())
                    .getAs(Integer.class)
                    .toBlocking().firstOrDefault(null);
            if (id != null) {
                return new SQLClanInvitation(id);
            }
            return null;
        }));
    }

    public static DatabaseTask<List<SQLClanInvitation>> fromPlayer(SQLPlayer player) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            List<Integer> idList = PrimeCore.getInstance().getDb().select(
                    "SELECT id FROM prime_clan_requests WHERE uuid=?"
            ).parameters(player.retrieveUniqueId().complete().toString())
                    .getAs(Integer.class)
                    .toList().toBlocking().singleOrDefault(new ArrayList<>());
            return idList.stream().map(SQLClanInvitation::new).collect(Collectors.toList());
        }));
    }

    private <T> DatabaseTask<T> readDatabase(String column, Class<T> type) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> PrimeCore.getInstance().getDb().select(
                "SELECT " + column + " FROM prime_clan_requests WHERE id = ?"
        )
                .parameters(id).getAs(type).toBlocking().singleOrDefault(null)));
    }


    public DatabaseTask<UUID> getUUID() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> UUID.fromString(readDatabase("uuid", String.class).complete())));
    }

    public DatabaseTask<SQLClan> getClan() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> new SQLClan(readDatabase("clan", Integer.class).complete())));
    }


    public void delete() {
        PrimeCore.getInstance().getDb().update("DELETE FROM prime_clan_requests WHERE id = ?")
                .parameters(id).execute();
    }

}
