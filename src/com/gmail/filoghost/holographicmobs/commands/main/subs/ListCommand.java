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

import com.gmail.filoghost.holographicmobs.commands.CommandValidator;
import com.gmail.filoghost.holographicmobs.commands.Messages;
import com.gmail.filoghost.holographicmobs.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicmobs.exception.CommandException;
import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.object.HologramManager;
import com.gmail.filoghost.holographicmobs.utils.Format;

public class ListCommand extends HologramSubCommand {

	private static final int HOLOGRAMS_PER_PAGE = 10;

	public ListCommand() {
		super("list");
		setPermission(Messages.BASE_PERM + "list");
	}

	@Override
	public String getPossibleArguments() {
		return "[page]";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {

		int page = args.length > 0 ? CommandValidator.getInteger(args[0]) : 1;

		if (page < 1) {
			throw new CommandException("Page number must be 1 or greater.");
		}

		int totalPages = HologramManager.pluginHologramsSize() / HOLOGRAMS_PER_PAGE;
		if (HologramManager.pluginHologramsSize() % HOLOGRAMS_PER_PAGE != 0) {
			totalPages++;
		}
		
		
		if (HologramManager.pluginHologramsSize() == 0) {
			throw new CommandException("There are no hologram mobs. Create one with /holomobs create.");
		}

		sender.sendMessage("");
		sender.sendMessage(Format.formatTitle("Hologram mobs list §f(Page " + page + " of " + totalPages + ")"));
		int fromIndex = (page - 1) * HOLOGRAMS_PER_PAGE;
		int toIndex = fromIndex + HOLOGRAMS_PER_PAGE;

		for (int i = fromIndex; i < toIndex; i++) {
			if (i < HologramManager.pluginHologramsSize()) {
				HologramMob hologram = HologramManager.getPluginHologram(i);
				sender.sendMessage("§3- §f'" + hologram.getName() + "' §7at x: " + hologram.getBlockX() + ", y: " + hologram.getBlockY() + ", z: " + hologram.getBlockZ() + ", world: \"" + hologram.getWorld().getName() + "\")");
			}
		}
		if (page < totalPages) {
			sender.sendMessage("§f[Tip] §7See the next page with /holomobs list " + (page + 1));
		}

	}
}
