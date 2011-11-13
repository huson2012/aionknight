/**
*  This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
*
*  Aion-Knight is free software: you can redistribute it and/or modify
*  it under the terms of the GNU General Public License as published by
*  the Free Software Foundation, either version 3 of the License, or
*  (at your option) any later version.
*
*  Aion-Knight is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
*  GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License
*  along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
*/
package quest.eltnen;

import java.util.Collections;


import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.quest.HandlerResult;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.services.ZoneService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;


/**
* @author Nephis
*
*/
public class _1373WaterTherapy extends QuestHandler
{

   private final static int   questId   = 1373;

   public _1373WaterTherapy()
   {
      super(questId);
   }

   @Override
   public void register()
   {
      qe.setNpcQuestData(203949).addOnQuestStart(questId); //Aerope
      qe.setNpcQuestData(203949).addOnTalkEvent(questId);
	  qe.setQuestItemIds(182201372).add(questId); //Empty Flask
   }
   
   	@Override
	public HandlerResult onItemUseEvent(final QuestCookie env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(qs == null || id != 182201372) //Empty Flask
			return HandlerResult.UNKNOWN;
		if(!ZoneService.getInstance().isInsideZone(player, ZoneName.GEROCH_CALDERA))
			return HandlerResult.FAILED;

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				player.getInventory().removeFromBagByObjectId(itemObjId, 1);
				ItemService.addItems(player, Collections.singletonList(new QuestItems(182201373, 1)));
				qs.setQuestVar(2);
				updateQuestStatus(env);
			}
		}, 3000);
		return HandlerResult.SUCCESS;
	}
   
   @Override
   public boolean onDialogEvent(QuestCookie env)
   {
      final Player player = env.getPlayer();
      int targetId = 0;
      if(env.getVisibleObject() instanceof Npc)
         targetId = ((Npc) env.getVisibleObject()).getNpcId();
      final QuestState qs = player.getQuestStateList().getQuestState(questId);
      if(qs == null || qs.getStatus() == QuestStatus.NONE)
      {
			if(targetId == 203949) //Aerope
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 1011);
				else if(env.getDialogId() == 1002)
				{
					if (ItemService.addItems(player, Collections.singletonList(new QuestItems(182201372, 1))))
						return defaultQuestStartDialog(env);
					return true;
				}
				else
					return defaultQuestStartDialog(env);
			}
      }

		else if (qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 203949:
				{
					if (qs.getQuestVarById(0) == 2)
					{
						if(env.getDialogId() == 26)
							return sendQuestDialog(env, 2375);
						else if(env.getDialogId() == 34)
						{							
							if(QuestService.collectItemCheck(env, true))
							{
								qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
							else
								return sendQuestDialog(env, 10001);
						}
						else
							return defaultQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 203949)
				return defaultQuestEndDialog(env);
		}
		return false;
   }
}