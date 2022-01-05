package de.primeapi.primeplugins.spigotapi.sql.clan;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.DatabaseTask;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class SQLClan {
    final int id;

    public static DatabaseTask<SQLClan> create(String name, String tag) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Integer id = PrimeCore.getInstance().getDb().update("INSERT INTO prime_clan_clans values(id,?,?,?,?)")
                    .parameters(name.toLowerCase(), name, tag, 0)
                    .returnGeneratedKeys()
                    .getAs(Integer.class).toBlocking().firstOrDefault(null);
            return new SQLClan(id);
        }));
    }

    public static DatabaseTask<SQLClan> fromName(String name) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Integer id = PrimeCore.getInstance().getDb().select("SELECT id FROM prime_clan_clans WHERE name = ?")
                    .parameters(name.toLowerCase())
                    .getAs(Integer.class).toBlocking().singleOrDefault(null);
            if (id != null) {
                return new SQLClan(id);
            }
            return null;
        }));
    }

    public static DatabaseTask<SQLClan> fromTag(String tag) {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> {
            Integer id = PrimeCore.getInstance().getDb().select("SELECT id FROM prime_clan_clans WHERE UPPER(tag) = UPPER(?)")
                    .parameters(tag.toUpperCase())
                    .getAs(Integer.class).toBlocking().singleOrDefault(null);
            if (id != null) {
                return new SQLClan(id);
            }
            return null;
        }));
    }

    public DatabaseTask<String> getName() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() -> PrimeCore.getInstance().getDb().select(
                        "SELECT name FROM prime_clan_clans WHERE id = ?"
                )
                .parameters(id)
                .getAs(String.class).toBlocking().singleOrDefault(null)));
    }

    public DatabaseTask<String> getRealname() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() ->
                PrimeCore.getInstance().getDb().select("SELECT realname FROM prime_clan_clans WHERE id = ?")
                        .parameters(id)
                        .getAs(String.class).toBlocking().singleOrDefault(null)
        )
        );
    }

    public DatabaseTask<String> getTag() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() ->
                PrimeCore.getInstance().getDb().select("SELECT tag FROM prime_clan_clans WHERE id=?")
                        .parameters(id)
                        .getAs(String.class).toBlocking().singleOrDefault(null)
        ));
    }

    public DatabaseTask<Integer> getCoins() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() ->
                PrimeCore.getInstance().getDb().select("SELECT coins FROM prime_clan_clans WHERE id=?")
                        .parameters(id)
                        .getAs(Integer.class).toBlocking().singleOrDefault(null)
        ));
    }

    public DatabaseTask<List<SQLPlayerAllocation>> getMembers() {
        return new DatabaseTask<>(CompletableFuture.supplyAsync(() ->
                PrimeCore.getInstance().getDb().select("SELECT id FROM prime_clan_players WHERE clan = ?")
                        .parameters(id)
                        .getAs(Integer.class)
                        .toList()
                        .toBlocking()
                        .singleOrDefault(new ArrayList<>())
                        .stream()
                        .map(SQLPlayerAllocation::new)
                        .sorted((o1, o2) -> Integer.compare(o2.getRank().complete(), o1.getRank().complete()))
                        .collect(Collectors.toList())));
    }

    public void updateName(String name) {
        PrimeCore.getInstance().getDb().update("UPDATE prime_clan_clans SET name = ?, realname = ? WHERE id = ?")
                .parameters(name.toLowerCase(), name, id).execute();
    }

    public void updateTag(String tag) {
        PrimeCore.getInstance().getDb().update("UPDATE prime_clan_clans SET tag = ? WHERE id = ?")
                .parameters(tag, id).execute();
    }

    public void updateCoins(Integer coins) {
        PrimeCore.getInstance().getDb().update("UPDATE prime_clan_clans SET coins = ? WHERE id = ?")
                .parameters(coins, id).execute();
    }

    public void addCoins(Integer coins) {
        getCoins().submit(integer -> updateCoins(coins + integer));
    }

    public void delete() {
        PrimeCore.getInstance().getDb().update("DELETE FROM prime_clan_clans WHERE id = ?")
                .parameters(id).execute();
    }


}
