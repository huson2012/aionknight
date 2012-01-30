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

package gameserver.utils;

import gameserver.model.gameobjects.VisibleObject;

public class PositionUtil
{
	private static final float MAX_ANGLE_DIFF = 90f;
	public static boolean isBehindTarget(VisibleObject object1, VisibleObject object2)
	{
		float angleObject1 = MathUtil.calculateAngleFrom(object1, object2);
		float angleObject2 = MathUtil.convertHeadingToDegree(object2.getHeading());
		float angleDiff = angleObject1 - angleObject2;
		
		if (angleDiff <= -360 + MAX_ANGLE_DIFF) angleDiff += 360;
        if (angleDiff >= 360 - MAX_ANGLE_DIFF) angleDiff -= 360;
        return Math.abs(angleDiff) <= MAX_ANGLE_DIFF;
	}
	public static boolean isInFrontOfTarget(VisibleObject object1, VisibleObject object2)
	{
		float angleObject2 = MathUtil.calculateAngleFrom(object2, object1);
		float angleObject1 = MathUtil.convertHeadingToDegree(object2.getHeading());
		float angleDiff = angleObject1 - angleObject2;
		
		if (angleDiff <= -360 + MAX_ANGLE_DIFF) angleDiff += 360;
		if (angleDiff >= 360 - MAX_ANGLE_DIFF) angleDiff -= 360;
		return Math.abs(angleDiff) <= MAX_ANGLE_DIFF;
	}
}
