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

import lombok.Getter;
import lombok.Setter;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public abstract class HologramEquippable extends HologramLivingEntity {

	@Getter @Setter
	protected ItemStack itemInHand;
	
	@Getter @Setter
	protected ItemStack itemInOffHand;
	
	@Getter @Setter
	protected ItemStack helmet;
	
	@Getter @Setter
	protected ItemStack chestplate;
	
	@Getter @Setter
	protected ItemStack leggings;
	
	@Getter @Setter
	protected ItemStack boots;
	
	protected HologramEquippable(String name, Location source) {
		super(name, source);
	}
	
	@Override
	public void readData(ConfigurationSection section) {
		this.itemInHand = sanitizeItem(section.get("equip.hand"));
		this.itemInOffHand = sanitizeItem(section.get("equip.offhand"));
		this.helmet = sanitizeItem(section.get("equip.helmet"));
		this.chestplate = sanitizeItem(section.get("equip.chestplate"));
		this.leggings = sanitizeItem(section.get("equip.leggings"));
		this.boots = sanitizeItem(section.get("equip.boots"));
		super.readData(section);
	}

	@Override
	public void saveData(ConfigurationSection section) {
		if (itemInHand != null) {
			section.set("equip.hand", itemInHand);
		}
		if (itemInOffHand != null) {
			section.set("equip.offhand", itemInOffHand);
		}
		if (helmet != null) {
			section.set("equip.helmet", helmet);
		}
		if (chestplate != null) {
			section.set("equip.chestplate", chestplate);
		}
		if (leggings != null) {
			section.set("equip.leggings", leggings);
		}
		if (boots != null) {
			section.set("equip.boots", boots);
		}
		super.saveData(section);
	}
	
	private ItemStack sanitizeItem(Object o) {
		if (o == null) {
			return null;
		}
		
		if (o instanceof ItemStack) {
			return (ItemStack) o;
		}
		
		return null;
	}
	
}
