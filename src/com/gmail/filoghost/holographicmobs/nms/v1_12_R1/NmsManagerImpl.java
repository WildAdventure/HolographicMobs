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
package com.gmail.filoghost.holographicmobs.nms.v1_12_R1;

import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.gmail.filoghost.holographicdisplays.util.reflection.ReflectField;
import com.gmail.filoghost.holographicmobs.MobType;
import com.gmail.filoghost.holographicmobs.exception.SpawnFailedException;
import com.gmail.filoghost.holographicmobs.nms.interfaces.NMSHolographicMob;
import com.gmail.filoghost.holographicmobs.nms.interfaces.NmsManager;
import com.gmail.filoghost.holographicmobs.nms.v1_12_R1.blaze.EntityHolographicBlaze;
import com.gmail.filoghost.holographicmobs.nms.v1_12_R1.skeleton.EntityHolographicSkeleton;
import com.gmail.filoghost.holographicmobs.nms.v1_12_R1.slime.EntityHolographicSlime;
import com.gmail.filoghost.holographicmobs.nms.v1_12_R1.villager.EntityHolographicVillager;
import com.gmail.filoghost.holographicmobs.nms.v1_12_R1.zombie.EntityHolographicZombie;
import com.gmail.filoghost.holographicmobs.object.HologramMob;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityTypes;
import net.minecraft.server.v1_12_R1.RegistryID;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
import net.minecraft.server.v1_12_R1.WorldServer;

public class NmsManagerImpl implements NmsManager {
	
	private static final ReflectField<RegistryID<Class<? extends Entity>>> REGISTRY_ID_FIELD = new ReflectField<>(RegistryMaterials.class, "a");
	private static final ReflectField<Object[]> ID_TO_CLASS_MAP_FIELD = new ReflectField<>(RegistryID.class, "d");

	@Override
	public void registerCustomEntities() throws Exception {
		registerCustomEntity(EntityHolographicVillager.class, 120);
		registerCustomEntity(EntityHolographicBlaze.class, 61);
		registerCustomEntity(EntityHolographicZombie.class, 54);
		registerCustomEntity(EntityHolographicSlime.class, 55);
		registerCustomEntity(EntityHolographicSkeleton.class, 51);
	}
	
	public void registerCustomEntity(Class<? extends Entity> entityClass, int id) throws Exception {
		// Use reflection to get the RegistryID of entities.
		RegistryID<Class<? extends Entity>> registryID = REGISTRY_ID_FIELD.get(EntityTypes.b);
		Object[] idToClassMap = ID_TO_CLASS_MAP_FIELD.get(registryID);
		
		// Save the the ID -> entity class mapping before the registration.
		Object oldValue = idToClassMap[id];

		// Register the entity class.
		registryID.a(entityClass, id);

		// Restore the ID -> entity class mapping.
		idToClassMap[id] = oldValue;
	}
	
	@Override
	public NMSHolographicMob spawnHologramMob(org.bukkit.World world, double x, double y, double z, float yaw, float pitch, MobType type, HologramMob parent) throws SpawnFailedException {
		WorldServer nmsWorld = ((CraftWorld) world).getHandle();
		
		NMSHolographicMob nmsEntity = null;
		
		switch (type) {
			case VILLAGER:
				nmsEntity = new EntityHolographicVillager(nmsWorld, parent);
				break;
			case BLAZE:
				nmsEntity = new EntityHolographicBlaze(nmsWorld, parent);
				break;
			case ZOMBIE:
				nmsEntity = new EntityHolographicZombie(nmsWorld, parent);
				break;
			case SKELETON:
				nmsEntity = new EntityHolographicSkeleton(nmsWorld, parent);
				break;
			case SLIME:
				nmsEntity = new EntityHolographicSlime(nmsWorld, parent);
				break;
		}

		nmsEntity.setLocationNMS(x, y, z, yaw, pitch);
		if (!nmsWorld.addEntity((Entity) nmsEntity, SpawnReason.CUSTOM)) {
			throw new SpawnFailedException();
		}
		return nmsEntity;
	}

	@Override
	public HologramMob getParentHologramBase(org.bukkit.entity.Entity bukkitEntity) {
		Entity handle = ((CraftEntity) bukkitEntity).getHandle();
		
		if (handle instanceof NMSHolographicMob) {
			return ((NMSHolographicMob) handle).getParent();
		}
		
		return null;
	}
	
}
