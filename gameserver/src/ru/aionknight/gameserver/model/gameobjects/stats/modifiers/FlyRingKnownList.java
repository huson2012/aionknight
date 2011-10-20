/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.model.gameobjects.stats.modifiers;


import ru.aionknight.gameserver.model.flyring.FlyRing;
import ru.aionknight.gameserver.model.gameobjects.VisibleObject;
import ru.aionknight.gameserver.utils.MathUtil;
import ru.aionknight.gameserver.world.KnownList;


/**
 * @author blakawk
 *
 */
public class FlyRingKnownList extends KnownList
{
	public FlyRingKnownList(VisibleObject owner)
	{
		super(owner);
	}
	
	@Override
	protected boolean checkObjectInRange(VisibleObject owner, VisibleObject newObject)
	{
		FlyRing fr = (FlyRing) owner;
		if(MathUtil.isIn3dRange(owner, newObject, fr.getTemplate().getRadius()*2))
		{
			return true;
		}
		return false;
	}
}
