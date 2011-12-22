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

package gameserver.model;

public enum EmotionType
{
	UNK(-1), SELECT_TARGET(0), JUMP(1),	SIT(2),	STAND(3), CHAIR_SIT(4),	CHAIR_UP(5), START_FLYTELEPORT(6),
	LAND_FLYTELEPORT(7), WINDSTREAM(8),	WINDSTREAM_END(9), WINDSTREAM_BOOST(11), FLY(13), LAND(14), DIE(18),
	RESURRECT(19), EMOTE(21), END_DUEL(22),	ATTACKMODE(24),	NEUTRALMODE(25), WALK(26), RUN(27),	SWITCH_DOOR(31),
	START_EMOTE(32), OPEN_PRIVATESHOP(33), CLOSE_PRIVATESHOP(34), START_EMOTE2(35),	POWERSHARD_ON(36),
	POWERSHARD_OFF(37), ATTACKMODE2(38), NEUTRALMODE2(39), START_LOOT(40), END_LOOT(41), START_QUESTLOOT(42),
	END_QUESTLOOT(43), PET_FEEDING(50),	PET_FEEDING2(51);
	
	private int id;
	
	private EmotionType(int id)
	{
		this.id = id;
	}
	
	public int getTypeId()
	{
		return id;
	}
	
	public static EmotionType getEmotionTypeById(int id)
	{
		for(EmotionType emotionType : values())
		{
			if(emotionType.getTypeId() == id)
				return emotionType;
		}
		return UNK;
	}	
}