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

package quest.greater_stigma_quest;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import java.util.Collections;

public class _3931HowToUseStigma extends QuestHandler
{
	private final static int	questId	= 3931;
	private final static int[]	npc_ids	= { 798321, 279005, 203711 };	

  public _3931HowToUseStigma()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
			
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 203711)//Miriya
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else return defaultQuestStartDialog(env);
        
      }  
      return false;
    }
	
    
    int var = qs.getQuestVarById(0);
    
    if(qs.getStatus() == QuestStatus.REWARD)
		{
		 
     if(targetId == 203711 && player.getInventory().getItemCountByItemId(182206081) == 1)//Miriya
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(env, 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(env, 5);
		    else return defaultQuestEndDialog(env); 
     	}
		 return false;	
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
     if(targetId == 798321)//Koruchinerk
		{
      switch(env.getDialogId())
					{
					case 26:
					 if (var == 0)
            return sendQuestDialog(env, 1011);
           if (var == 1)
            return sendQuestDialog(env, 1352);
          case 34:
           if (var == 1)
            {
             if(QuestService.collectItemCheck(env, true))				
					   {
						  qs.setQuestVarById(0, var + 1);					
						  updateQuestStatus(env);
						  ItemService.addItems(player, Collections.singletonList(new QuestItems(182206080, 1)));
						  return sendQuestDialog(env, 10000);
					   }
					   else
						  return sendQuestDialog(env, 10001);
            }
          case 10000:
				  if (var == 0)
				  qs.setQuestVarById(0, var + 1);
				  updateQuestStatus(env);
          PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
          return true;
          }
		return false;				
    }else if(targetId == 279005 && player.getInventory().getItemCountByItemId(182206080) == 1 )//Kohrunerk
    {
      switch(env.getDialogId())
					{
					case 26:
					 if (var == 2)
					  return sendQuestDialog(env, 1693);
          case 10255:
				   if (var == 2)
				   player.getInventory().removeFromBagByItemId(182206080, 1);
           ItemService.addItems(player, Collections.singletonList(new QuestItems(182206081, 1)));
           PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
           qs.setStatus(QuestStatus.REWARD);
				   updateQuestStatus(env);
           return true;
          }
    
    
    }
    return false;
    }
  return false;
  }

	
	@Override
	public void register()
	{
		qe.setNpcQuestData(203711).addOnQuestStart(questId);	//Miriya
    qe.setQuestItemIds(182206080).add(questId); //Kohrunerks Belt
    qe.setQuestItemIds(182206081).add(questId);	//Stigma Manual	
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}
   
}
