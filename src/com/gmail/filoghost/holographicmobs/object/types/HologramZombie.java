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
package com.gmail.filoghost.holographicmobs.object.types;

import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicmobs.HolographicMobs;
import com.gmail.filoghost.holographicmobs.MobType;
import com.gmail.filoghost.holographicmobs.exception.SpawnFailedException;
import com.gmail.filoghost.holographicmobs.nms.interfaces.NMSHolographicZombie;
import com.gmail.filoghost.holographicmobs.object.HologramEquippable;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */

public class HologramZombie extends HologramEquippable {
	
	protected NMSHolographicZombie nmsEntity;
	private Hologram hologram;
	

	public HologramZombie(String name, Location source) {
		super(name, source);
	}

	@Override
	public void spawnEntity() throws SpawnFailedException {
		despawnEntity();
		
		nmsEntity = (NMSHolographicZombie) HolographicMobs.nmsManager.spawnHologramMob(bukkitWorld, x, y, z, yaw, pitch, MobType.ZOMBIE, this);
		nmsEntity.setLockTick(true);
			
		if (customName != null) {
			
			hologram = HologramsAPI.createHologram(HolographicMobs.getInstance(), new Location(bukkitWorld, x, y, z));
			hologram.setAllowPlaceholders(true);
			for (String line : customName) {
				hologram.appendTextLine(line);
			}
			
			hologram.teleport(bukkitWorld, x, y + getHeight() + HolographicMobs.SPACE_BETWEEN_NAMETAG + hologram.getHeight(), z);
		}

		if (itemInHand != null) {
			nmsEntity.setItemInHandNMS(itemInHand);
		}
		if (itemInOffHand != null) {
			nmsEntity.setItemInOffHandNMS(itemInOffHand);
		}
		if (helmet != null) {
			nmsEntity.setHelmetNMS(helmet);
		}
		if (chestplate != null) {
			nmsEntity.setChestplateNMS(chestplate);
		}
		if (leggings != null) {
			nmsEntity.setLeggingsNMS(leggings);
		}
		if (boots != null) {
			nmsEntity.setBootsNMS(boots);
		}
		
		nmsEntity.sendEquipPacketNMS();
	}

	@Override
	public void despawnEntity() {
		if (nmsEntity != null) {
			nmsEntity.killEntityNMS();
			nmsEntity = null;
		}
		if (hologram != null) {
			hologram.delete();
			hologram = null;
		}
	}

	@Override
	public double getHeight() {
		return 2;
	}
	
}
