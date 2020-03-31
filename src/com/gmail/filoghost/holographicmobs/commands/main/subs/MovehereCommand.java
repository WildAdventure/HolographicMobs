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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicmobs.commands.CommandValidator;
import com.gmail.filoghost.holographicmobs.commands.Messages;
import com.gmail.filoghost.holographicmobs.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicmobs.database.HologramDatabase;
import com.gmail.filoghost.holographicmobs.exception.CommandException;
import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.object.HologramManager;
import com.gmail.filoghost.holographicmobs.utils.Format;

public class MovehereCommand extends HologramSubCommand {


	public MovehereCommand() {
		super("movehere");
		setPermission(Messages.BASE_PERM + "movehere");
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName>";
	}

	@Override
	public int getMinimumArguments() {
		return 1;
	}


	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		Player player = CommandValidator.getPlayerSender(sender);
		HologramMob hologram = HologramManager.getPluginHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);
		
		hologram.setLocation(player.getLocation());
		if (!hologram.update()) {
			player.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
		}
		
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		player.sendMessage(Format.HIGHLIGHT + "You moved the hologram mob '" + hologram.getName() + "' near to you.");
	}
}
