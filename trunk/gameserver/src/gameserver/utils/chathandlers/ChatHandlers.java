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


package gameserver.utils.chathandlers;

import commons.scripting.scriptmanager.ScriptManager;
import gameserver.GameServerError;
import javolution.util.FastList;
import java.io.File;

/**
 * This class is managing a list of all chat handlers.
 * 
 * @see ChatHandler
 */
public class ChatHandlers
{
	private FastList<ChatHandler>	handlers;

	public static final File CHAT_DESCRIPTOR_FILE = new File("./data/scripts/system/handlers.xml");
	
	private ScriptManager sm;

	public static final ChatHandlers getInstance()
	{
		return SingletonHolder.instance;
	}

	private ChatHandlers()
	{
		handlers	= new FastList<ChatHandler>();
		sm = new ScriptManager();
		createChatHandlers();
	}

	void addChatHandler(ChatHandler ch)
	{
		handlers.add(ch);
	}
	/**
	 * @return the handlers
	 */
	public FastList<ChatHandler> getHandlers()
	{
		return handlers;
	}

	/**
	 * Creates and return object of {@link ChatHandlers} class
	 * 
	 * @return ChatHandlers
	 */
	private void createChatHandlers()
	{
		final CommandChatHandler adminCCH = new CommandChatHandler();
		addChatHandler(adminCCH);

		// set global loader
		sm.setGlobalClassListener(new ChatHandlersLoader(adminCCH));

		try
		{
			sm.load(CHAT_DESCRIPTOR_FILE);
		}
		catch (Exception e)
		{
			throw new GameServerError("Can't initialize chat handlers.", e);
		}
	}

	public void reloadChatHandlers()
	{
		ScriptManager tmpSM;
		final CommandChatHandler adminCCH;
		try
		{
			tmpSM = new ScriptManager();
			adminCCH = new CommandChatHandler();
			tmpSM.setGlobalClassListener(new ChatHandlersLoader(adminCCH));
			
			tmpSM.load(CHAT_DESCRIPTOR_FILE);
		}
		catch(Exception e)
		{
			throw new GameServerError("Can't reload chat handlers.", e);
		}
		
		if(tmpSM != null && adminCCH != null)
		{
			sm.shutdown();
			sm = null;
			handlers.clear();
			sm = tmpSM;
			addChatHandler(adminCCH);
		}
		
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ChatHandlers instance = new ChatHandlers();
	}
}
