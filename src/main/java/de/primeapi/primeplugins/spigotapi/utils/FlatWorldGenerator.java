package de.primeapi.primeplugins.spigotapi.utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * @author Lukas S. PrimeAPI
 * created on 04.05.2021
 * crated for PrimePlugins
 */
public class FlatWorldGenerator extends ChunkGenerator {

	@Override
	public ChunkData generateChunkData(World world, Random random, int ChunkX, int ChunkY, BiomeGrid biome) {
		ChunkData data = createChunkData(world);
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				data.setBlock(x, 0, y, Material.BEDROCK);
			}
		}
		return data;
	}
}