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

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum PlayerClass
{
	WARRIOR(0, true), GLADIATOR(1),	TEMPLAR(2), SCOUT(3, true),	ASSASSIN(4),
	RANGER(5), MAGE(6, true), SORCERER(7), SPIRIT_MASTER(8), PRIEST(9, true),
	CLERIC(10),	CHANTER(11);

	private byte classId;
	private int idMask;
	private boolean	startingClass;
	private PlayerClass(int classId)
	{
		this(classId, false);
	}

	private PlayerClass(int classId, boolean startingClass)
	{
		this.classId = (byte) classId;
		this.startingClass = startingClass;
		this.idMask = (int)Math.pow(2, classId);
	}

	public byte getClassId()
	{
		return classId;
	}

	public static PlayerClass getPlayerClassById(byte classId)
	{
		for(PlayerClass pc : values())
		{
			if(pc.getClassId() == classId)
				return pc;
		}

		throw new IllegalArgumentException("There is no player class with id " + classId);
	}

	public boolean isStartingClass()
	{
		return startingClass;
	}

	public static PlayerClass getStartingClassFor(PlayerClass pc)
	{
		switch(pc)
		{
			case ASSASSIN:
			case RANGER:
				return SCOUT;
			case GLADIATOR:
			case TEMPLAR:
				return WARRIOR;
			case CHANTER:
			case CLERIC:
				return PRIEST;
			case SORCERER:
			case SPIRIT_MASTER:
				return MAGE;
			default:
				return pc;
		}
	}
	
	public static PlayerClass getPlayerClassByString(String fieldName)
	{
		for(PlayerClass pc : values())
		{
			if(pc.toString().equals(fieldName))
				return pc;
		}
		return null;
	}
	
	public int getMask()
	{
		return idMask;
	}
}