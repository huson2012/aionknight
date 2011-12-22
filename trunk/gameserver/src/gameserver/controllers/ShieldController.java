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

package gameserver.controllers;

import gameserver.controllers.attack.AttackStatus;
import gameserver.model.Race;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.shield.Shield;
import gameserver.model.siege.SiegeRace;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.services.SiegeService;
import org.apache.log4j.Logger;

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