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

package commons.configuration.transformers;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

public class InetSocketAddressTransformer implements PropertyTransformer<InetSocketAddress>
{
	public static final InetSocketAddressTransformer	SHARED_INSTANCE	= new InetSocketAddressTransformer();

	@Override
	public InetSocketAddress transform(String value, Field field) throws TransformationException
	{
		String[] parts = value.split(":");

		if(parts.length != 2)
		{
			throw new TransformationException("Can't transform property, must be in format \"address:port\"");
		}

		try
		{
			if("*".equals(parts[0]))
			{
				return new InetSocketAddress(Integer.parseInt(parts[1]));
			}
			else
			{
				InetAddress address = InetAddress.getByName(parts[0]);
				int port = Integer.parseInt(parts[1]);
				return new InetSocketAddress(address, port);
			}
		}
		catch(Exception e)
		{
			throw new TransformationException(e);
		}
	}
}