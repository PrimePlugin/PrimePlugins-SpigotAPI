package de.primeapi.primeplugins.spigotapi.sql.friend;


import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import de.primeapi.util.sql.queries.Retriever;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class SQLFriendEntry {
	public final int id;


	public static Retriever<List<SQLFriendEntry>> getFriendsFromPlayer(SQLPlayer p) {
		return new Retriever<>(() -> {
			List<SQLFriendEntry> list = new ArrayList<>();
			try {
				PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
						"SELECT * FROM prime_bungee_friends WHERE uuid = ?"
				                                                                               );
				st.setString(1, p.retrieveUniqueId().complete().toString());
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					list.add(new SQLFriendEntry(rs.getInt("id")));
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return list;
		});
	}

	public static Retriever<SQLFriendEntry> getFromPlayers(SQLPlayer p, SQLPlayer friend) {
		return new Retriever<>(() -> {
			SQLFriendEntry entry = null;
			try {
				PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
						"SELECT * FROM prime_bungee_friends WHERE uuid = ? AND friend = ?"
				                                                                               );
				st.setString(1, p.retrieveUniqueId().complete().toString());
				st.setString(2, friend.retrieveUniqueId().complete().toString());
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					entry = new SQLFriendEntry(rs.getInt("id"));
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return entry;
		});
	}

	public static Retriever<SQLFriendEntry> create(SQLPlayer p1, SQLPlayer p2, Long time) {
		return new Retriever<>(() -> {
			SQLFriendEntry entry = null;
			try {
				PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
						"INSERT INTO prime_bungee_friends value (id, ?,?,?)",
						Statement.RETURN_GENERATED_KEYS
				                                                                               );
				st.setString(1, p1.retrieveUniqueId().complete().toString());
				st.setString(2, p2.retrieveUniqueId().complete().toString());
				st.setLong(3, time);
				st.executeUpdate();
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					entry = new SQLFriendEntry(rs.getInt(1));
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return entry;
		});
	}


	public Retriever<SQLPlayer> retrieveFriend() {
		return new Retriever<>(() -> {
			SQLPlayer player = null;
			try {
				PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
						"SELECT * FROM prime_bungee_friends WHERE id = ?"
				                                                                               );
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					player = new SQLPlayer(UUID.fromString(rs.getString("friend")));
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return player;
		});
	}

	public Retriever<Long> getTime() {
		return new Retriever<>(() -> {
			Long l = null;
			try {
				PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
						"SELECT * FROM prime_bungee_friends WHERE id = ?"
				                                                                               );
				st.setInt(1, id);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					l = rs.getLong("time");
				}
				rs.close();
				st.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
			return l;
		});
	}

	public void delete() {
		PrimeCore.getInstance().getThreadPoolExecutor().submit(() -> {
			try {
				PreparedStatement st = PrimeCore.getInstance().getConnection().prepareStatement(
						"DELETE FROM prime_bungee_friends WHERE id=?"
				                                                                               );
				st.setInt(1, id);
				st.execute();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		});
	}

}
