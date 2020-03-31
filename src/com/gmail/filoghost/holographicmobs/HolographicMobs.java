/*
 * Copyright (c) 2020, Wild Adventure
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 4. Redistribution of this software in source or binary forms shall be free
 *    of all charges or fees to the recipient of this software.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gmail.filoghost.holographicmobs;

import java.util.Set;
import java.util.logging.Logger;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicmobs.commands.main.HologramsCommandHandler;
import com.gmail.filoghost.holographicmobs.database.HologramDatabase;
import com.gmail.filoghost.holographicmobs.exception.HologramNotFoundException;
import com.gmail.filoghost.holographicmobs.exception.InvalidLocationException;
import com.gmail.filoghost.holographicmobs.exception.WorldNotFoundException;
import com.gmail.filoghost.holographicmobs.listener.MainListener;
import com.gmail.filoghost.holographicmobs.nms.interfaces.NmsManager;
import com.gmail.filoghost.holographicmobs.object.HologramManager;
import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.utils.VersionUtils;

public class HolographicMobs extends JavaPlugin {
	
	public static final double SPACE_BETWEEN_NAMETAG = 0.2;

	private static Logger logger;
	private static HolographicMobs instance;
	
	public static NmsManager nmsManager;
	private HologramsCommandHandler mainCommandHandler;
	
	@Getter
	private static boolean wildCommons;
	
	@Override
	public void onEnable() {
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getName() + "] Richiesto Holographic Displays!");
			setEnabled(false);
			return;
		}
		
		wildCommons = Bukkit.getPluginManager().isPluginEnabled("WildCommons");
		
		instance = this;
		logger = getLogger();

		String version = VersionUtils.getBukkitVersion();
		
		// It's simple, we don't need reflection.
		if ("v1_9_R2".equals(version)) {
			nmsManager = new com.gmail.filoghost.holographicmobs.nms.v1_9_R2.NmsManagerImpl();
		} else if ("v1_12_R1".equals(version)) {
			nmsManager = new com.gmail.filoghost.holographicmobs.nms.v1_12_R1.NmsManagerImpl();
		} else {
			printWarnAndDisable(
				"******************************************************",
				"     This version of Holographic Mobs can",
				"     only work on these server versions:",
				"     1.9.4, 1.12.2.",
				"     The plugin will be disabled.",
				"******************************************************"
			);
			return;
		}

		try {
			
			nmsManager.registerCustomEntities();
			
		} catch (Exception e) {
			e.printStackTrace();
			printWarnAndDisable(
				"******************************************************",
				"     Holographic Mobs was unable to register",
				"     custom entities, the plugin will be disabled.",
				"     Are you using the correct Bukkit version?",
				"******************************************************"
			);
			return;
		}
		
		// Initalize other static classes.
		HologramDatabase.initialize();
		
		Set<String> savedHolograms = HologramDatabase.getHolograms();
		if (savedHolograms != null && savedHolograms.size() > 0) {
			for (String singleSavedHologram : savedHolograms) {
				try {
					HologramMob singleHologramEntity = HologramDatabase.loadHologram(singleSavedHologram);
					HologramManager.addPluginHologram(singleHologramEntity);
				} catch (HologramNotFoundException e) {
					logger.warning("Holographi mob '" + singleSavedHologram + "' not found, skipping it.");
				} catch (InvalidLocationException e) {
					logger.warning("Holographi mob '" + singleSavedHologram + "' has an invalid location format.");
				} catch (WorldNotFoundException e) {
					logger.warning("Holographi mob '" + singleSavedHologram + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
				} catch (Exception e) {
					e.printStackTrace();
					logger.warning("Unhandled exception while loading '" + singleSavedHologram + "'. Please contact the developer.");
				}
			}
		}
		
		getCommand("holographicmobs").setExecutor(mainCommandHandler = new HologramsCommandHandler());
		Bukkit.getPluginManager().registerEvents(new MainListener(nmsManager), this);
		
		// The entities are loaded when the server is ready.
		new BukkitRunnable() {
			@Override
			public void run() {

				for (HologramMob hologram : HologramManager.getPluginHolograms()) {
					if (!hologram.update()) {
						logger.warning("Unable to spawn entities for the holographic mob '" + hologram.getName() + "'.");
					}
				}
				for (HologramMob hologram : HologramManager.getAPIHolograms()) {
					if (!hologram.update()) {
						logger.warning("Unable to spawn entities for the holographic API mob at " + hologram.getLocation() + ".");
					}
				}
			}
		}.runTaskLater(this, 10L);
	}
	

	@Override
	public void onDisable() {
		for (HologramMob hologram : HologramManager.getPluginHolograms()) {
			hologram.despawnEntity();
		}
	}
	
	private static void printWarnAndDisable(String... messages) {
		StringBuffer buffer = new StringBuffer("\n ");
		for (String message : messages) {
			buffer.append('\n');
			buffer.append(message);
		}
		buffer.append('\n');
		System.out.println(buffer.toString());
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) { }
		instance.setEnabled(false);
	}

	public static HolographicMobs getInstance() {
		return instance;
	}

	public HologramsCommandHandler getMainCommandHandler() {
		return mainCommandHandler;
	}

	public static void logInfo(String message) {
		logger.info(message);
	}
	
	public static void logWarning(String message) {
		logger.warning(message);
	}
	
	public static void logSevere(String message) {
		logger.severe(message);
	}
}
