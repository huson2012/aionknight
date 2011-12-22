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

package gameserver.dataholders;

import gameserver.model.Race;
import gameserver.model.gameobjects.Survey;
import gameserver.model.gameobjects.player.Player;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "surveys" })
@XmlRootElement(name = "levelup_surveys")

public class LevelUpSurveyData
{
	@XmlElement(name = "survey")
    protected List<Survey> surveys;
    
    public List<Survey> getSurveys() 
    {
        if (surveys == null)
            surveys = new ArrayList<Survey>();
        return this.surveys;
    }
    
    public Survey getSurvey(Player player)
    {
    	for (Survey survey : getSurveys())
    	{
    		if (player.getLevel() == survey.getLevel() &&
    			(player.getCommonData().getRace() == survey.getRace() || survey.getRace() == Race.PC_ALL))
    			return survey;
    	}
    	return null;
    }
    
    public int size()
    {
    	return getSurveys().size();
    }
}