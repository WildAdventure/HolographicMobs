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
package com.gmail.filoghost.holographicmobs.nms.v1_9_R2.villager;

import java.util.Collection;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftVillager;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicmobs.nms.interfaces.BukkitHolographicMob;

public class CraftHolographicVillager extends CraftVillager implements BukkitHolographicMob {

	public CraftHolographicVillager(CraftServer server, EntityHolographicVillager entity) {
		super(server, entity);
	}
	
	// Disallow all the bukkit methods.
	
	@Override
	public void remove() {
		// Cannot be removed, this is the most important to override.
	}

	// Methods form Ageable class
	@Override public void setAge(int age) { }
	@Override public void setAgeLock(boolean lock) { }
	@Override public void setBreed(boolean breed) { }
	@Override public void setAdult() { }
	@Override public void setBaby() { }
	
	// Methods from LivingEntity class
	@Override public boolean addPotionEffect(PotionEffect effect) { return false; }
	@Override public boolean addPotionEffect(PotionEffect effect, boolean param) { return false; }
	@Override public boolean addPotionEffects(Collection<PotionEffect> effects) { return false; }
	@Override public void setRemoveWhenFarAway(boolean remove) { }
	
	// Methods from Entity
	@Override public void setVelocity(Vector vel) { }
	@Override public boolean teleport(Location loc) { return false; }
	@Override public boolean teleport(Entity entity) { return false; }
	@Override public boolean teleport(Location loc, TeleportCause cause) { return false; }
	@Override public boolean teleport(Entity entity, TeleportCause cause) { return false; }
	@Override public void setFireTicks(int ticks) { }
	@Override public boolean setPassenger(Entity entity) { return false; }
	@Override public boolean eject() { return false; }
	@Override public boolean leaveVehicle() { return false; }
	@Override public void playEffect(EntityEffect effect) { }
	
}
