package quest.reshanta;


import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.utils.PacketSendUtility;


/**
 * author : Altaress
 */
public class _2773SignedSealedDelivered extends QuestHandler
{
    private final static int    questId    = 2773;

    public _2773SignedSealedDelivered()
    {
        super(questId);
    }
    
    @Override
    public void register()
    {
        qe.setNpcQuestData(279019).addOnQuestStart(questId);
        qe.setNpcQuestData(279019).addOnTalkEvent(questId);
        qe.setNpcQuestData(798110).addOnTalkEvent(questId);
        qe.setNpcQuestData(204734).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env)
    {
        final Player player = env.getPlayer();
        int targetId = 0;
        if(env.getVisibleObject() instanceof Npc)
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if(targetId == 279019)
        {
            if(qs == null || qs.getStatus() == QuestStatus.NONE)
            {
                if(env.getDialogId() == 26)
                    return sendQuestDialog(env, 1011);
                else
                    return defaultQuestStartDialog(env);
            }
            else if(qs != null && qs.getStatus() == QuestStatus.START)
            {
                if(env.getDialogId() == 26)
                    return sendQuestDialog(env, 2375);
                else if(env.getDialogId() == 1009)
                {
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                }
                else
                    return defaultQuestEndDialog(env);
            }
            else if(qs != null && qs.getStatus() == QuestStatus.REWARD)
            {
                return defaultQuestEndDialog(env);
            }
        }
        else if(targetId == 798110)
        {
            if(qs != null && qs.getStatus() == QuestStatus.START && qs.getQuestVarById(0) == 0)
            {
                if(env.getDialogId() == 26)
                    return sendQuestDialog(env, 1352);
                else if(env.getDialogId() == 10000)
                {
                    qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
                    updateQuestStatus(env);
                    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                    return true;
                }
                else
                    return defaultQuestStartDialog(env);
            }
        }
        else if(targetId == 204734)
        {
            if(qs != null)
            {
                if(env.getDialogId() == 26 && qs.getStatus() == QuestStatus.START)
                    return sendQuestDialog(env, 2375);
                else if(env.getDialogId() == 1009)
                {
                    qs.setQuestVar(3);
                    qs.setStatus(QuestStatus.REWARD);
                    updateQuestStatus(env);
                    return defaultQuestEndDialog(env);
                }
                else
                    return defaultQuestEndDialog(env);
            }
        }
        return false;
    }
}
