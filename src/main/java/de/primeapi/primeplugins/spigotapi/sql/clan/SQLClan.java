package de.primeapi.primeplugins.spigotapi.sql.clan;


import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.util.sql.queries.Retriever;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class SQLClan {
    final int id;

    public static Retriever<SQLClan> create(String name, String tag) {
        return PrimeCore.getInstance().getDb().update("INSERT INTO prime_clan_clans values(id,?,?,?,?)")
                        .parameters(name.toLowerCase(), name, tag, 0)
                        .returnGeneratedKeys(Integer.class)
                        .get()
                        .map(SQLClan::new);
    }

    public static Retriever<SQLClan> fromName(String name) {
        return PrimeCore.getInstance().getDb().select("SELECT id FROM prime_clan_clans WHERE name = ?")
                        .parameters(name.toLowerCase())
                        .execute(Integer.class)
                        .get()
                        .map(SQLClan::new);
    }

    public static Retriever<SQLClan> fromTag(String tag) {
        return PrimeCore.getInstance()
                        .getDb()
                        .select("SELECT id FROM prime_clan_clans WHERE UPPER(tag) = UPPER(?)")
                        .parameters(tag.toUpperCase())
                        .execute(Integer.class)
                        .get()
                        .map(SQLClan::new);
    }

    public Retriever<String> getName() {
        return PrimeCore.getInstance().getDb().select(
                                "SELECT name FROM prime_clan_clans WHERE id = ?"
                                                     )
                        .parameters(id)
                        .execute(String.class)
                        .get();
    }

    public Retriever<String> getRealname() {
        return PrimeCore.getInstance().getDb().select("SELECT realname FROM prime_clan_clans WHERE id = ?")
                        .parameters(id)
                        .execute(String.class)
                        .get();
    }

    public Retriever<String> getTag() {
        return PrimeCore.getInstance().getDb().select("SELECT tag FROM prime_clan_clans WHERE id=?")
                        .parameters(id)
                        .execute(String.class)
                        .get();
    }

    public Retriever<Integer> getCoins() {
        return PrimeCore.getInstance().getDb().select("SELECT coins FROM prime_clan_clans WHERE id=?")
                        .parameters(id)
                        .execute(Integer.class)
                        .get();
    }

    public Retriever<List<SQLPlayerAllocation>> getMembers() {
        return PrimeCore.getInstance().getDb().select("SELECT id FROM prime_clan_players WHERE clan = ?")
                        .parameters(id)
                        .execute(Integer.class)
                        .getAsSet()
                        .map(integers -> integers.stream()
                                                 .map(SQLPlayerAllocation::new)
                                                 .sorted((o1, o2) -> Integer.compare(
                                                         o2.getRank().complete(),
                                                         o1.getRank().complete()
                                                                                    ))
                                                 .collect(Collectors.toList()));
    }

    public Retriever<String> getColor() {
        return PrimeCore.getInstance().getDb().select("SELECT color FROM prime_clan_clans WHERE id=?")
                 .parameters(id)
                 .execute(String.class).get();
    }

    public void updateColor(String color) {
        PrimeCore.getInstance().getDb().update("UPDATE prime_clan_clans SET color = ? WHERE id = ?")
                 .parameters(color.toLowerCase(), color, id).execute();
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
