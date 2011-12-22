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

import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

public class SM_ATTACK_STATUS extends AionServerPacket
{
    private Creature creature;
    private TYPE type;
    private int skillId;
    private int value;
    private int logId;
   
    public static enum TYPE
    {
    	NATURAL_HP(3), USED_HP(4), REGULAR(5), HP(7), DELAYDAMAGE(10),
    	FALL_DAMAGE(17), HEALED_MP(19), ABSORBED_MP(20), MP(21),
    	NATURAL_MP(22), FP_RINGS(23), FP(25), NATURAL_FP(26);
    	
    	private int value;
    	
    	private TYPE(int value)
    	{
    		this.value = value;
    	}
    	
    	public int getValue()
    	{
    		return this.value;
    	}
    }
	
    public SM_ATTACK_STATUS(Creature creature, TYPE type, int value, int skillId, int logId)
    {
    	this.creature = creature;
		this.type = type;
		this.skillId = skillId;
		this.value = value;
    	this.logId = logId;
    }
    
    public SM_ATTACK_STATUS(Creature creature, TYPE type, int value)
    {
    	this.creature = creature;
		this.type = type;
		this.skillId = 0;
		this.value = value;
    	this.logId = 170;
    }
    
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{		
		writeD(buf, creature.getObjectId());
		writeD(buf, value);
		writeC(buf, type.getValue());
		writeC(buf, creature.getLifeStats().getHpPercentage());
		writeH(buf, skillId);
		writeH(buf, logId);
	}	
}