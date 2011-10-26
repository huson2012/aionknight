package ru.aionknight.gameserver.itemengine.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import ru.aionknight.gameserver.model.DescriptionId;
import ru.aionknight.gameserver.model.gameobjects.Item;
import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import ru.aionknight.gameserver.skill.SkillEngine;
import ru.aionknight.gameserver.skill.model.Skill;
import ru.aionknight.gameserver.utils.PacketSendUtility;
import ru.aionknight.gameserver.world.WorldMapType;

import org.apache.log4j.Logger;
/**
 * @author andre
 * 
 */
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
      skill.setItemObjectId(parentItem.getObjectId().intValue());
      skill.useSkill();
    }
    else
    {
      Logger.getLogger(SkillUseAction.class).error("Skill id is null for SkillUseAction !");
    }
  }
}
