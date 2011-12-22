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

import gameserver.model.gameobjects.VisibleObject;
import gameserver.world.World;

/**
 * This class is for controlling VisibleObjects [players, npc's etc].
 * Its controlling movement, visibility etc.
 */
public abstract class VisibleObjectController<T extends VisibleObject>
{
	
	/**
	 * Object that is controlled by this controller.
	 */
	private T owner;

	/**
	 * Set owner (controller object).
	 * 
	 * @param owner
	 */
	public void setOwner(T owner)
	{
		this.owner = owner;
	}

	/**
	 * Get owner (controller object).
	 */
	public T getOwner()
	{
		return owner;
	}

	/**
	 * Called when controlled object is seeing other VisibleObject.
	 * 
	 * @param object
	 */
	public void see(VisibleObject object)
	{

	}

	/**
	 * Called when controlled object no longer see some other VisibleObject.
	 * 
	 * @param object
	 */
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{

	}
	
	/**
	 * Removes controlled object from the world.
	 */
	public void delete()
	{
		delete(false);
	}

	/**
	 * Removes controlled object from the world.
	 */
	public void delete(boolean instance)
	{
		/**
		 * despawn object from world.
		 */
		if(getOwner().isSpawned())
			World.getInstance().despawn(getOwner(), instance);
		/**
		 * Delete object from World.
		 */

		World.getInstance().removeObject(getOwner());
	}
	
	/**
	 * Called when object is re-spawned
	 */
	public void onRespawn()
	{
		
	}
}
