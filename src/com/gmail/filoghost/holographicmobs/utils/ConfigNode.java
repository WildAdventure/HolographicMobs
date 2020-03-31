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

import org.bukkit.configuration.file.FileConfiguration;


public enum ConfigNode {

	VERTICAL_SPACING("vertical-spacing", 0.25),
	IMAGES_SYMBOL("images.symbol", "[x]"),
	TRANSPARENCY_SPACE("images.transparency.space", " [|] "),
	TRANSPARENCY_COLOR("images.transparency.color", "&7"),
	UPDATE_NOTIFICATION("update-notification", true),
	BUNGEE_REFRESH_SECONDS("bungee-refresh-seconds", 3),
	BUNGEE_ONLINE_FORMAT("bungee-online-format", "&aOnline"),
	BUNGEE_OFFLINE_FORMAT("bungee-offline-format", "&cOffline"),
	TIME_FORMAT("time-format", "H:mm");
	
	private String path;
	private Object value;
	
	private ConfigNode(String path, Object defaultValue) {
		this.path = path;
		value = defaultValue;
	}
	
	public String getPath() {
		return path;
	}
	
	public Object getDefault() {
		return value;
	}
	
	public String getString(FileConfiguration config) {
		return config.getString(path);
	}
	
	public boolean getBoolean(FileConfiguration config) {
		return config.getBoolean(path);
	}
	
	public double getDouble(FileConfiguration config) {
		return config.getDouble(path);
	}
	
	public int getInt(FileConfiguration config) {
		return config.getInt(path);
	}
}
