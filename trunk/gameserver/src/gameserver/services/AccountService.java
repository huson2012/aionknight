/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package gameserver.services;

import commons.database.dao.DAOManager;
import gameserver.GameServer;
import gameserver.configs.main.CacheConfig;
import gameserver.configs.main.GSConfig;
import gameserver.controllers.PlayerController;
import gameserver.dao.InventoryDAO;
import gameserver.dao.LegionMemberDAO;
import gameserver.dao.PlayerAppearanceDAO;
import gameserver.dao.PlayerDAO;
import gameserver.model.Race;
import gameserver.model.account.Account;
import gameserver.model.account.AccountTime;
import gameserver.model.account.PlayerAccountData;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.*;
import gameserver.model.legion.LegionMember;
import gameserver.utils.collections.cachemap.CacheMap;
import gameserver.utils.collections.cachemap.CacheMapFactory;
import org.apache.log4j.Logger;
import java.util.Iterator;
import java.util.List;

public class AccountService
{
	private static final Logger log	 = Logger.getLogger(AccountService.class);
	private static CacheMap<Integer, Account> accountsMap = CacheMapFactory.createSoftCacheMap("Account", "account");

	/**
	 * ���������� {@link Account} ID �������.
	 * 
	 * @param accountId
	 * @param accountTime
	 * @param accountName
	 * @param accessLevel
	 * @param membership
	 * @return Account
	 */
	public static Account getAccount(int accountId, String accountName, AccountTime accountTime, byte accessLevel,
		byte membership)
	{
		log.debug("[AS] request for account: " + accountId);

		Account account = accountsMap.get(accountId);
		if(account == null)
		{
			account = loadAccount(accountId);

			if(CacheConfig.CACHE_ACCOUNTS)
				accountsMap.put(accountId, account);
		}

		account.setName(accountName);
		account.setAccountTime(accountTime);
		account.setAccessLevel(accessLevel);
		account.setMembership(membership);

		removeDeletedCharacters(account);

		return account;
	}

	/**
	 * ������� �� ���� ������ ����������, ������� ������ ���� ������� (������� ����� �� ��������).
	 * 
	 * @param account
	 */
	private static void removeDeletedCharacters(Account account)
	{
		/** 
		 * ������� ����������, ������� ������ ���� �������.
		 */
		Iterator<PlayerAccountData> it = account.iterator();
		while(it.hasNext())
		{
			PlayerAccountData pad = it.next();
			Race race = pad.getPlayerCommonData().getRace();
			long deletionTime = ((long) pad.getDeletionTimeInSeconds()) * 1000;
			if(deletionTime != 0 && deletionTime <= System.currentTimeMillis())
			{
				it.remove();
				account.decrementCountOf(race);
				PlayerService.deletePlayerFromDB(pad.getPlayerCommonData().getPlayerObjId());
				if (GSConfig.FACTIONS_RATIO_LIMITED && pad.getPlayerCommonData().getLevel() >= GSConfig.FACTIONS_RATIO_LEVEL)
				{
					if (account.getNumberOf(race) == 0)
					{
						GameServer.updateRatio(pad.getPlayerCommonData().getRace(), -1);
					}
				}
			}
		}
	}

	/**
	 * ��������� ������ �� ���-�� ����������.
	 * 
	 * @param accountId
	 * @param accountName
	 * @return
	 */
	private static Account loadAccount(int accountId)
	{
		Account account = new Account(accountId);

		PlayerDAO playerDAO = DAOManager.getDAO(PlayerDAO.class);
		PlayerAppearanceDAO appereanceDAO = DAOManager.getDAO(PlayerAppearanceDAO.class);
		
		List<Integer> playerOids = playerDAO.getPlayerOidsOnAccount(accountId);
		for(int playerOid : playerOids)
		{
			PlayerCommonData playerCommonData = playerDAO.loadPlayerCommonData(playerOid);
			PlayerAppearance appereance = appereanceDAO.load(playerOid);

			LegionMember legionMember = DAOManager.getDAO(LegionMemberDAO.class).loadLegionMember(playerOid);

			/**
			 * �������� ����������, ��� ����������� �� ������ ������ ���������
			 */
			List<Item> equipments = DAOManager.getDAO(InventoryDAO.class).loadEquipment(playerOid);

			for (int i = equipments.size() - 1; i >= 0; --i)
			{
				if (equipments.get(i).getEquipmentSlot() == 131072 || equipments.get(i).getEquipmentSlot() == 262144)
				{ 
					equipments.remove(i);
				}
			}

			PlayerAccountData acData = new PlayerAccountData(playerCommonData, appereance, equipments, legionMember);
			playerDAO.setCreationDeletionTime(acData);
			account.addPlayerAccountData(acData);

			/**
			 * �������������� ��������� ������ ���������
			 */	
			if(account.getAccountWarehouse() == null)
			{
				Player player = new Player(new PlayerController(), playerCommonData, appereance, account);
				Storage accWarehouse = DAOManager.getDAO(InventoryDAO.class).loadStorage(player, StorageType.ACCOUNT_WAREHOUSE);
				ItemService.loadItemStones(accWarehouse.getStorageItems());
				account.setAccountWarehouse(accWarehouse);
			}
		}
		
		/**
		 * �������� ������� ������������ ��� ����� ������ - ������ ��� ����� ����������.
		 */
		if(account.getAccountWarehouse() == null)
			account.setAccountWarehouse(new Storage(StorageType.ACCOUNT_WAREHOUSE));

		return account;
	}
	
	/**
	 * �������� ���-�� ������� ������� �� ���������� �� ��.
	 * 
	 * @param accountId
	 * @return
	 */
	public static int getCharacterCountFor(int accountId)
	{
		PlayerDAO playerDAO = DAOManager.getDAO(PlayerDAO.class);
		return playerDAO.getCharacterCountOnAccount(accountId);
	}
}