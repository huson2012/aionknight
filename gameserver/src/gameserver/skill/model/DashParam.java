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

package gameserver.skill.model;

public class DashParam
{
	private float x;
    private float y;
    private float z;
    private int heading;
    private DashType dashType = DashType.NONE;
    
    public DashParam(DashType dashType, float x, float y, float z, int heading)
    {
    	this.dashType = dashType;
    	this.x = x;
    	this.y = y;
    	this.z = z;
    	this.heading = heading;
    }
    
    public int getType()
    {
    	return dashType.getTypeId();
    }
    
    public float getX()
    {
    	return this.x;
    }
    public float getY()
    {
    	return this.y;
    }
    public float getZ()
    {
    	return this.z;
    }
    public int getHeading()
    {
    	return this.heading;
    }
    
	public enum DashType
	{
		/**
		 * Dash Type
		 * 
		 * 1 : teleport to back (1463) 2 : dash (816) 4 : assault (803)
		 */
		NONE(0),
		RANDOMMOVELOC(1),
		DASH(2),
		MOVEBEHIND(4);

		private int	id;

		private DashType(int id)
		{
			this.id = id;
		}

		public int getTypeId()
		{
			return id;
		}

		public static DashType getDashTypeById(int id)
		{
			for(DashType dashType : values())
			{
				if(dashType.id == id)
					return dashType;
			}
			return NONE;
		}
	}
}