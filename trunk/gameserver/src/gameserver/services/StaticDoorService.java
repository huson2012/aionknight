package gameserver.services;

import gameserver.dataholders.DataManager;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.staticdoor.StaticDoorTemplate;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.utils.PacketSendUtility;

 
public class StaticDoorService
{

	public static StaticDoorService getInstance()
	{
		return SingletonHolder.instance;
	}

	public void openStaticDoor(Player player, int doorId) {
		if(player.getAccessLevel() >= 1) {
			PacketSendUtility.sendMessage(player, "[Admin Info]Door ID: " + doorId);
		}
		StaticDoorTemplate template = DataManager.STATICDOOR_DATA.getStaticDoorTemplate(doorId);
		int keyId = 1;
		int mapId = 0;		

		if(template != null) {
			keyId = template.getKeyId();
			mapId = template.getMapId();
		}
		
		if(player.getAccessLevel() >= 1) {
			PacketSendUtility.sendMessage(player, "[Admin Info]Key ID: " + keyId);
		}
		if(checkStaticDoorKey(player, doorId, keyId, mapId))
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_EMOTION(doorId));
		else
			PacketSendUtility.sendMessage(player,"For opening this door you need [item:" + keyId + "]");
	}

	public boolean checkStaticDoorKey(Player player, int doorId, int keyId, int mapId)
  {
    if (keyId == 0)
      return false;
    if (keyId == 1) {
      return true;
    }

    if (player.getWorldId() != mapId) {
      return true;
    }

    if (hasItem(player, keyId)) {
      player.getInventory().removeFromBagByItemId(keyId, 1);
      return true;
    }

    return false;
  }

  private boolean hasItem(Player player, int itemId) {
    return player.getInventory().getItemCountByItemId(itemId) > 0;
  }

  private static class SingletonHolder
  {
    protected static final StaticDoorService instance = new StaticDoorService();
  }
}