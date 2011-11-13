/**
 * This file is part of Aion-Knight Dev. Team [http://www.aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */
package gameserver.controllers;

import org.apache.log4j.Logger;

import gameserver.controllers.attack.AttackStatus;
import gameserver.model.Race;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.shield.Shield;
import gameserver.model.siege.SiegeRace;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.services.SiegeService;


/**
 * @author blakawk
 * @modified ViAl(fortress shields)
 */
public class ShieldController extends CreatureController<Shield>
{	
	@Override
	public void see(VisibleObject object)
	{
		if (object instanceof Player)
		{
			Player p = (Player) object;
			Shield owner = (Shield)getOwner();
			//areshurat, teminon, primum entrances
			if (owner.getTemplate().getRace() != Race.NONE)
			{
				if (p.getCommonData().getPosition().getX()>2080 && p.getCommonData().getPosition().getX()<2215 && 
					p.getCommonData().getPosition().getY()>1820 && p.getCommonData().getPosition().getY()<2045 &&
					p.getCommonData().getPosition().getZ()>2309)
				{
					// areshurat entrance, nothing to do
					Logger.getLogger(this.getClass()).info("Player "+p.getName()+" passed areshurat entrance.");
				}
				else
			   	{
			    	if (!p.isProtectionActive() && p.getCommonData().getRace() != owner.getTemplate().getRace())
			    		kill(owner,p);

			    }
			}
			if (owner.getTemplate().getRace() == Race.NONE)
			{
				if (SiegeService.getInstance().getSiegeLocation(owner.getTemplate().getFortressId()).isShieldActive())
				{
					SiegeRace sRace = SiegeService.getInstance().getSiegeLocation(owner.getTemplate().getFortressId()).getRace();
					Race race;
					switch (sRace) 
					{
 					case ASMODIANS:
 						race = Race.ASMODIANS;
 						break;
 					case ELYOS:
 						race = Race.ELYOS;
 						break;
 					default:
 						race = Race.DRAKAN;
 						break;
					}
					
					if(p.getCommonData().getRace() != race)
							if(p.getCommonData().getPosition().getZ() >= owner.getTemplate().getZ() -12)
								kill(owner,p);
				}
			}
		}
	}
	
	private void kill(Shield owner, Player p)
	{
		Logger.getLogger(this.getClass()).info("Shield "+owner.getName()+" killing "+p.getName());
		
		if(owner.getTemplate().getFortressId() != 0)
			p.getController().onAttack(owner, owner.getTemplate().getSkill(), TYPE.HP, p.getLifeStats().getCurrentHp()+1, 0x5B ,AttackStatus.NORMALHIT, true, true);
		else
		{
			p.getController().setCanAutoRevive(false);
			p.getController().onAttack(owner, owner.getTemplate().getSkill(), TYPE.HP, p.getLifeStats().getCurrentHp()+1, 0x5B ,AttackStatus.NORMALHIT, true, true);
			p.getReviveController().bindRevive();
			p.getController().setCanAutoRevive(true);
		}
	}
}
