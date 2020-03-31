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
package com.gmail.filoghost.holographicmobs.commands.main.subs;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicmobs.commands.CommandValidator;
import com.gmail.filoghost.holographicmobs.commands.Messages;
import com.gmail.filoghost.holographicmobs.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicmobs.database.HologramDatabase;
import com.gmail.filoghost.holographicmobs.exception.CommandException;
import com.gmail.filoghost.holographicmobs.object.HologramEquippable;
import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.object.HologramManager;
import com.gmail.filoghost.holographicmobs.utils.Format;
import com.gmail.filoghost.holographicmobs.utils.StringUtils;

public class SetequipCommand extends HologramSubCommand {

	public SetequipCommand() {
		super("setequip");
		setPermission(Messages.BASE_PERM + "setequip");
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName> <slot> <item>";
	}

	@Override
	public int getMinimumArguments() {
		return 3;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		HologramMob hologram = HologramManager.getPluginHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);
		CommandValidator.isTrue(hologram instanceof HologramEquippable, "This is not equippable!");
		
		HologramEquippable hologramEquip = (HologramEquippable) hologram;
		ItemStack item = null;
		
		
		String itemString = StringUtils.join(args, " ", 2, args.length).toLowerCase();
		
		if (itemString.equals("empty") || itemString.equals("{empty}") || itemString.equals("air") || itemString.equals("0") || itemString.equals("null")) {
			
			item = null; // To remove it
			
		} else {
		
			boolean glow = false;
			if (itemString.endsWith("glow")) {
				itemString = itemString.replace("glow", "");
				glow = true;
			}
			
			itemString = itemString.replace(" ", "");
			
			short data = 0;
			
			if (itemString.contains(":")) {
				String dataString = itemString.split(":", 2)[1];
				itemString = itemString.split(":", 2)[0];
				
				data = (short) CommandValidator.getInteger(dataString);
			}
			
			Material mat = null;
			
			itemString = itemString.replace("_", "");
			
			for (Material possible : Material.values()) {
				if (possible.toString().toLowerCase().replace("_", "").equals(itemString)) {
					mat = possible;
				}
			}
			
			CommandValidator.notNull(mat, "Material not found.");
			CommandValidator.isTrue(mat != Material.AIR, "Material cannot be air.");
			
			item = new ItemStack(mat, 1, data);
			if (glow) {
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			}
		
		}
		
		switch (args[1].toLowerCase()) {
			case "hand":
				hologramEquip.setItemInHand(item);
				break;
			case "helmet":
			case "head":
				hologramEquip.setHelmet(item);
				break;
			case "chest":
			case "chestplate":
				hologramEquip.setChestplate(item);
				break;
			case "leggings":
			case "legs":
				hologramEquip.setLeggings(item);
				break;
			case "boots":
				hologramEquip.setBoots(item);
				break;
			default:
				throw new CommandException("Valid slots: hand, helmet, chest, legs, boots.");
		}
		
		
		if (!hologram.update()) {
			sender.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
		}
			
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Format.HIGHLIGHT + "Equipment set!");
	}

}
