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

package loginserver.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;
import commons.utils.Base64;

/**
 * Class with usefull methods to use with accounts
 */
public class AccountUtils
{
	/**
	 * Logger
	 */
	private static final Logger	log	= Logger.getLogger(AccountUtils.class);

	/**
	 * Encodes password. SHA-1 is used to encode password bytes, Base64 wraps SHA1-hash to string.
	 * 
	 * @param password
	 *           password to encode
	 * @return retunrs encoded password.
	 */
	public static String encodePassword(String password)
	{
		try
		{
			MessageDigest messageDiegest = MessageDigest.getInstance("SHA-1");
			messageDiegest.update(password.getBytes("UTF-8"));
			return Base64.encodeToString(messageDiegest.digest(), false);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error("Exception while encoding password");
			throw new Error(e);
		}
		catch (UnsupportedEncodingException e)
		{
			log.error("Exception while encoding password");
			throw new Error(e);
		}
	}
}