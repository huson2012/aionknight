/**
 * This file is part of Aion-Knight Dev. Team <http://aion-knight.ru>.
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
 
package chatserver;

import org.apache.log4j.Logger;
import chatserver.configs.Config;
import chatserver.network.netty.NettyServer;
import chatserver.utils.Util;
import commons.services.LoggingService;
import commons.utils.AEInfos;

public class ChatServer
{
	private static final Logger log = Logger.getLogger(ChatServer.class);
	
    public static void main(String[] args)
    {
    	long start = System.currentTimeMillis();
		
        LoggingService.init();
		log.info("Logging Initialized.");
		
        Util.printSection("Config Manager");
		Config.load();
        
		Util.printSection("Netty Manager");
        new NettyServer();	

        Util.printSection("System Manager");
        AEInfos.printAllInfos();
        
        Util.printSection("Log Manager");
        log.info("Total Boot Time: " + (System.currentTimeMillis() - start) / 1000 + " sec.");
    }
}