/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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

package quest.the_hidden_truth_mission;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.utils.PacketSendUtility;

import java.util.Collections;

public class _1098PearlofProtection extends QuestHandler
{	
	private final static int	questId	= 1098;
	private final static int[]	npc_ids	= { 790001, 730008, 730019, 204647, 203183, 203989, 798155, 204549, 203752, 203164, 203917, 203996, 798176, 798212, 204535};
	
	public _1098PearlofProtection()
	{
		super(questId);
	}
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 790001:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							if (var == 0)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182206062, 1)));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 730008:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
						case 10001:
							if (var == 1)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								player.getInventory().removeFromBagByItemId(182206062, 1);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182206063, 1)));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;	
				case 730019:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 2)
								return sendQuestDialog(env, 1693);
						case 10002:
							if (var == 2)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;	
				case 204647:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 3)
								return sendQuestDialog(env, 2034);
						case 10003:
							if (var == 3)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203183:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 4)
								return sendQuestDialog(env, 2375);
						case 10004:
							if (var == 4)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								player.getInventory().removeFromBagByItemId(182206063, 1);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182206064, 1)));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203989:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 5)
								return sendQuestDialog(env, 2716);
						case 10005:
							if (var == 5)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 798155:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 6)
								return sendQuestDialog(env, 3057);
						case 10006:
							if (var == 6)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 204549:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 7)
								return sendQuestDialog(env, 3398);
						case 10007:
							if (var == 7)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203752:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 8)
								return sendQuestDialog(env, 3739);
						case 10008:
							if (var == 8)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203164:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 9)
								return sendQuestDialog(env, 4080);
						case 10009:
							if (var == 9)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203917:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 10)
								return sendQuestDialog(env, 1608);
						case 10010:
							if (var == 10)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203996:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 11)
								return sendQuestDialog(env, 1949);
						case 10011:
							if (var == 11)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 798176:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 12)
								return sendQuestDialog(env, 2290);
						case 10012:
							if (var == 12)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 798212:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 13)
								return sendQuestDialog(env, 2631);
						case 10013:
							if (var == 13)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 204535:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 14)
								return sendQuestDialog(env, 2972);
						case 10255:
							if (var == 14)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								player.getInventory().removeFromBagByItemId(182206064, 1);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182206065, 1)));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 790001)
			{	
				if (env.getDialogId() == -1 )				
					return sendQuestDialog(env, 10002);
				else
					{
					player.getInventory().removeFromBagByItemId(182206065, 1);	
					return defaultQuestEndDialog(env);
					}
			}
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, false);
	}
	
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}
}