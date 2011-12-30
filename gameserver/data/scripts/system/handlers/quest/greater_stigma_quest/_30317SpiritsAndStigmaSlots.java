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

public class _30317SpiritsAndStigmaSlots extends QuestHandler
{
    private final static int questId = 30317;

    public _30317SpiritsAndStigmaSlots()
	{
        super(questId);
    }
	
    @Override
    public boolean onDialogEvent(QuestCookie env)
	{
        // Instanceof
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);

        // ------------------------------------------------------------
        // NPC Quest :
        // 0 - Start to //Garath
        if (qs == null || qs.getStatus() == QuestStatus.NONE)
		{
            if (targetId == 798208) //Garath
			{
                // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                if (env.getDialogId() == 26)
                    // Send HTML_PAGE_SELECT_NONE to eddit-HtmlPages.xml
                    return sendQuestDialog(env, 4762);
                else
                    return defaultQuestStartDialog(env);
            }
        }

        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START)
		{
            switch (targetId)
			{
                case 799322: //Herka
                    switch (env.getDialogId())
					{
                        // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                        case 26:
                        // Send select1 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 1011);
                        // Get HACTION_SETPRO1 in the eddit-HyperLinks.xml
                        case 10000:
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                    }
                case 799506: //Faithful Respondent Utra
                    if (var == 1)
					{
                        switch (env.getDialogId())
						{
                            // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                            case 26:
                            // Send select2 to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 1352);
                            // Get HACTION_SETPRO2 in the eddit-HyperLinks.xml
                            case 10001:
                                qs.setQuestVarById(0, var + 1);
                                updateQuestStatus(env);
                                PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                                return true;
                        }
                    }
                    // Report The Result To Garath.
                case 798208: //Garath
                    switch (env.getDialogId())
					{
                        // Get HACTION_QUEST_SELECT in the eddit-HyperLinks.xml
                        case 26:
                        // Send select1 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 2375);
                         case 2034:
                        // Send select2 to eddit-HtmlPages.xml
                            return sendQuestDialog(env, 2034);
                        // Get HACTION_CHECK_USER_HAS_QUEST_ITEM in the eddit-HyperLinks.xml
                        case 34:
                        // Collect Spirit's Incense Burner (1)
                        // Collect Scroll Of Repose (1)
                            if (QuestService.collectItemCheck(env, true))
	          {
                                player.getInventory().removeFromBagByItemId(182209718, 1);
                                player.getInventory().removeFromBagByItemId(182209719, 1);
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            } else {
                                // Send check_user_item_fail to eddit-HtmlPages.xml
                                return sendQuestDialog(env, 2716);
                            }
                    }
                    break;
                // No match
                default:
                    return defaultQuestStartDialog(env);
            }
        } else if (qs.getStatus() == QuestStatus.REWARD)
		{
            if(targetId == 798208) //Garath
                return defaultQuestEndDialog(env);
        }
        return false;
    }
	
    @Override
    public void register()
	{
        qe.setNpcQuestData(798208).addOnQuestStart(questId); //Garath
        qe.setNpcQuestData(799322).addOnTalkEvent(questId); //Herka
		qe.setNpcQuestData(799506).addOnTalkEvent(questId); //Faithful Respondent Utra
		qe.setNpcQuestData(798208).addOnTalkEvent(questId); //Garath
    }
}