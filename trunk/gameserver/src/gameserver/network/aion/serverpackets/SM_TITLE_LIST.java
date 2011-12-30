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

package gameserver.network.aion.serverpackets;

import gameserver.configs.main.GSConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Title;
import gameserver.model.gameobjects.player.TitleList;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.TitleService;
import gameserver.utils.PacketSendUtility;
import java.nio.ByteBuffer;

public class SM_TITLE_LIST extends AionServerPacket
{
	private TitleList	titleList;
	private int objectId;
	private int titleId;

	// TODO Make List from DataBase
	public SM_TITLE_LIST(Player player)
	{
		this.titleList = player.getTitleList();
	}

	public SM_TITLE_LIST(int objectId, int titleId)
	{
		this.objectId = objectId;
		this.titleId = titleId;
	}

	public SM_TITLE_LIST(int titleId)
	{
		this.titleId = titleId;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(titleList != null)
		{
			writeImplTitleList(buf);
			return;
		}

		if(objectId > 0 && titleId > 0)
		{
			writeImplTitleUpdate(buf);
			return;
		}

		writeImplTitleSet(buf);
		return;
	}

	private void writeImplTitleList(ByteBuffer buf)
	{
		Player player = titleList.getOwner();
		TitleService.removeExpiredTitles(player);

		if(GSConfig.SERVER_VERSION.startsWith("2."))
			writeH(buf, 0); // unk
		else
			writeC(buf, 0); // unk

		writeH(buf, titleList.size());

		for(final Title title : titleList.getTitles())
		{
			writeD(buf, title.getTitleId());
			writeD(buf, (int)title.getTitleTimeLeft());
		}

		if(player.getCommonData().getTitleId() > 0)
		{
			if(titleList.canAddTitle(player.getCommonData().getTitleId()))
			{
				player.getCommonData().setTitleId(0);
				PacketSendUtility.sendMessage(player, "The usage time of title has expired.");
			}
		}

	}

	private void writeImplTitleUpdate(ByteBuffer buf)
	{
		writeD(buf, objectId);
		writeD(buf, titleId);
	}

	protected void writeImplTitleSet(ByteBuffer buf)
	{
		writeC(buf, 1);
		writeD(buf, titleId);
	}
}
