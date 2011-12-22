/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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
