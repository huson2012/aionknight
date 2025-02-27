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

package quest.verteron;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.InstanceService;
import gameserver.services.QuestService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.WorldMapInstance;
import gameserver.world.WorldMapType;
import java.util.Collection;

public class _1020SealingTheAbyssGate extends QuestHandler 
{
    private final static int questId = 1020;
    private final static int[] npcIds = {203098, 700141, 700142, 700551};
    private boolean spawned = false;
    private int instanceId = 0;

    public _1020SealingTheAbyssGate() 
	{
        super(questId);
    }

    @Override
    public void register() 
	{
        qe.addOnEnterWorld(questId);
        qe.addQuestLvlUp(questId);
        qe.addOnDie(questId);
        qe.setNpcQuestData(700141).addOnActionItemEvent(questId); // Abyss gate
        qe.setNpcQuestData(700142).addOnActionItemEvent(questId); // Abyss Gate Guardian Stone
        qe.setNpcQuestData(700551).addOnActionItemEvent(questId); // Fissure of Destiny
        
		for (int npcId : npcIds)
		qe.setNpcQuestData(npcId).addOnTalkEvent(questId);
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) 
	{
        int[] quests = {1130, 1023, 1022, 1021, 1019, 1018, 1017, 1016, 1015, 1014, 1013, 1012, 1011};
        return defaultQuestOnLvlUpEvent(env, quests);
    }

    @Override
    public boolean onDialogEvent(final QuestCookie env) 
	{
        if (!super.defaultQuestOnDialogInitStart(env))
            return false;
        long itemCount = 0;

        final Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.REWARD) 
		{
            if (env.getTargetId() == 203098) 
			{
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 1352);
                return defaultQuestEndDialog(env);
            }
        } 
		else if (qs.getStatus() != QuestStatus.START)
            return false;
        
		switch (env.getTargetId()) 
		{
			case 203098:
                
				switch (env.getDialogId()) 
				{
                    case 26:
                        if (var == 0)
                            return sendQuestDialog(env, 1011);
                    case 10000:
                        return defaultCloseDialog(env, 0, 1);
                    default:
                        return false;
                }
            
			case 700141:
                
				if (var == 1 && player.getPlayerGroup() != null) 
				{
                    final int targetObjectId = env.getVisibleObject().getObjectId();
                    PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                    PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                    
					// Create instance
                    WorldMapInstance newInstance = InstanceService.getRegisteredInstance(310030000, player.getPlayerGroup().getGroupId());
                    if (newInstance == null) 
					{
                        newInstance = InstanceService.getNextAvailableInstance(310030000);
                        InstanceService.registerGroupWithInstance(newInstance, player.getPlayerGroup());
                        instanceId = newInstance.getInstanceId();
                        spawned = false;
                    }

                    // Update var
                    qs.setQuestVarById(0, var + 1);

                    ThreadPoolManager.getInstance().schedule(new Runnable() 
					{
                        @Override
                        public void run() 
						{
                            PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                            updateQuestStatus(env);
                            TeleportService.teleportTo(player, 310030000, instanceId, (float) 270.5, (float) 174.3, (float) 204.3, 0);
                        }
                    }, 3000);
                    return true;
                }
                
				//TODO: Find proper message, just temp fix
                else if (var == 1 && player.getPlayerGroup() == null)
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);
                	
				else if (var == 3) 
				{
                    itemCount = player.getInventory().getItemCountByItemId(182200024);
                    if (itemCount >= 1) 
					{
                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));

                        qs.setQuestVarById(0, 4);
                        
						// Update status
                        qs.setStatus(QuestStatus.REWARD);

                        ThreadPoolManager.getInstance().schedule(new Runnable() 
						{
                            @Override
                            public void run() 
							{
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                                player.getInventory().removeFromBagByItemId(182200024, 1);
                                updateQuestStatus(env);
                                TeleportService.teleportTo(player, WorldMapType.VERTERON.getId(), 2684.308f, 1068.7382f, 199.375f, 0);
                            }
                        }, 3000);
                        return true;
                    }
                }
                return false;
            
			case 700551:
                
				if (var == 2) 
				{
                    itemCount = player.getInventory().getItemCountByItemId(182200024);
                    if (itemCount >= 1) 
					{
                        final int targetObjectId = env.getVisibleObject().getObjectId();
                        final Npc npc = (Npc) env.getVisibleObject();
                        PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                        PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                        ThreadPoolManager.getInstance().schedule(new Runnable() 
						{
                            @Override
                            public void run() 
							{
                                PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                                
								// Update quest for all players in instance
                                Collection<Player> members = player.getPlayerGroup().getMembers();
                                for (Player member : members) 
								{
                                    if (instanceId == member.getInstanceId() && member.getWorldId() == 310030000) 
									{
                                        QuestState qs1 = member.getQuestStateList().getQuestState(questId);
                                        if (qs1 != null && qs1.getStatus() == QuestStatus.START && qs1.getQuestVarById(0) == 2) 
										{
                                            qs1.setQuestVarById(0, 3);
                                            updateQuestStatus(env);
                                            PacketSendUtility.sendPacket(member, new SM_PLAY_MOVIE(0, 153));
                                        }
                                    }
                                }
                                npc.getController().onDespawn(false);
                            }
                        }, 3000);

						qs.setQuestVarById(0, 4);
						qs.setStatus(QuestStatus.REWARD);
						player.getInventory().removeFromBagByItemId(182200024, 1);
						updateQuestStatus(env);
						TeleportService.teleportTo(player, WorldMapType.VERTERON.getId(), 2684.308f, 1068.7382f, 199.375f, 0);

                        return true;
                    }
                }
            
			case 700142:
                
				if (var == 2 && spawned == false) 
				{
                    final int targetObjectId = env.getVisibleObject().getObjectId();
                    PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
                    PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
                    spawned = true;
                    
					ThreadPoolManager.getInstance().schedule(new Runnable() 
					{
                        @Override
                        public void run() 
						{
                            PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
                            PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_LOOT, 0, targetObjectId), true);
                            QuestService.addNewSpawn(310030000, player.getInstanceId(), 210753, (float) 258.89917, (float) 237.20166, (float) 217.06035, (byte) 0, true);
                            updateQuestStatus(env);
                        }
                    }, 3000);
                    return true;

                }
            default:
                return false;
        }
    }

    @Override
    public boolean onDieEvent(QuestCookie env) 
	{
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        
		if (qs == null || qs.getStatus() != QuestStatus.START)
            return false;
        int var = qs.getQuestVars().getQuestVars();
        
		if (var == 2 || var == 3) 
		{
            qs.setQuestVar(1);
            player.getInventory().removeFromBagByItemId(182200024, 1);
            updateQuestStatus(env);
        }

        return false;
    }

    @Override
    public boolean onEnterWorldEvent(QuestCookie env) 
	{
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        
		if (qs == null)
            return false;

        if (qs.getStatus() == QuestStatus.START) 
		{
            int var = qs.getQuestVars().getQuestVars();
            if (var == 2 || var == 3) 
			{
                if (player.getWorldId() != 310030000) 
				{
                    qs.setQuestVar(1);
                    player.getInventory().removeFromBagByItemId(182200024, 1);
                    updateQuestStatus(env);
                }
            }
        }

        return false;
    }

    public boolean onActionItemEvent(QuestCookie env) 
	{
        return true;
    }
}