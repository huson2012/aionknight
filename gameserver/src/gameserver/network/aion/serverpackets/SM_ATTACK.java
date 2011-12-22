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

import gameserver.controllers.attack.AttackResult;
import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_ATTACK extends AionServerPacket
{
	private int	attackno;
	private int	time;
	private int	type;
	private List<AttackResult> attackList;
	private Creature attacker;
	private Creature target;

	public SM_ATTACK(Creature attacker, Creature target, int attackno, int time, int type, List<AttackResult> attackList)
	{
		this.attacker = attacker;
		this.target = target;
		this.attackno = attackno;
		this.time = time ;
		this.type = type;
		this.attackList = attackList;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, attacker.getObjectId());
		writeC(buf, attackno);
		writeH(buf, time);
		writeC(buf, type);
		writeD(buf, target.getObjectId());

		int attackerMaxHp = attacker.getLifeStats().getMaxHp();
		int attackerCurrHp = attacker.getLifeStats().getCurrentHp();
		int targetMaxHp = target.getLifeStats().getMaxHp();
		int targetCurrHp = target.getLifeStats().getCurrentHp();

		writeC(buf, 100 * targetCurrHp / targetMaxHp);
		writeC(buf, 100 * attackerCurrHp / attackerMaxHp);

		switch(attackList.get(0).getAttackStatus().getId())
		{
			case -60:
			case 4:
				writeD(buf, 32);
			    break;
			case -62:
			case 2:
				writeD(buf, 64);
			    break;
			case -64:
			case 0:
				writeD(buf, 128);
			    break;
            case -58:
			case 6:
				writeD(buf, 256);
			    break;
			default:
				writeD(buf, 0);
			    break;
		}

		writeC(buf, attackList.size());
		for (AttackResult attack : attackList)
		{
			writeD(buf, attack.getDamage());
			writeC(buf, attack.getAttackStatus().getId());
			writeC(buf, attack.getShieldType());

			switch (attack.getShieldType())
			{
				case 0:
				case 2:
				    break;
				default:
					writeD(buf, 0x00);
					writeD(buf, 0x00);
					writeD(buf, 0x00);
					writeD(buf, 0);
					writeD(buf, 0);
				    break;
			}
		}
		writeC(buf, 0);
	}
}