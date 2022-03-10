package de.primeapi.primeplugins.spigotapi.managers.vault;

import de.primeapi.primeplugins.spigotapi.PrimeCore;
import de.primeapi.primeplugins.spigotapi.sql.SQLPlayer;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;

import java.util.List;

/**
 * @author Lukas S. PrimeAPI
 * created on 27.05.2021
 * crated for PrimePlugins
 */
public class VaultEconomy implements Economy {

	public VaultEconomy() {
		Bukkit.getServicesManager().register(Economy.class, this, PrimeCore.getInstance(), ServicePriority.High);
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getName() {
		return "PrimeCore Coins";
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public String format(double v) {
		return null;
	}

	@Override
	public String currencyNamePlural() {
		return "Coins";
	}

	@Override
	public String currencyNameSingular() {
		return "Coin";
	}

	@Override
	public boolean hasAccount(String s) {
		return true;
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer) {
		return true;
	}

	@Override
	public boolean hasAccount(String s, String s1) {
		return true;
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
		return true;
	}

	@Override
	public double getBalance(String s) {
		SQLPlayer p = SQLPlayer.loadPlayerByName(s).complete();
		if (p != null) {
			return p.retrieveCoins().complete();
		}
		return 0;
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		SQLPlayer p = new SQLPlayer(offlinePlayer.getUniqueId());
		if (p != null) {
			return p.retrieveCoins().complete();
		}
		return 0;
	}

	@Override
	public double getBalance(String s, String s1) {
		SQLPlayer p = SQLPlayer.loadPlayerByName(s).complete();
		if (p != null) {
			return p.retrieveCoins().complete();
		}
		return 0;
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer, String s) {
		SQLPlayer p = new SQLPlayer(offlinePlayer.getUniqueId());
		if (p != null) {
			return p.retrieveCoins().complete();
		}
		return 0;
	}

	@Override
	public boolean has(String s, double v) {
		return getBalance(s) >= v;
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, double v) {
		return getBalance(offlinePlayer) >= v;
	}

	@Override
	public boolean has(String s, String s1, double v) {
		return getBalance(s) >= v;
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
		return getBalance(offlinePlayer) >= v;
	}

	@Override
	public EconomyResponse withdrawPlayer(String s, double v) {
		SQLPlayer p = SQLPlayer.loadPlayerByName(s).complete();
		if (p != null) {
			p.addCoins(-(int) v);
		}
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
		SQLPlayer p = new SQLPlayer(offlinePlayer.getUniqueId());
		if (p != null) {
			p.addCoins(-(int) v);
		}
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(String s, String s1, double v) {
		SQLPlayer p = SQLPlayer.loadPlayerByName(s).complete();
		if (p != null) {
			p.addCoins(-(int) v);
		}
		return null;
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		SQLPlayer p = new SQLPlayer(offlinePlayer.getUniqueId());
		if (p != null) {
			p.addCoins(-(int) v);
		}
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String s, double v) {
		SQLPlayer p = SQLPlayer.loadPlayerByName(s).complete();
		if (p != null) {
			p.addCoins((int) v);
		}
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
		SQLPlayer p = new SQLPlayer(offlinePlayer.getUniqueId());
		if (p != null) {
			p.addCoins((int) v);
		}
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String s, String s1, double v) {
		SQLPlayer p = SQLPlayer.loadPlayerByName(s).complete();
		if (p != null) {
			p.addCoins((int) v);
		}
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
		SQLPlayer p = new SQLPlayer(offlinePlayer.getUniqueId());
		if (p != null) {
			p.addCoins((int) v);
		}
		return null;
	}

	@Override
	public EconomyResponse createBank(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public EconomyResponse deleteBank(String s) {
		return null;
	}

	@Override
	public EconomyResponse bankBalance(String s) {
		return null;
	}

	@Override
	public EconomyResponse bankHas(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String s, double v) {
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String s, String s1) {
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
		return null;
	}

	@Override
	public List<String> getBanks() {
		return null;
	}

	@Override
	public boolean createPlayerAccount(String s) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(String s, String s1) {
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
		return false;
	}
}
