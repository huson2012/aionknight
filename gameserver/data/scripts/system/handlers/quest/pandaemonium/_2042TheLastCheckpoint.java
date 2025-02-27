package quest.pandaemonium;


import gameserver.model.flyring.FlyRing;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.flyring.FlyRingTemplate;
import gameserver.quest.HandlerResult;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.QuestService;
import gameserver.world.WorldMapType;


/**
 * @author Hellboy, edited Rolandas
 *
 */
public class _2042TheLastCheckpoint extends QuestHandler
{
	private final static int	questId	= 2042;

	public _2042TheLastCheckpoint()
	{
		super(questId);
	}
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(204301).addOnTalkEvent(questId);
		qe.setNpcQuestData(204319).addOnTalkEvent(questId);
		qe.addOnQuestTimerEnd(questId);
		qe.addOnFlyThroughRing(questId);
	}

	@Override
	public HandlerResult onFlyThroughRingEvent(QuestCookie env, FlyRing ring)
	{
		FlyRingTemplate template = ring.getTemplate();

		if(template.getMap() != WorldMapType.MORHEIM.getId())
			return HandlerResult.UNKNOWN;

		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return HandlerResult.FAILED;

		int var = qs.getQuestVarById(0);
		if (var < 2 || var > 7)
			return HandlerResult.FAILED;
		
		if(ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_1") && var == 2
			|| ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_2") && var == 3
			|| ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_3") && var == 4
			|| ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_4") && var == 5
			|| ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_5") && var == 6)
		{
			qs.setQuestVarById(0, var + 1);
			updateQuestStatus(env);
		}
		else if(ring.getName().equals("MORHEIM_ICE_FORTRESS_220020000_6") && var == 7)
		{
			QuestService.questTimerEnd(env);
			qs.setQuestVarById(0, 9);
			updateQuestStatus(env);
		}
		return HandlerResult.SUCCESS;
	}

	@Override
	public boolean onQuestTimerEndEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		qs.setQuestVarById(0, 8);
		updateQuestStatus(env);
		return true;
	}
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(env.getTargetId())
			{
				case 204301:
				{
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
						case 10000:
							if(var == 0)
								return defaultCloseDialog(env, 0, 1);
					}
				}
				break;
				case 204319:
				{
					switch(env.getDialogId())
					{
						case -1:
							if(player.getQuestTimerOn())
								return sendQuestDialog(env, 3143);
						case 26:
							if(var == 1)
								return sendQuestDialog(env, 1352);
							else if(var == 9)
								return sendQuestDialog(env, 1693);
							else if(var == 8)
								return sendQuestDialog(env, 3057);
						case 1353:
							if(var == 1)
							{
								defaultQuestMovie(env, 89);
								return sendQuestDialog(env, 1353);
							}								
						case 10001:
							if (var == 1 || var == 8)
							{
								defaultCloseDialog(env, var, 2);
								QuestService.questTimerStart(env, 60);
								return true;
							}
						case 10255:
							if (var == 9)
								return defaultCloseDialog(env, 9, 8, true, false);
					}
				}
				break;
			}
		}
		return defaultQuestRewardDialog(env, 204301, 10002);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env, false);
	}
}
