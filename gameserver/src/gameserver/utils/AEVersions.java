/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.utils;

import commons.utils.AEInfos;
import commons.versionning.Version;
import gameserver.GameServer;
import org.apache.log4j.Logger;

public class AEVersions
{
	private static final Logger log = Logger.getLogger(AEVersions.class);
	private static final Version commons = new Version(AEInfos.class);
	private static final Version gameserver	= new Version(GameServer.class);
	private static String getRevisionInfo(Version version)
	{
		return String.format("%-6s", version.getRevision());
	}

	private static String getDateInfo(Version version)
	{
		return String.format("[ %4s ]", version.getDate());
	}

	public static String[] getFullVersionInfo()
	{
		return new String[] { 
			"Commons Revision: " + getRevisionInfo(commons),
			"Commons Build Date: " + getDateInfo(commons), 
			"==================================================",
			"GS Revision: " + getRevisionInfo(gameserver),
			"GS Build Date: " + getDateInfo(gameserver),
			"=================================================="
		};
	}

	public static void printFullVersionInfo()
	{
		for(String line : getFullVersionInfo())
			log.info(line);
	}
}
