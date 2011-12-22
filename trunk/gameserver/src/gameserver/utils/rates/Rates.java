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

package gameserver.utils.rates;

public abstract class Rates
{
	public abstract int getGroupXpRate();
	public abstract int getXpRate();
	public abstract float getApNpcRate();
	public abstract float getApPlayerRate();
	public abstract float getGatheringXPRate();
	public abstract float getGatheringLvlRate();
	public abstract float getCraftingXPRate();
	public abstract float getCraftingLvlRate();
	public abstract int getDropRate();
	public abstract int getChestDropRate();
	public abstract int getQuestXpRate();
	public abstract int getQuestKinahRate();
	public abstract int getKinahRate();
	public static Rates getRatesFor(byte membership)
	{
		switch(membership)
		{
			case 0:
				return new RegularRates();
			case 1:
				return new PremiumRates();
			default:
				return new RegularRates();
		}
	}
}