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

/**
 * This class represents ServerCfg for configuring NioServer
 * @see commons.network.ConnectionFactory
 * @see commons.network.AConnection
 */
public class ServerCfg
{
	/**
	 * Host Name on wich we will listen for connections.
	 */
	public final String	hostName;
	/**
	 * Port number on wich we will listen for connections.
	 */
	public final int port;
	/**
	 * Connection Name only for logging purposes.
	 */
	public final String	connectionName;
	/**
	 * <code>ConnectionFactory</code> that will create <code>AConection</code> object<br>
	 * representing new socket connection.
	 * 
	 * @see commons.network.ConnectionFactory
	 * @see commons.network.AConnection
	 */
	public final ConnectionFactory	factory;

	/**
	 * Constructor
	 * 
	 * @param hostName
	 *           - Host Name on witch we will listen for connections.
	 * @param port
	 *           - Port number on witch we will listen for connections.
	 * @param connectionName
	 *           - only for logging purposes.
	 * @param factory
	 *           <code>ConnectionFactory</code> that will create <code>AConection</code> object
	 */
	public ServerCfg(String hostName, int port, String connectionName, ConnectionFactory factory)
	{
		this.hostName = hostName;
		this.port = port;
		this.connectionName = connectionName;
		this.factory = factory;
	}
}