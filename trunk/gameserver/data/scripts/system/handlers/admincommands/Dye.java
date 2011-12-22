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

import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_UPDATE_ITEM;
import gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class Dye extends AdminCommand
{

	public Dye()
	{
		super("dye");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_DYE)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}

		Player target = null;
		VisibleObject creature = admin.getTarget();

		if (admin.getTarget() instanceof Player)
		{
			target = (Player) creature;
		}

		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "You should select a player as target first!");
			return;
		}

		if (params.length == 0 || params.length > 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //dye <dye color | hex color | remove>");
			return;
		}

		String color = "";
		if (params.length == 2) { 
			if (params[1].equalsIgnoreCase("petal")) { color = params[0]; }
			else { color = params[0] + " " + params[1]; }
		}
		else { color = params[0]; }

		int rgb = 0;
		int bgra = 0;

			 if (color.equalsIgnoreCase("turquoise"))		{ color = "198d94"; }	//169200001, 169201001
		else if (color.equalsIgnoreCase("blue"))			{ color = "1f87f5"; }	//169200002, 169201002
		else if (color.equalsIgnoreCase("brown"))			{ color = "66250e"; }	//169200003, 169201003
		else if (color.equalsIgnoreCase("purple"))			{ color = "c38df5"; }	//169200004, 169201004
		else if (color.equalsIgnoreCase("red"))				{ color = "c22626"; }	//169200005, 169201005, 169220001, 169230001, 169231001
		else if (color.equalsIgnoreCase("white"))			{ color = "ffffff"; }	//169200006, 169201006, 169220002, 169231002
		else if (color.equalsIgnoreCase("black"))			{ color = "000000"; }	//169200007, 169201007, 169230008, 169231008
		else if (color.equalsIgnoreCase("orange"))			{ color = "e36b00"; }	//169201008, 169220004, 169230009, 169231009
		else if (color.equalsIgnoreCase("dark purple"))		{ color = "440b9a"; }	//169201009, 169220005, 169230007, 169231003
		else if (color.equalsIgnoreCase("pink"))			{ color = "d60b7e"; }	//169201010, 169220006, 169230010, 169231010
		else if (color.equalsIgnoreCase("mustard"))			{ color = "fcd251"; }	//169201011, 169220007, 169230004, 169231004
		else if (color.equalsIgnoreCase("green tea"))		{ color = "61bb4f"; }	//169201012, 169220008, 169230003, 169231005
		else if (color.equalsIgnoreCase("green olive"))		{ color = "5f730e"; }	//169201013, 169220009, 169230005, 169231006
		else if (color.equalsIgnoreCase("dark blue"))		{ color = "14398b"; }	//169201014, 169220010, 169230006, 169231007
		else if (color.equalsIgnoreCase("light purple"))	{ color = "80185d"; }	//169230011
		else if (color.equalsIgnoreCase("wiki"))			{ color = "85e831"; }	//169240001
		else if (color.equalsIgnoreCase("omblic"))			{ color = "ff5151"; } 	//169240002
		else if (color.equalsIgnoreCase("meon"))			{ color = "afaf26"; }	//169240003
		else if (color.equalsIgnoreCase("ormea"))			{ color = "ffaa11"; }	//169240004
		else if (color.equalsIgnoreCase("tange"))			{ color = "bd5fff"; }	//169240005
		else if (color.equalsIgnoreCase("ervio"))			{ color = "3bb7fe"; }	//169240006
		else if (color.equalsIgnoreCase("lunime"))			{ color = "c7af27"; }	//169240007
		else if (color.equalsIgnoreCase("vinna"))			{ color = "052775"; }	//169240008
		else if (color.equalsIgnoreCase("kirka"))			{ color = "ca84ff"; }	//169240009
		else if (color.equalsIgnoreCase("brommel"))			{ color = "c7af27"; }	//169240010
		else if (color.equalsIgnoreCase("pressa"))			{ color = "ff9d29"; }	//169240011
		else if (color.equalsIgnoreCase("merone"))			{ color = "8df598"; }	//169240012
		else if (color.equalsIgnoreCase("kukar"))			{ color = "ffff96"; }	//169240013
		else if (color.equalsIgnoreCase("leopis"))			{ color = "31dfff"; }	//169240014

		try
		{
			rgb = Integer.parseInt(color, 16);
			bgra = 0xFF | ((rgb & 0xFF) << 24) | ((rgb & 0xFF00) << 8) | ((rgb & 0xFF0000) >>> 8);
		}

		catch (NumberFormatException e)
		{
			if (!color.equalsIgnoreCase("remove")) {
				PacketSendUtility.sendMessage(admin, color + " is not a valid color parameter!");
				return;
			}
		}

		for (Item targetItem : target.getEquipment().getEquippedItemsWithoutStigma())
		{
			if (color.equals("remove"))
			{
				targetItem.setItemColor(0);
			}
			else
			{
				targetItem.setItemColor(bgra);
			}
			PacketSendUtility.sendPacket(target, new SM_UPDATE_ITEM(targetItem));
		}
		PacketSendUtility.broadcastPacket(target, new SM_UPDATE_PLAYER_APPEARANCE(target.getObjectId(), target.getEquipment().getEquippedItemsWithoutStigma()), true);
		target.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
		if (target.getObjectId() != admin.getObjectId()) { PacketSendUtility.sendMessage(target, "You have been dyed by " + admin.getName() + "!"); }
		PacketSendUtility.sendMessage(admin, "Dyed " + target.getName() + " successfully!");
	}
}
