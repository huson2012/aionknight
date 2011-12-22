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

import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_CMOTION extends AionServerPacket
{
	private CmotionList	cmotionList;
	private byte 		control;
	private int		 	active;
	private int 		cmotionid;
	
	/**
	 * Активный список
	 */
	public SM_CMOTION(Player player)
	{
		this.control	= 1;
		this.cmotionList 	= player.getCmotionList();
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public SM_CMOTION(Player player, int active, int id)
	{
		this.cmotionid  = id;
		this.active 	= active;
		this.control	= 2;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		switch (control)
		{
			case 1:
			{
				writeC(buf, 0x01); 
				writeH(buf, 0x09);
				for(Cmotion cmotion : cmotionList.getCmotions())
				{  
					writeC(buf, cmotion.getCmotionId());
					writeD(buf, 0);
					writeH(buf, 0);				
				}
			}
			break;
			case 2:
			{
				writeC(buf, 0x05); 
				writeH(buf, cmotionid);
				writeC(buf, active);
			}
			break;
		}
	}
}