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

package gameserver.quest.model;

public class QuestVars
{
	private Integer[]	questVars	= new Integer[6];

	public QuestVars()
	{
	}

	public QuestVars(int var)
	{
		setVar(var);
	}

	/**
	 * @param id
	 * @return Quest var by id.
	 */
	public int getVarById(int id)
	{
		if(id == 5)
			return (questVars[id] & 0x03);
		return questVars[id];
	}

	/**
	 * @param id
	 * @param var
	 */
	public void setVarById(int id, int var)
	{
		if(id == 5)
			questVars[id] = (var & 0x03);
		else
			questVars[id] = (var & 0x3F);
	}

	/**
	 * @return integer
	 */
	public int getQuestVars()
	{
		int var = 0;
		var |= questVars[5];
		for(int i = 5; i >= 0; i--)
		{
			if(i == 5)
				var <<= 0x02;
			else
				var <<= 0x06;
			var |= questVars[i];
		}
		return var;
	}
	
	public void setVar(int var)
	{
		for(int i = 0; i < 6; i++)
		{
			if(i == 5)
				questVars[i] = (var & 0x03);
			else
			{
				questVars[i] = (var & 0x3F);
				var >>= 0x06;
			}
		}
	}
}