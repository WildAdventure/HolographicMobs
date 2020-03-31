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

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.object.types.HologramBlaze;
import com.gmail.filoghost.holographicmobs.object.types.HologramSkeleton;
import com.gmail.filoghost.holographicmobs.object.types.HologramSlime;
import com.gmail.filoghost.holographicmobs.object.types.HologramVillager;
import com.gmail.filoghost.holographicmobs.object.types.HologramZombie;

@AllArgsConstructor
public enum MobType {
	
	VILLAGER(HologramVillager.class),
	BLAZE(HologramBlaze.class),
	ZOMBIE(HologramZombie.class),
	SKELETON(HologramSkeleton.class),
	SLIME (HologramSlime.class);
	
	@Getter
	private Class<? extends HologramMob> hologramClass;
	
	public static MobType match(String input) {
		
		input = input.toLowerCase().replace("_", "").replace(" ", "");
		
		if (input == null) return null;
		for (MobType type : values()) {
			if (type.toString().toLowerCase().equals(input)) {
				return type;
			}
		}
		
		return null;
	}
	
	public static MobType match(Class<?> clazz) {
		for (MobType type : values()) {
			if (type.getHologramClass() == clazz) {
				return type;
			}
		}
		
		return null;
	}
	
}
