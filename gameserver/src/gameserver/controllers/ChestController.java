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

package gameserver.controllers;

import gameserver.controllers.movement.ActionObserver;
import gameserver.controllers.movement.ActionObserver.ObserverType;
import gameserver.controllers.movement.StartMovingListener;
import gameserver.dataholders.DataManager;
import gameserver.model.ChatType;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.chest.ChestTemplate;
import gameserver.model.templates.chest.KeyItem;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_MESSAGE;
import gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import gameserver.services.DropService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import java.util.List;
import java.util.concurrent.Future;

public class ChestController extends NpcController
{
	ChestTemplate	chestTemplate;
	private Player lastActor = null;
	private ChestState state = ChestState.IDLE;
	private Future<?> openingTask;
	
	private final int defaultUseTime = 3000;
	
	private enum ChestState
	{
		IDLE,
		OPENING
	}
	
	

	@Override
	public void setOwner(Creature owner)
	{
		super.setOwner(owner);
		chestTemplate = DataManager.CHEST_DATA.getChestTemplate(owner.getObjectTemplate().getTemplateId());
	}

	@Override
	public void onDialogRequest(final Player player)
	{
		if(chestTemplate == null || state == ChestState.OPENING)
			return;

		final List<KeyItem> keyItems = chestTemplate.getKeyItem();
		for(KeyItem keyItem : keyItems)
		{
			if(keyItem.getItemId() != 0 && player.getInventory().getItemCountByItemId(keyItem.getItemId()) < 1)
			{
				PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player,
					"I need a key to open this chest ! ", ChatType.NORMAL), true);
				return;
			}
		}
		
		state = ChestState.OPENING;
		
		player.getObserveController().attach(new StartMovingListener(){
			@Override
			public void moved()
			{
				cancelOpening(player);
			}
		});

		player.getObserveController().attach(new ActionObserver(ObserverType.ATTACKED){
			@Override
			public void attacked(Creature creature)
			{		
				cancelOpening(player);
			}
		});
		
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
			defaultUseTime, 1));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner()
			.getObjectId()), true);
		
		openingTask = ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
					defaultUseTime, 0));
				getOwner().setTarget(player);
				lastActor = player;
				onDie(player);
				// Remove keys
				for(KeyItem keyItem : keyItems)
					if(keyItem.getItemId() != 0)
						player.getInventory().removeFromBagByItemId(keyItem.getItemId(), keyItem.getQuantity());				
			}
		}, defaultUseTime);
		
	}
	
	@Override
	public void doReward()
	{
		if (lastActor == null || getOwner() == null)
			return;
		
		DropService.getInstance().registerDrop(getOwner() , lastActor, lastActor.getLevel());
		DropService.getInstance().requestDropList(lastActor, getOwner().getObjectId());
		lastActor = null;
	}
	
	private void cancelOpening(Player player)
	{
		state = ChestState.IDLE;
		openingTask.cancel(false);
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
			defaultUseTime, 0));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0,
			getOwner().getObjectId()), true);
	}
}