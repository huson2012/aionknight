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
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _3932StopTheShulacks extends QuestHandler
{
	private final static int	questId	= 3932;
	private final static int[]	npc_ids	= { 203711, 204656};       

  public _3932StopTheShulacks()
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
					return sendQuestDialog(env, 1011);
				else return defaultQuestStartDialog(env);
        
      }  
      return false; 
    }
	
    
    int var = qs.getQuestVarById(0);
    
    if(qs.getStatus() == QuestStatus.REWARD)
     {
      if(targetId == 203711)//Miriya
			{
			  return defaultQuestEndDialog(env);
      }
      return false;
     }
    else if(qs.getStatus() == QuestStatus.START)
		{
		 
     if(targetId == 203711 && var == 1)//Miriya
			{
			  switch(env.getDialogId())
        {
         case 26:
          return sendQuestDialog(env, 2375);
         case 34:
          if(QuestService.collectItemCheck(env, true))				
					   {
						  qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
             return sendQuestDialog(env, 5);
            }
					else
						  return sendQuestDialog(env, 2716);
         }
       
      }	
      else if(targetId == 204656 && var == 0)//Maloren
      {
        switch(env.getDialogId())
					{
					case 26:
					 return sendQuestDialog(env, 1352);
          case 10000:
				   PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
           qs.setQuestVarById(0, var + 1);
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
    for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	 
	}
  
} 
