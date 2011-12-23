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

package loginserver.controller;

import java.util.ArrayList;
import loginserver.model.FloodIP;
import org.apache.log4j.Logger;

/**
 * Class that controlls all flood
 */
public class FloodController
{
	/**
	 * Logger for this class.
	 */
	@SuppressWarnings("unused")
	private static final Logger	log	= Logger.getLogger(FloodController.class);

	private static ArrayList<FloodIP> IPs = new ArrayList<FloodIP>();
	
	public static void addIP(String IP)
	{
		IPs.add(new FloodIP(IP));
	}
	
	public static boolean checkFlood(String IP)
	{
		boolean found	= false;
		int 	i		= 0;
		
		while (!found && i<IPs.size())
		{
			if (IPs.get(i).getIP().equals(IP))
				found = true;
			else
				i++;
		}
		
		return IPs.get(i).checkFlood();
	}
	
	public static boolean exist(String IP)
	{
		for (FloodIP fIP : IPs)
			if (fIP.getIP().equals(IP))
				return true;
				
		return false;
	}
	
	public static int getIP(String ip)
	{
		boolean found	= false;
		int 	i		= 0;
		
		while (!found && i<IPs.size())
		{
			if (IPs.get(i).getIP().equals(ip))
				found = true;
			else
				i++;
		}
		
		return i;
	}
	
	public static void addConnection(String IP)
	{
		IPs.get(getIP(IP)).addConnection();
	}
	
	public static int getConnection(String IP)
	{
		return IPs.get(getIP(IP)).size();
	}
}