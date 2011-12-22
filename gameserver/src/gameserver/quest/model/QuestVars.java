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

package gameserver.quest.model;

public class QuestVars
{
	private Integer[]	questVars	= new Integer[6];

	public QuestVars()
	{
	}

	public QuestVars(int var)
	{
		setVar(var);
	}

	/**
	 * @param id
	 * @return Quest var by id.
	 */
	public int getVarById(int id)
	{
		if(id == 5)
			return (questVars[id] & 0x03);
		return questVars[id];
	}

	/**
	 * @param id
	 * @param var
	 */
	public void setVarById(int id, int var)
	{
		if(id == 5)
			questVars[id] = (var & 0x03);
		else
			questVars[id] = (var & 0x3F);
	}

	/**
	 * @return integer
	 */
	public int getQuestVars()
	{
		int var = 0;
		var |= questVars[5];
		for(int i = 5; i >= 0; i--)
		{
			if(i == 5)
				var <<= 0x02;
			else
				var <<= 0x06;
			var |= questVars[i];
		}
		return var;
	}
	
	public void setVar(int var)
	{
		for(int i = 0; i < 6; i++)
		{
			if(i == 5)
				questVars[i] = (var & 0x03);
			else
			{
				questVars[i] = (var & 0x3F);
				var >>= 0x06;
			}
		}
	}
}