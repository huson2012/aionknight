package quest.theobomos;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.model.templates.bonus.AbstractInventoryBonus;
import ru.aionknight.gameserver.model.templates.bonus.InventoryBonusType;
import ru.aionknight.gameserver.model.templates.bonus.RedeemBonus;
import ru.aionknight.gameserver.quest.HandlerResult;
import ru.aionknight.gameserver.quest.handlers.QuestHandler;
import ru.aionknight.gameserver.quest.model.QuestCookie;
import ru.aionknight.gameserver.quest.model.QuestState;
import ru.aionknight.gameserver.quest.model.QuestStatus;
import ru.aionknight.gameserver.services.QuestService;


/**
 *
 * @author Rolandas, HellBoy
 */
public class _3074DangerousProbability extends QuestHandler
{
	private final static int questId = 3074;
	private final static int	Items[][] =
	{{1011, 1352, 1693},
	{1000, 5000, 25000}};

	public _3074DangerousProbability()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798193).addOnTalkEvent(questId);
		qe.setQuestBonusType(InventoryBonusType.REDEEM).add(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		int	removeKinahCount = 0;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		int var = 0;
		
		if(qs != null)
			var = qs.getQuestVarById(1);
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE || qs.getStatus() == QuestStatus.COMPLETE)
		{
			if(env.getTargetId() == 798193)
			{
				switch(env.getDialogId())
				{
					case 26:
						if(QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
							return sendQuestDialog(env, 1011);
						else
							return sendQuestDialog(env, 3398);
					case 1011:
					case 1352:
					case 1693:
						int i = 0;
						for(int id: Items[0])
						{
							if(id == env.getDialogId())
								break;
							i++;
						}
						removeKinahCount = Items[1][i];
						var = i;
						
						if(player.getInventory().getKinahCount() >= removeKinahCount && player.getInventory().getItemCountByItemId(186000037) > 0)
						{
							if(qs == null)
							{
								qs = new QuestState(questId, QuestStatus.REWARD, 0, 0);
								player.getQuestStateList().addQuest(questId, qs);
							}
							else
								qs.setStatus(QuestStatus.REWARD);
							qs.setQuestVarById(1, var);
							
							return sendQuestDialog(env, var + 5);
						}
						else
							return sendQuestDialog(env, 1009);
				}
			}
		}
		
		if(qs == null)
			return false;
		
		return defaultQuestRewardDialog(env, 798193, 0, var);
	}

	@Override
	public HandlerResult onBonusApplyEvent(QuestCookie env, int index, AbstractInventoryBonus bonus)
	{
		if(!(bonus instanceof RedeemBonus))
			return HandlerResult.UNKNOWN;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null && qs.getStatus() == QuestStatus.REWARD)
		{
			if(index == 0 && qs.getQuestVarById(1) == 0 ||
				index == 1 && qs.getQuestVarById(1) == 1 ||
				index == 2 && qs.getQuestVarById(1) == 2)
				return HandlerResult.SUCCESS;
		}
		return HandlerResult.FAILED;
	}
}
