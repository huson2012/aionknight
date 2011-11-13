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
package gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.services.TeleportService;
import gameserver.skill.model.Effect;


/**

 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnPointEffect")
public class ReturnPointEffect extends EffectTemplate
{

	@Override
	public void applyEffect(Effect effect)
	{
		ItemTemplate itemTemplate = effect.getItemTemplate();
		int worldId = itemTemplate.getReturnWorldId();
		String pointAlias = itemTemplate.getReturnAlias();
		TeleportService.teleportToPortalExit(((Player) effect.getEffector()), pointAlias, worldId,
			0);
	}

	@Override
	public void calculate(Effect effect)
	{
		ItemTemplate itemTemplate = effect.getItemTemplate();
		if(itemTemplate != null)
			super.calculate(effect);
	}

}
