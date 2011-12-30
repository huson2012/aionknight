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

public class _28405KexkrasPast extends QuestHandler {
    private final static int questId = 28405;

    public _28405KexkrasPast() {
        super(questId);
    }

    @Override
    public void register() {
        qe.setNpcQuestData(799558).addOnTalkEvent(questId);
		qe.setNpcQuestData(799557).addOnTalkEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestCookie env) {
        Player player = env.getPlayer();

        if (env.getTargetId() == 0)
            return defaultQuestStartItem(env);

        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null)
            return false;

        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            if (env.getTargetId() == 799558) {
                switch (env.getDialogId()) {
                    case 26:
                        if (var == 0) {
                            if (player.getInventory().getItemCountByItemId(182215014) != 0)
                                return sendQuestDialog(env, 1352);
                            else
                                return sendQuestDialog(env, 1354);
                        }
                    case 1353:
                        if (var == 0) {
                            player.getInventory().removeFromBagByItemId(182215014, 1);
                            ItemService.addItems(player, Collections.singletonList(new QuestItems(182215025, 1)));
                        }
                        return false;
                    case 10000:
                        if (var == 0) {
                            qs.setQuestVarById(0, var + 1);
                            updateQuestStatus(env);
                            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
                            return true;
                        }
                }
			}else if (env.getTargetId() == 799557) {
                switch (env.getDialogId()) {
                    case 26:
                        if (var == 1)
                            return sendQuestDialog(env, 2375);
                    case 1009:
                        defaultQuestRemoveItem(env, 182215025, 1);
                        return defaultCloseDialog(env, 1, 1, true, true);
                }
            }
        }
        return defaultQuestRewardDialog(env, 799557, 0);
    }
}