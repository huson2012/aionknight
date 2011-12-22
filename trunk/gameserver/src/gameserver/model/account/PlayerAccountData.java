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

package gameserver.model.account;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.PlayerAppearance;
import gameserver.model.gameobjects.player.PlayerCommonData;
import gameserver.model.legion.Legion;
import gameserver.model.legion.LegionMember;
import java.sql.Timestamp;
import java.util.List;

public class PlayerAccountData
{
	private PlayerCommonData playerCommonData;
	private PlayerAppearance appereance;
	private List<Item> equipment;
	private Timestamp creationDate;
	private Timestamp deletionDate;
	private LegionMember legionMember;
	
	public PlayerAccountData(PlayerCommonData playerCommonData, PlayerAppearance appereance, List<Item> equipment, 
	LegionMember legionMember)
	{
		this.playerCommonData = playerCommonData;
		this.appereance = appereance;
		this.equipment = equipment;
		this.legionMember = legionMember;
	}

	public Timestamp getCreationDate()
	{
		return creationDate;
	}

	public void setDeletionDate(Timestamp deletionDate)
	{
		this.deletionDate = deletionDate;
	}

	public Timestamp getDeletionDate()
	{
		return deletionDate;
	}

	public int getDeletionTimeInSeconds()
	{
		return deletionDate == null ? 0 : (int) (deletionDate.getTime() / 1000);
	}

	public PlayerCommonData getPlayerCommonData()
	{
		return playerCommonData;
	}

	public void setPlayerCommonData(PlayerCommonData playerCommonData)
	{
		this.playerCommonData = playerCommonData;
	}

	public PlayerAppearance getAppereance()
	{
		return appereance;
	}

	public void setCreationDate(Timestamp creationDate)
	{
		this.creationDate = creationDate;
	}

	public Legion getLegion()
	{
		return legionMember.getLegion();
	}

	public boolean isLegionMember()
	{
		return legionMember != null;
	}

	public List<Item> getEquipment()
	{
		return equipment;
	}

	public void setEquipment(List<Item> equipment)
	{
		this.equipment = equipment;
	}
}