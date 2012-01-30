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

package gameserver.network.aion.clientpackets;

import gameserver.configs.administration.AdminConfig;
import gameserver.configs.main.CustomConfig;
import gameserver.model.gameobjects.player.FriendList.Status;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PLAYER_SEARCH;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.Util;
import gameserver.world.World;
import java.util.ArrayList;
import java.util.List;

/**
 * Received when a player searches using the social search panel
 */
public class CM_PLAYER_SEARCH extends AionClientPacket
{
	/**
	 * The max number of players to return as results
	 */
	public static final int	MAX_RESULTS	= 111;

	private String			name;
	private int				region;
	private int				classMask;
	private int				minLevel;
	private int				maxLevel;
	private int				lfgOnly;

	public CM_PLAYER_SEARCH(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		if(!(name = readS()).isEmpty())
		{
			name = Util.convertName(name);
			readB(52 - (name.length() * 2 + 2));
		}
		else
		{
			readB(50);
		}
		region = readD();
		classMask = readD();
		minLevel = readC();
		maxLevel = readC();
		lfgOnly = readC();
		readC(); // 0x00 in search pane 0x30 in /who?
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();

		final List<Player> matches = new ArrayList<Player>(MAX_RESULTS);

		if(activePlayer != null && activePlayer.getLevel() < CustomConfig.LEVEL_TO_SEARCH)
		{
			sendPacket(SM_SYSTEM_MESSAGE.LEVEL_NOT_ENOUGH_FOR_SEARCH(String.valueOf(CustomConfig.LEVEL_TO_SEARCH)));
			return;
		}

		World.getInstance().doOnAllPlayers(new Executor<Player>()
		{
			@Override
			public boolean run(Player player)
			{
				if (matches.size() >= MAX_RESULTS)
					return false;

				if(!player.isSpawned())
					return true;
				else if(CustomConfig.SEARCH_LIST_ALL && (activePlayer.getAccessLevel() >= AdminConfig.SEARCH_LIST_ALL))
				{
					if(player.getFriendList().getStatus() != Status.OFFLINE)
					{
						matches.add(player);
						return true;
					}
				}
				else if(player.getFriendList().getStatus() == Status.OFFLINE)
					return true;
				else if(lfgOnly == 1 && !player.isLookingForGroup())
					return true;
				else if(!name.isEmpty() && !player.getName().toLowerCase().contains(name.toLowerCase()))
					return true;
				else if(minLevel != 0xFF && player.getLevel() < minLevel)
					return true;
				else if(maxLevel != 0xFF && player.getLevel() > maxLevel)
					return true;
				else if(classMask > 0 && (player.getPlayerClass().getMask() & classMask) == 0)
					return true;
				else if(region > 0 && player.getActiveRegion().getMapId() != region)
					return true;
				else if((player.getCommonData().getRace() != activePlayer.getCommonData().getRace())&& (!CustomConfig.FACTIONS_SEARCH_MODE))
					return true;
				else
				{
					matches.add(player);
					return true;
				}
				return true;
			}
		}, true);

		sendPacket(new SM_PLAYER_SEARCH(matches, region));
	}
}
