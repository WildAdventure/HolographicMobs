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
package com.gmail.filoghost.holographicmobs.api;

import lombok.NonNull;

import org.bukkit.Location;

import com.gmail.filoghost.holographicmobs.object.HologramManager;
import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.object.types.HologramBlaze;
import com.gmail.filoghost.holographicmobs.object.types.HologramSkeleton;
import com.gmail.filoghost.holographicmobs.object.types.HologramSlime;
import com.gmail.filoghost.holographicmobs.object.types.HologramVillager;
import com.gmail.filoghost.holographicmobs.object.types.HologramZombie;

public class HolographicMobsAPI {
	
	@SuppressWarnings("unchecked")
	public static <T extends HologramMob> T spawn(@NonNull Location loc, Class<T> clazz) {
		
		HologramMob mob = null;
		
		if (clazz == HologramZombie.class) {
			mob = spawnZombie(loc);
		} else if (clazz == HologramSkeleton.class) {
			mob = spawnSkeleton(loc);
		} else if (clazz == HologramVillager.class) {
			mob = spawnVillager(loc);
		} else if (clazz == HologramBlaze.class) {
			mob = spawnBlaze(loc);
		} else if (clazz == HologramSlime.class) {
			mob = spawnSlime(loc);
		}
		
		if (mob != null) {
			return (T) mob;
		}
		
		throw new IllegalArgumentException("Invalid class");
	}

	public static HologramBlaze spawnBlaze(@NonNull Location loc) {
		HologramBlaze hologram = new HologramBlaze("{API-Hologram}", loc);
		HologramManager.addAPIHologram(hologram);
		hologram.update();
		return hologram;
	}
	
	public static HologramSlime spawnSlime(@NonNull Location loc) {
		HologramSlime hologram = new HologramSlime("{API-Hologram}", loc);
		HologramManager.addAPIHologram(hologram);
		hologram.update();
		return hologram;
	}
	
	public static HologramZombie spawnZombie(@NonNull Location loc) {
		HologramZombie hologram = new HologramZombie("{API-Hologram}", loc);
		HologramManager.addAPIHologram(hologram);
		hologram.update();
		return hologram;
	}
	
	public static HologramSkeleton spawnSkeleton(@NonNull Location loc) {
		HologramSkeleton hologram = new HologramSkeleton("{API-Hologram}", loc);
		HologramManager.addAPIHologram(hologram);
		hologram.update();
		return hologram;
	}
	
	public static HologramVillager spawnVillager(@NonNull Location loc) {
		HologramVillager hologram = new HologramVillager("{API-Hologram}", loc);
		HologramManager.addAPIHologram(hologram);
		hologram.update();
		return hologram;
	}
	
}
