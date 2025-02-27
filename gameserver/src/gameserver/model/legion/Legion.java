/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.model.legion;

import gameserver.configs.main.LegionConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.world.World;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

public class Legion
{
	private static final int PERMISSION1_MIN = 0x00;
	private static final int PERMISSION2_MIN = 0x00;
	private static final int VOLUNTEER_PERMISSION1_MAX = 0x00;
	private static final int VOLUNTEER_PERMISSION2_MAX = 0x18;
	private static final int LEGIONARY_PERMISSION1_MAX = 0x04;
	private static final int LEGIONARY_PERMISSION2_MAX = 0x18;
	private static final int CENTURION_PERMISSION1_MAX = 0x1C;
	private static final int CENTURION_PERMISSION2_MAX = 0x1E;
	private static final int DEPUTY_PERMISSION1_MAX = 0x1C;
	private static final int DEPUTY_PERMISSION2_MAX = 0x1E;
	private int legionId = 0;
	private String legionName = "";
	private int legionLevel	= 1;
	private int	legionRank = 0;
	private int	contributionPoints = 0;
	private List<Integer> legionMembers = new ArrayList<Integer>();
	private int deputyPermission1 = 0x00;
	private int deputyPermission2 = 0x00;
	private int centurionPermission1 = 0x00;
	private int centurionPermission2 = 0x00;
	private int legionaryPermission1 = 0x00;
	private int legionaryPermission2 = 0x00;
	private int volunteerPermission1 = 0x00;
	private int volunteerPermission2 = 0x00;
	private int	disbandTime;
	private TreeMap<Timestamp, String> announcementList = new TreeMap<Timestamp, String>();
	private LegionEmblem legionEmblem = new LegionEmblem();
	private LegionWarehouse legionWarehouse;
	private SortedSet<LegionHistory> legionHistory;
	public Legion(int legionId, String legionName)
	{
		this();
		this.legionId = legionId;
		this.legionName = legionName;
	}

	public Legion()
	{
		this.legionWarehouse = new LegionWarehouse(this);
		this.legionHistory = new TreeSet<LegionHistory>(new Comparator<LegionHistory>(){

			@Override
			public int compare(LegionHistory o1, LegionHistory o2)
			{
				return o1.getTime().getTime() < o2.getTime().getTime() ? 1 : -1;
			}
			
		});
	}

	public void setLegionId(int legionId)
	{
		this.legionId = legionId;
	}

	public int getLegionId()
	{
		return legionId;
	}

	public void setLegionName(String legionName)
	{
		this.legionName = legionName;
	}

	public String getLegionName()
	{
		return legionName;
	}

	public void setLegionMembers(ArrayList<Integer> legionMembers)
	{
		this.legionMembers = legionMembers;
	}

	public List<Integer> getLegionMembers()
	{
		return legionMembers;
	}

	public ArrayList<Player> getOnlineLegionMembers()
	{
		ArrayList<Player> onlineLegionMembers = new ArrayList<Player>();
		for(int legionMemberObjId : legionMembers)
		{
			Player onlineLegionMember = World.getInstance().findPlayer(legionMemberObjId);
			if(onlineLegionMember != null)
				onlineLegionMembers.add(onlineLegionMember);
		}
		return onlineLegionMembers;
	}

	public boolean addLegionMember(int playerObjId)
	{
		if(canAddMember())
		{
			legionMembers.add(playerObjId);
			return true;
		}
		return false;
	}

	public void deleteLegionMember(int playerObjId)
	{
		legionMembers.remove(new Integer(playerObjId));
	}

	public boolean setLegionPermissions(int lp1, int lp2, int cp1, int cp2, int dp1, int dp2, int vp1, int vp2)
	{
		if(checkPermissions(vp1, vp2, lp1, lp2, cp1, cp2, dp1, dp2))
	      {
                this.deputyPermission1 = dp1;
                this.deputyPermission2 = dp2;
        	    this.centurionPermission1 = cp1;
        	    this.centurionPermission2 = cp2;
                this.legionaryPermission1 = lp1;
                this.legionaryPermission2 = lp2;
                this.volunteerPermission1 = vp1;
                this.volunteerPermission2 = vp2;

	         return true;
	      }
	      return false;
	}

	private boolean checkPermissions(int vp1,int  vp2,int  lp1,int  lp2,int  cp1,int  cp2,int  dp1,int  dp2)
	{
	    if(vp1 < PERMISSION1_MIN || vp1 > VOLUNTEER_PERMISSION1_MAX)
	        return false;
	    if(vp2 < PERMISSION2_MIN || vp2 > VOLUNTEER_PERMISSION2_MAX)
	        return false;
	    if(lp1 < PERMISSION1_MIN || lp1 > LEGIONARY_PERMISSION1_MAX)
	        return false;
	    if(lp2 < PERMISSION2_MIN || lp2 > LEGIONARY_PERMISSION2_MAX)
	        return false;
	    if(cp1 < PERMISSION1_MIN || cp1 > CENTURION_PERMISSION1_MAX)
	        return false;
	    if(cp2 < PERMISSION2_MIN || cp2 > CENTURION_PERMISSION2_MAX)
	        return false;
	    if(dp1 < PERMISSION1_MIN || dp1 > DEPUTY_PERMISSION1_MAX)
	        return false;
        return !(dp2 < PERMISSION2_MIN || dp2 > DEPUTY_PERMISSION2_MAX);
    }

	public int getLegionaryPermission1()
	{
		return legionaryPermission1;
	}

	public int getLegionaryPermission2()
	{
		return legionaryPermission2;
	}

	public int getCenturionPermission1()
	{
		return centurionPermission1;
	}

	public int getCenturionPermission2()
	{
		return centurionPermission2;
	}

	public int getLegionLevel()
	{
		return legionLevel;
	}

	public void setLegionLevel(int legionLevel)
	{
		this.legionLevel = legionLevel;
	}

	public void setLegionRank(int legionRank)
	{
		this.legionRank = legionRank;
	}

	public int getLegionRank()
	{
		return legionRank;
	}

	public void addContributionPoints(int contributionPoints)
	{
		this.contributionPoints = this.contributionPoints + contributionPoints;
	}

	public void setContributionPoints(int contributionPoints)
	{
		this.contributionPoints = contributionPoints;
	}

	public int getContributionPoints()
	{
		return contributionPoints;
	}

	public boolean hasRequiredMembers()
	{
		switch(legionLevel)
		{
			case 1:
				if(legionMembers.size() >= LegionConfig.LEGION_LEVEL2_REQUIRED_MEMBERS)
					return true;
				break;
			case 2:
				if(legionMembers.size() >= LegionConfig.LEGION_LEVEL3_REQUIRED_MEMBERS)
					return true;
				break;
			case 3:
				if(legionMembers.size() >= LegionConfig.LEGION_LEVEL4_REQUIRED_MEMBERS)
					return true;
				break;
			case 4:
				if(legionMembers.size() >= LegionConfig.LEGION_LEVEL5_REQUIRED_MEMBERS)
					return true;
				break;
		}
		return false;
	}

	public int getKinahPrice()
	{
		switch(legionLevel)
		{
			case 1:
				return LegionConfig.LEGION_LEVEL2_REQUIRED_KINAH;
			case 2:
				return LegionConfig.LEGION_LEVEL3_REQUIRED_KINAH;
			case 3:
				return LegionConfig.LEGION_LEVEL4_REQUIRED_KINAH;
			case 4:
				return LegionConfig.LEGION_LEVEL5_REQUIRED_KINAH;
		}
		return 0;
	}

	public int getContributionPrice()
	{
		switch(legionLevel)
		{
			case 1:
				return LegionConfig.LEGION_LEVEL2_REQUIRED_CONTRIBUTION;
			case 2:
				return LegionConfig.LEGION_LEVEL3_REQUIRED_CONTRIBUTION;
			case 3:
				return LegionConfig.LEGION_LEVEL4_REQUIRED_CONTRIBUTION;
			case 4:
				return LegionConfig.LEGION_LEVEL5_REQUIRED_CONTRIBUTION;
		}
		return 0;
	}

	private boolean canAddMember()
	{
		switch(legionLevel)
		{
			case 1:
				if(legionMembers.size() < LegionConfig.LEGION_LEVEL1_MAX_MEMBERS)
					return true;
				break;
			case 2:
				if(legionMembers.size() < LegionConfig.LEGION_LEVEL2_MAX_MEMBERS)
					return true;
				break;
			case 3:
				if(legionMembers.size() < LegionConfig.LEGION_LEVEL3_MAX_MEMBERS)
					return true;
				break;
			case 4:
				if(legionMembers.size() < LegionConfig.LEGION_LEVEL4_MAX_MEMBERS)
					return true;
				break;
			case 5:
				if(legionMembers.size() < LegionConfig.LEGION_LEVEL5_MAX_MEMBERS)
					return true;
				break;
		}
		return false;
	}

	public void setAnnouncementList(TreeMap<Timestamp, String> announcementList)
	{
		this.announcementList = announcementList;
	}

	public void addAnnouncementToList(Timestamp unixTime, String announcement)
	{
		this.announcementList.put(unixTime, announcement);
	}

	public void removeFirstEntry()
	{
		this.announcementList.remove(this.announcementList.firstEntry().getKey());
	}

	public TreeMap<Timestamp, String> getAnnouncementList()
	{
		return this.announcementList;
	}

	public Entry<Timestamp, String> getCurrentAnnouncement()
	{
		if(this.announcementList.size() > 0)
			return this.announcementList.lastEntry();
		return null;
	}

	public void setDisbandTime(int disbandTime)
	{
		this.disbandTime = disbandTime;
	}

	public int getDisbandTime()
	{
		return disbandTime;
	}

	public boolean isDisbanding()
	{
        return disbandTime > 0;
    }

	public boolean isMember(int playerObjId)
	{
		return legionMembers.contains(playerObjId);
	}

	public void setLegionEmblem(LegionEmblem legionEmblem)
	{
		this.legionEmblem = legionEmblem;
	}

	public LegionEmblem getLegionEmblem()
	{
		return legionEmblem;
	}

	public void setLegionWarehouse(LegionWarehouse legionWarehouse)
	{
		this.legionWarehouse = legionWarehouse;
	}

	public LegionWarehouse getLegionWarehouse()
	{
		return legionWarehouse;
	}

	public int getWarehouseSlots()
	{
		switch(legionLevel)
		{
			case 1:
				return 24;
			case 2:
				return 32;
			case 3:
				return 40;
			case 4:
				return 48;
			case 5:
				return 56;
		}
		return 24;
	}

	public Collection<LegionHistory> getLegionHistory()
	{
		return this.legionHistory;
	}

	public void addHistory(LegionHistory history)
	{
		this.legionHistory.add(history);
	}

    public int getDeputyPermission1() {

        return deputyPermission1;
    }

    public int getDeputyPermission2() {
		return deputyPermission2;
    }

    public int getVolunteerPermission1() {
        return volunteerPermission1;
    }

    public int getVolunteerPermission2() {

        return volunteerPermission2;
    }
}
