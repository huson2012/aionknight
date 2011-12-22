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

package gameserver.model.alliance;

import gameserver.model.gameobjects.AionObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;

public class PlayerAllianceMember extends AionObject
{
	private Player player;
	private String name;
	private int allianceId;
	private PlayerCommonData playerCommonData;

	public PlayerAllianceMember(Player player)
	{
		super(player.getObjectId());
		this.player = player;
		this.name = player.getName();
		this.playerCommonData = player.getCommonData();
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void onLogin(Player player)
	{
		this.player = player;
		this.playerCommonData = player.getCommonData();
	}

	public void onDisconnect()
	{
		this.player = null;
	}

	public boolean isOnline()
	{
		return (player != null);
	}

	public PlayerCommonData getCommonData()
	{
		return playerCommonData;
	}

	public int getAllianceId()
	{
		return allianceId;
	}

	public void setAllianceId(int allianceId)
	{
		this.allianceId = allianceId;
	}
}