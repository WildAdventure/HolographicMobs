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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.ChatColor.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicmobs.HolographicMobs;
import com.gmail.filoghost.holographicmobs.commands.main.subs.*;
import com.gmail.filoghost.holographicmobs.exception.CommandException;
import com.gmail.filoghost.holographicmobs.utils.Format;

public class HologramsCommandHandler implements CommandExecutor {

	private List<HologramSubCommand> subCommands;
	
	public HologramsCommandHandler() {
		subCommands = new ArrayList<HologramSubCommand>();
		
		registerSubCommand(new SetnameCommand());
		registerSubCommand(new CreateCommand());
		registerSubCommand(new DeleteCommand());
		registerSubCommand(new ListCommand());
		registerSubCommand(new TeleportCommand());
		registerSubCommand(new MovehereCommand());
		registerSubCommand(new AlignCommand());
		registerSubCommand(new SetnameCommand());
		registerSubCommand(new SetcommandCommand());
		registerSubCommand(new SetequipCommand());
		registerSubCommand(new SetprofessionCommand());
		registerSubCommand(new SetsizeCommand());
		registerSubCommand(new ReloadCommand());
		
		registerSubCommand(new HelpCommand());
	}
	
	public void registerSubCommand(HologramSubCommand subCommand) {
		subCommands.add(subCommand);
	}
	
	public List<HologramSubCommand> getSubCommands() {
		return new ArrayList<HologramSubCommand>(subCommands);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 0) {
			sender.sendMessage("");
			sender.sendMessage(Format.formatTitle("Holographic Mobs"));
			sender.sendMessage(Format.HIGHLIGHT + "Version: §7" + HolographicMobs.getInstance().getDescription().getVersion());
			sender.sendMessage(Format.HIGHLIGHT + "Developer: §7filoghost");
			sender.sendMessage(Format.HIGHLIGHT + "Commands: §7/" + label + " help");
			return true;
		}
		
		for (HologramSubCommand subCommand : subCommands) {
			if (subCommand.isValidTrigger(args[0])) {
				
				if (!subCommand.hasPermission(sender)) {
					sender.sendMessage(RED + "You don't have permission.");
					return true;
				}
				
				if (args.length - 1 >= subCommand.getMinimumArguments()) {
					try {
						subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
					} catch (CommandException e) {
						sender.sendMessage(RED + e.getMessage());
					}
				} else {
					sender.sendMessage(RED + "Usage: /" + label + " " + subCommand.getName() + " " + subCommand.getPossibleArguments());
				}
				
				return true;
			}
		}
		
		sender.sendMessage(RED + "Unknown sub-command. Type \"/holomobs help\" for a list of commands.");
		return true;
	}
}
