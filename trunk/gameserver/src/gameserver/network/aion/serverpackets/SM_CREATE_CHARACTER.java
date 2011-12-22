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

package gameserver.network.aion.serverpackets;

import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.PlayerInfo;
import java.nio.ByteBuffer;

/**
 * Этот пакет является ответом на CM_CREATE_CHARACTER
 */
public class SM_CREATE_CHARACTER extends PlayerInfo
{	
	/** 
	 * Если ответ будет ок
	 */
	public static final int	RESPONSE_OK	= 0x00;
	
	/** 
	 * Ошибка создания персонажа
	 */
	public static final int	FAILED_TO_CREATE_THE_CHARACTER = 1;
	
	/** 
	 * Не удалось создать персонажа из-за ошибки в БД
	 */
	public static final int RESPONSE_DB_ERROR = 2;
	
	/** 
	 * Количество персонажей превышает максимально допустимое для сервера
	 */
	public static final int RESPONSE_SERVER_LIMIT_EXCEEDED = 4;
	
	/** 
	 * Некорректное имя персонажа
	 */
	public static final int	RESPONSE_INVALID_NAME = 5;
	
	/** 
	 * Имя персонажа содержит недопустимые символы 
	 */
	public static final int RESPONSE_FORBIDDEN_CHAR_NAME= 9;
	
	/** 
	 * Персонаж с таким именем уже существует 
	 */
	public static final int	RESPONSE_NAME_ALREADY_USED = 10;
	
	/** 
	 * Имя уже зарезервировано
	 */
	public static final int RESPONSE_NAME_RESERVED = 11;
	
	/** 
	 * Вы не сможете создать персонажей другой расы на одном и том же сервере 
	 */
	public static final int RESPONSE_OTHER_RACE = 12;

	/**
	 * Код ответа
	 */
	private final int responseCode;

	/**
	 * Вновь созданный игрок
	 */
	private final PlayerAccountData	player;

	/**
	 * Создание нового SM_CREATE_CHARACTER пакета
	 * 
	 * @param accPlData
	 *           playerAccountData игрок, который был создан
	 * @param responseCode
	 *           код ответа (invalid nickname, nickname is already taken, ok)
	 */

	public SM_CREATE_CHARACTER(PlayerAccountData accPlData, int responseCode)
	{
		this.player = accPlData;
		this.responseCode = responseCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, responseCode);

		if(responseCode == RESPONSE_OK)
		{
			writePlayerInfo(buf, player); // Если все хорошо, то все данные по персонажа будут отправлены в БД
		}
		else
		{
			writeB(buf, new byte[512]); // Если что-то не так, в пакет будет помощен только код возврата
		}
	}
}
