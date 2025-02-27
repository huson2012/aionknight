package quest.relic;


import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.QuestTemplate;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;

public class _1849RelicRewardAncientGobletCore extends QuestHandler {
    private final static int questId = 1849;

    public _1849RelicRewardAncientGobletCore() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(279058).addOnQuestStart(questId); //Amarunerk
        qe.setNpcQuestData(279058).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        final Player player = env.getPlayer();
        int targetId = 0;
        if (env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (targetId == 279058) {
            if (qs == null || qs.getStatus() == QuestStatus.NONE || (qs.getStatus() == QuestStatus.COMPLETE && (qs.getCompleteCount() <= template.getMaxRepeatCount()))) {
                if (env.getDialogId() == 53) {
                    if (player.getCommonData().getLevel() >= 30) {
                        QuestService.startQuest(env, QuestStatus.START);
                        return sendQuestDialog(env, 1011);
                    } else
                        return sendQuestDialog(env, 3398);
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0) {
                if (env.getDialogId() == -1)
                    return sendQuestDialog(env, 1011);
                else if (env.getDialogId() == 1011) {
                    if (player.getInventory().getItemCountByItemId(186000058) > 0) {
                        player.getInventory().removeFromBagByItemId(186000058, 1);
                        qs.setQuestVar(1);
                        qs.setStatus(QuestStatus.REWARD);
                        qs.setCompliteCount(0);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 5);
                    } else
                        return sendQuestDialog(env, 1009);
                } else if (env.getDialogId() == 1352) {
                    if (player.getInventory().getItemCountByItemId(186000057) > 0) {
                        player.getInventory().removeFromBagByItemId(186000057, 1);
                        qs.setQuestVar(2);
                        qs.setStatus(QuestStatus.REWARD);
                        qs.setCompliteCount(0);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 6);
                    } else
                        return sendQuestDialog(env, 1009);
                } else if (env.getDialogId() == 1693) {
                    if (player.getInventory().getItemCountByItemId(186000056) > 0) {
                        player.getInventory().removeFromBagByItemId(186000056, 1);
                        qs.setQuestVar(3);
                        qs.setStatus(QuestStatus.REWARD);
                        qs.setCompliteCount(0);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 7);
                    } else
                        return sendQuestDialog(env, 1009);
                } else if (env.getDialogId() == 2034) {
                    if (player.getInventory().getItemCountByItemId(186000055) > 0) {
                        player.getInventory().removeFromBagByItemId(186000055, 1);
                        qs.setQuestVar(4);
                        qs.setStatus(QuestStatus.REWARD);
                        qs.setCompliteCount(0);
                        updateQuestStatus(env);
                        return sendQuestDialog(env, 8);
                    } else
                        return sendQuestDialog(env, 1009);
                }
            } else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
                int var = qs.getQuestVarById(0);
                switch (env.getDialogId()) {
                    case -1:
                        if (var == 1)
                            return sendQuestDialog(env, 5);
                        else if (var == 2)
                            return sendQuestDialog(env, 6);
                        else if (var == 3)
                            return sendQuestDialog(env, 7);
                        else if (var == 4)
                            return sendQuestDialog(env, 8);
                    case 17:
                        QuestService.questFinish(env, qs.getQuestVars().getQuestVars() - 1);
                        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                        return true;
                }
            }
        }

        return false;
    }
}
