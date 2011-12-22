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

package gameserver.utils.stats;

import java.util.NoSuchElementException;

public enum DropRewardEnum
{
	MINUS_11(-11, 0),
	MINUS_10(-10, 1),
	MINUS_9(-9, 10),
	MINUS_8(-8, 20),
	MINUS_7(-7, 30),
	MINUS_6(-6, 40),
	MINUS_5(-5, 50),
	MINUS_4(-4, 60),
	MINUS_3(-3, 90),
	MINUS_2(-2, 100),
	MINUS_1(-1, 100),
	ZERO(0, 100);

	private int dropRewardPercent;
	private int levelDifference;
	
	private DropRewardEnum(int levelDifference, int dropRewardPercent)
	{
		this.levelDifference = levelDifference;
		this.dropRewardPercent = dropRewardPercent;
	}
	
	public int rewardPercent()
	{
		return dropRewardPercent;
	}
	
	/**
	 * 
	 * @param levelDifference between two objects
	 * @return Drop reward percentage
	 */
	public static int dropRewardFrom(int levelDifference)
	{
		if(levelDifference < MINUS_11.levelDifference)
		{
			return MINUS_11.dropRewardPercent;
		}
		if(levelDifference > ZERO.levelDifference)
		{
			return ZERO.dropRewardPercent;
		}
	
		for(DropRewardEnum dropReward : values())
		{
			if(dropReward.levelDifference == levelDifference)
			{
				return dropReward.dropRewardPercent;
			}
		}
		throw new NoSuchElementException("Drop reward for such level difference was not found");
	}
}