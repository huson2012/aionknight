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

package gameserver.itemengine.actions;

import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.skill.SkillEngine;
import gameserver.skill.model.Skill;
import gameserver.utils.PacketSendUtility;
import gameserver.world.WorldMapType;
import org.apache.log4j.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="ArenaSkillUseAction")
public class ArenaSkillUseAction extends AbstractItemAction
{

  @XmlAttribute
  protected int skillid;

  @XmlAttribute
  protected int level;

  public int getSkillid()
  {
    return skillid;
  }

  public int getLevel()
  {
    return level;
  }

  public boolean canAct(Player player, Item parentItem, Item targetItem)
  {
    Skill skill = SkillEngine.getInstance().getSkill(player, skillid, level, player.getTarget(), parentItem.getItemTemplate());
    if (skill == null)
      return false;
    if (player.getWorldId() != WorldMapType.EMPYREAN_CRUCIBLE.getId())
      return false;
    return skill.canUseSkill();
  }

  public void act(Player player, Item parentItem, Item targetItem)
  {
    Skill skill = SkillEngine.getInstance().getSkill(player, skillid, level, player.getTarget(), parentItem.getItemTemplate());
    if (skill != null)
    {
      PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.USE_ITEM(new DescriptionId(parentItem.getItemTemplate().getNameId())));
      skill.setItemObjectId(parentItem.getObjectId());
      skill.useSkill();
    }
    else
    {
		Logger.getLogger(SkillUseAction.class).error("Skill id is null for SkillUseAction !");
    }
  }
}