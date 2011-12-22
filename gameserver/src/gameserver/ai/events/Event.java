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

package gameserver.ai.events;

public enum Event
{
	/**
	 * This event is received on each enemy attack
	 */
	ATTACKED,
	/**
	 * Target is too far or long time passed since last attak
	 */
	TIRED_ATTACKING_TARGET,
	/**
	 * During attack most hated creature changed from current target
	 */
	MOST_HATED_CHANGED,
	/**
	 * In active state there is nothing to do
	 */
	NOTHING_TODO,
	/**
	 * Npc is far from spawn point
	 */
	FAR_FROM_HOME,
	/**
	 * Npc returned to spawn point
	 */
	BACK_HOME,
	/**
	 * Npc restored health fully (after returning to home)
	 */
	RESTORED_HEALTH,
	/**
	 * Npc sees another player
	 */
	SEE_PLAYER,
	/**
	 * Player removed from knownlist
	 */
	NOT_SEE_PLAYER,
	/**
	 * Any creature is in the visible radius
	 */
	SEE_CREATURE,
	/**
	 * Creature removed from knownlist
	 */
	NOT_SEE_CREATURE,
	/**
	 * Talk request
	 */
	TALK,
	/**
	 * Npc is respawned
	 */
	RESPAWNED,
	/**
	 * Creature died
	 */
	DIED,
	/**
	 * Despawn service was called
	 */
	DESPAWN
}
