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
package com.gmail.filoghost.holographicmobs.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

import com.gmail.filoghost.holographicmobs.HolographicMobs;

public class GenericUtils {

	public static <E> List<E> createList(E object) {
		if (object == null) {
			return null;
		}
		
		List<E> list = new ArrayList<E>();
		list.add(object);
		return list;
	}
	
	public static double square(double d) {
		return d * d;
	}
	
	public static boolean connectToBungeeServer(Player player, String server) {
		
		try {
			
			Messenger messenger = Bukkit.getMessenger();
			if (!messenger.isOutgoingChannelRegistered(HolographicMobs.getInstance(), "BungeeCord")) {
				messenger.registerOutgoingPluginChannel(HolographicMobs.getInstance(), "BungeeCord");
			}
			
			if (server.length() == 0) {
				player.sendMessage("§cTarget server was \"\" (empty string) cannot connect to it.");
				return false;
			}
		
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(byteArray);
		 
			out.writeUTF("Connect");
			out.writeUTF(server); // Target Server
		    
			player.sendPluginMessage(HolographicMobs.getInstance(), "BungeeCord", byteArray.toByteArray());
			
		} catch (Exception ex) {
			ex.printStackTrace();
			player.sendMessage("§cErrore interno.");
			return false;
		}
		
		return true;
	}
}
