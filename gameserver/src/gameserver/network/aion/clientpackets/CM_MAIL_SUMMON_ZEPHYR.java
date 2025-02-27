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


import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.spawn.SpawnEngine;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;

/**
 * 
 * @author Sylar
 * 
 */
public class CM_MAIL_SUMMON_ZEPHYR extends AionClientPacket
{	
	
	private int value;
	
	public CM_MAIL_SUMMON_ZEPHYR(int opcode)
	{
		super(opcode);
	}

	
	@Override
	protected void readImpl()
	{
		value = readC();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if(player != null && value == 1)
		{
			int zephyrNpcId = 0;
			
			switch(player.getCommonData().getRace())
			{
				case ELYOS: zephyrNpcId = 798044; break;
				case ASMODIANS: zephyrNpcId = 798101; break;
			}
			
			if(zephyrNpcId == 0)
				return;
			
			// POSTMAN_ALREADY_SUMMONED
			if(player.getZephyrObjectId() != 0)
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300877));
				return;
			}
			
			// POSTMAN_COOLDOWN (30mn)
			if(player.getLastZephyrInvokationSeconds() > (System.currentTimeMillis() / 1000) - 600)
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300878));
				return;
			}
			
			if(player.isInState(CreatureState.FLYING) || player.isInState(CreatureState.FLIGHT_TELEPORT) || player.isInState(CreatureState.GLIDING))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300879));
				return;
			}
			
			SpawnTemplate zst = SpawnEngine.getInstance().addNewSpawn(player.getWorldId(), player.getInstanceId(), zephyrNpcId, player.getX(), player.getY(), player.getZ(), player.getHeading(), 0, 0, true, true);
			VisibleObject zvo = SpawnEngine.getInstance().spawnObject(zst, player.getInstanceId());
			
			if(zvo != null && zvo instanceof Creature)
			{
				final Creature zc = (Creature)zvo;
				player.setZephyrObjectId(zc.getObjectId());
				player.setLastZephyrInvokationSeconds(System.currentTimeMillis() / 1000);
				zc.setTarget(player);
				zc.getMoveController().followTarget(4);
				if(!zc.getMoveController().isScheduled())
					zc.getMoveController().schedule();
				
				// Despawn Zephyr after 5 minutesIf not already despawned
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					
					@Override
					public void run()
					{
						int zid = zc.getObjectId();
						AionObject obj = World.getInstance().findAionObject(zid);
						if(obj != null && obj instanceof Creature)
						{
							Creature zephyr = (Creature)obj;
							DataManager.SPAWNS_DATA.removeSpawn(zephyr.getSpawn());
							zephyr.getMoveController().stop();
							zephyr.getController().delete();
						}
						player.setZephyrObjectId(0);
					}
				}, 300000);
			}			
		}
	}
}
