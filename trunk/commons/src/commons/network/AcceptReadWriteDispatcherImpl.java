/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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

package commons.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is implementation of <code>Dispatcher</code> that may accept connections, read and write data.
 * @see commons.network.Dispatcher
 * @see java.nio.channels.Selector
 */
public class AcceptReadWriteDispatcherImpl extends Dispatcher
{
	/**
	 * List of connections that should be closed by this <code>Dispatcher</code> as soon as possible.
	 */
	private final List<AConnection>	pendingClose	= new ArrayList<AConnection>();

	/**
	 * Constructor that accept <code>String</code> name and <code>DisconnectionThreadPool</code> dcPool as parameter.
	 * 
	 * @param name
	 * @param dcPool
	 * @throws IOException
	 * @see commons.network.DisconnectionThreadPool
	 */
	public AcceptReadWriteDispatcherImpl(String name, DisconnectionThreadPool dcPool) throws IOException
	{
		super(name, dcPool);
	}

	/**
	 * Process Pending Close connections and then dispatch <code>Selector</code> selected-key set.
	 * 
	 * @see commons.network.Dispatcher#dispatch()
	 */
	@Override
	void dispatch() throws IOException
	{
		int selected = selector.select();

		processPendingClose();

		if(selected != 0)
		{
			Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
			while(selectedKeys.hasNext())
			{
				SelectionKey key = selectedKeys.next();
				selectedKeys.remove();

				if(!key.isValid())
					continue;

				/** Check what event is available and deal with it */
				switch(key.readyOps())
				{
					case SelectionKey.OP_ACCEPT:
						this.accept(key);
						break;
					case SelectionKey.OP_READ:
						this.read(key);
						break;
					case SelectionKey.OP_WRITE:
						this.write(key);
						break;
					case SelectionKey.OP_READ | SelectionKey.OP_WRITE:
						this.read(key);
						if(key.isValid())
							this.write(key);
						break;
				}
			}
		}
	}

	/**
	 * Add connection to pendingClose list, so this connection will be closed by this <code>Dispatcher</code> as soon as
	 * possible.
	 * 
	 * @see commons.network.Dispatcher#closeConnection(commons.network.AConnection)
	 */
	@Override
	void closeConnection(AConnection con)
	{
		synchronized(pendingClose)
		{
			pendingClose.add(con);
		}
	}

	/**
	 * Process Pending Close connections.
	 */
	private void processPendingClose()
	{
		synchronized(pendingClose)
		{
			for(AConnection connection : pendingClose)
				closeConnectionImpl(connection);
			pendingClose.clear();
		}
	}
}
