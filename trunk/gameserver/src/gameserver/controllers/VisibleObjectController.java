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
