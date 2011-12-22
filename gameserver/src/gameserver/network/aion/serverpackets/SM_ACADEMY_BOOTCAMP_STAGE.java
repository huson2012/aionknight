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

import java.nio.ByteBuffer;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

public class SM_ACADEMY_BOOTCAMP_STAGE extends AionServerPacket
{
	private int arenaId;
    private int stage;
    private int round;
    private boolean win;
    
    public SM_ACADEMY_BOOTCAMP_STAGE(int stage, int round, boolean win)
    {
    	int arenaId = 1;
		switch(stage)
		{
			case 1:
			case 2:
			case 3:
			case 4:
				arenaId = 1;
				break;
			case 5:
				arenaId = 2;
				break;
			case 6:
				arenaId = 3;
				break;
			case 7:
				arenaId = 4;
				break;
			case 8:
				arenaId = 5;
				break;				
			case 9:
				arenaId = 6;
				break;
			case 10:
				arenaId = 7;
				break;
			default:
				arenaId = 1;
				break;				
		}
    	
    	this.arenaId = arenaId;
        this.stage = stage;
        this.round = round;
        this.win = win;
    }
    
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        writeC(buf, 2);
        writeD(buf, 0);
        
        int stagevalue = (100000 * arenaId) + (1000 * stage) + (win ? 100 : 0) + round;
        writeD(buf, stagevalue);
    }
}