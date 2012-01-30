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
 
package gameserver.configs.network;

import commons.configuration.Property;
import java.net.InetSocketAddress;

public class NetworkConfig
{
	@Property(key = "gameserver.network.client.port", defaultValue = "7777")
	public static int GAME_PORT;
	
	@Property(key = "gameserver.network.client.host", defaultValue = "*")
	public static String GAME_BIND_ADDRESS;
	
	@Property(key = "gameserver.network.rdc.client.host", defaultValue = "*")
	public static String RDC_BIND_ADDRESS;
	
	@Property(key = "gameserver.network.rdc.client.port", defaultValue = "732")
	public static int RDC_BIND_PORT;
	
	@Property(key = "gameserver.network.geoserver.address", defaultValue = "localhost:5550")
	public static InetSocketAddress	GEOSERVER_ADDRESS;
	
	@Property(key = "gameserver.network.geoserver.password", defaultValue = "password")
	public static String GEOSERVER_PASSWORD;

	@Property(key = "gameserver.network.client.maxplayers", defaultValue = "100")
	public static int MAX_ONLINE_PLAYERS;

	@Property(key = "gameserver.network.client.requiredlevel", defaultValue = "0")
	public static int REQUIRED_ACCESS;

	@Property(key = "gameserver.network.login.address", defaultValue = "localhost:9014")
	public static InetSocketAddress	LOGIN_ADDRESS;

	@Property(key = "gameserver.network.chat.address", defaultValue = "localhost:9021")
	public static InetSocketAddress	CHAT_ADDRESS;

	@Property(key = "gameserver.network.chat.password", defaultValue = "")
	public static String CHAT_PASSWORD;

	@Property(key = "gameserver.network.login.gsid", defaultValue = "0")
	public static int GAMESERVER_ID;

	@Property(key = "gameserver.network.login.password", defaultValue = "")
	public static String LOGIN_PASSWORD;

	@Property(key = "gameserver.network.nio.debug", defaultValue = "false")
	public static boolean NIO_DEBUG;

	@Property(key = "gameserver.network.nio.threads.read", defaultValue = "1")
	public static int NIO_READ_THREADS;

	@Property(key = "gameserver.network.nio.threads.read.retries", defaultValue = "8")
	public static int NIO_READ_RETRIES;

	@Property(key = "gameserver.network.nio.threads.write", defaultValue = "1")
	public static int NIO_WRITE_THREADS;

	@Property(key = "gameserver.network.nio.threads.write.retries", defaultValue = "8")
	public static int NIO_WRITE_RETRIES;
	
	@Property(key = "gameserver.network.display.unknownpackets", defaultValue = "false")
	public static boolean DISPLAY_UNKNOWNPACKETS;

	@Property(key = "gameserver.network.nio.threads.workers.enable", defaultValue = "false")
	public static boolean NIO_ENABLE_WORKERS;

	@Property(key = "gameserver.network.nio.threads.workers", defaultValue = "1")
	public static int NIO_WORKER_THREADS;

	@Property(key = "gameserver.network.nio.threads.workers.buffers", defaultValue = "16")
	public static int NIO_WORKER_THREAD_BUFFERS;
}