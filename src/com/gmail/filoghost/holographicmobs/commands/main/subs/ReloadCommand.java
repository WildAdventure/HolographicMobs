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

import java.util.Set;

import org.bukkit.command.CommandSender;

import com.gmail.filoghost.holographicmobs.HolographicMobs;
import com.gmail.filoghost.holographicmobs.commands.Messages;
import com.gmail.filoghost.holographicmobs.commands.main.HologramSubCommand;
import com.gmail.filoghost.holographicmobs.database.HologramDatabase;
import com.gmail.filoghost.holographicmobs.exception.CommandException;
import com.gmail.filoghost.holographicmobs.exception.HologramNotFoundException;
import com.gmail.filoghost.holographicmobs.exception.InvalidLocationException;
import com.gmail.filoghost.holographicmobs.exception.WorldNotFoundException;
import com.gmail.filoghost.holographicmobs.object.HologramMob;
import com.gmail.filoghost.holographicmobs.object.HologramManager;
import com.gmail.filoghost.holographicmobs.utils.Format;

public class ReloadCommand extends HologramSubCommand {

	public ReloadCommand() {
		super("reload");
		setPermission(Messages.BASE_PERM + "reload");
	}

	@Override
	public String getPossibleArguments() {
		return "";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public void execute(CommandSender sender, String[] args) throws CommandException {
		try {
			
			long startMillis = System.currentTimeMillis();
			
			HolographicMobs.getInstance().reloadConfig();
			
			HologramDatabase.initialize();
			HologramManager.clearPluginHolograms();

			Set<String> savedHolograms = HologramDatabase.getHolograms();
			if (savedHolograms != null && savedHolograms.size() > 0) {
				for (String singleSavedHologram : savedHolograms) {
					try {
						HologramMob singleHologramEntity = HologramDatabase.loadHologram(singleSavedHologram);
						HologramManager.addPluginHologram(singleHologramEntity);
					} catch (HologramNotFoundException e) {
						Format.sendWarning(sender, "Hologram mob '" + singleSavedHologram + "' not found, skipping it.");
					} catch (InvalidLocationException e) {
						Format.sendWarning(sender, "Hologram mob '" + singleSavedHologram + "' has an invalid location format.");
					} catch (WorldNotFoundException e) {
						Format.sendWarning(sender, "Hologram mob '" + singleSavedHologram + "' was in the world '" + e.getMessage() + "' but it wasn't loaded.");
					}
				}
			}
			
			for (HologramMob hologram : HologramManager.getPluginHolograms()) {
				if (!hologram.update()) {
					sender.sendMessage("§c[ ! ] §7Unable to spawn entities for the hologram '" + hologram.getName() + "'.");
				}
			}
			
			long endMillis = System.currentTimeMillis();
			
			sender.sendMessage("§bConfiguration reloaded successfully in " + (endMillis - startMillis) + "ms!");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CommandException("Exception while reloading the configuration. Please look the console.");
		}
	}
}
