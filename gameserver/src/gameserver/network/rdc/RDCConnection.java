/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.rdc;

import commons.ngen.network.Connection;
import gameserver.network.rdc.commands.RDCACommandTable;
import gameserver.network.rdc.commands.RDCCommand;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RDCConnection extends Connection
{
	private String result;
	
	public RDCConnection(SocketChannel sc, boolean debugEnabled)
	{
		super(sc, debugEnabled, Mode.TEXT);
	}
	
	@Override
	protected void onDisconnect()
	{
		// nothing to do
	}

	@Override
	protected void onInit()
	{
		// nothing to do, maybe send a welcome packet ?
	}

	@Override
	protected void onServerClose()
	{
		// nothing to do
	}

	@Override
	protected boolean processData(ByteBuffer data)
	{
		String query = new String ();
		String[] queryData;
		RDCCommand command;
		
		while (data.hasRemaining())
		{
			query += (char)data.get();
		}
		
		queryData = query.trim().split(" ");
		
		command = RDCACommandTable.getCommandByName(queryData[0]);
		
		if(command != null)
		{
			if(queryData.length > 1)
			{
				String[] commandArgs = new String[queryData.length - 1];
				for(int i=1; i < queryData.length; i++)
				{
					commandArgs[i-1] = queryData[i];
				}
				result = command.handleRequest(commandArgs);
			}
			else
			{
				result = command.handleRequest(new String[0]);
			}
		}
		
		if (result != null && result.length() > 0)
			enableWriteInterest();
		else
			return false;
		
		return true;
	}

	@Override
	protected boolean writeData(ByteBuffer data)
	{
		data.asCharBuffer().put(result);
		data.limit(result.length()*2);
		
		pendingClose = true;
		
		return true;
	}

}
