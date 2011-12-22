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

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.siege.AethericField;
import gameserver.model.siege.SiegeLocation;
import gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import gameserver.services.SiegeService;
import gameserver.utils.PacketSendUtility;

public class AethericFieldController extends NpcController
{	
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
		int id = getOwner().getFortressId();
		SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(id);
		//disable field
		loc.setShieldActive(false);
		//TODO : Find sys message sended on generator death
		getOwner().getKnownList().doOnAllPlayers(new Executor<Player>(){		
			@Override
			public boolean run(Player object)
			{
				//Needed to update the display of shield effect
				PacketSendUtility.sendPacket(object, new SM_SIEGE_LOCATION_INFO());
				return true;
			}
		}, true);
	}

	@Override
	public AethericField getOwner()
	{
		return (AethericField) super.getOwner();
	}	
}