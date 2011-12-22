/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package gameserver.skill.task;

import gameserver.model.gameobjects.StaticObject;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.model.templates.recipe.RecipeTemplate;
import gameserver.network.aion.serverpackets.SM_CRAFT_ANIMATION;
import gameserver.network.aion.serverpackets.SM_CRAFT_UPDATE;
import gameserver.services.CraftService;
import gameserver.utils.PacketSendUtility;

public class CraftingTask extends AbstractCraftTask
{
	private RecipeTemplate recipeTemplate;
	private ItemTemplate itemTemplate;
	private int skillId;

	/**
	 * @param requestor
	 * @param responder
	 * @param recipeTemplate
	 */

	public CraftingTask(Player requestor, StaticObject responder, RecipeTemplate recipeTemplate, ItemTemplate itemTemplate)
	{
		super(requestor, responder, recipeTemplate.getSkillid() == 40009 ? 99999 :requestor.getSkillList().getSkillLevel(recipeTemplate.getSkillid())-recipeTemplate.getSkillpoint());
		this.recipeTemplate = recipeTemplate;
		this.itemTemplate = itemTemplate;
		this.skillId = recipeTemplate.getSkillid();
	}

	/** (non-Javadoc)
	 * @see gameserver.skill.task.AbstractCraftTask#onFailureFinish()
	 */
	@Override
	protected void onFailureFinish()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, currentSuccessValue, currentFailureValue, 6));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), 0, 3), true);
	}

	/** (non-Javadoc)
	 * @see gameserver.skill.task.AbstractCraftTask#onSuccessFinish()
	 */
	@Override
	protected boolean onSuccessFinish()
	{
		ItemTemplate critItemTemplate = CraftService.finishCrafting(requestor, recipeTemplate, itemTemplate.getTemplateId());
		
		if(critItemTemplate == null)
		{
			PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, currentSuccessValue, currentFailureValue, 5));
			PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), 0, 2), true);
			return true;
		}
		else
		{
			this.itemTemplate = critItemTemplate;
			PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, 0, 0, 3));
			onInteractionStart();
			currentSuccessValue = 0;
			currentFailureValue = 0;
			return false;
		}
	}

	/** (non-Javadoc)
	 * @see gameserver.skill.task.AbstractCraftTask#sendInteractionUpdate()
	 */
	@Override
	protected void sendInteractionUpdate()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, currentSuccessValue, currentFailureValue, speedUp? 2:1));
	}

	/** (non-Javadoc)
	 * @see gameserver.skill.task.AbstractInteractionTask#onInteractionAbort()
	 */
	@Override
	protected void onInteractionAbort()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, 0, 0, 4));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), 0, 2), true);
	}

	/** (non-Javadoc)
	 * @see gameserver.skill.task.AbstractInteractionTask#onInteractionFinish()
	 */
	@Override
	protected void onInteractionFinish()
	{
	}

	/** (non-Javadoc)
	 * @see gameserver.skill.task.AbstractInteractionTask#onInteractionStart()
	 */
	@Override
	protected void onInteractionStart()
	{
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, 100, 100, 0));
		PacketSendUtility.sendPacket(requestor, new SM_CRAFT_UPDATE(skillId, itemTemplate, 0, 0, 1));
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), skillId, 0), true);
		PacketSendUtility.broadcastPacket(requestor, new SM_CRAFT_ANIMATION(requestor.getObjectId(),responder.getObjectId(), skillId, 1), true);
	}
}