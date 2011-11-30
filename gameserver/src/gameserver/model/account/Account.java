/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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