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

package commons.ngen.network;

public class ServerConfig
{
	public String name;
	public String hostname;
	public int port;
	public ConnectionFactory factory;
	public int readThreads;
	public int writeThreads;
	public boolean	enableWorkers;
	public int workerThreads;	
	public int bufferCount;	
	public int readTries;	
	public int writeTries;	
	public boolean debugEnabled;	
	public ServerConfig (String name, String hostname, int port, ConnectionFactory factory, int readThreads, int writeThreads, boolean enableWorkers, int workerThreads, int bufferCount, int readTries, int writeTries, boolean debugEnabled)
	{
		this.name = name;
		this.hostname = hostname;
		this.port = port;
		this.factory = factory;
		this.readThreads = readThreads;
		this.writeThreads = writeThreads;
		this.enableWorkers = enableWorkers;
		this.workerThreads = workerThreads;
		this.bufferCount = bufferCount;
		this.readTries = readTries;
		this.writeTries = writeTries;
		this.debugEnabled = debugEnabled;
	}
}