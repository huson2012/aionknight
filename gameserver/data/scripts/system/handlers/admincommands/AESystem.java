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

import commons.utils.AEInfos;
import gameserver.ShutdownHook;
import gameserver.ShutdownHook.ShutdownMode;
import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.utils.AEVersions;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.utils.chathandlers.AdminCommand;

import java.util.List;

public class AESystem extends AdminCommand
{

	public AESystem()
	{
		super("sys");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_SYSTEM)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "Usage: //sys <info | memory | gc | restart <countdown time> <announce delay> | shutdown <countdown time> <announce delay>");
			return;
		}

		if (params[0].equals("info"))
		{
			// Time
			PacketSendUtility.sendMessage(admin, "System Informations at: " + AEInfos.getRealTime().toString());

			// Version Infos
			for (String line : AEVersions.getFullVersionInfo())
				PacketSendUtility.sendMessage(admin, line);

			// OS Infos
			for (String line : AEInfos.getOSInfo())
				PacketSendUtility.sendMessage(admin, line);

			// CPU Infos
			for (String line : AEInfos.getCPUInfo())
				PacketSendUtility.sendMessage(admin, line);

			// JRE Infos
			for (String line : AEInfos.getJREInfo())
				PacketSendUtility.sendMessage(admin, line);

			// JVM Infos
			for (String line : AEInfos.getJVMInfo())
				PacketSendUtility.sendMessage(admin, line);
		}

		else if (params[0].equals("memory"))
		{
			// Memory Infos
			for(String line : AEInfos.getMemoryInfo())
				PacketSendUtility.sendMessage(admin, line);
		}

		else if (params[0].equals("gc"))
		{
			long time = System.currentTimeMillis();
			PacketSendUtility.sendMessage(admin, "RAM Used (Before): "
				+ ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576));
			System.gc();
			PacketSendUtility.sendMessage(admin, "RAM Used (After): "
				+ ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576));
			System.runFinalization();
			PacketSendUtility.sendMessage(admin, "RAM Used (Final): "
				+ ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576));
			PacketSendUtility.sendMessage(admin, "Garbage Collection and Finalization finished in: "
				+ (System.currentTimeMillis() - time) + " milliseconds...");
		}
		else if (params[0].equals("shutdown"))
		{
			try
			{
				int val = Integer.parseInt(params[1]);
				int announceInterval = Integer.parseInt(params[2]);
				ShutdownHook.getInstance().doShutdown(val, announceInterval, ShutdownMode.SHUTDOWN);
				PacketSendUtility.sendMessage(admin, "Server will shutdown in " + val + " seconds.");
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "Numbers only!");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Numbers only!");
			}
		}
		else if (params[0].equals("restart"))
		{
			try
			{
				int val = Integer.parseInt(params[1]);
				int announceInterval = Integer.parseInt(params[2]);
				ShutdownHook.getInstance().doShutdown(val, announceInterval, ShutdownMode.RESTART);
				PacketSendUtility.sendMessage(admin, "Server will restart in " + val + " seconds.");
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				PacketSendUtility.sendMessage(admin, "Numbers only!");
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Numbers only!");
			}
		}
		else if (params[0].equals("threadpool"))
		{
			List<String> stats = ThreadPoolManager.getInstance().getStats();
			for (String stat : stats)
			{
				PacketSendUtility.sendMessage(admin, stat.replaceAll("\t", ""));
			}
		}
	}
}