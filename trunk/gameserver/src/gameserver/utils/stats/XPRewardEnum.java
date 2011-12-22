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

public enum XPRewardEnum
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
	ZERO(0, 100),
	PLUS_1(1, 105),
	PLUS_2(2, 110),
	PLUS_3(3, 115),
	PLUS_4(4, 120);

	
	private int xpRewardPercent;
	
	private int levelDifference;
	
	private XPRewardEnum(int levelDifference, int xpRewardPercent)
	{
		this.levelDifference = levelDifference;
		this.xpRewardPercent = xpRewardPercent;
	}
	
	public int rewardPercent()
	{
		return xpRewardPercent;
	}
	
	/**
	 * 
	 * @param levelDifference between two objects
	 * @return XP reward percentage
	 */
	public static int xpRewardFrom(int levelDifference)
	{
		if(levelDifference < MINUS_11.levelDifference)
		{
			return MINUS_11.xpRewardPercent;
		}
		if(levelDifference > PLUS_4.levelDifference)
		{
			return PLUS_4.xpRewardPercent;
		}
	
		for(XPRewardEnum xpReward : values())
		{
			if(xpReward.levelDifference == levelDifference)
			{
				return xpReward.xpRewardPercent;
			}
		}
		throw new NoSuchElementException("XP reward for such level difference was not found");
	}
}