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
package com.gmail.filoghost.holographicmobs.nms.v1_12_R1.zombie;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;

import com.gmail.filoghost.holographicmobs.nms.v1_12_R1.NullBoundingBox;
import com.gmail.filoghost.holographicmobs.nms.interfaces.NMSHolographicZombie;
import com.gmail.filoghost.holographicmobs.object.HologramMob;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityDamageSource;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.SoundEffect;
import net.minecraft.server.v1_12_R1.World;
import net.minecraft.server.v1_12_R1.WorldServer;

public class EntityHolographicZombie extends EntityZombie implements NMSHolographicZombie {

	@Setter
	private boolean lockTick;
	
	@Getter
	private HologramMob parent;
	
	public EntityHolographicZombie(World world, HologramMob parent) {
		super(world);
		this.parent = parent;
		super.persistent = true;
		
		forceSetBoundingBox(new NullBoundingBox());
	}
	
	@Override
	public void a(AxisAlignedBB boundingBox) {
		// Do not change it!
	}
	
	public void forceSetBoundingBox(AxisAlignedBB boundingBox) {
		super.a(boundingBox);
	}
	
	@Override
	public void B_() {
		if (!lockTick) {
			super.B_();
		}
	}
	
	@Override
	public void inactiveTick() {
		// Check inactive ticks.
		
		if (!lockTick) {
			super.inactiveTick();
		}
	}
	
	@Override
	public void b(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}
	
	@Override
	public boolean c(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}

	@Override
	public boolean d(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}
	
	@Override
	public NBTTagCompound save(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return nbttagcompound;
	}
	
	@Override
	public void collide(Entity entity) {
		// Do not collide
	}
	
	@Override
	public boolean isCollidable() {
		return false;
	}
	
	@Override
	public boolean isInvulnerable(DamageSource damagesource) {
		/*
		 * The field Entity.invulnerable is private.
		 * It's only used while saving NBTTags, but since the entity would be killed
		 * on chunk unload, we prefer to override isInvulnerable().
		 */
	    return true;
	}
	
	@Override
	public boolean damageEntity(DamageSource damageSource, float f) {
		if (damageSource instanceof EntityDamageSource) {
			EntityDamageSource entityDamage = (EntityDamageSource) damageSource;
			
			if (entityDamage.getEntity() instanceof EntityPlayer) {
				parent.onInteract(((EntityPlayer) entityDamage.getEntity()).getBukkitEntity());
			}
		}
		
		return false;
	}

	@Override
	public void setCustomName(String customName) {
		// Locks the custom name.
	}
	
	@Override
	public void setCustomNameVisible(boolean visible) {
		// Locks the custom name.
	}
	
	@Override
	public void a(SoundEffect sound, float volume, float pitch) {
	    // Remove sounds.
	}
	
	@Override
	public void die() {
		setLockTick(false);
		super.die();
	}

	@Override
	public void setCustomNameNMS(String name) {
		if (name != null && name.length() > 300) {
			name = name.substring(0, 300);
		}
		super.setCustomName(name);
		super.setCustomNameVisible(name != null);
	}
	
	@Override
	public CraftEntity getBukkitEntity() {
		if (super.bukkitEntity == null) {
			this.bukkitEntity = new CraftHolographicZombie(this.world.getServer(), this);
	    }
		return this.bukkitEntity;
	}

	@Override
	public boolean isDeadNMS() {
		return this.dead;
	}

	@Override
	public String getCustomNameNMS() {
		return super.getCustomName();
	}
	
	@Override
	public void killEntityNMS() {
		super.dead = true;
	}
	
	@Override
	public void setLocationNMS(double x, double y, double z, float yaw, float pitch) {
		super.setLocation(x, y, z, yaw, pitch);
		super.aP = yaw; // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/5245147d004f648afcddec6a689e60d451c027bf
	}

	@Override
	public void setItemInHandNMS(org.bukkit.inventory.ItemStack item) {
		setSlot(EnumItemSlot.MAINHAND, item != null ? CraftItemStack.asNMSCopy(item) : null);
	}
	
	@Override
	public void setItemInOffHandNMS(org.bukkit.inventory.ItemStack item) {
		setSlot(EnumItemSlot.OFFHAND, item != null ? CraftItemStack.asNMSCopy(item) : null);
	}

	@Override
	public void setHelmetNMS(org.bukkit.inventory.ItemStack item) {
		setSlot(EnumItemSlot.HEAD, item != null ? CraftItemStack.asNMSCopy(item) : null);
	}

	@Override
	public void setChestplateNMS(org.bukkit.inventory.ItemStack item) {
		setSlot(EnumItemSlot.CHEST, item != null ? CraftItemStack.asNMSCopy(item) : null);
	}

	@Override
	public void setLeggingsNMS(org.bukkit.inventory.ItemStack item) {
		setSlot(EnumItemSlot.LEGS, item != null ? CraftItemStack.asNMSCopy(item) : null);
	}

	@Override
	public void setBootsNMS(org.bukkit.inventory.ItemStack item) {
		setSlot(EnumItemSlot.FEET, item != null ? CraftItemStack.asNMSCopy(item) : null);
	}

	@Override
	public void sendEquipPacketNMS() {
		for (EnumItemSlot slot : EnumItemSlot.values()) {
			ItemStack piece = getEquipment(slot);
			
			if (piece != null) {
				((WorldServer) world).getTracker().a(this, new PacketPlayOutEntityEquipment(getId(), slot, piece));
			}
		}
	}

}
