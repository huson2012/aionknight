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
package quest.pandaemonium;


import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;


/**
 * @author Hellboy
 * 
 */
public class _2947FollowingThrough extends QuestHandler {
	private final static int questId	= 2947;
	private final static int[] npc_ids = {204053, 204301, 204089, 700368, 700369, 700268};
	private final static int[] mob_ids = {212396, 212611, 212408, 213583, 213584};

	public _2947FollowingThrough() {
		super(questId);
	}

    @Override
    public void register() {
        qe.addQuestLvlUp(questId);
        qe.addOnQuestTimerEnd(questId);
        qe.addOnEnterWorld(questId);
        qe.setQuestMovieEndIds(167).add(questId);
        qe.setQuestMovieEndIds(168).add(questId);
        for (int npc_id : npc_ids) {
            qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
        }
        for (int mob_id : mob_ids) {
           qe.setNpcQuestData(mob_id).addOnKillEvent(questId);
        }
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        if (!super.defaultQuestOnDialogInitStart(env)) {
            return false;
        }

        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            switch (env.getTargetId()) {
                case 204053: {
                    switch (env.getDialogId()) {
                        case 26:
                            if (var == 0) {
                                return sendQuestDialog(env, 1011);
                            }
                        case 10010:
                            return defaultCloseDialog(env, 0, 1);
                        case 10011:
                            return defaultCloseDialog(env, 0, 4);
                        case 10012:
                            return defaultCloseDialog(env, 0, 9);
                        case 34:
                            if (var == 1 || var == 4 || var == 9) {
                                qs.setQuestVarById(0, 0);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 2375);
                            }
                    }
                }
                break;
                case 204301: {
                    switch (env.getDialogId()) {
                        case 26:
                            if (var == 1) {
                                return sendQuestDialog(env, 1352);
                            } else if (var == 2) {
                                return sendQuestDialog(env, 3398);
                            } else if (var == 7) {
                                return sendQuestDialog(env, 3739);
                            } else {
                                return defaultQuestItemCheck(env, 9, 0, true, 7, 4080);
                            }
                        case 10001:
                            return defaultCloseDialog(env, 1, 2);
                        case 1009:
                            if (var == 2) {
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 5);
                            }
                            if (var == 7) {
                                qs.setStatus(QuestStatus.REWARD);
                                updateQuestStatus(env);
                                return sendQuestDialog(env, 6);
                            }
                    }
                }
                break;
                case 204089: {
                    switch (env.getDialogId()) {
                        case 26:
                            if (var == 4) {
                                return sendQuestDialog(env, 1693);
                            } else if (var == 5) {
                                return sendQuestDialog(env, 2034);
                            }
                        case 10002:
                           if (defaultCloseDialog(env, 4, 5)) {
                                TeleportService.teleportTo(player, 320090000, 1, 276, 293, 163, (byte) 90, 3000);
                                return true;
                            } else {
                                return false;
                            }
                        case 10003:
                            if (var == 5) {
                                qs.setQuestVarById(0, 7);
                                updateQuestStatus(env);
                               PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
                                return true;
                            } else {
                                return false;
                            }
                    }
                }
                break;
                case 700368:
                case 700369:
                    if (env.getDialogId() == -1) {
                        return (defaultQuestUseNpc(env, 5, 6, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, false));
                    }
                    break;
                case 700268:
                    if (env.getDialogId() == -1) {
                        return (defaultQuestUseNpc(env, 9, 10, EmotionType.NEUTRALMODE2, EmotionType.START_LOOT, true));
                    }
                    break;
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (var == 2) {
                return defaultQuestRewardDialog(env, 204301, 3739);
            } else if (var == 7) {
                return defaultQuestRewardDialog(env, 204301, 3739, 1);
            } else if (var == 9) {
                return defaultQuestRewardDialog(env, 204301, 3739, 2);
            }
        }
        return false;
    }

    /*@Override
    public boolean onKillEvent(QuestCookie env) {
    if (defaultQuestOnKillEvent(env, 212396, 0, 3, 1) || defaultQuestOnKillEvent(env, 212611, 0, 3, 2)
    || defaultQuestOnKillEvent(env, 212408, 0, 3, 3) || defaultQuestOnKillEvent(env, 212409, 0, 3, 3)
    || defaultQuestOnKillEvent(env, 213583, 0, 10, 4) || defaultQuestOnKillEvent(env, 213584, 0, 10, 4)) {
    return true;
    } else {
    return false;
    }
    }*/

    @Override
    public boolean onKillEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }

        int targetId = 0;
        int var = 0;
        if (env.getVisibleObject() instanceof Npc) {
            targetId = ((Npc) env.getVisibleObject()).getNpcId();
        }
        switch (targetId) {
            case 212396:
                var = qs.getQuestVarById(1);
                if (var < 3) {
                    qs.setQuestVarById(1, var + 1);
                    updateQuestStatus(env);
                }
                break;
            case 212611:
                var = qs.getQuestVarById(2);
                if (var < 3) {
                    qs.setQuestVarById(2, var + 1);
                    updateQuestStatus(env);
                }
                break;
            case 212408:
                var = qs.getQuestVarById(3);
                if (var < 3) {
                    qs.setQuestVarById(3, var + 1);
                    updateQuestStatus(env);
                }
                break;
            case 213583:
            case 213584:
                var = qs.getQuestVarById(4);
                if (var < 9) {
                    qs.setQuestVarById(4, var + 1);
                    updateQuestStatus(env);
                } else if (var == 9) {
                    QuestService.questTimerEnd(env);
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 168));
                }
        }
        return false;
    }

    @Override
    public boolean onLvlUpEvent(QuestCookie env) {
        return defaultQuestOnLvlUpEvent(env, false);
    }

    @Override
    public boolean onQuestTimerEndEvent(QuestCookie env) {
        Player player = env.getPlayer();
       QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);

            if (var < 10) {
                TeleportService.teleportTo(player, 120010000, 1006.1f, 1526, 222.2f, 3000);
                qs.setQuestVar(4);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onEnterWorldEvent(QuestCookie env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVars().getQuestVars();
            if (var == 5) {
                if (player.getWorldId() != 320090000) {
                    qs.setQuestVar(4); 
                    updateQuestStatus(env);
                } else {
                    PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 167));
                    QuestService.questTimerStart(env, 240);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onMovieEndEvent(QuestCookie env, int movieId) {
        if (movieId != 168) {
            return false;
        }
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null || qs.getStatus() != QuestStatus.START) {
            return false;
        }
        TeleportService.teleportTo(player, 120010000, 1006.1f, 1526, 222.2f, 3000);
        qs.setQuestVarById(4, 10);
        updateQuestStatus(env);
        return true;
    }

    @Override
    public void QuestUseNpcInsideFunction(QuestCookie env) {
        Player player = env.getPlayer();
        if (env.getTargetId() == 700368) {
            TeleportService.teleportTo(player, 320090000, 1, 276, 293, 163, (byte) 90);
        } else if (env.getTargetId() == 700369) {
            TeleportService.teleportTo(player, 120010000, 1, 982, 1556, 210, (byte) 90);
        }
    }
}
