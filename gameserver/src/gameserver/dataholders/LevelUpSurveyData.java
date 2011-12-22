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