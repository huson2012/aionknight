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
import java.util.Iterator;

/**
 * This is implementation of <code>Dispatcher</code> that may only accept connections.
 * @see commons.network.Dispatcher
 * @see java.nio.channels.Selector
 */
public class AcceptDispatcherImpl extends Dispatcher
{
	/**
	 * Constructor that accept <code>String</code> name as parameter.
	 * 
	 * @param name
	 * @throws IOException
	 */
	public AcceptDispatcherImpl(String name) throws IOException
	{
		super(name, null);
	}

	/**
	 * Dispatch <code>Selector</code> selected-key set.
	 * 
	 * @see commons.network.Dispatcher#dispatch()
	 */
	@Override
	void dispatch() throws IOException
	{
		if(selector.select() != 0)
		{
			Iterator<SelectionKey> selectedKeys = this.selector.selectedKeys().iterator();
			while(selectedKeys.hasNext())
			{
				SelectionKey key = selectedKeys.next();
				selectedKeys.remove();

				if(key.isValid())
					accept(key);
			}
		}
	}

	/**
	 * This method should never be called on this implementation of <code>Dispatcher</code>
	 * 
	 * @throws UnsupportedOperationException
	 *            always!
	 * @see commons.network.Dispatcher#closeConnection(commons.network.AConnection)
	 */
	@Override
	void closeConnection(AConnection con)
	{
		throw new UnsupportedOperationException("This method should never be called!");
	}
}