/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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