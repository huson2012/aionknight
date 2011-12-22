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

package commons.ngen.utils.ssl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class ResourceLoader
{
	public static InputStream loadResource (String name)
	{
		InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(name);
		DataInputStream dis = null;
		if (is==null)
		{
			FileInputStream fis;
			try {
				fis = new FileInputStream(name);
				dis = new DataInputStream(fis);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				dis = new DataInputStream(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
		    byte[] bytes = new byte[dis.available()];
	        dis.readFully(bytes);
	        is = new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
}