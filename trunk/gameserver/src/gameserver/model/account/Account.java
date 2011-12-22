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

import gameserver.model.Race;
import gameserver.model.gameobjects.player.Storage;

import java.sql.Timestamp;
import java.util.*;

public class Account implements Iterable<PlayerAccountData>
{
    private final int id;
    private String name;
    private byte accessLevel;
    private byte membership;
    private AccountTime accountTime;
    private Map<Integer, PlayerAccountData> players = new HashMap<Integer, PlayerAccountData>();
    private Storage accountWarehouse;
    private int numberOfAsmos = 0;
    private int numberOfElyos = 0;
	public int shopMoney;
    private CharacterPasskey characterPasskey;

    public Account(int id)
    {
		this.id = id;
    }

    public int getId()
    {
		return id;
    }

    public String getName()
    {
		return name;
    }

    public void setName(String name)
    {
		this.name = name;
    }

    public AccountTime getAccountTime()
    {
		return accountTime;
    }

    public void setAccountTime(AccountTime accountTime)
    {
		this.accountTime = accountTime;
    }

    /**
     * @return the accessLevel
     */
    public byte getAccessLevel()
    {
            return accessLevel;
    }

    /**
     * @return the characterPasskey
     */
    public CharacterPasskey getCharacterPasskey()
    {
		if (characterPasskey == null)
			characterPasskey = new CharacterPasskey();
		return characterPasskey;
    }

    /**
     * @param accessLevel the accessLevel to set
     */
    public void setAccessLevel(byte accessLevel)
    {
		this.accessLevel = accessLevel;
    }

    /**
     * @return the membership
     */
    public byte getMembership()
    {
		return membership;
    }

    /**
     * @param membership the membership to set
     */
    public void setMembership(byte membership)
    {
		this.membership = membership;
    }

    @Override
    public boolean equals(Object o)
    {
		if(this == o)
		{
			return true;
		}
	
		if(!(o instanceof Account))
		{
			return false;
		}
	
		Account account = (Account) o;
	
		return id == account.id;
    }

    @Override
    public int hashCode()
    {
		return id;
    }

    /**
     * @param chaOid
     * @return PlayerAccountData
     */
    public PlayerAccountData getPlayerAccountData(int chaOid)
    {
		return players.get(chaOid);
    }

    /**
     * @param accPlData
     */
    public void addPlayerAccountData(PlayerAccountData accPlData)
    {
        players.put(accPlData.getPlayerCommonData().getPlayerObjId(), accPlData);
        switch (accPlData.getPlayerCommonData().getRace())
        {
            case ASMODIANS:
				numberOfAsmos++;
			break;
            case ELYOS:
				numberOfElyos++;
			break;
        }
    }

    /**
     * @return the accountWarehouse
     */
    public Storage getAccountWarehouse()
    {
		return accountWarehouse;
    }

    /**
     * @param accountWarehouse the accountWarehouse to set
     */
    public void setAccountWarehouse(Storage accountWarehouse)
    {
		this.accountWarehouse = accountWarehouse;
    }

    /** Returns the number of players that are on this account */
    public int size()
    {
		return players.size();
    }

    /**
     * Sorts the accounts on last online.
     */
    public ArrayList<PlayerAccountData> getSortedAccountsList()
    {
		ArrayList<PlayerAccountData> list = new ArrayList<PlayerAccountData>();
		list.addAll(players.values());
		Collections.sort(list, new Comparator<PlayerAccountData>(){
			@Override
			public int compare(PlayerAccountData x, PlayerAccountData y)
			{
				Timestamp t1 = x.getPlayerCommonData().getLastOnline();
				Timestamp t2 = y.getPlayerCommonData().getLastOnline();
				if(t2 == null)
					return 1;
				else if(t1 == null)
					return -1;
				return y.getPlayerCommonData().getLastOnline().compareTo(x.getPlayerCommonData().getLastOnline());
			}
		});
		return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<PlayerAccountData> iterator()
    {
		return players.values().iterator();
    }

    public int getNumberOf (Race race)
    {
        switch (race)
        {
            case ASMODIANS:
				return numberOfAsmos;
            case ELYOS:
		return numberOfElyos;
        }
        return 0;
    }

    public void decrementCountOf (Race race)
    {
        switch (race)
        {
            case ASMODIANS:
                 numberOfAsmos--;
            break;
            case ELYOS:
                 numberOfElyos--;
            break;
        }
    }

    public int getShopMoney()
    {
		return shopMoney;
    }

    public void SetShopMoney(int shopMoney)
    {
		this.shopMoney = shopMoney;
    }
}