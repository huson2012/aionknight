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

import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CUBE_UPDATE extends AionServerPacket
{
	private Player player;
	private int cubeType;
	private int advancedSlots;

	/**
	 * Создание нового пакета SM_CUBE_UPDATE
	 * 
	 * @param player
	 */
	public SM_CUBE_UPDATE(Player player, int cubeType, int advancedSlots)
	{
		this.player = player;
		this.cubeType = cubeType;
		this.advancedSlots = advancedSlots;
	}

	public SM_CUBE_UPDATE(Player player, int cubeType)
	{
		this.player = player;
		this.cubeType = cubeType;
		this.advancedSlots = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeC(buf, cubeType);
		writeC(buf, advancedSlots);
		switch(cubeType)
		{
			case 0:
				writeD(buf, player.getInventory().size());
				writeC(buf, player.getCubeSize()); // Размер куба npc (max 5, в настоящее время)
				writeC(buf, 0); // Размер куба при выполнении квеста (макс 2, в настоящее время)
				writeC(buf, 0); // Неизвестно
				break;
			case 6:
				break;
			default:
				break;
		}
	}
}