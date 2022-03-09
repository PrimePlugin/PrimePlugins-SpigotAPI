package de.primeapi.primeplugins.spigotapi.sql.clan;


import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import de.primeapi.util.sql.queries.Retriever;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class SQLPlayerAllocation {
    final int id;

    public static Retriever<SQLPlayerAllocation> create(SQLPlayer player, SQLClan clan, Integer rank) {
        return PrimeCore.getInstance().getDb().update("INSERT INTO prime_clan_players value (id,?,?,?)")
                        .parameters(player.retrieveUniqueId().complete().toString(), clan.getId(), rank)
                        .returnGeneratedKeys(Integer.class)
                        .get()
                        .map(SQLPlayerAllocation::new);
    }

    public static Retriever<SQLPlayerAllocation> fromPlayer(SQLPlayer player) {
        return PrimeCore.getInstance().getDb().select("SELECT id FROM prime_clan_players WHERE uuid = ?")
                        .parameters(player.retrieveUniqueId().complete().toString())
                        .execute(Integer.class)
                        .get()
                        .map(SQLPlayerAllocation::new);
    }

    private <T> Retriever<T> readDatabase(String column, Class<T> type) {
        return PrimeCore.getInstance().getDb().select(
                                "SELECT " + column + " FROM prime_clan_players WHERE id = ?"
                                                           )
                        .parameters(id)
                        .execute(type)
                        .get();
    }

    public Retriever<UUID> getUUID() {
        return new Retriever<>(() -> UUID.fromString(readDatabase("uuid", String.class).complete()));
    }

    public Retriever<SQLClan> getClan() {
        return PrimeCore.getInstance().getDb().select("SELECT clan FROM prime_clan_players WHERE id = ?")
                .parameter(id)
                .execute(Integer.class)
                .get().map(SQLClan::new);
    }

    public Retriever<Integer> getRank() {
        return readDatabase("rank", Integer.class);
    }

    public void delete() {
        PrimeCore.getInstance().getDb().update("DELETE FROM prime_clan_players WHERE id = ?")
                 .parameters(id).execute();
    }

    public void updateRank(int rank) {
        PrimeCore.getInstance().getDb().update("UPDATE prime_clan_players SET `rank` = ? WHERE id = ?")
                 .parameters(rank, id).execute();
    }
}
