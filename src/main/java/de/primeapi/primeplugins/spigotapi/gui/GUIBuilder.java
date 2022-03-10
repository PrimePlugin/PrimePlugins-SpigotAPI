package de.primeapi.primeplugins.spigotapi.gui;

import de.primeapi.primeplugins.spigotapi.gui.itembuilder.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.*;

public class GUIBuilder {

	public HashMap<ItemStack, IClickAction> items = new HashMap<>();
	public ItemStack[] content;
	public InventoryHolder inventoryHolder;
	public int size = 0;
	public String title;
	public InventoryType inventoryType;
	public ICloseAction closeAction;
	public Inventory inventory;
	public ItemStack filler;
	private Listener listener;


	public GUIBuilder(@NonNull int size) {
		this.size = size;
		content = new ItemStack[size];
		this.title = "";
		this.inventoryHolder = null;
	}

	public GUIBuilder(@NonNull int size, @NonNull String title) {
		this.size = size;
		content = new ItemStack[size];
		this.title = title;
		this.inventoryHolder = null;
	}

	public GUIBuilder(@Nullable InventoryHolder inventoryHolder, @NonNull int size) {
		this.size = size;
		content = new ItemStack[size];
		this.title = "";
		this.inventoryHolder = inventoryHolder;
	}

	public GUIBuilder(@Nullable InventoryHolder inventoryHolder, @NonNull int size, @NonNull String title) {
		this.size = size;
		content = new ItemStack[size];
		this.title = title;
		this.inventoryHolder = inventoryHolder;
	}

	public GUIBuilder(
			@Nullable InventoryHolder inventoryHolder,
			@NonNull InventoryType inventoryType,
			@NonNull String title
	                 ) {
		this.inventoryType = inventoryType;
		this.title = title;
		this.inventoryHolder = inventoryHolder;
	}

	//Factory
	public static AnimationConfiguration createDefaultAnimationConfiguration() {
		return new AnimationConfiguration(Animation.STAR, 50, Sound.CLICK, Sound.LEVEL_UP);
	}

	public static AnimationConfiguration createDefaultAnimationConfiguration(Animation animation) {
		switch (animation) {
			case LEFT:
			case LEFT_FILLER:
			case STAR:
				return new AnimationConfiguration(animation, 50, Sound.CLICK, Sound.LEVEL_UP);
			case CLOCKWISE:
				return new AnimationConfiguration(animation, 10, Sound.CLICK, Sound.LEVEL_UP);
			default:
				return new AnimationConfiguration(Animation.STAR, 50, Sound.CLICK, Sound.LEVEL_UP);
		}
	}

	public GUIBuilder fillInventory(ItemStack itemStack) {
		this.filler = itemStack;
		return this;
	}

	public GUIBuilder fillInventory() {
		return fillInventory(new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 8).setDisplayName(" ").build());
	}

	private GUIBuilder fillNow() {
		for (int i = 0; i < size; i++) {
			if (content[i] == null) {
				addItem(i, filler, (p, itemStack) -> {
				});
			}
		}
		return this;
	}

	private ItemStack[] fillNow(ItemStack[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				array[i] = filler;
			}
		}
		return array;
	}

	public GUIBuilder addItem(int slot, ItemStack itemStack, IClickAction clickAction) {
		items.put(itemStack, clickAction);
		addItem(slot, itemStack);
		return this;
	}

	public GUIBuilder addItem(int slot, ItemStack itemStack, IClickAction clickAction, IClickAction rightClickAction) {
		items.put(itemStack, clickAction);
		addItem(slot, itemStack);
		return this;
	}

	public GUIBuilder addItem(int slot, ItemStack itemStack) {
		content[slot] = itemStack;
		return this;
	}

	public GUIBuilder addCloseAction(ICloseAction closeAction) {
		this.closeAction = closeAction;
		return this;
	}

	public Inventory build(Plugin plugin) {
		fillNow();
		Inventory inventory;
		if (inventoryType == null) {
			inventory = Bukkit.createInventory(inventoryHolder, size, title);
		} else {
			inventory = Bukkit.createInventory(inventoryHolder, inventoryType, title);
		}
		inventory.setContents(content);
		this.inventory = inventory;
		register(plugin);
		return this.inventory;
	}

	public void animate(Player p, AnimationConfiguration configuration, Plugin plugin) {
		ItemStack[] items = new ItemStack[size];
		Inventory inventory;
		if (inventoryType == null) {
			inventory = Bukkit.createInventory(inventoryHolder, size, title);
		} else {
			inventory = Bukkit.createInventory(inventoryHolder, inventoryType, title);
		}
		this.inventory = inventory;
		p.openInventory(inventory);
		Thread thread = new Thread(() -> {
			HashMap<Integer, ItemStack[]> rows = new HashMap<>();
			for (int i = 0; i < (size / 9); i++) {
				ItemStack[] row;
				row = Arrays.copyOfRange(content, i * 9, i * 9 + 9);
				rows.put(i, row);
			}

			if (configuration.getAnimation() == Animation.LEFT || configuration.getAnimation() == Animation.LEFT_FILLER) {
				for (int tick = 9; tick > 0; tick--) {
					HashMap<Integer, ItemStack[]> newRows = new HashMap<>();
					if (configuration.getTickSound() != null) {
						p.playSound(p.getLocation(), configuration.getTickSound(), 1, 1);
					}
					for (int rowId : rows.keySet()) {
						ItemStack[] row = rows.get(rowId);
						ItemStack[] newRow = new ItemStack[row.length];
						for (int i = 0; i < row.length; i++) {
							int j = i - tick;
							if (j >= 0) {
								newRow[j] = row[i];
							}
						}
						newRows.put(rowId, newRow);
					}

					List<ItemStack> finalList = new ArrayList<>();
					for (int rowId : newRows.keySet()) {
						ItemStack[] row = newRows.get(rowId);
						finalList.addAll(Arrays.asList(row));
					}
					ItemStack[] finalArray = new ItemStack[size];
					finalArray = finalList.toArray(finalArray);

					if (configuration.getAnimation() == Animation.LEFT_FILLER) {
						fillNow(finalArray);
					}

					inventory.setContents(finalArray);
					p.updateInventory();


					try {
						Thread.sleep(configuration.getTickSpeed());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			if (configuration.getAnimation() == Animation.CLOCKWISE) {
				ItemStack[] inv = new ItemStack[size];
				Direction direction = Direction.RIGHT;
				fillNow();
				int slot = 0;
				for (int i = 0; i < size; i++) {
					if (configuration.getTickSound() != null) {
						p.playSound(p.getLocation(), configuration.getTickSound(), 1, 1);
					}
					inv[slot] = content[slot];
					if (slot + direction.getAmount() >= inv.length || slot + direction.getAmount() < 0) {
						direction = direction.next();
					} else if (inv[slot + direction.getAmount()] != null) {
						direction = direction.next();
					} else if (slot % 9 == 0) {
						if (slot + Direction.UP.getAmount() < 0) {
							direction = Direction.RIGHT;
						} else {
							direction = Direction.UP;
						}
					} else if (isLastCollum(slot)) {
						if (slot + Direction.DOWN.getAmount() > size) {
							direction = Direction.LEFT;
						} else {
							direction = Direction.DOWN;
						}
					}

					assert direction != null;
					slot += direction.getAmount();


					inventory.setContents(inv);
					p.updateInventory();
					try {
						Thread.sleep(configuration.getTickSpeed());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			if (configuration.getAnimation() == Animation.STAR) {
				ItemStack[] inv = new ItemStack[size];
				Direction direction = Direction.RIGHT;
				fillNow();
				int slot = 0;
				boolean b = true;
				int middle;
				if (size % 2 == 0) {
					middle = size / 2 + 4;
				} else {
					middle = size / 2;
				}
				inv[middle] = content[middle];
				if (configuration.getTickSound() != null) {
					p.playSound(p.getLocation(), configuration.getTickSound(), 1, 1);
				}
				inventory.setContents(inv);
				p.updateInventory();
				try {
					Thread.sleep(configuration.getTickSpeed());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				while (b) {
					b = false;
					for (ItemStack itemStack : inv) {
						if (itemStack == null) {
							b = true;
							break;
						}
					}
					if (!b) break;
					if (configuration.getTickSound() != null) {
						p.playSound(p.getLocation(), configuration.getTickSound(), 1, 1);
					}

					int i = 0;
					for (ItemStack item : inv.clone()) {
						if (item != null) {
							{
								int curr = i + Direction.UP.getAmount();
								if (curr >= 0 && curr < size) {
									if (inv[curr] == null) inv[curr] = content[curr];
								}
							}
							{
								int curr = i + Direction.DOWN.getAmount();
								if (curr >= 0 && curr < size) {
									if (inv[curr] == null) inv[curr] = content[curr];
								}
							}
							{
								int curr = i + Direction.LEFT.getAmount();
								if (curr >= 0 && curr < size) {
									if (inv[curr] == null) inv[curr] = content[curr];
								}
							}
							{
								int curr = i + Direction.RIGHT.getAmount();
								if (curr >= 0 && curr < size) {
									if (inv[curr] == null) inv[curr] = content[curr];
								}
							}
						}
						i++;
					}


					inventory.setContents(inv);
					p.updateInventory();
					try {
						Thread.sleep(configuration.getTickSpeed());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					b = false;
					for (ItemStack itemStack : inv) {
						if (itemStack == null) {
							b = true;
							break;
						}
					}
				}
			}

			fillNow();
			if (configuration.getFinalSound() != null) {
				p.playSound(p.getLocation(), configuration.getFinalSound(), 1, 1);
			}
			inventory.setContents(content);
			p.updateInventory();
		});

		addCloseAction((p1, inventory1) -> thread.stop());
		thread.start();


		register(plugin);
	}

	private void register(Plugin plugin) {
		this.listener = new Listener() {
			@EventHandler
			public void onInventoryClick(InventoryClickEvent e) {
				if (!e.getInventory().equals(inventory)) {
					return;
				}

				e.setCancelled(true);

				if (Objects.isNull(e.getCurrentItem())) return;
				if (items.containsKey(e.getCurrentItem())) {
					items.get(e.getCurrentItem()).onClick((Player) e.getWhoClicked(), e.getCurrentItem());
					return;
				}
				if (Objects.isNull(e.getCurrentItem().getItemMeta())) return;
				if (Objects.isNull(e.getCurrentItem().getItemMeta().getDisplayName())) return;
				for (ItemStack itemStack : items.keySet()) {
					if (Objects.isNull(itemStack)) continue;
					if (Objects.isNull(itemStack.getItemMeta())) continue;
					if (Objects.isNull(itemStack.getItemMeta().getDisplayName())) continue;
					if (itemStack.getItemMeta()
					             .getDisplayName()
					             .equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
						items.get(itemStack).onClick((Player) e.getWhoClicked(), e.getCurrentItem());
						return;
					}
				}


			}

			@EventHandler
			public void onInventoryClose(InventoryCloseEvent e) {
				if (inventory.equals(e.getInventory())) {
					if (e.getPlayer() instanceof Player) {
						if (Objects.nonNull(inventory)) {
							if (Objects.nonNull(closeAction)) {
								closeAction.onClose((Player) e.getPlayer(), e.getInventory());
							}
							HandlerList.unregisterAll(listener);
						}
					}
				}
			}

		};

		Bukkit.getPluginManager().registerEvents(listener, plugin);


	}

	private boolean isLastCollum(int i) {
		int row = i / 9;
		return (i - row) % 8 == 0;
	}


	public enum Animation {
		LEFT_FILLER,
		LEFT,
		CLOCKWISE,
		STAR
	}

	@AllArgsConstructor
	@Getter
	private enum Direction {
		RIGHT(1),
		LEFT(-1),
		UP(-9),
		DOWN(9);


		int amount;

		public Direction next() {
			switch (this) {
				case RIGHT:
					return DOWN;
				case DOWN:
					return LEFT;
				case LEFT:
					return UP;
				case UP:
					return RIGHT;
			}
			return null;
		}
	}


}
