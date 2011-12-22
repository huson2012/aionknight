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

package gameserver.quest.handlers.models.xmlQuest.operations;

import gameserver.quest.model.QuestCookie;
import gameserver.services.QuestService;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollectItemQuestOperation", propOrder = { "_true", "_false" })
public class CollectItemQuestOperation extends QuestOperation
{
	@XmlElement(name = "true", required = true)
	protected QuestOperations	_true;
	@XmlElement(name = "false", required = true)
	protected QuestOperations	_false;
	@XmlAttribute
	protected Boolean			removeItems;

	/** (non-Javadoc)
	 * @see gameserver.quest.handlers.models.xmlQuest.operations.QuestOperation#doOperate(gameserver.quest.model.QuestEnv)
	 */
	@Override
	public void doOperate(QuestCookie env)
	{
		if(QuestService.collectItemCheck(env, removeItems == null ? true : false))
			_true.operate(env);
		else
			_false.operate(env);
	}	
}