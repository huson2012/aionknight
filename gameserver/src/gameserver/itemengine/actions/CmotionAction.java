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

package gameserver.itemengine.actions;

import commons.database.dao.DAOManager;
import gameserver.dao.PlayerCmotionListDAO;
import gameserver.model.DescriptionId;
import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.network.aion.serverpackets.SM_CMOTION;
import gameserver.network.aion.serverpackets.SM_DELETE_ITEM;
import gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.utils.PacketSendUtility;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CmotionAction")
public class CmotionAction extends AbstractItemAction
{
	@XmlAttribute
	protected String cmotionid;
	@XmlAttribute
	protected int expire;
	
	protected String[] arraystr;
	protected String[] cmotionName;
	protected int[] cmid = {0,0,0,0};
	protected int[] cm_dsc = {600533, 600535, 600537, 600539, 600571, 600573, 600575, 600577};
	/**
	 * Gets the value of the id property.
	 */
	public void getCmotionId() 
	{
		
		arraystr = cmotionid.split("\\,");
		for (int i = 0; i < arraystr.length; i++)
		{
			cmid[i] = Integer.valueOf (arraystr[i]).intValue();
		}
		
	}
	
	/**
	 * Gets the value of the id property.
	 */
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{	
		boolean valid = false;
		getCmotionId();
		
		for (int i = 0; i < cmid.length; i++)
		{
			if(player.getCmotionList().canAdd(cmid[i]))
				valid = true;
			else
			    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400907));
		}

		return valid;
	}

	@Override
	public void act(Player player, Item parentItem, Item targetItem)
	{
		Item item = player.getInventory().getItemByObjId(parentItem.getObjectId());
		ItemTemplate itemTemplate = parentItem.getItemTemplate();
		if(item != null)
		{
		
			if(player.getInventory().removeFromBag(item, true))
			{				
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),	parentItem.getObjectId(), itemTemplate.getTemplateId()), true);				
				for (int i = 0; i < cmid.length; i++)
				{
					
					if(player.getCmotionList().add(cmid[i],  false, System.currentTimeMillis(),(expire  * 60L)))
					{
						int dummy = cmid[i] - 1;
						if(expire > 0)
						    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401029, new DescriptionId(cm_dsc[dummy]*2+1)));			    
						else
						    PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400917, new DescriptionId(cm_dsc[dummy]*2+1)));
					}
				}
				
				DAOManager.getDAO(PlayerCmotionListDAO.class).AddCmotions(player);
				for (Cmotion cmotion : player.getCmotionList().getCmotions()) 
	            {	
					int dummy = 0;
	            	if(cmotion.getActive())
	            		cmotion.setActive(false);
	            	else
	            	{
	            		cmotion.setActive(true);
	            		if(cmotion.getCmotionId() > 4)
	            			dummy = cmotion.getCmotionId() - 4;
	            		else
	            			dummy = cmotion.getCmotionId();
	            			
	            		PacketSendUtility.sendPacket(player, new SM_CMOTION(player, dummy, cmotion.getCmotionId()));
	            	}
	            }
	            DAOManager.getDAO(PlayerCmotionListDAO.class).storeCmotions(player);
		        PacketSendUtility.sendPacket(player, new SM_CMOTION(player));
		        PacketSendUtility.sendPacket(player, new SM_DELETE_ITEM(parentItem.getObjectId()));
			}
		}
	}
}