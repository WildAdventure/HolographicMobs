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
package com.gmail.filoghost.holographicmobs.object;

import java.util.Map;
import java.util.logging.Level;

import lombok.Getter;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicmobs.HolographicMobs;
import com.gmail.filoghost.holographicmobs.api.ClickHandler;
import com.gmail.filoghost.holographicmobs.exception.SpawnFailedException;
import com.gmail.filoghost.holographicmobs.utils.StringUtils;
import com.gmail.filoghost.holographicmobs.utils.Validator;
import com.google.common.collect.Maps;

public abstract class HologramMob {
	
	public static Map<Player, Long> cooldowns = Maps.newHashMap();
	
	public void onInteract(Player player) {
		if (clickHandler != null) {
			
			Long cooldown = cooldowns.get(player);
			long now = System.currentTimeMillis();
			
			if (cooldown == null || now - cooldown.longValue() > 500) {
				cooldowns.put(player, now);
				try {
					clickHandler.onClick(player);
				} catch (Exception e) {
					HolographicMobs.getInstance().getLogger().log(Level.SEVERE, "Unhandled exception while executing ClickHandler " + clickHandler.getClass().getName(), e);
				}
			}
			
			return;
		}
		
		if (command != null && !command.isEmpty()) {
			
			Long cooldown = cooldowns.get(player);
			long now = System.currentTimeMillis();
			
			if (cooldown == null || now - cooldown.longValue() > 500) {
				cooldowns.put(player, now);
				try {
					getCommandType().execute(player, command);
				} catch (Exception e) {
					HolographicMobs.getInstance().getLogger().log(Level.SEVERE, "Unhandled exception while executing command type " + getCommandType().toString(), e);
				}
			}
		}
	}
	
	protected String name;
	protected World bukkitWorld;
	
	@Getter
	protected double x, y, z;
	
	protected float yaw, pitch;
	
	@Getter
	protected int chunkX, chunkZ;
	
	private boolean deleted;
	
	@Getter
	protected String command;
	
	@Getter
	private CommandType commandType;
	
	@Getter
	private ClickHandler clickHandler;
	
	protected HologramMob(String name, Location source) {
		this.name = name;
		bukkitWorld = source.getWorld();
		x = source.getX();
		y = source.getY();
		z = source.getZ();
		yaw = source.getYaw();
		pitch = source.getPitch();
		chunkX = source.getChunk().getX();
		chunkZ = source.getChunk().getZ();
	}

	public final String getName() {
		return name;
	}
	
	public final World getWorld() {
		return bukkitWorld;
	}
	
	public final Location getLocation() {
		return new Location(bukkitWorld, x, y, z, yaw, pitch);
	}
	
	public final int getBlockX() {
		return (int) x;
	}
	
	public final int getBlockY() {
		return (int) y;
	}
	
	public final int getBlockZ() {
		return (int) z;
	}
	
	public final boolean isInChunk(Chunk chunk) {
		return chunkX == chunk.getX() && chunkZ == chunk.getZ();
	}
	
	public final boolean isInLoadedChunk() {
		return bukkitWorld.isChunkLoaded(chunkX, chunkZ);
	}
	
	public final void delete() {
		deleted = true;
		despawnEntity();
		
		if (HologramManager.isTracked(this)) {
			HologramManager.remove(this);
		}
	}
	
	public final boolean isDeleted() {
		return deleted;
	}
	
	public final void setLocation(Location source) {
		Validator.notNull(source, "location cannot be null");
		Validator.notNull(source.getWorld(), "location's world cannot be null");
		
		bukkitWorld = source.getWorld();
		x = source.getX();
		y = source.getY();
		z = source.getZ();
		yaw = source.getYaw();
		pitch = source.getPitch();
		chunkX = source.getChunk().getX();
		chunkZ = source.getChunk().getZ();
	}
	
	public void setClickHandler(ClickHandler handler) {
		this.clickHandler = handler;
	}
	
	public void setCommand(String command) {
		if (command == null || command.isEmpty()) {
			this.command = null;
			this.commandType = null;
			return;
		}
		
		if (command.toLowerCase().startsWith("console:")) {
			this.command = command.substring(8).trim();
			this.commandType = CommandType.CONSOLE;
		} else if (command.toLowerCase().startsWith("tell:")) {
			this.command = command.substring(5).trim();
			this.commandType = CommandType.TELL;
		} else if (command.toLowerCase().startsWith("menu:")) {
			this.command = command.substring(5).trim();
			this.commandType = CommandType.MENU;
		} else if (command.toLowerCase().startsWith("silent:")) {
			this.command = command.substring(7).trim();
			this.commandType = CommandType.SILENT;
		} else if (command.toLowerCase().startsWith("server:")) {
			this.command = command.substring(7).trim();
			this.commandType = CommandType.SERVER;
		} else {
			this.command = command.trim();
			this.commandType = CommandType.PLAYER;
		}
	}
	
	public void readData(ConfigurationSection section) {
		String rawCommand = StringUtils.toReadableFormat(section.getString("command"));
		setCommand(rawCommand);
	}
	
	public void saveData(ConfigurationSection section) {
		if (commandType != null && command != null) {
			section.set("command", commandType.getPrefix() + StringUtils.toSaveableFormat(command));
		}
	}
	
	public boolean update() {
		Validator.checkState(!isDeleted(), "Hologram already deleted");
		
		if (isInLoadedChunk()) {
			return forceUpdate();
		}
		
		return true;
	}
	
	public  boolean forceUpdate() {
		Validator.checkState(!isDeleted(), "Hologram already deleted");
		
		// Remove previous entities.
		despawnEntity();
		
		try {
			spawnEntity();
			return true;
		} catch (SpawnFailedException ex) {
			return false;
		}
	}
	
	public abstract void spawnEntity() throws SpawnFailedException;
	
	public abstract void despawnEntity();
	
	public abstract double getHeight();
	
}
