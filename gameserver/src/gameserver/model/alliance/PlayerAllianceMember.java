/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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