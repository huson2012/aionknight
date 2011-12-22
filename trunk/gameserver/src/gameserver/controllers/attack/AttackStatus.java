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

package gameserver.controllers.attack;

public enum AttackStatus
{
	DODGE(0),
	SUBHAND_DODGE(1),
	PARRY(2),
	SUBHAND_PARRY(3),
	BLOCK(4),
	SUBHAND_BLOCK(5),
	RESIST(6),
	SUBHAND_RESIST(7),
	BUF(8),// ??
	SUBHAND_BUF(9),
	NORMALHIT(10),
	SUBHAND_NORMALHIT(11),
	CRITICAL_DODGE(-64),
	CRITICAL_PARRY(-62),
	CRITICAL_BLOCK(-60),
	CRITICAL_RESIST(-58),
	CRITICAL(-54),
	SUBHAND_CRITICAL_DODGE(-47),
	SUBHAND_CRITICAL_PARRY(-45),
	SUBHAND_CRITICAL_BLOCK(-43),
	SUBHAND_CRITICAL_RESIST(-41),
	SUBHAND_CRITICAL(-37);

	private int _type;

	private AttackStatus(int type)
	{
		this._type = type;
	}

	public int getId()
	{
		return _type;
	}
}
