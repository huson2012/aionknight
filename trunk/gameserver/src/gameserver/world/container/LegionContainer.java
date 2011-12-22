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

package gameserver.world.container;

import gameserver.model.legion.Legion;
import gameserver.world.exceptions.DuplicateAionObjectException;
import javolution.util.FastMap;
import java.util.Iterator;
import java.util.Map;

public class LegionContainer implements Iterable<Legion>
{
	private final Map<Integer, Legion> legionsById = new FastMap<Integer, Legion>().shared();
	private final Map<String, Legion> legionsByName	= new FastMap<String, Legion>().shared();
	public void add(Legion legion)
	{
		if(legion == null || legion.getLegionName() == null)
			return;
		if(legionsById.put(legion.getLegionId(), legion) != null)
			throw new DuplicateAionObjectException();
		if(legionsByName.put(legion.getLegionName().toLowerCase(), legion) != null)
			throw new DuplicateAionObjectException();
	}

	public void remove(Legion legion)
	{
		legionsById.remove(legion.getLegionId());
		legionsByName.remove(legion.getLegionName().toLowerCase());
	}

	public Legion get(int legionId)
	{
		return legionsById.get(legionId);
	}

	public Legion get(String name)
	{
		return legionsByName.get(name.toLowerCase());
	}

	public boolean contains(int legionId)
	{
		return legionsById.containsKey(legionId);
	}

	public boolean contains(String name)
	{
		return legionsByName.containsKey(name.toLowerCase());
	}

	@Override
	public Iterator<Legion> iterator()
	{
		return legionsById.values().iterator();
	}
}