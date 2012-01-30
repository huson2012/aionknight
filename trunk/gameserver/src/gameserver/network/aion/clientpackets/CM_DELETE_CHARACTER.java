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

import commons.database.dao.DAOManager;
import gameserver.configs.main.GSConfig;
import gameserver.dao.PlayerPasskeyDAO;
import gameserver.model.account.CharacterPasskey.ConnectType;
import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import gameserver.network.aion.serverpackets.SM_DELETE_CHARACTER;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.PlayerService;

/**
 * In this packets aion client is requesting deletion of character.
 */
public class CM_DELETE_CHARACTER extends AionClientPacket
{
	/**
	 * PlayOk2 - we dont care...
	 */
	@SuppressWarnings("unused")
	private int	playOk2;
	/**
	 * ObjectId of character that should be deleted.
	 */
	private int	chaOid;
	
	/**
	 * Constructs new instance of <tt>CM_DELETE_CHARACTER </tt> packet
	 * @param opcode
	 */
	public CM_DELETE_CHARACTER(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		playOk2 = readD();
		chaOid = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();
		PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(chaOid);
		if(playerAccData != null && !playerAccData.isLegionMember())
		{
			// passkey check
			if(GSConfig.PASSKEY_ENABLE && !client.getAccount().getCharacterPasskey().isPass())
			{
				client.getAccount().getCharacterPasskey().setConnectType(ConnectType.DELETE);
				client.getAccount().getCharacterPasskey().setObjectId(chaOid);
				boolean isExistPasskey = DAOManager.getDAO(PlayerPasskeyDAO.class).existCheckPlayerPasskey(client.getAccount().getId());

				if (!isExistPasskey)
					client.sendPacket(new SM_CHARACTER_SELECT(0));
				else
					client.sendPacket(new SM_CHARACTER_SELECT(1));
			}
			else
			{
				PlayerService.deletePlayer(playerAccData);
				client.sendPacket(new SM_DELETE_CHARACTER(chaOid, playerAccData.getDeletionTimeInSeconds()));
			}
		}
		else
		{
			client.sendPacket(SM_SYSTEM_MESSAGE.STR_DELETE_CHARACTER_IN_LEGION());
		}		
	}
}