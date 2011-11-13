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

package gameserver.model.legion;

public class LegionMember
{
	private static final int LP_CENT_NONE = 0x60;
	private int objectId = 0;
	protected Legion legion = null;
	protected String nickname = "";
	protected String selfIntro = "";
	protected LegionRank rank = LegionRank.LEGIONARY;
	public LegionMember(int objectId)
	{
		this.objectId = objectId;
	}

	public LegionMember(int objectId, Legion legion, LegionRank rank)
	{
		this.setObjectId(objectId);
		this.setLegion(legion);
		this.setRank(rank);
	}

	public LegionMember()
	{
	}

	public void setLegion(Legion legion)
	{
		this.legion = legion;
	}

	public Legion getLegion()
	{
		return legion;
	}

	public void setRank(LegionRank rank)
	{
		this.rank = rank;
	}

	public LegionRank getRank()
	{
		return rank;
	}

	public boolean isBrigadeGeneral()
	{
		return rank == LegionRank.BRIGADE_GENERAL;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setSelfIntro(String selfIntro)
	{
		this.selfIntro = selfIntro;
	}

	public String getSelfIntro()
	{
		return selfIntro;
	}

	public void setObjectId(int objectId)
	{
		this.objectId = objectId;
	}

	public int getObjectId()
	{
		return objectId;
	}

   public boolean hasRights(int type)
   {
      if(getRank() == LegionRank.BRIGADE_GENERAL)
         return true;

      int legionaryPermission1 = getLegion().getLegionaryPermission1();
      int legionaryPermission2 = getLegion().getLegionaryPermission2();
      int centurionPermission1 = getLegion().getCenturionPermission1();
      int centurionPermission2 = getLegion().getCenturionPermission2();
      int deputyPermission1 = getLegion().getDeputyPermission1();
      int deputyPermission2 = getLegion().getDeputyPermission2();
      int volunteerPermission1 = getLegion().getVolunteerPermission1();
      int volunteerPermission2 = getLegion().getVolunteerPermission2();

      switch(type)
      {
        case 1:
			if(getRank().canInviteToLegion(legionaryPermission1, centurionPermission1))
		return true;

        case 2:
			if(getRank().canKickFromLegion(legionaryPermission1, centurionPermission1))
		return true;

        case 3:
			if(getRank().canUseLegionWarehouse(legionaryPermission1, centurionPermission1, deputyPermission1))
		return true;

        case 4:
			if(getRank().canEditAnnouncement(legionaryPermission2, centurionPermission2))
		return true;

        case 5:
			if(getRank().canStoreLegionWarehouse(legionaryPermission2, centurionPermission2, deputyPermission2, volunteerPermission2))
		return true;

		case 6:
			if(getRank().canUseArtifact(legionaryPermission2, centurionPermission2))
		return true;

		case 7:
			if(getRank().canUseGateGuardianStone(legionaryPermission2, centurionPermission2, deputyPermission2, volunteerPermission2))
		return true;
      }
      return false;
   }
}