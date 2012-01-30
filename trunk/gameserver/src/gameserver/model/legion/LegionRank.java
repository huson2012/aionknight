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

public enum LegionRank
{
    BRIGADE_GENERAL(0),
    SUB_GENERAL(1),
    CENTURION(2),
    LEGIONARY(3),
    NEW_LEGIONARY(4);

	private static final int LP_LEGION_WAREHOUSE = 0x04;
	private static final int LP_INVITE_TO_LEGION = 0x08;
	private static final int LP_KICK_FROM_LEGION = 0x10;
	private static final int LP_COMBINATION_60_12 = 0x0C;
	private static final int LP_COMBINATION_60_13 = 0x14;
	private static final int LP_COMBINATION_60_23 = 0x18;
	private static final int LP_COMBINATION_60_123 = 0x1C;
	private static final int LP_EDIT_ANNOUNCEMENT = 0x02;
	private static final int LP_ARTIFACT = 0x04;
	private static final int LP_GATE_GUARDIAN_STONE	= 0x08;
	private static final int LP_COMBINATION_00_12 = 0x06;
	private static final int LP_COMBINATION_00_13 = 0x0A;
	private static final int LP_COMBINATION_00_23 = 0x0C;
	private static final int LP_COMBINATION_00_123 = 0x0E;
	private static final int LP_STORE_LEGION_WAREHOUSE = 0x0F;
	private byte rank;

	private LegionRank(int rank)
	{
		this.rank = (byte) rank;
	}

	public byte getRankId()
	{
		return this.rank;
	}
	   public boolean canUseGateGuardianStone(final int legionarPermission2, final int centurionPermission2, final int memberPermission2, final int newbiePermission2)
	   {
	      switch(this)
	      {
	         case SUB_GENERAL:
	            if(legionarPermission2 == LP_GATE_GUARDIAN_STONE || legionarPermission2 == (LP_COMBINATION_00_13)
	               || legionarPermission2 == (LP_COMBINATION_00_23)
	               || legionarPermission2 == (LP_COMBINATION_00_123))
	               return true;
	            break;
	         case CENTURION:
	            if(centurionPermission2 == LP_GATE_GUARDIAN_STONE || centurionPermission2 == (LP_COMBINATION_00_13)
	               || centurionPermission2 == (LP_COMBINATION_00_23)
	               || centurionPermission2 == (LP_COMBINATION_00_123))
	               return true;
	            break;
	         case LEGIONARY:
	            if(memberPermission2 == LP_GATE_GUARDIAN_STONE)
	               return true;
	            break;
	         case NEW_LEGIONARY:
	            if(newbiePermission2 == LP_GATE_GUARDIAN_STONE)
	               return true;
	            break;
	      }
	      return false;
	   }
	   public boolean canUseArtifact(final int legionarPermission2, final int centurionPermission2)
	   {
	      switch(this)
	      {
	         case SUB_GENERAL:
	         {
	            if(legionarPermission2 == LP_ARTIFACT || legionarPermission2 == (LP_COMBINATION_00_12)
	               || legionarPermission2 == (LP_COMBINATION_00_23)
	               || legionarPermission2 == (LP_COMBINATION_00_123))
	               return true;
	            break;
	         }
	         case CENTURION:
	         {
	            if(centurionPermission2 == LP_ARTIFACT || centurionPermission2 == (LP_COMBINATION_00_12)
	               || centurionPermission2 == (LP_COMBINATION_00_23)
	               || centurionPermission2 == (LP_COMBINATION_00_123))
	               return true;
	            break;
	         }
	      }
	      return false;
	   }
	   public boolean canEditAnnouncement(final int legionarPermission2, final int centurionPermission2)
	   {
	      switch(this)
	      {
	         case SUB_GENERAL:
	         {
	            if(legionarPermission2 == LP_EDIT_ANNOUNCEMENT || legionarPermission2 == (LP_COMBINATION_00_13)
	               || legionarPermission2 == (LP_COMBINATION_00_23)
	               || legionarPermission2 == (LP_COMBINATION_00_123))
	               return true;
	            break;
	         }
	         case CENTURION:
	         {
	            if(centurionPermission2 == LP_EDIT_ANNOUNCEMENT || centurionPermission2 == (LP_COMBINATION_00_13)
	               || centurionPermission2 == (LP_COMBINATION_00_23)
	               || centurionPermission2 == (LP_COMBINATION_00_123))
	               return true;
	            break;
	         }
	      }
	      return false;
	   }
	   public boolean canUseLegionWarehouse(final int legionarPermission1, final int centurionPermission1, final int memberPermission1)
	   {
	      switch(this)
	      {
	        case SUB_GENERAL:
	        {
	            if(legionarPermission1 == LP_LEGION_WAREHOUSE || legionarPermission1 == (LP_COMBINATION_60_13)
	               || legionarPermission1 == (LP_COMBINATION_60_13)
	               || legionarPermission1 == (LP_COMBINATION_60_123))
	               return true;
	            break;
	        }
	        case CENTURION:
	        {
	            if(centurionPermission1 == LP_LEGION_WAREHOUSE || centurionPermission1 == (LP_COMBINATION_60_13)
	               || centurionPermission1 == (LP_COMBINATION_60_13)
	               || centurionPermission1 == (LP_COMBINATION_60_123))
	               return true;
	            break;
	        }
	         case LEGIONARY:
	        {
	            if(memberPermission1 == LP_LEGION_WAREHOUSE)
	               return true;
	            break;
	        }
	      }
	      return false;
	   }
	   public boolean canStoreLegionWarehouse(final int legionarPermission2, final int centurionPermission2, final int memberPermission2, final int newbiePermission2)
	   {
	      switch(this)
	      {
	        case SUB_GENERAL:
	        {
	            if(legionarPermission2 == LP_STORE_LEGION_WAREHOUSE || legionarPermission2 == (LP_COMBINATION_60_13)
	              || legionarPermission2 == (LP_COMBINATION_60_13)
	              || legionarPermission2 == (LP_COMBINATION_60_123))
	              return true;
	            break;
	        }
	        case CENTURION:
	        {
	            if(centurionPermission2 == LP_STORE_LEGION_WAREHOUSE || centurionPermission2 == (LP_COMBINATION_60_13)
	              || centurionPermission2 == (LP_COMBINATION_60_13)
	              || centurionPermission2 == (LP_COMBINATION_60_123))
	              return true;
	            break;
	        }
	        case LEGIONARY:
	        {
	            if(memberPermission2 == LP_STORE_LEGION_WAREHOUSE)
	              return true;
	            break;
	        }
	        case NEW_LEGIONARY:
	        {
	            if(newbiePermission2 == LP_LEGION_WAREHOUSE)
	              return true;
	            break;
	        }
	    }
	      return false;
	   }	   
	   public boolean canKickFromLegion(final int legionarPermission1, final int centurionPermission1)
	    {
	      switch(this)
	      {
	        case SUB_GENERAL:
	        {
	            if(legionarPermission1 == LP_KICK_FROM_LEGION || legionarPermission1 == (LP_COMBINATION_60_12)
	               || legionarPermission1 == (LP_COMBINATION_60_23)
	               || legionarPermission1 == (LP_COMBINATION_60_123))
	               return true;
	            break;
	        }
	         case CENTURION:
	        {
	            if(centurionPermission1 == LP_KICK_FROM_LEGION || centurionPermission1 == (LP_COMBINATION_60_12)
	               || centurionPermission1 == (LP_COMBINATION_60_23)
	               || centurionPermission1 == (LP_COMBINATION_60_123))
	               return true;
	            break;
	        }
	    }
	      return false;
	    }
	   public boolean canInviteToLegion(int legionarPermission1, int centurionPermission1)
	    {
	      switch(this)
		{
	        case SUB_GENERAL:
	        {
	            if(legionarPermission1 == LP_INVITE_TO_LEGION || legionarPermission1 == (LP_COMBINATION_60_13)
	               || legionarPermission1 == (LP_COMBINATION_60_23)
	               || legionarPermission1 == (LP_COMBINATION_60_123))
	               return true;
	            break;
	        }
	         case CENTURION:
	        {
	            if(centurionPermission1 == LP_INVITE_TO_LEGION || centurionPermission1 == (LP_COMBINATION_60_13)
	               || centurionPermission1 == (LP_COMBINATION_60_23)
	               || centurionPermission1 == (LP_COMBINATION_60_123))
	               return true;
	            break;
			}
		}
		return false;
	}
}
