/**
 * This file is part of Aion-Knight <Aion-Knight.smfnew.com>.
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.List;
import gameserver.controllers.attack.AttackResult;
import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

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
					writeD(buf, attack.getReflectedDamage());
					writeD(buf, attack.getSkillId());
				break;
			}
		}
		writeC(buf, 0);
	}
}