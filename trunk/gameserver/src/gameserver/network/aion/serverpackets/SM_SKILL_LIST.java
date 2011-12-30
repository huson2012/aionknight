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

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillListEntry;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;

/**
 * In this packet Server is sending Skill Info.
 */
public class SM_SKILL_LIST extends AionServerPacket
{

	private SkillListEntry[] skillList;
	private int messageId;
	private int skillNameId;
	private String skillLvl;
	public static final int YOU_LEARNED_SKILL = 1300050;

	/**
	 * This constructor is used on player entering the world
	 * 
 	 * Constructs new <tt>SM_SKILL_LIST </tt> packet
 	 */

	public SM_SKILL_LIST(Player player)
 	{
		this.skillList = player.getSkillList().getAllSkills();
		this.messageId = 0;
 	}
	
	public SM_SKILL_LIST(SkillListEntry skillListEntry, int messageId)
 	{
		this.skillList = new SkillListEntry[]{skillListEntry};
		this.messageId = messageId;
		this.skillNameId = DataManager.SKILL_DATA.getSkillTemplate(skillListEntry.getSkillId()).getNameId();
		this.skillLvl = String.valueOf(skillListEntry.getSkillLevel());
 	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		final int size = skillList.length;
		writeH(buf, size); //skills list size
		
		if (size > 0)
		{
			for (SkillListEntry entry : skillList)
			{
				writeH(buf, entry.getSkillId());//id
				writeH(buf, entry.getSkillLevel());//lvl
				writeC(buf, 0x00);
				writeC(buf, entry.getExtraLvl());
				writeD(buf, 0); //use time? [s]
				writeC(buf, entry.isStigma() ? 1 : 0); // stigma flag
			}
		}
		writeD(buf, messageId);
		if (messageId != 0);
		{
			writeH(buf, 0x24); //unk
			writeD(buf, skillNameId);
			writeH(buf, 0x00);
			writeS(buf, skillLvl);
		}
	}
}