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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.gmail.filoghost.holographicmobs.HolographicMobs;
import com.gmail.filoghost.holographicmobs.exception.UnreadableImageException;

public class FileUtils {

	public static List<String> readLines(String fileName) throws FileNotFoundException, IOException, Exception {
		return readLines( new File(HolographicMobs.getInstance().getDataFolder(), fileName));
	}
	
	public static List<String> readLines(File file) throws FileNotFoundException, IOException, Exception {

		BufferedReader br = null;

		try {

			List<String> lines = new ArrayList<String>();

			if (!file.exists()) {
				throw new FileNotFoundException();
			}

			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}

			return lines;

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
					HolographicMobs.getInstance().getLogger().severe("Unable to close file stream!");
				}
			}
		}
	}
	
	public static BufferedImage readImage(String fileName) throws FileNotFoundException, UnreadableImageException, IOException, Exception {

		File file = new File(HolographicMobs.getInstance().getDataFolder(), fileName);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
			
		BufferedImage image = ImageIO.read(file);
		
		if (image == null) {
			throw new UnreadableImageException();
		}
			
		return image;
	}
}
