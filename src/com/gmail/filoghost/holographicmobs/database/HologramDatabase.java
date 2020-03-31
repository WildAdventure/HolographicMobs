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
package com.gmail.filoghost.holographicmobs.database;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.filoghost.holographicmobs.HolographicMobs;
import com.gmail.filoghost.holographicmobs.MobType;
import com.gmail.filoghost.holographicmobs.exception.HologramNotFoundException;
import com.gmail.filoghost.holographicmobs.exception.InvalidLocationException;
import com.gmail.filoghost.holographicmobs.exception.WorldNotFoundException;
import com.gmail.filoghost.holographicmobs.object.HologramMob;

public class HologramDatabase {

	private static File file;
	private static FileConfiguration config;
	
	private HologramDatabase() { }
	
	public static void initialize() {
		file = new File(HolographicMobs.getInstance().getDataFolder(), "database.yml");
		if (!file.exists()) {
			HolographicMobs.getInstance().saveResource("database.yml", false);
		}
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public static HologramMob loadHologram(String name) throws HologramNotFoundException, InvalidLocationException, WorldNotFoundException {
		
		ConfigurationSection configSection = config.getConfigurationSection(name);
		String locationString = configSection.getString("location");
		String mobTypeString = configSection.getString("type");
		
		if (locationString == null) {
			throw new HologramNotFoundException();
		}
		
		Location loc;
		try {
			loc = LocationSerializer.locationFromString(locationString);
		} catch (IllegalArgumentException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new InvalidLocationException();
		}
		
		try {
			MobType type = MobType.match(mobTypeString);
			HologramMob hologram = (HologramMob) type.getHologramClass().getConstructor(String.class, Location.class).newInstance(name, loc);
			
			hologram.readData(configSection);
			return hologram;
			
		} catch (Exception ex) {
			throw new HologramNotFoundException();
		}
	}
	
	public static void deleteHologram(HologramMob hologram) {
		config.set(hologram.getName(), null);
	}
	
	public static void saveHologram(HologramMob hologram) {
		ConfigurationSection configSection = config.createSection(hologram.getName());
		configSection.set("location", LocationSerializer.locationToString(hologram.getLocation()));
		configSection.set("type", MobType.match(hologram.getClass()).toString().toLowerCase());
		hologram.saveData(configSection);
	}
	
	public static Set<String> getHolograms() {
		return config.getKeys(false);
	}
	
	public static boolean isExistingHologram(String name) {
		return config.isConfigurationSection(name);
	}
	
	public static void saveToDisk() throws IOException {
		if (config != null && file != null) {
			config.save(file);
		}
	}
	
	public static void trySaveToDisk() {
		try {
			saveToDisk();
		} catch (IOException ex) {
			ex.printStackTrace();
			HolographicMobs.getInstance().getLogger().severe("Unable to save database.yml to disk!");
		}
	}
}
