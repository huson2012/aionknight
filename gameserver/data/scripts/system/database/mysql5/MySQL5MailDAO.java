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

package mysql5;

import commons.database.DatabaseFactory;
import commons.database.dao.DAOManager;
import gameserver.dao.ItemStoneListDAO;
import gameserver.dao.MailDAO;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Letter;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Mailbox;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.gameobjects.player.StorageType;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MySQL5MailDAO extends MailDAO
{
	private static final Logger log = Logger.getLogger(MySQL5MailDAO.class);
	
	@Override
	public Mailbox loadPlayerMailbox(Player player)
	{
		final Mailbox mailbox = new Mailbox();
		final int playerId = player.getObjectId();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM mail WHERE mailRecipientId = ? LIMIT 100");
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			List<Item> mailboxItems = loadMailboxItems(playerId);
			while(rset.next())
			{
				int mailUniqueId = rset.getInt("mailUniqueId");
				int recipientId = rset.getInt("mailRecipientId");
				String senderName = rset.getString("senderName");
				String mailTitle = rset.getString("mailTitle");
				String mailMessage = rset.getString("mailMessage");
				int unread = rset.getInt("unread");
				int attachedItemId = rset.getInt("attachedItemId");
				long attachedKinahCount = rset.getLong("attachedKinahCount");
				int express = rset.getInt("express");
				Timestamp recievedTime = rset.getTimestamp("recievedTime");
				Item attachedItem = null;
				if(attachedItemId != 0)
					for(Item item : mailboxItems)
						if(item.getObjectId() == attachedItemId)
						{
							if(item.getItemTemplate().isArmor(true) || item.getItemTemplate().isWeapon())
								DAOManager.getDAO(ItemStoneListDAO.class).load(Collections.singletonList(item));
							
							attachedItem = item;
						}
						
				Letter letter = new Letter(mailUniqueId, recipientId, attachedItem, attachedKinahCount, mailTitle,
					mailMessage, senderName, recievedTime, unread == 1, express == 1);
				letter.setPersistState(PersistentState.UPDATED);
				mailbox.putLetterToMailbox(letter);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		return mailbox;
	}
	
	private List<Item> loadMailboxItems(final int playerId)
	{
		final List<Item> mailboxItems = new ArrayList<Item>();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM inventory WHERE `itemOwner` = ? AND `itemLocation` = 127");
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int itemUniqueId = rset.getInt("itemUniqueId");
				int itemId = rset.getInt("itemId");
				long itemCount = rset.getLong("itemCount");
				int itemColor = rset.getInt("itemColor");
				int itemOwner = rset.getInt("itemOwner");
				int isEquiped = rset.getInt("isEquiped");
				int isSoulBound = rset.getInt("isSoulBound");
				int slot = rset.getInt("slot");
				int enchant = rset.getInt("enchant");
				int itemSkin = rset.getInt("itemSkin");
				int fusionedItem = rset.getInt("fusionedItem");
				int optionalSocket = rset.getInt("optionalSocket");
				int optionalFusionSocket = rset.getInt("optionalFusionSocket");
				String crafterName = rset.getString("itemCreator");
				long itemCreationTime = rset.getTimestamp("itemCreationTime").getTime();
				long tempItemTime = rset.getLong("itemExistTime");
				int tempTradeTime = rset.getInt("itemTradeTime");
				Item item = new Item(itemUniqueId, itemId, itemCount, itemColor, isEquiped == 1, isSoulBound == 1, slot, StorageType.MAILBOX.getId(), enchant, itemSkin, fusionedItem, optionalSocket, optionalFusionSocket, crafterName, itemOwner, itemCreationTime, tempItemTime, tempTradeTime);
				item.setPersistentState(PersistentState.UPDATED);
				mailboxItems.add(item);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		return mailboxItems;
	}
	
	@Override
	public void storeMailbox(Player player)
	{
		Mailbox mailbox = player.getMailbox();
		if (mailbox == null)
			return;
		Collection<Letter> letters = mailbox.getLetters();		
		for(Letter letter : letters)
		{
			storeLetter(letter.getTimeStamp(), letter);
		}		
	}
	
	@Override
	public boolean storeLetter(Timestamp time, Letter letter)
	{
		boolean result = false;
		switch(letter.getLetterPersistentState())
		{
			case NEW:
				result = saveLetter(time, letter);
				break;
			
			case UPDATE_REQUIRED:
				result = updateLetter(time, letter);
				break;
			/**	
			case DELETED:
				return deleteLetter(letter);*/
		}
		letter.setPersistState(PersistentState.UPDATED);
		
		return result;
	}
	
	private boolean saveLetter(final Timestamp time, final Letter letter)
	{
		int attachedItemId = 0;
		if(letter.getAttachedItem() != null)
			attachedItemId = letter.getAttachedItem().getObjectId();
			
		final int fAttachedItemId = attachedItemId;
		boolean success = false;
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("INSERT INTO `mail` (`mailUniqueId`, `mailRecipientId`, `senderName`, `mailTitle`, `mailMessage`, `unread`, `attachedItemId`, `attachedKinahCount`, `express`, `recievedTime`) VALUES(?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1, letter.getObjectId());
			stmt.setInt(2, letter.getRecipientId());
			stmt.setString(3, letter.getSenderName());
			stmt.setString(4, letter.getTitle());
			stmt.setString(5, letter.getMessage());
			stmt.setBoolean(6, letter.isUnread());
			stmt.setInt(7, fAttachedItemId);
			stmt.setLong(8, letter.getAttachedKinah());
			stmt.setBoolean(9, letter.isExpress());
			stmt.setTimestamp(10, time);
			stmt.execute();
			success = true;
		}
		catch(Exception e)
		{
			log.error(e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return success;
	}
	
	private boolean updateLetter(final Timestamp time, final Letter letter)
	{
		int attachedItemId = 0;
		if(letter.getAttachedItem() != null)
			attachedItemId = letter.getAttachedItem().getObjectId();
		
		final int fAttachedItemId = attachedItemId;
		boolean success = false;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE mail SET  unread=?, attachedItemId=?, attachedKinahCount=?, recievedTime=? WHERE mailUniqueId=?");
			stmt.setBoolean(1, letter.isUnread());
			stmt.setInt(2, fAttachedItemId);
			stmt.setLong(3, letter.getAttachedKinah());
			stmt.setTimestamp(4, time);
			stmt.setInt(5, letter.getObjectId());
			stmt.execute();
			success = true;
		}
		catch(Exception e)
		{
			log.error(e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return success;
	}
	
	@Override
	public boolean deleteLetter (final int letterId)
	{
		Connection con = null;
		boolean success = false;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("DELETE FROM mail WHERE mailUniqueId=?");
			stmt.setInt(1, letterId);
			stmt.execute();
			success = true;
		}
		catch(Exception e)
		{
			log.error(e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return success;
	}

	@Override
	public void updateOfflineMailCounter(final PlayerCommonData recipientCommonData)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement("UPDATE players SET mailboxLetters=? WHERE name=?");
			stmt.setInt(1, recipientCommonData.getMailboxLetters());				
			stmt.setString(2, recipientCommonData.getName());
			stmt.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public int[] getUsedIDs() 
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT mailUniqueId FROM mail", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = statement.executeQuery();
			rs.last();
			int count = rs.getRow();
			rs.beforeFirst();
			int[] ids = new int[count];
			for(int i = 0; i < count; i++)
			{
				rs.next();
				ids[i] = rs.getInt("mailUniqueId");
			}
			return ids;
		}
		catch(SQLException e)
		{
			log.error("Can't get list of id's from mail table", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}

		return new int[0];
	}
	
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}