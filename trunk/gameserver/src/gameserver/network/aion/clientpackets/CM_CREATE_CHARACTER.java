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
import gameserver.dao.InventoryDAO;
import gameserver.model.Gender;
import gameserver.model.PlayerClass;
import gameserver.model.Race;
import gameserver.model.account.Account;
import gameserver.model.account.PlayerAccountData;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerAppearance;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.serverpackets.SM_CREATE_CHARACTER;
import gameserver.services.PlayerService;
import gameserver.utils.Util;
import gameserver.utils.idfactory.IDFactory;
import java.sql.Timestamp;
import java.util.List;

/**
 * In this packets aion client is requesting creation of character.
 */
public class CM_CREATE_CHARACTER extends AionClientPacket
{
	/** Character appearance */
	private PlayerAppearance	playerAppearance;
	/** Player base data */
	private PlayerCommonData	playerCommonData;

	/**
	 * Constructs new instance of <tt>CM_CREATE_CHARACTER </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_CREATE_CHARACTER(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		@SuppressWarnings("unused")
		int playOk2 = readD(); // ignored for now
		@SuppressWarnings("unused")
		String someShit = readS(); // something + accointId

		playerCommonData = new PlayerCommonData(IDFactory.getInstance().nextId());
		String name = Util.convertName(readS());

		playerCommonData.setName(name);
		readB(50 - (name.length() * 2)); // some shit 2.5.x
		
		playerCommonData.setLevel(1);
		playerCommonData.setGender(readD() == 0 ? Gender.MALE : Gender.FEMALE);
		playerCommonData.setRace(readD() == 0 ? Race.ELYOS : Race.ASMODIANS);
		playerCommonData.setPlayerClass(PlayerClass.getPlayerClassById((byte) readD()));

		playerAppearance = new PlayerAppearance();

		playerAppearance.setVoice(readD());

		playerAppearance.setSkinRGB(readD());
		playerAppearance.setHairRGB(readD());
		playerAppearance.setEyeRGB(readD());
		playerAppearance.setLipRGB(readD());

		playerAppearance.setFace(readC());
		playerAppearance.setHair(readC());
		playerAppearance.setDecoration(readC());
		playerAppearance.setTattoo(readC());
		playerAppearance.setFaceContour(readC());
		playerAppearance.setExpression(readC());
		      
		readC(); // Always 6 - 2.5.x
		      
		playerAppearance.setJawLine(readC());
		playerAppearance.setForehead(readC());
		playerAppearance.setEyeHeight(readC());
		playerAppearance.setEyeSpace(readC());
		playerAppearance.setEyeWidth(readC());
		playerAppearance.setEyeSize(readC());
		playerAppearance.setEyeShape(readC());
		playerAppearance.setEyeAngle(readC());

		playerAppearance.setBrowHeight(readC());
		playerAppearance.setBrowAngle(readC());
		playerAppearance.setBrowShape(readC());

		playerAppearance.setNose(readC());
		playerAppearance.setNoseBridge(readC());
		playerAppearance.setNoseWidth(readC());
		playerAppearance.setNoseTip(readC());

		playerAppearance.setCheeks(readC());
		playerAppearance.setLipHeight(readC());
		playerAppearance.setMouthSize(readC());
		playerAppearance.setLipSize(readC());
		playerAppearance.setSmile(readC());
		playerAppearance.setLipShape(readC());

		playerAppearance.setChinHeight(readC());
		playerAppearance.setCheekBones(readC());

		playerAppearance.setEarShape(readC());
		playerAppearance.setHeadSize(readC());

		playerAppearance.setNeck(readC());
		playerAppearance.setNeckLength(readC());

		playerAppearance.setShoulderSize(readC());

		playerAppearance.setTorso(readC());
		playerAppearance.setChest(readC()); // only woman
		playerAppearance.setWaist(readC());
		playerAppearance.setHips(readC());

		playerAppearance.setArmThickness(readC());
		playerAppearance.setHandSize(readC());

		playerAppearance.setLegThickness(readC());
		playerAppearance.setFootSize(readC());
		
		readC(); // always 0

		playerAppearance.setFacialRatio(readC());

		playerAppearance.setArmLength(readC());
		playerAppearance.setLegLength(readC());
		
		playerAppearance.setShoulders(readC());
		playerAppearance.setFaceShape(readC());
		    
		readC(); // always 0
		readC(); // always 0
		readC(); // always 0
		playerAppearance.setHeight(readF());
	}

	/**
	 * Actually does the dirty job
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();

		/** Some reasons why player can' be created */
		if(!PlayerService.isValidName(playerCommonData.getName()))
		{
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_INVALID_NAME));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		if(!PlayerService.isFreeName(playerCommonData.getName()))
		{
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_NAME_ALREADY_USED));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}
		if(!playerCommonData.getPlayerClass().isStartingClass())
		{
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.FAILED_TO_CREATE_THE_CHARACTER));
			IDFactory.getInstance().releaseId(playerCommonData.getPlayerObjId());
			return;
		}

		Account account = client.getAccount();

		Player player = PlayerService.newPlayer(playerCommonData, playerAppearance, account);

		if(!PlayerService.storeNewPlayer(player, account.getName(), account.getId()))
		{
			client.sendPacket(new SM_CREATE_CHARACTER(null, SM_CREATE_CHARACTER.RESPONSE_DB_ERROR));
		}
		else
		{
			List<Item> equipment = DAOManager.getDAO(InventoryDAO.class).loadEquipment(player.getObjectId());
			PlayerAccountData accPlData = new PlayerAccountData(playerCommonData, playerAppearance, equipment, null);

			accPlData.setCreationDate(new Timestamp(System.currentTimeMillis()));
			PlayerService.storeCreationTime(player.getObjectId(), accPlData.getCreationDate());

			account.addPlayerAccountData(accPlData);
			client.sendPacket(new SM_CREATE_CHARACTER(accPlData, SM_CREATE_CHARACTER.RESPONSE_OK));
		}
	}
}