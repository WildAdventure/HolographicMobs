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

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import wild.api.command.SilentCommand;

import com.gmail.filoghost.chestcommands.api.ChestCommandsAPI;
import com.gmail.filoghost.holographicmobs.HolographicMobs;
import com.gmail.filoghost.holographicmobs.utils.GenericUtils;

@AllArgsConstructor
@Getter
public enum CommandType {

	PLAYER ("") {
		public void execute(Player executor, String command) {
			executor.chat("/" + command.replace("{player}", executor.getName()));
		}
	},
	CONSOLE ("console: ") {
		public void execute(Player executor, String command) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", executor.getName()));
		}
	},
	TELL ("tell: ") {
		public void execute(Player executor, String command) {
			executor.sendMessage(command.replace("{player}", executor.getName()));
		}
	},
	MENU ("menu: ") {
		public void execute(Player executor, String command) {
			if (!ChestCommandsAPI.openPluginMenu(executor, command)) {
				executor.sendMessage(ChatColor.RED + "Menu non trovato.");
			}
		}
	},
	SERVER ("server: ") {
		public void execute(Player executor, String command) {
			GenericUtils.connectToBungeeServer(executor, command);
		}
	},
	SILENT ("silent: ") {
		@SuppressWarnings("deprecation")
		public void execute(Player executor, String command) {
			if (HolographicMobs.isWildCommons()) {
				
				if (command.contains(" ")) {
					String[] split = command.split(" ");
					SilentCommand.execute(split[0], executor, Arrays.copyOfRange(split, 1, split.length));
				} else {
					SilentCommand.execute(command, executor, new String[0]);
				}
			}
		}
	};
	
	private String prefix;
	
	public abstract void execute(Player executor, String command);
	
}
