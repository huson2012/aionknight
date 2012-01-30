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

package gameserver.network.aion;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.Letter;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import java.nio.ByteBuffer;
import java.util.Collection;

public abstract class MailServicePacket extends InventoryPacket
{
	protected void writeLettersList(ByteBuffer buf, Collection<Letter> letters, Player player)
	{
		writeC(buf, 2);
		writeD(buf, player.getObjectId());
		writeC(buf, 0);
		writeH(buf, (0 - player.getMailbox().size()));

		for(Letter letter : letters)
		{
			writeD(buf, letter.getObjectId());
			writeS(buf, letter.getSenderName());
			writeS(buf, letter.getTitle());
			if(letter.isUnread())
				writeC(buf, 0);
			else
				writeC(buf, 1);
			if(letter.getAttachedItem() != null)
			{
				writeD(buf, letter.getAttachedItem().getObjectId());
				writeD(buf, letter.getAttachedItem().getItemTemplate().getTemplateId());
			}
			else
			{
				writeD(buf, 0);
				writeD(buf, 0);
			}
			writeQ(buf, letter.getAttachedKinah());
			writeC(buf, 0);
		}
	}

	protected void writeEmptyLettersList(ByteBuffer buf, Player player)
	{
		writeC(buf, 2);
		writeD(buf, player.getObjectId());
		writeH(buf, 0);
		writeC(buf, 0);
	}

	protected void writeMailMessage(ByteBuffer buf, int messageId)
	{
		writeC(buf, 1);
		writeC(buf, messageId);
	}

	protected void writeMailboxState(ByteBuffer buf, int mailCount, int unreadCount, boolean hasExpress)
	{
		writeC(buf, 0);
		writeC(buf, mailCount);
		writeC(buf, 0);
		writeC(buf, unreadCount);
		writeC(buf, 0);
		writeC(buf, hasExpress ? 1 : 0);
		writeH(buf, 0);
		writeC(buf, 0);
	}

	protected void writeLetterRead(ByteBuffer buf, Player player, Letter letter, long time)
	{
		writeC(buf, 3);
		writeD(buf, letter.getRecipientId());
		writeD(buf, player.getMailbox().size());
		writeD(buf, 0);
		writeD(buf, letter.getObjectId());
		writeD(buf, letter.getRecipientId());
		writeS(buf, letter.getSenderName());
		writeS(buf, letter.getTitle());
		writeS(buf, letter.getMessage());

		Item item = letter.getAttachedItem();
		if(item != null)
		{
			ItemTemplate itemTemplate = item.getItemTemplate();

			writeMailGeneralInfo(buf, item);

			if(itemTemplate.isArmor())
				writeArmorInfo(buf, item);
			else if(itemTemplate.isWeapon())
				writeWeaponInfo(buf, item);
			else
				writeGeneralItemInfo(buf, item);
		}
		else
		{
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
		}

		writeQ(buf, letter.getAttachedKinah());
		writeC(buf, 0);
		writeQ(buf, time / 1000);
		writeC(buf, 0);
	}

	protected void writeLetterState(ByteBuffer buf, int letterId, int attachmentType)
	{
		writeC(buf, 5);
		writeD(buf, letterId);
		writeC(buf, attachmentType);
		writeC(buf, 1);
	}

	protected void writeLetterDelete(ByteBuffer buf, Player player, int letterId)
	{
		writeC(buf, 6);
		writeD(buf, player.getMailbox().size());
		writeD(buf, 0);
		writeD(buf, letterId);
	}
}
