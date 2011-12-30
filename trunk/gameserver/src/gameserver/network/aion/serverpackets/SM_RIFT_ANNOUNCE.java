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

import gameserver.model.Race;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import gameserver.spawn.RiftSpawnManager;
import java.nio.ByteBuffer;

public class SM_RIFT_ANNOUNCE extends AionServerPacket
{
	private Race race;
	private SpawnTemplate spawnTemplate;
	private RiftSpawnManager.RiftEnum rift;
	private int action;
	private int targetObjectId;
	private int annouce;
	private int usedEntries;
	private int count;
	private int time;

	public SM_RIFT_ANNOUNCE(int action, int annouce, int count, Race race)
	{
		this.action = action;
		this.annouce = annouce;
		this.count = count;
		this.race = race;
	}

	public SM_RIFT_ANNOUNCE(int action, int targetObjectId, RiftSpawnManager.RiftEnum rift, SpawnTemplate spawnTemplate, int time)
	{
		this.action = action;
		this.targetObjectId = targetObjectId;
		this.rift = rift;
		this.spawnTemplate = spawnTemplate;
		this.time = time;
	}

	public SM_RIFT_ANNOUNCE(int action, int targetObjectId, int usedEntries, int time)
	{
		this.action = action;
		this.targetObjectId = targetObjectId;
		this.usedEntries = usedEntries;
		this.time = time;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, action);
		switch(action)
		{
			case 9:
				writeC(buf, annouce);
				writeD(buf, count);
				switch(race.getRaceId())
				{
					case 1:
						writeD(buf, 0);
						break;
					case 2:
						writeD(buf, 1);
				}
				break;
			case 13:
				writeC(buf, 3);
				writeD(buf, targetObjectId);
				writeD(buf, usedEntries);
				writeD(buf, time);
				break;
			case 33:
				writeC(buf, 2);
				writeD(buf, targetObjectId);
				writeD(buf, rift.getEntries());
				writeD(buf, time);
				writeD(buf, 25);
				writeD(buf, rift.getMaxLevel());
				writeF(buf, spawnTemplate.getX());
				writeF(buf, spawnTemplate.getY());
				writeF(buf, spawnTemplate.getZ());
		}
	}
}
