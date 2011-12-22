/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
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

package admincommands;

import gameserver.model.drop.DropItem;
import gameserver.model.drop.DropList;
import gameserver.model.drop.DropTemplate;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.DropService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

import java.util.Set;

public class DropInfo extends AdminCommand {

	private DropList dropList;

	public DropInfo() {
		super("dropinfo");
	}

	@Override
	public void executeCommand(Player player, String[] params) {

		VisibleObject visibleObject = player.getTarget();

		if (visibleObject == null) {
			PacketSendUtility.sendMessage(player, "You should select target npc first.");
			return;
		}

		if (visibleObject instanceof Npc) {
			dropList = DropService.getInstance().getDropList();

			Set<DropTemplate> templates = dropList.getDropsFor(((Npc) visibleObject).getNpcId());

			if (templates != null) {
				PacketSendUtility.sendMessage(player, "[Drop Info about npc]\n");
				for (DropTemplate dropTemplate : templates) {
					DropItem dropItem = new DropItem(dropTemplate);
					PacketSendUtility.sendMessage(player, "[item:" + dropItem.getDropTemplate().getItemId() + "]" + "	Rate: "
						+ dropItem.getDropTemplate().getChance());
				}
			}
		}

	}
}