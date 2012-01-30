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
import gameserver.model.account.CharacterPasskey;
import gameserver.model.account.CharacterPasskey.ConnectType;
import gameserver.model.account.PlayerAccountData;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import gameserver.network.aion.serverpackets.SM_DELETE_CHARACTER;
import gameserver.network.loginserver.LoginServer;
import gameserver.services.PlayerService;

public class CM_CHARACTER_PASSKEY extends AionClientPacket
{
	private int	type;
	private String passkey;
	private String newPasskey;

	public CM_CHARACTER_PASSKEY(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		type = readH(); // 0:new, 2:update, 3:input
		passkey = readS();
		if (type == 2)
			newPasskey = readS();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();
		CharacterPasskey chaPasskey = client.getAccount().getCharacterPasskey();

		switch (type)
		{
			case 0:
				chaPasskey.setIsPass(false);
				chaPasskey.setWrongCount(0);
				DAOManager.getDAO(PlayerPasskeyDAO.class).insertPlayerPasskey(client.getAccount().getId(), passkey);
				client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));
				break;
			case 2:
				boolean isSuccess = DAOManager.getDAO(PlayerPasskeyDAO.class).updatePlayerPasskey(
					client.getAccount().getId(),
					passkey,
					newPasskey);

				chaPasskey.setIsPass(false);
				if (isSuccess)
				{
					chaPasskey.setWrongCount(0);
					client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));
				}
				else
				{
					chaPasskey.setWrongCount(chaPasskey.getWrongCount() + 1);
					checkBlock(client.getAccount().getId(), chaPasskey.getWrongCount());
					client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));
				}
				break;
			case 3:
				boolean isPass = DAOManager.getDAO(PlayerPasskeyDAO.class).checkPlayerPasskey(
					client.getAccount().getId(),
					passkey);

				if (isPass)
				{
					chaPasskey.setIsPass(true);
					chaPasskey.setWrongCount(0);
					client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));

					if (chaPasskey.getConnectType() == ConnectType.ENTER)
						CM_ENTER_WORLD.enterWorld(client, chaPasskey.getObjectId());
					else if (chaPasskey.getConnectType() == ConnectType.DELETE)
					{
						PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(
							chaPasskey.getObjectId());

						PlayerService.deletePlayer(playerAccData);
						client.sendPacket(new SM_DELETE_CHARACTER(chaPasskey.getObjectId(), playerAccData
							.getDeletionTimeInSeconds()));
					}
				}
				else
				{
					chaPasskey.setIsPass(false);
					chaPasskey.setWrongCount(chaPasskey.getWrongCount() + 1);
					checkBlock(client.getAccount().getId(), chaPasskey.getWrongCount());
					client.sendPacket(new SM_CHARACTER_SELECT(2, type, chaPasskey.getWrongCount()));
				}
				break;
		}
	}

	/**
	 * @param accountId
	 * @param wrongCount
	 */
	private void checkBlock(int accountId, int wrongCount)
	{
		if (wrongCount >= GSConfig.PASSKEY_WRONG_MAXCOUNT)
		{
			// TODO : Change the account to be blocked
			LoginServer.getInstance().sendBanPacket((byte) 2, accountId, "", 60 * 8, 0);
		}
	}
}
