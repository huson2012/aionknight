/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package ru.aionknight.gameserver.model.gameobjects.stats.listeners;


import ru.aionknight.gameserver.dataholders.DataManager;
import ru.aionknight.gameserver.model.gameobjects.stats.CreatureGameStats;
import ru.aionknight.gameserver.model.gameobjects.stats.StatEffectType;
import ru.aionknight.gameserver.model.gameobjects.stats.id.StatEffectId;
import ru.aionknight.gameserver.model.templates.TitleTemplate;

/**
 * @author blakawk
 * 
 */
public class TitleChangeListener
{
	public static void onTitleChange(CreatureGameStats<?> cgs, int titleId, boolean isSet)
	{
		TitleTemplate tt = DataManager.TITLE_DATA.getTitleTemplate(titleId);
		if(tt == null)
		{
			return;
		}
		StatEffectId eid = StatEffectId.getInstance(tt.getTitleId(), StatEffectType.TITLE_EFFECT);
		if(!isSet)
		{
			cgs.endEffect(eid);
		}
		else
		{
			cgs.addModifiers(eid, tt.getModifiers());
		}
	}
}
