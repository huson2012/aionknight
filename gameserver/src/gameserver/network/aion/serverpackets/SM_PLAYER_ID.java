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

import gameserver.model.gameobjects.AionObject;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_PLAYER_ID extends AionServerPacket
{
    private AionObject playerAionObject;
    private boolean init = false;
    private int instanceId = 0;
    private int remainingTime = 0;

    public SM_PLAYER_ID(AionObject playerAionObject)
    {
        this.playerAionObject = playerAionObject;
    }
    
    public SM_PLAYER_ID(AionObject playerAionObject, boolean init)
    {
    	this.playerAionObject = playerAionObject;
    	this.init = init;
    }
    
    public SM_PLAYER_ID(AionObject playerAionObject, int id, int time)
    {
    	this.playerAionObject = playerAionObject;
    	this.instanceId = id;
    	this.remainingTime = time;
    }

    /**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		if(!init)
		{
			writeH(buf, 0x2);
	        writeD(buf, 0x0);
	        writeH(buf, 0x1);
	        writeD(buf, playerAionObject.getObjectId());
	        if(instanceId != 0 && remainingTime != 0)
	        {
	        	writeH(buf, 0x1); //instance info or not
	        	writeD(buf, instanceId); //instance ID
	        	writeD(buf, 0x0); //unk
	        	writeD(buf, remainingTime); //remaingTime in seconds
	        	writeH(buf, 0x0); //unk
	        	writeS(buf, playerAionObject.getName());
	        }
	        else
	        {
	        	writeH(buf, 0x0); //instance info or not
	        	writeS(buf, playerAionObject.getName());
	        }
		}
		else
		{
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
			writeH(buf, 0x0);
		}
	}
}
