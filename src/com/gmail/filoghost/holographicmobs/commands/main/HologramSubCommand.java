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
package com.gmail.filoghost.holographicmobs.commands.main;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicmobs.exception.CommandException;

public abstract class HologramSubCommand {
	
	private String name;
	private String permission;
	private String[] aliases;
	
	public HologramSubCommand(String name) {
		this(name, new String[0]);
	}
	
	public HologramSubCommand(String name, String... aliases) {
		this.name = name;
		this.aliases = aliases;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
	public final boolean hasPermission(CommandSender sender) {
		if (permission == null) return true;
		return sender.hasPermission(permission);
	}
	
	public abstract String getPossibleArguments();

	public abstract int getMinimumArguments();

	public abstract void execute(CommandSender sender, String[] args) throws CommandException;

	public final boolean isValidTrigger(String name) {
		if (this.name.equalsIgnoreCase(name)) {
			return true;
		}
		
		if (aliases != null) {
			for (String alias : aliases) {
				if (alias.equalsIgnoreCase(name)) {
					return true;
				}
			}
		}
		
		return false;
	}

}
