package net.fsmdev.biggerenderchests;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod class for Bigger Ender Chests.
 * This mod increases the ender chest inventory size from 3 rows (27 slots) to 6 rows (54 slots).
 * The mod uses Mixin to modify the player entity initialization and ender chest block behavior.
 */
public class BiggerEnderChests implements ModInitializer {
	/** Mod identifier used for registration and logging */
	public static final String MOD_ID = "biggerenderchests";

	/**
	 * Logger instance for this mod.
	 * Used to write text to the console and log file.
	 * Uses the mod ID as the logger name for clear identification.
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/**
	 * Initializes the mod when Minecraft is in a mod-load-ready state.
	 * The actual functionality is implemented via mixins, so this just logs initialization.
	 */
	@Override
	public void onInitialize() {
		// Log successful initialization
		// The actual ender chest resizing is handled by mixins at runtime
		LOGGER.info("Bigger Ender Chests initialized!");
	}
}