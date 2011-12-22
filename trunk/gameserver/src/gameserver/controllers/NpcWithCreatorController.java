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

import gameserver.ai.events.Event;
import gameserver.model.EmotionType;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.NpcWithCreator;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_EMOTION;
import gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import gameserver.utils.PacketSendUtility;

public class NpcWithCreatorController extends NpcController
{

	@Override
    public void onDie(Creature lastAttacker)
    {
		super.onCreatureDie(lastAttacker);
		
		PacketSendUtility.broadcastPacket(this.getOwner(),
			new SM_EMOTION(this.getOwner(), EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()));
		
		this.getOwner().getAi().handleEvent(Event.DIED);

		// deselect target at the end
		this.getOwner().setTarget(null);
		PacketSendUtility.broadcastPacket(this.getOwner(), new SM_LOOKATOBJECT(this.getOwner()));
		
		onDelete();
    }

	@Override
	public void onDialogRequest(Player player)
	{
		return;
	}

	@Override
	public NpcWithCreator getOwner()
	{
		return  (NpcWithCreator)super.getOwner();
	}
	@Override
	public void onDelete()
	{
		getOwner().setCreator(null);
		super.onDelete();
	}
}
