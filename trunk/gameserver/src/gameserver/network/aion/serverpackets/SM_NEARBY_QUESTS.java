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

import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.services.QuestService;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_NEARBY_QUESTS extends AionServerPacket 
{
    private Integer[] questIds;
    private int size;

    public SM_NEARBY_QUESTS(List<Integer> questIds) 
	{
        this.questIds = questIds.toArray(new Integer[questIds.size()]);
        this.size = questIds.size();
    }


    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf) 
	{
        if (questIds == null || con.getActivePlayer() == null)
            return;
        int playerLevel = con.getActivePlayer().getLevel();
		writeC(buf, 0x00); // 2.1
		writeH(buf, (-1*size) & 0xFFFF); // 2.1
        for (int id : questIds) {
            writeH(buf, id);
            if (QuestService.checkLevelRequirement(id, playerLevel))
                writeH(buf, 0);
            else
                writeH(buf, 2);
        }
    }
}