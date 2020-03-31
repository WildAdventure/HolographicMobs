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

import java.util.Arrays;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicmobs.commands.CommandValidator;
import com.gmail.filoghost.holographicmobs.commands.Messages;
import com.gmail.filoghost.holographicmobs.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicmobs.database.HologramDatabase;
import com.gmail.filoghost.holographicmobs.exception.CommandException;
import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.object.HologramLivingEntity;
import com.gmail.filoghost.holographicmobs.object.HologramManager;
import com.gmail.filoghost.holographicmobs.utils.Format;
import com.gmail.filoghost.holographicmobs.utils.StringUtils;

public class SetnameCommand extends HologramSubCommand {

	public SetnameCommand() {
		super("setname");
		setPermission(Messages.BASE_PERM + "setname");
	}

	@Override
	public String getPossibleArguments() {
		return "<hologramName> <text>";
	}

	@Override
	public int getMinimumArguments() {
		return 2;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		HologramMob hologram = HologramManager.getPluginHologram(args[0].toLowerCase());
		CommandValidator.notNull(hologram, Messages.NO_SUCH_HOLOGRAM);

		CommandValidator.isTrue(hologram instanceof HologramLivingEntity, "That hologram cannot have a name!");
		
		HologramLivingEntity livingHologram = (HologramLivingEntity) hologram;
		
		String name = StringUtils.toReadableFormat(StringUtils.join(args, " ", 1, args.length));
		
		if (name.equalsIgnoreCase("{empty}")) {
			livingHologram.setCustomName(null);
		} else {
			livingHologram.setCustomName(Arrays.asList(Pattern.compile("\\n", Pattern.LITERAL).split(name)));
		}

		if (!hologram.update()) {
			sender.sendMessage(Messages.FAILED_TO_SPAWN_HERE);
		}
			
		HologramDatabase.saveHologram(hologram);
		HologramDatabase.trySaveToDisk();
		sender.sendMessage(Format.HIGHLIGHT + "Name set!");
	}

}
