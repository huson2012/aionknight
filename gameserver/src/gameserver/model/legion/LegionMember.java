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
        this.objectId = objectId;
        this.legion = legion;
        this.rank = rank;
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
      if(rank == LegionRank.BRIGADE_GENERAL)
         return true;

      int legionaryPermission1 = legion.getLegionaryPermission1();
      int legionaryPermission2 = legion.getLegionaryPermission2();
      int centurionPermission1 = legion.getCenturionPermission1();
      int centurionPermission2 = legion.getCenturionPermission2();
      int deputyPermission1 = legion.getDeputyPermission1();
      int deputyPermission2 = legion.getDeputyPermission2();
      int volunteerPermission1 = legion.getVolunteerPermission1();
      int volunteerPermission2 = legion.getVolunteerPermission2();

      switch(type)
      {
        case 1:
			if(rank.canInviteToLegion(legionaryPermission1, centurionPermission1))
		return true;

        case 2:
			if(rank.canKickFromLegion(legionaryPermission1, centurionPermission1))
		return true;

        case 3:
			if(rank.canUseLegionWarehouse(legionaryPermission1, centurionPermission1, deputyPermission1))
		return true;

        case 4:
			if(rank.canEditAnnouncement(legionaryPermission2, centurionPermission2))
		return true;

        case 5:
			if(rank.canStoreLegionWarehouse(legionaryPermission2, centurionPermission2, deputyPermission2, volunteerPermission2))
		return true;

		case 6:
			if(rank.canUseArtifact(legionaryPermission2, centurionPermission2))
		return true;

		case 7:
			if(rank.canUseGateGuardianStone(legionaryPermission2, centurionPermission2, deputyPermission2, volunteerPermission2))
		return true;
      }
      return false;
   }
}
