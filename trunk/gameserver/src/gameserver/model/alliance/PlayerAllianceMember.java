/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
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
