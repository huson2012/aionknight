/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
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

package gameserver.skill.effect;

public enum EffectId
{
	BUFF(0),
	POISON(1),
	BLEED(2),
	PARALYZE(4),
	SLEEP(8),
	ROOT(16), // ?? cannot move ?
	BLIND(32),
	UNKNOWN(64),
	DISEASE(128),
	SILENCE(256),
	FEAR(512), //Fear I
	CURSE(1024),
	CHAOS(2056),
	STUN(4096),
	PETRIFICATION(8192),
	STUMBLE(16384),
	STAGGER(32768),
	OPENAERIAL(65536),
	SNARE(131072),
	SLOW(262144),
	SPIN(524288),
	BIND(1048576),
	DEFORM(2097152), //(Curse of Roots I, Fear I)
	CANNOT_MOVE(4194304), //(Inescapable Judgment I)
	SHAPECHANGE(8388608), //cannot fly
	KNOCKBACK(16777216),//simple_root
	INVISIBLE_RELATED(536870912),//hide 33554432
	
	/**
	 * Compound abnormal states
	 */
	CANT_ATTACK_STATE(
		SPIN.effectId |
		SLEEP.effectId |
		STUN.effectId |
		STUMBLE.effectId |
		STAGGER.effectId |
		OPENAERIAL.effectId |
		PARALYZE.effectId |
		FEAR.effectId |
	  	CANNOT_MOVE.effectId
	),
	CANT_MOVE_STATE(SPIN.effectId | ROOT.effectId | SLEEP.effectId | STUMBLE.effectId | STUN.effectId | STAGGER.effectId | OPENAERIAL.effectId | PARALYZE.effectId | CANNOT_MOVE.effectId | KNOCKBACK.effectId);
	
	private int effectId;
	
	private EffectId(int effectId)
	{
		this.effectId = effectId;
	}
	
	public int getEffectId()
	{
		return effectId;
	}
	
	public static EffectId getEffectIdByName(String name)
	{
		for(EffectId id : values())
		{
			if(id.name().equals(name))
				return id;
		}
		return null;
	}	
}
