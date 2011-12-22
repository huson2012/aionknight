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

package usercommands;

import gameserver.model.Race;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.StatEnum;
import gameserver.model.group.PlayerGroup;
import gameserver.model.legion.Legion;
import gameserver.network.aion.serverpackets.SM_QUESTIONNAIRE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.Util;
import gameserver.utils.chathandlers.UserCommand;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.World;

import java.util.Iterator;
import java.util.List;

public class PlayerInfo extends UserCommand {
    public PlayerInfo() {
        super("playerinfo");
    }

    @Override
    public void executeCommand(Player player, String params) {
        
		String[] args = params.split(" ");
		Player target = World.getInstance().findPlayer(Util.convertName(args[0]));

        if (target == null) {
            PacketSendUtility.sendMessage(player, "Ein Spieler mit diesem Namen ist nicht online bzw. existiert nicht.");
            return;
        }
		
		StringBuilder sb = new StringBuilder();

        sb.append("<poll>\n");
        sb.append("<poll_introduction>\n");
        sb.append("	<![CDATA[<font color='4CB1E5'>Spielerinfo -- Aion-Germany.net</font>]]>\n");
        sb.append("</poll_introduction>\n");
        sb.append("<poll_title>\n");
        sb.append("	<font color='ffc519'></font>\n");
        sb.append("</poll_title>\n");
        sb.append("<start_date></start_date>\n");
        sb.append("<end_date></end_date>\n");
        sb.append("<servers></servers>\n");
        sb.append("<order_num></order_num>\n");
        sb.append("<race></race>\n");
        sb.append("<main_class></main_class>\n");
        sb.append("<world_id></world_id>\n");
        sb.append("<item_id></item_id>\n");
        sb.append("<item_cnt></item_cnt>\n");
        sb.append("<level>1~55</level>\n");
        sb.append("<questions>\n");
        sb.append("	<question>\n");
        sb.append("		<title>\n");
        sb.append("			<![CDATA[\n");
        sb.append("<br><br>\n");
		if (target.getCommonData().getRace() == Race.ELYOS)
		{
			sb.append("Informationen zum Spieler " + target.getName() + ":   <img src='http://www.aion-destination.de/_images/elyos.png'><br><hr><br><br>\n");
		}
		else
		{
			sb.append("Informationen zum Spieler " + target.getName() + ":   <img src='http://sites.google.com/site/aioninfos/bilder/sonstiges/Asmodier-Symbol-35x40.png'><br><hr><br><br>\n");
		}
			
			
		//Falls sie die selbe Rasse haben bzw. es ein GM ist, wird der HP/MP/DP Status in % ausgegeben	
		if (target.getCommonData().getRace() == player.getCommonData().getRace() || player.getAccessLevel() > 0)
		{
			int hpinPercent = target.getLifeStats().getCurrentHp()*100/target.getGameStats().getCurrentStat(StatEnum.MAXHP);
			int mpinPercent = target.getLifeStats().getCurrentMp()*100/target.getGameStats().getCurrentStat(StatEnum.MAXMP);
            int dpinPercent = target.getCommonData().getDp()*100/4000;
			
			//Gesundheit grafisch darstellen
			sb.append("Gesundheit: [<font color ='00FF00'>");
			int i = 1;
			for (i = 1; i < hpinPercent/5; i++)
			{
				sb.append("|");
				if (i == 10)
				{
					sb.append(" ");
				}
			}
			sb.append("</font><font color ='FF0000'>");
			int k = 20-i;
			for (i = 1; i < k; i++)
			{
				sb.append("|");
                if (20-k+i == 10)
                {
                    sb.append(" ");
                }
			}
			sb.append("</font>] (" + hpinPercent + "%)  (" + target.getLifeStats().getCurrentHp() + "/" + target.getGameStats().getCurrentStat(StatEnum.MAXHP) + " TP)<br>");
			
			if (hpinPercent == 0) //Falls der Spieler keine Gesundheit mehr hat also tot ist sind alle Balken rot und es erscheint Tot neben dem Leben
			{
				sb.append(" <font color ='FF0000'>Tot</font>");
                sb.append("<br>Mana: [<font color='FF0000'>|||||||||| ||||||||||</font>]");
				sb.append("<br>G&ouml;ttliche Kraft: [font color='FF0000'>|||||||||| ||||||||||</font>");
			}
			else //ansonsten werden Mana und G�ttliche Kraft berechnet
            {
                sb.append("<br>");
				//Mana grafisch darstellen
                sb.append("Mana: [<font color ='00FFFF'>");
                for (i = 1; i < mpinPercent/5; i++) //gr�ne Gesundheitsbalken
                {
                    sb.append("|");
                    if (i == 10)
                    {
                        sb.append(" ");
                    }
                }
                sb.append("</font><font color ='FF0000'>");
                k = 20-i;
                for (i = 1; i < k; i++) //Rote Gesundheitsbalken
                {
                    sb.append("|");
                    if (20-k+i == 10)
                    {
                        sb.append(" ");
                    }
                }
                sb.append("</font>] (" + mpinPercent + "%)  (" + target.getLifeStats().getCurrentMp() + "/" + target.getGameStats().getCurrentStat(StatEnum.MAXMP) + " MP)<br>");
            
				//G�ttliche Kraft grafisch darstellen
				sb.append("G&ouml;ttliche Kraft: [<font color='FFCC00'>");
				for (i = 1; i < dpinPercent/5; i++)
				{
					sb.append("|");
					if (i == 10)
					{
						sb.append(" ");
					}
				}
				sb.append("</font><font color ='FF0000'>");
				k = 20-i;
				for (i = 1; i < k; i++)
				{
					sb.append("|");
					if (20-k+i == 10)
					{
						sb.append(" ");
					}
				}
				sb.append("</font>] (" + dpinPercent + "%)  (" + target.getCommonData().getDp() + "/4000 GK)<br>");
			}
		}
		
		sb.append("Level: " + target.getLevel() + "<br>\n");
		//sb.append("Online: " + (System.currentTimeMillis() - target.getCommonData().getLastOnline().getTime()) / 60000 + " Minuten<br>\n");
		
		
		if (target.getCommonData().getRace() == player.getCommonData().getRace() || player.getAccessLevel() > 0)
		{
			//PacketSendUtility.broadcastPacket(((Player) player), new SM_MESSAGE(((Player) player),"Position von [Pos:" + target.getName() + ";" + target.getPosition().getMapId() + " " + target.getPosition().getX() + " " + target.getPosition().getY() + " 0.0 -1]" , ChatType.NORMAL), true);
			//TODO Post des Ortes via Chat... Umwandlung in Ortsbezogene Markierung??
			}
        
		sb.append("Klasse: ");
		int targetclass = target.getPlayerClass().getClassId();
		if (targetclass == 0) { sb.append("Krieger"); }
		else if (targetclass == 1) { sb.append("Gladiator"); }
		else if (targetclass == 2) { sb.append("Templer"); }
		else if (targetclass == 3) { sb.append("Sp&auml;her"); }
		else if (targetclass == 4) { sb.append("Assassine"); }
		else if (targetclass == 5) { sb.append("J&auml;ger"); }
		else if (targetclass == 6) { sb.append("Magier"); }
		else if (targetclass == 7) { sb.append("Zauberer"); }
		else if (targetclass == 8) { sb.append("Beschw&ouml;rer"); }
		else if (targetclass == 9) { sb.append("Priester"); }
		else if (targetclass == 10) { sb.append("Kleriker"); }
		else if (targetclass == 11) { sb.append("Kantor"); }

        sb.append("<br>");   
       
		if (target.getCommonData().getRace() == Race.ELYOS)
		{
			sb.append("Rasse: Elyos<br>\n");
			}
		else
		{
			sb.append("Rasse: Asmodier<br>\n");
			}
		
		StringBuilder strbld = new StringBuilder("Gruppe: <br>--Leitung: \n");

            PlayerGroup group = target.getPlayerGroup();
            if (group == null)
                sb.append("Gruppe: keine<br>\n");
            else {
                Iterator<Player> it = group.getMembers().iterator();

                strbld.append(group.getGroupLeader().getName() + "<br>--Mitglieder:<br>\n");
                while (it.hasNext()) {
                    Player act = (Player) it.next();
                    strbld.append("----" + act.getName() + "<br>\n");
					}
				sb.append(strbld.toString());
				}
		
		Legion legion = target.getLegion();
		if (legion == null)
		{
			sb.append("Legion: keine<br>\n");
			}
		else
		{
			sb.append("Legion: " + legion.getLegionName() + "<br>\n");
			}
		
		if (player.getAccessLevel() > 0)
		{
			sb.append("<hr>");
			sb.append("Zus&auml;tzliche Informationen f&uuml;r Teammitglieder:<br><br>");
			sb.append("Account Name: " + target.getClientConnection().getAccount().getName() + "<br>\n");
			//sb.append("IP: " + target.getClientConnection.getIP() + "<br>\n");
			
			try {
				if (args[1].equals("+items"))
				{
					sb.append("<br><hr><br>Item Informationen:<br><br>");
					StringBuilder itstrbld = new StringBuilder("-- Inventar:<br>");

					List<Item> items = target.getInventory().getAllItems();
					Iterator<Item> it = items.iterator();

					if (items.isEmpty())
						itstrbld.append("---- Keine");
					else {
						while (it.hasNext()) {

							Item act = (Item) it.next();
							itstrbld.append("---- " + act.getItemCount() + "mal " + "[item:" + act.getItemTemplate().getTemplateId() + "]<br>");
						}
					}
					items.clear();
					items = target.getEquipment().getEquippedItems();
					it = items.iterator();
					itstrbld.append("<br>-- Ausger&uuml;stet:<br>");
					if (items.isEmpty())
						itstrbld.append("---- Keine");
					else {
						while (it.hasNext()) {
							Item act = (Item) it.next();
							itstrbld.append("---- " + act.getItemCount() + "mal " + "[item:" + act.getItemTemplate().getTemplateId() + "]<br>");
						}
					}
					
					items.clear();
					items = target.getWarehouse().getAllItems();
					it = items.iterator();
					itstrbld.append("<br>-- Lagerhaus:<br>");
					if (items.isEmpty())
						itstrbld.append("---- Keine");
					else {
						while (it.hasNext()) {
							Item act = (Item) it.next();
							itstrbld.append("---- " + act.getItemCount() + "mal " + "[item:" + act.getItemTemplate().getTemplateId() + "]" + "<br>");
						}
					}
					sb.append(itstrbld.toString());
				}
			} catch (Exception e) { }
		}
		
		else if (player.getAccessLevel() == 0 && args[1].equals("+items"))
		{
			sb.append("<br><hr><br>Du hast nicht genug Rechte, um Item Informationen abzurufen.<br><br>");
			}
		sb.append("<br><br><br>\n");
        sb.append("			]]>\n");
        sb.append("		</title>\n");
        sb.append("		<select>\n");
        sb.append("<input type='radio'>");
        sb.append("Danke f&uuml;r die Info!");
        sb.append("</input>\n");
        sb.append("		</select>\n");
        sb.append("	</question>\n");
        sb.append("</questions>\n");
        sb.append("</poll>\n");
		
		
		String html = sb.toString();
		final int messageId = IDFactory.getInstance().nextId();
        byte packet_count = (byte) Math.ceil(html.length() / (Short.MAX_VALUE - 8) + 1);
        if (packet_count < 256) {
            for (byte i = 0; i < packet_count; i++) {
                try {
                    int from = i * (Short.MAX_VALUE - 8), to = (i + 1) * (Short.MAX_VALUE - 8);
                    if (from < 0)
                        from = 0;
                    if (to > html.length())
                        to = html.length();
                    String sub = html.substring(from, to);
                    player.getClientConnection().sendPacket(new SM_QUESTIONNAIRE(messageId, i, packet_count, sub));
                }
                catch (Exception e) {
				
                }
            }
        }
    }
}