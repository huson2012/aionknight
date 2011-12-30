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

package admincommands;

import gameserver.configs.administration.AdminConfig;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.QuestStateList;
import gameserver.model.templates.GuildTemplate;
import gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.GuildService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

import java.sql.Timestamp;
import java.util.Calendar;

public class QuestCommand extends AdminCommand
{
	public QuestCommand()
	{
		super("quest");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() >= AdminConfig.COMMAND_QUESTCOMMAND)
		{
			if (params == null || params.length < 1)
			{
				PacketSendUtility.sendMessage(admin, "syntax //quest <start | set | show | delete>");
				return;
			}
	
			Player target = null;
			VisibleObject creature = admin.getTarget();
			if (creature instanceof Player)
			{
				target = (Player)creature;
			}
	
			if (target == null)
			{
				PacketSendUtility.sendMessage(admin, "Incorrect target!");
				return;
			}
	
			if (params[0].equals("start"))
			{
				if (params.length != 2)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest start <quest id>");
					return;
				}
				int id;
				try
				{
					id = Integer.valueOf(params[1]);
				}
				catch (NumberFormatException e)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest start <quest id>");
					return;
				}
	
				QuestCookie env = new QuestCookie(null, target, id, 0);
				
				GuildTemplate guildTemplate = DataManager.GUILDS_DATA.getGuildTemplateByQuestId(id);
				if(guildTemplate != null)
				{
					admin.getGuild().setGuildId(guildTemplate.getGuildId());
					admin.getGuild().setCompleteTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
					admin.getGuild().setCurrentQuest(id);
					PacketSendUtility.sendPacket(admin, new SM_QUEST_ACCEPTED(6, id));
				}
				else
				{
					if (QuestService.startQuest(env, QuestStatus.START))
					{
						PacketSendUtility.sendMessage(admin, "Quest started.");
					}
					else
					{
						PacketSendUtility.sendMessage(admin, "Quest not started.");
					}
				}
			}
			else if (params[0].equals("delete"))
			{
				if (params.length != 2)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest delete <quest id>");
					return;
				}
				int id;
				try
				{
					id = Integer.valueOf(params[1]);
				}
				catch (NumberFormatException e)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest delete <quest id>");
					return;
				}
	
				QuestStateList list = admin.getQuestStateList();
				if (list == null || list.getQuestState(id) == null)
				{
					PacketSendUtility.sendMessage(admin, "Quest not deleted.");
				}
				else
				{
					QuestState qs = list.getQuestState(id);
					qs.setQuestVar(0);
					qs.setCompliteCount(0);
					qs.setStatus(null);
					qs.setPersistentState(PersistentState.DELETED);
					GuildService.getInstance().deleteDaily(admin, id);
					PacketSendUtility.sendMessage(admin, "Quest deleted. Please logout.");
				}
			}
			else if (params[0].equals("set") && params.length == 4)
			{
				int questId,var;
				QuestStatus questStatus;
				try
				{
					questId = Integer.valueOf(params[1]);
					questStatus = QuestStatus.valueOf(params[2]);
					var = Integer.valueOf(params[3]);
				}
				catch (NumberFormatException e)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
					return;
				}
				catch (IllegalArgumentException e)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
					return;
				}
				QuestState qs = target.getQuestStateList().getQuestState(questId);
				if (qs == null)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest set <quest id status var>");
					return;
				}
				qs.setStatus(questStatus);
				qs.setQuestVar(var);
				PacketSendUtility.sendPacket(target, new SM_QUEST_ACCEPTED(2, questId, qs.getStatus(), qs.getQuestVars().getQuestVars()));
			}
			else if (params[0].equals("set") && params.length == 5)
			{
				int questId,varId,var; 
				QuestStatus questStatus; 

				try 
				{ 
					questId = Integer.valueOf(params[1]); 
					questStatus = QuestStatus.valueOf(params[2]); 
					varId = Integer.valueOf(params[3]); 
					var = Integer.valueOf(params[4]); 
				} 
				catch (NumberFormatException e) 
				{ 
					PacketSendUtility.sendMessage(admin, "syntax //quest set <questId status varId var>"); 
					return; 
				} 
				QuestState qs = target.getQuestStateList().getQuestState(questId); 
				if (qs == null) 
				{ 
					PacketSendUtility.sendMessage(admin, "syntax //quest set <questId status varId var>"); 
					return; 
				} 
				qs.setStatus(questStatus); 
				qs.setQuestVarById(varId,var); 
				PacketSendUtility.sendPacket(target, new SM_QUEST_ACCEPTED(2, questId, qs.getStatus(), qs.getQuestVars().getQuestVars())); 
			}
			else if (params[0].equals("show"))
			{
				if (params.length != 2)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest show <quest id>");
					return;
				}
				ShowQuestInfo(target, admin, params[1]);
			}
			else 
				PacketSendUtility.sendMessage(admin, "syntax //quest <start | set | show | delete>");
			return;
		}
		else if(admin.getAccessLevel() >= AdminConfig.COMMAND_QUESTCOMMANDPLAYERS)
		{
			if (params == null || params.length < 1)
			{
				PacketSendUtility.sendMessage(admin, "syntax //quest <show | restart>");
				return;
			}
	
			if (params[0].equals("restart"))
			{
				if (params.length != 2)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest restart <quest id>");
					return;
				}
				int id;
				try
				{
					id = Integer.valueOf(params[1]);
				}
				catch (NumberFormatException e)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest restart <quest id>");
					return;
				}
				
				QuestState qs = admin.getQuestStateList().getQuestState(id);

				if (qs == null || id == 1006 || id == 2008)
				{
					PacketSendUtility.sendMessage(admin, "Quest "+id+" can't be restarted");
					return;
				}
	
				if (qs.getStatus() == QuestStatus.START || qs.getStatus() == QuestStatus.REWARD)
				{
					if(qs.getQuestVarById(0) != 0)
					{
						qs.setStatus(QuestStatus.START);
						qs.setQuestVar(0);
						PacketSendUtility.sendPacket(admin, new SM_QUEST_ACCEPTED(2, id, qs.getStatus(), qs.getQuestVars().getQuestVars()));
						PacketSendUtility.sendMessage(admin, "Quest "+id+" restarted");
					}
					else
						PacketSendUtility.sendMessage(admin, "Quest "+id+" can't be restarted");
				}
				else
				{
					PacketSendUtility.sendMessage(admin, "Quest "+id+" can't be restarted");
				}
			}
			else if (params[0].equals("show"))
			{
				if (params.length != 2)
				{
					PacketSendUtility.sendMessage(admin, "syntax //quest show <quest id>");
					return;
				}
				ShowQuestInfo(admin, admin, params[1]);
			}
			else 
				PacketSendUtility.sendMessage(admin, "syntax //quest <show | restart>");
			return;
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
	}
	
	private void ShowQuestInfo(Player player, Player admin, String param)
	{
		int id;
		try
		{
			id = Integer.valueOf(param);
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "syntax //quest show <quest id>");
			return;
		}
		QuestState qs = player.getQuestStateList().getQuestState(id);
		if (qs == null)
		{
			PacketSendUtility.sendMessage(admin, "Quest state: NULL");
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 5; i++)
				sb.append(Integer.toString(qs.getQuestVarById(i)) + " ");
			PacketSendUtility.sendMessage(admin, "Quest state: "+qs.getStatus().toString()+"; vars: "
				+sb.toString()+qs.getQuestVarById(5));
			sb.setLength(0);
			sb = null;
		}		
	}
}