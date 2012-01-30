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

package gameserver.network.rdc;

import commons.ngen.network.Connection;
import gameserver.network.rdc.commands.RDCACommandTable;
import gameserver.network.rdc.commands.RDCCommand;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.regex.Pattern;

public class RDCConnection extends Connection
{
    private static final Pattern COMPILE = Pattern.compile(" ");
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
		
		queryData = COMPILE.split(query.trim());
		
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