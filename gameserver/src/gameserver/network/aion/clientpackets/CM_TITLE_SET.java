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

package gameserver.network.aion.clientpackets;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.Title;
import gameserver.model.gameobjects.stats.listeners.TitleChangeListener;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_TITLE_LIST;
import gameserver.utils.PacketSendUtility;

public class CM_TITLE_SET extends AionClientPacket 
{
   /**
	* Title id
	*/
	private int titleId;

	/**
	* Constructs new instance of <tt>CM_TITLE_SET </tt> packet
	*
	* @param opcode
	*/
	public CM_TITLE_SET(int opcode)
	{
		super(opcode);
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	protected void readImpl()
	{
		titleId = readC();
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		boolean isValidTitle = false;

		if(titleId != -1)
		{
			//check title exploit
			for(Title title : player.getTitleList().getTitles())
			{
				if(title.getTitleId() == titleId)
				{
					isValidTitle = true;
					break;
				}
			}

			if(!isValidTitle)
				return;
		}

		sendPacket(new SM_TITLE_LIST(titleId));
		PacketSendUtility.broadcastPacket(player, (new SM_TITLE_LIST(player.getObjectId(), titleId)));

		if(player.getCommonData().getTitleId() > 0)
			if (player.getGameStats() != null)
				TitleChangeListener.onTitleChange(player.getGameStats(), player.getCommonData().getTitleId(), false);

		player.getCommonData().setTitleId(titleId);
		if (player.getGameStats() != null)
		{
			TitleChangeListener.onTitleChange(player.getGameStats(), titleId, true);
		}
	}
}