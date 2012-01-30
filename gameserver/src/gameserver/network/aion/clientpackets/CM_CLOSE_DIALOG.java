/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.clientpackets;

import gameserver.ai.AI;
import gameserver.ai.state.AIState;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Letter;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;
import java.util.Collection;

public class CM_CLOSE_DIALOG extends AionClientPacket
{
	/**
	* Target object id that client wants to TALK WITH or 0 if wants to unselect
	*/
	private int	targetObjectId;

	/**
	* Constructs new instance of <tt>CM_CM_REQUEST_DIALOG </tt> packet
	* @param opcode
	*/
	public CM_CLOSE_DIALOG(int opcode)
	{
		super(opcode);
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	protected void readImpl()
	{
		targetObjectId = readD();
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	protected void runImpl()
	{
		AionObject targetObject = World.getInstance().findAionObject(targetObjectId);
		Player player = getConnection().getActivePlayer();

		if(targetObject == null || player == null)
			return;

		if(targetObject instanceof Npc)
		{
			Npc npc = (Npc) targetObject;
			// Zephyr Deliveryman
			if(targetObject.getObjectId() == player.getZephyrObjectId())
			{
				int zid = npc.getObjectId();
				AionObject obj = World.getInstance().findAionObject(zid);
				if(obj != null && obj instanceof Creature)
				{
					Creature zephyr = (Creature)obj;
					DataManager.SPAWNS_DATA.removeSpawn(zephyr.getSpawn());
					zephyr.getController().delete();
				}
				player.setZephyrObjectId(0);
				
				// Refresh Mails client display
				Collection<Letter> lts = player.getMailbox().getLetters();
				int mailCount =  player.getMailbox().size();
				int unreadMailCount = 0;
				boolean hasExpress = false;
				
				for(Letter lt : lts)
				{
					if(lt.isUnread())
					{
						unreadMailCount++;
						if(!hasExpress && lt.isExpress())
							hasExpress = true;
					}
				}				
				
				PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(mailCount, unreadMailCount, hasExpress));
				
				return;
			}

			if(npc.hasWalkRoutes() && !npc.getMoveController().canWalk()) 
			{
				// Hack to resume heading after talking
				World.getInstance().updatePosition(npc, npc.getX(), npc.getY(), npc.getZ(), (byte)(npc.getHeading()+ 1), true);
				npc.getMoveController().setCanWalk(true);
				npc.getAi().setAiState(AIState.ACTIVE);
			}
			else
			{
				AI<?> ai = npc.getAi();
				ai.setAiState(AIState.ACTIVE);
				if (!ai.isScheduled())
					ai.schedule();
			}

			//TODO: need check it on retail
			if(npc.getTarget() == player)
				npc.setTarget(null);

			PacketSendUtility.broadcastPacket(npc,
				new SM_LOOKATOBJECT(npc));
		}
	}
}