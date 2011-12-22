/**
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.itemengine.actions;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemActions")
public class ItemActions {

	@XmlElements({
		@XmlElement(name = "skilllearn", type = SkillLearnAction.class),
		@XmlElement(name = "extract", type = ExtractAction.class),
		@XmlElement(name = "skilluse", type = SkillUseAction.class),
		@XmlElement(name = "arenaskilluse", type=ArenaSkillUseAction.class),
		@XmlElement(name = "enchant", type = EnchantItemAction.class),
		@XmlElement(name = "queststart", type = QuestStartAction.class),
		@XmlElement(name = "dye", type = DyeAction.class),
		@XmlElement(name = "craftlearn", type = CraftLearnAction.class),
		@XmlElement(name = "toypetspawn", type = ToyPetSpawnAction.class),
		@XmlElement(name = "split", type = SplitAction.class),
		@XmlElement(name = "read", type = ReadAction.class),
		@XmlElement(name = "ticket", type = TicketAction.class),
		@XmlElement(name = "title", type = TitleAction.class),
		@XmlElement(name = "cmotion", type = CmotionAction.class),
		@XmlElement(name = "emotion", type = EmotionAction.class),
		@XmlElement(name = "cosmetic", type = CosmeticAction.class)
	})
	protected List<AbstractItemAction> itemActions;

	/**
	 * Gets the value of the itemActions property.
	 *
	 * Objects of the following type(s) are allowed in the list
	 * {@link SkillLearnAction }
	 * {@link SkillUseAction }
	 *
	 */
	public List<AbstractItemAction> getItemActions()
	{
		if (itemActions == null)
			itemActions = new ArrayList<AbstractItemAction>();
		return this.itemActions;
	}

	public List<SkillLearnAction> getSkillLearnActions()
	{
		List<SkillLearnAction> result = new ArrayList<SkillLearnAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof SkillLearnAction)
				result.add((SkillLearnAction)action);
		return result;
	}

	public List<ExtractAction> getExtractActions()
	{
		List<ExtractAction> result = new ArrayList<ExtractAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof ExtractAction)
				result.add((ExtractAction)action);
		return result;
	}

	public List<SkillUseAction> getSkillUseActions()
	{
		List<SkillUseAction> result = new ArrayList<SkillUseAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof SkillUseAction)
				result.add((SkillUseAction)action);
		return result;
	}

    public List<ArenaSkillUseAction> getArenaSkillUseActions()
    {
        List<ArenaSkillUseAction> result = new ArrayList<ArenaSkillUseAction>();
        if (itemActions == null)
            return result;

        for(AbstractItemAction action : itemActions)
            if(action instanceof ArenaSkillUseAction)
                result.add((ArenaSkillUseAction)action);
        return result;
    }

	public List<EnchantItemAction> getEnchantActions()
	{
		List<EnchantItemAction> result = new ArrayList<EnchantItemAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof EnchantItemAction)
				result.add((EnchantItemAction)action);
		return result;
	}

	public List<QuestStartAction> getQuestStartActions()
	{
		List<QuestStartAction> result = new ArrayList<QuestStartAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof QuestStartAction)
				result.add((QuestStartAction)action);
		return result;
	}

	public List<DyeAction> getDyeActions()
	{
		List<DyeAction> result = new ArrayList<DyeAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof DyeAction)
				result.add((DyeAction)action);
		return result;
	}

	public List<TicketAction> getTicketAction()
	{
		List<TicketAction> result = new ArrayList<TicketAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof TicketAction)
				result.add((TicketAction)action);
		return result;
	}

	public List<TitleAction> getTitleAction()
	{
		List<TitleAction> result = new ArrayList<TitleAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof TitleAction)
				result.add((TitleAction)action);
		return result;
	}

	public List<CraftLearnAction> getCraftLearnActions()
	{
		List<CraftLearnAction> result = new ArrayList<CraftLearnAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof CraftLearnAction)
				result.add((CraftLearnAction)action);
		return result;
	}

	public List<ToyPetSpawnAction> getToyPetSpawnActions()
	{
		List<ToyPetSpawnAction> result = new ArrayList<ToyPetSpawnAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof ToyPetSpawnAction)
				result.add((ToyPetSpawnAction)action);
		return result;
	}

	public List<SplitAction> getSplitActions()
	{
		List<SplitAction> result = new ArrayList<SplitAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof SplitAction)
				result.add((SplitAction)action);
		return result;
	}

	public List<ReadAction> getReadActions()
	{
		List<ReadAction> result = new ArrayList<ReadAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof ReadAction)
				result.add((ReadAction)action);
		return result;
	}
	
	public List<CosmeticAction> getCosmeticActions()
	{
		List<CosmeticAction> result = new ArrayList<CosmeticAction>();
		if (itemActions == null)
			return result;

		for(AbstractItemAction action : itemActions)
			if(action instanceof CosmeticAction)
				result.add((CosmeticAction)action);
		return result;
	}
}