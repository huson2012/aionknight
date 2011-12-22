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

import commons.database.dao.DAOManager;
import gameserver.configs.administration.AdminConfig;
import gameserver.dao.PlayerAppearanceDAO;
import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerAppearance;
import gameserver.model.templates.preset.PresetTemplate;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class ApplyPreset extends AdminCommand
{
	public ApplyPreset()
	{
		super("preset");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_APPLY_PRESET)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}
		
		if (params.length == 0 || params.length > 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //preset <name>");
			return;
		}
		
		VisibleObject target = admin.getTarget();
		Player player = null;
	
		if (target == null)
			player = admin;
		else if (!(target instanceof Player))
		{
			PacketSendUtility.sendMessage(admin, "Presets can be applied only on players!");
			return;		
		}
		else
		{
			player = (Player)target;
		}
		
		String presetName = params[0];

		PresetTemplate template = DataManager.CUSTOM_PRESET_DATA.getPresetTemplate(presetName);
		if (template == null)
		{
			PacketSendUtility.sendMessage(admin, "No such preset!");
			return;
		}
		if (template.getGender().ordinal() != player.getCommonData().getGender().ordinal() ||
			template.getRace().ordinal() != player.getCommonData().getRace().ordinal())
		{
			PacketSendUtility.sendMessage(admin, "Preset can not be applied on current gender or race!");
			return;
		}
		
		PlayerAppearance.loadDetails(player.getPlayerAppearance(), template.getDetail());
		if (template.getFaceType() > 0)
			player.getPlayerAppearance().setFace(template.getFaceType());
		if (template.getHairType() > 0)
			player.getPlayerAppearance().setHair(template.getHairType());
		if (template.getHairRGB() != null)
			player.getPlayerAppearance().setHairRGB(getDyeColor(template.getHairRGB()));
		if (template.getLipsRGB() != null)
			player.getPlayerAppearance().setLipRGB(getDyeColor(template.getLipsRGB()));
		if (template.getSkinRGB() != null)
			player.getPlayerAppearance().setSkinRGB(getDyeColor(template.getSkinRGB()));
		if (template.getHeight() > 0)
			player.getPlayerAppearance().setHeight(template.getHeight());

		DAOManager.getDAO(PlayerAppearanceDAO.class).store(player);

		player.clearKnownlist();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		player.updateKnownlist();
	}
	
	private int getDyeColor(String hexRGB)
	{
		int rgb = Integer.parseInt(hexRGB, 16);
		int red = (rgb >> 16) & 0xFF;
		int green = (rgb >> 8) & 0xFF;
		int blue = (rgb >> 0) & 0xFF;
		int bgr = (blue << 16) | (green << 8) | (red << 0);
		return bgr;
	}
	
}
