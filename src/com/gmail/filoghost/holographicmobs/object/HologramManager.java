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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.Chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * This class is only used by the plugin itself. Other plugins should just use the API.
 */
public class HologramManager {

	private static List<HologramMob> pluginHolograms = Lists.newArrayList();
	private static Set<HologramMob> apiHolograms = Sets.newHashSet();
	
	public static void addPluginHologram(HologramMob hologram) {
		pluginHolograms.add(hologram);
	}
	
	public static void addAPIHologram(HologramMob hologram) {
		apiHolograms.add(hologram);
	}
	
	public static List<HologramMob> getPluginHolograms() {
		return new ArrayList<HologramMob>(pluginHolograms);
	}
	
	public static Collection<HologramMob> getAPIHolograms() {
		return pluginHolograms;
	}
	
	public static HologramMob getPluginHologram(String name) {
		for (HologramMob hologram : pluginHolograms) {
			if (hologram.getName().equals(name)) {
				return hologram;
			}
		}
		return null;
	}
	
	public static boolean isTracked(HologramMob hologram) {
		return pluginHolograms.contains(hologram) || apiHolograms.contains(hologram);
	}
	
	public static boolean isExistingPluginHologram(String name) {
		return (getPluginHologram(name) != null);
	}

	public static void onChunkLoad(Chunk chunk) {
		 // Load the holograms in that chunk.
		for (HologramMob hologram : pluginHolograms) {
			if (hologram.isInChunk(chunk)) {
				hologram.forceUpdate();
			}
		}
		for (HologramMob hologram : apiHolograms) {
			if (hologram.isInChunk(chunk)) {
				hologram.forceUpdate();
			}
		}
	}

	public static void remove(HologramMob hologram) {
		pluginHolograms.remove(hologram);
		apiHolograms.remove(hologram);
	}

	public static int pluginHologramsSize() {
		return pluginHolograms.size();
	}

	public static HologramMob getPluginHologram(int i) {
		return pluginHolograms.get(i);
	}
	
	public static void clearPluginHolograms() {
		for (HologramMob hologram : pluginHolograms) {
			hologram.despawnEntity();
		}
		pluginHolograms.clear();
	}
}
