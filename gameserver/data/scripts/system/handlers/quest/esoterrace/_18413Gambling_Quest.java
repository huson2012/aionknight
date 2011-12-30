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

package quest.esoterrace;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;

public class _18413Gambling_Quest extends QuestHandler
{
    private final static int questId = 18413;
	
    public _18413Gambling_Quest()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
		
        if(qs == null || qs.getStatus() == QuestStatus.NONE)
        {
	        if(targetId == 799586) //Derna.
	        {
		       if(env.getDialogId() == 26)
		       {
			      return sendQuestDialog(env, 1011);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		
        if (qs == null)
            return false;
			
        if(qs == null || qs.getStatus() == QuestStatus.COMPLETE) 
        {
	        if(targetId == 799586) //Derna.
	        {
		       if(env.getDialogId() == 26)
		       {
			     return sendQuestDialog(env, 1011);
		       }
		       else
			     return defaultQuestStartDialog(env);
	        }
        }
		
        if (qs.getStatus() == QuestStatus.START)
		{
            switch (targetId)
			{
                case 799586: //Derna.
                    switch (env.getDialogId())
					{
                        case 26:
                            return sendQuestDialog(env, 2375);
                        case 2034:
                            return sendQuestDialog(env, 2034);
                        case 34:
                        //Collect Drazma Heart (1)
                            if (QuestService.collectItemCheck(env, true))
	          {                 
			                    player.getInventory().removeFromBagByItemId(182215009, 2); //Drazma Heart.
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else
							{
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                //
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 799586) //Derna.
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(799586).addOnQuestStart(questId); //Derna.
        qe.setNpcQuestData(799586).addOnTalkEvent(questId); //Derna.
    }
}