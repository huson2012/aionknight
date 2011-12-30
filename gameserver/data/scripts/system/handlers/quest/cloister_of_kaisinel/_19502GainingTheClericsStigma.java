/**
 * Èãğîâîé ıìóëÿòîğ îò êîìàíäû ğàçğàáîò÷èêîâ 'Aion-Knight Dev. Team' ÿâëÿåòñÿ ñâîáîäíûì 
 * ïğîãğàììíûì îáåñïå÷åíèåì; âû ìîæåòå ğàñïğîñòğàíÿòü è/èëè èçìåíÿòü åãî ñîãëàñíî óñëîâèÿì 
 * Ñòàíäàğòíîé Îáùåñòâåííîé Ëèöåíçèè GNU (GNU GPL), îïóáëèêîâàííîé Ôîíäîì ñâîáîäíîãî 
 * ïğîãğàììíîãî îáåñïå÷åíèÿ (FSF), ëèáî Ëèöåíçèè âåğñèè 3, ëèáî (íà âàøå óñìîòğåíèå) ëşáîé 
 * áîëåå ïîçäíåé âåğñèè.
 *
 * Ïğîãğàììà ğàñïğîñòğàíÿåòñÿ â íàäåæäå, ÷òî îíà áóäåò ïîëåçíîé, íî ÁÅÇ ÊÀÊÈÕ ÁÛ ÒÎ ÍÈ ÁÛËÎ 
 * ÃÀĞÀÍÒÈÉÍÛÕ ÎÁßÇÀÒÅËÜÑÒÂ; äàæå áåç êîñâåííûõ  ãàğàíòèéíûõ  îáÿçàòåëüñòâ, ñâÿçàííûõ ñ 
 * ÏÎÒĞÅÁÈÒÅËÜÑÊÈÌÈ ÑÂÎÉÑÒÂÀÌÈ è ÏĞÈÃÎÄÍÎÑÒÜŞ ÄËß ÎÏĞÅÄÅËÅÍÍÛÕ ÖÅËÅÉ. Äëÿ ïîäğîáíîñòåé ñìîòğèòå 
 * Ñòàíäàğòíóş Îáùåñòâåííóş Ëèöåíçèş GNU.
 * 
 * Âû äîëæíû áûëè ïîëó÷èòü êîïèş Ñòàíäàğòíîé Îáùåñòâåííîé Ëèöåíçèè GNU âìåñòå ñ ıòîé ïğîãğàììîé. 
 * Åñëè ıòî íå òàê, íàïèøèòå â Ôîíä Ñâîáîäíîãî ÏÎ (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Âåá-càéò ğàçğàáîò÷èêîâ : http://aion-knight.ru
 * Ïîääåğæêà êëèåíòà èãğû : Aion 2.7 - 'Àğåíà Ñìåğòè' (Èííîâà)
 * Âåğñèÿ ñåğâåğíîé ÷àñòè : Aion-Knight 2.7 (Beta version)
 */

package quest.cloister_of_kaisinel;

import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;

public class _19502GainingTheClericsStigma extends QuestHandler
{
	private final static int	questId	= 19502;
	private final int			skillId = 1032;
	private final int			mainNpcId = 798501;

	public _19502GainingTheClericsStigma()
	{
		super(questId);
	}

	@Override
	public boolean onDialogEvent(QuestCookie env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(targetId == mainNpcId)
		{
			if(qs == null || qs.getStatus() == QuestStatus.NONE)
			{
				if(env.getDialogId() == 26)
					return sendQuestDialog(env, 4762);
				else
					return defaultQuestStartDialog(env);
			}
			else if(qs.getStatus() == QuestStatus.REWARD)
			{
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onSkillUseEvent(QuestCookie env, int skillUsedId)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		if(qs.getStatus() != QuestStatus.START)
			return false;

		if(skillUsedId == skillId)
		{
			if(var >= 0 && var < 9)
			{
				qs.setQuestVarById(0, var+1);
				updateQuestStatus(env);
				return true;
			}
			else if(var == 9)
			{
				qs.setQuestVarById(0, var+1);
				updateQuestStatus(env);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(mainNpcId).addOnQuestStart(questId);
		qe.setNpcQuestData(mainNpcId).addOnTalkEvent(questId);
		qe.setQuestSkillIds(skillId).add(questId);
	}
}