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

package quest.altgard;

import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.quest.QuestItems;
import gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.quest.handlers.QuestHandler;
import gameserver.quest.model.QuestCookie;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import gameserver.services.ItemService;
import gameserver.services.QuestService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;

import java.util.Collections;

public class _2013ADangerousCrop extends QuestHandler
{

	private final static int	questId	= 2013;

	public _2013ADangerousCrop()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(203605).addOnTalkEvent(questId);
		qe.setNpcQuestData(700096).addOnTalkEvent(questId);
		qe.setQuestEnterZone(ZoneName.MUMU_FARMLAND_220030000).add(questId);
	}

	@Override
	public boolean onDialogEvent(final QuestCookie env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 203605:
					switch(env.getDialogId())
					{
						case 26:
							if(var == 0)
								return sendQuestDialog(env, 1011);
							else if(var == 2)
								return sendQuestDialog(env, 1352);
							else if(var == 8)
								return sendQuestDialog(env, 1693);
							else if(var == 9)
								return sendQuestDialog(env, 2034);
						case 10000:
						case 10001:
						case 10002:
							if(var == 0 || var == 2 || var == 8)
							{
								if (var == 2)
									if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182203012, 1))))
										return true;
								if (var == 8)
									player.getInventory().removeFromBagByItemId(182203012, 1);
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case 34:
							if(var == 9)
							{
								if(QuestService.collectItemCheck(env, true))
								{
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									return sendQuestDialog(env, 5);
								}
								else
									return sendQuestDialog(env, 2120);
							}
					}
					break;
				case 700096:
					switch(env.getDialogId())
					{
						case -1:
							if(var >= 3 && var < 6)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
									1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
									targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable(){
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
											targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
											targetObjectId), true);
										if (var == 5)
											qs.setQuestVarById(0, 8);
										else
											qs.setQuestVarById(0, var + 1);
										updateQuestStatus(env);
										
									}
								}, 3000);
								return true;
							}
					}		
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203605)
				return defaultQuestEndDialog(env);
		}
		return false;
	}

	@Override
	public boolean onEnterZoneEvent(QuestCookie env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(zoneName != ZoneName.MUMU_FARMLAND_220030000)
			return false;
		if(qs == null)
			return false;
		if (qs.getQuestVarById(0) == 1)
		{
			qs.setQuestVarById(0, 2);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}

	@Override
	public boolean onLvlUpEvent(QuestCookie env)
	{
		return defaultQuestOnLvlUpEvent(env);
	}
}