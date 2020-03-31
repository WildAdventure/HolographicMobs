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
package com.gmail.filoghost.holographicmobs.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicmobs.exception.CommandException;
import com.gmail.filoghost.holographicmobs.utils.ItemUtils;

public class CommandValidator {
	
	public static void notNull(Object obj, String string) throws CommandException {
		if (obj == null) {
			throw new CommandException(string);
		}
	}
	
	public static void isTrue(boolean b, String string) throws CommandException {
		if (!b) {
			throw new CommandException(string);
		}
	}

	public static int getInteger(String integer) throws CommandException {
		try {
			return Integer.parseInt(integer);
		} catch (NumberFormatException ex) {
			throw new CommandException("Invalid number: '" + integer + "'.");
		}
	}
	
	public static boolean isInteger(String integer) {
		try {
			Integer.parseInt(integer);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static Player getPlayerSender(CommandSender sender) throws CommandException {
		if (sender instanceof Player) {
			return (Player) sender;
		} else {
			throw new CommandException("You must be a player to use this command.");
		}
	}
	
	public static boolean isPlayerSender(CommandSender sender) {
		return sender instanceof Player;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack matchItemStack(String input) throws CommandException {

		input = input.replace(" ", ""); // Remove the spaces
		
		int dataValue = 0;
		if (input.contains(":")) {
			String[] split = input.split(":", 2);
			dataValue = getInteger(split[1]);
			input = split[0];
		}
		
		Material match = null;
		if (isInteger(input)) {
			int id = getInteger(input);
			for (Material mat : Material.values()) {
				if (mat.getId() == id) {
					match = mat;
					break;
				}
			}
		} else {
			match = ItemUtils.matchMaterial(input);
		}
		
		if (match == null || match == Material.AIR) {
			throw new CommandException("Invalid material: " + input);
		}
		
		return new ItemStack(match, 1, (short) dataValue);
	}
	
}
