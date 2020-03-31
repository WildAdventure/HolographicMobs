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
package com.gmail.filoghost.holographicmobs.listener;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.gmail.filoghost.holographicmobs.nms.interfaces.BukkitHolographicMob;
import com.gmail.filoghost.holographicmobs.nms.interfaces.NmsManager;
import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.object.HologramManager;

public class MainListener implements Listener {
	
	private NmsManager nmsManager;
	
	public MainListener(NmsManager nmsManager) {
		this.nmsManager = nmsManager;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onChunkUnload(ChunkUnloadEvent event) {
		for (Entity entity : event.getChunk().getEntities()) {
			if (!entity.isDead() && entity instanceof BukkitHolographicMob) {
				HologramMob parent = nmsManager.getParentHologramBase(entity);
				if (parent != null) {
					parent.despawnEntity();
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		HologramManager.onChunkLoad(chunk);
	}
	
	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getEntity() instanceof BukkitHolographicMob) {
			if (event.isCancelled()) {
				event.setCancelled(false);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		HologramMob.cooldowns.remove(event.getPlayer());
	}

	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof BukkitHolographicMob) {
			HologramMob hologramMob = nmsManager.getParentHologramBase(event.getRightClicked());
			if (hologramMob == null) {
				return;
			}
			
			event.setCancelled(true);
			hologramMob.onInteract(event.getPlayer());
		}
	}
	

}
