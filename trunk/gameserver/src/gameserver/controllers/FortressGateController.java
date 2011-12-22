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

import gameserver.model.DescriptionId;
import gameserver.model.Race;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.Trap;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.RequestResponseHandler;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.model.siege.FortressGate;
import gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.TeleportService;
import gameserver.utils.MathUtil;
import gameserver.utils.PacketSendUtility;

public class FortressGateController extends NpcController
{	

	@Override
	public void onDie(Creature lastAttacker)
	{
		final Player destroyer;
		if(lastAttacker instanceof Player)
			destroyer = (Player)lastAttacker;
		else if(lastAttacker instanceof Trap)
			destroyer = (Player)((Trap)lastAttacker).getMaster();
		else if(lastAttacker instanceof Summon)
			destroyer = ((Summon)lastAttacker).getMaster();
		else
			destroyer = null;
		
		if(destroyer != null)
		{
			final int raceMsgId;
			if(destroyer.getCommonData().getRace() == Race.ELYOS)
				raceMsgId = 900240;
			else
				raceMsgId = 900241;
			getOwner().getKnownList().doOnAllPlayers(new Executor<Player>(){
				
				@Override
				public boolean run(Player object)
				{
					PacketSendUtility.sendPacket(object, new SM_SYSTEM_MESSAGE(1400305, destroyer.getName(), new DescriptionId(raceMsgId*2+1)));
					return true;
				}
			}, true);
		}
		super.onDelete();
	}
	
	@Override
	public void onDialogRequest(Player p)
	{
		
		if(((p.getCommonData().getRace() == Race.ELYOS)&&(getOwner().getObjectTemplate().getRace()==Race.PC_LIGHT_CASTLE_DOOR))||((p.getCommonData().getRace() == Race.ASMODIANS)&&(getOwner().getObjectTemplate().getRace()==Race.PC_DARK_CASTLE_DOOR)))
		
		{
			RequestResponseHandler gateHandler = new RequestResponseHandler(p)
			{
				@Override
				public void denyRequest(Creature requester, Player p)
				{
					// Close window
				}
				
				@Override
				public void acceptRequest(Creature requester, Player p)
				{
					double radian = Math.toRadians(MathUtil.convertHeadingToDegree(p.getHeading()));
					int worldId = getOwner().getWorldId();
					float x = (float)(p.getX() + (10 * Math.cos(radian)));
					float y = (float)(p.getY() + (10 * Math.sin(radian)));
					float z = p.getZ() + 0.5f;
					TeleportService.teleportTo(p, worldId, x, y, z, 1);
				}
			};
			if(p.getResponseRequester().putRequest(160017, gateHandler))
			{
				PacketSendUtility.sendPacket(p, new SM_QUESTION_WINDOW(160017, p.getObjectId()));
			}
		}
	}
	
	@Override
	public void onRespawn()
	{
		super.onRespawn();
	}

	@Override
	public FortressGate getOwner()
	{
		return (FortressGate) super.getOwner();
	}
}
