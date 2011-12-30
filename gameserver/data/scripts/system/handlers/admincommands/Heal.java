/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
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

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Heal extends AdminCommand
{
	public Heal()
	{
		super("heal");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_HEAL)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		String syntax = "//heal : Full HP and MP\n" + "//heal dp : Full DP, must be used on a players only !" + "//heal fp";

		VisibleObject target = admin.getTarget();

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "No target selected");
			return;
		}

		Creature creature = (Creature) target;

		if (params == null || params.length < 1)
		{
			if (target instanceof Creature)
			{
				creature.getLifeStats().increaseHp(TYPE.HP, creature.getLifeStats().getMaxHp()+1); 
				creature.getLifeStats().increaseMp(TYPE.MP, creature.getLifeStats().getMaxMp()+1);
				PacketSendUtility.sendMessage(admin, creature.getName() + " HP and MP has been fully refreshed !");
			}
		}
		else if (params[0].equals("dp") && target instanceof Player)
		{
			((Player) creature).getCommonData().setDp(creature.getGameStats().getCurrentStat(StatEnum.MAXDP));
			
			PacketSendUtility.sendMessage(admin, creature.getName() + " DP has been fully refreshed !");
		}
		else if (params[0].equals("fp") && target instanceof Player)
		{
			((Player) creature).getLifeStats().setCurrentFp(((Player) creature).getLifeStats().getMaxFp());
			
			PacketSendUtility.sendMessage(admin, creature.getName() + " FP has been fully refreshed !");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, syntax);
			return;
		}
	}
}