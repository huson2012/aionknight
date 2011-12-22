/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
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