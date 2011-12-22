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

package gameserver.quest.model;

import gameserver.model.gameobjects.PersistentState;
import java.sql.Timestamp;
import java.util.Calendar;

public class QuestState
{
       private final int questId;
       private QuestVars questVars;
       private QuestStatus status;
       private int completeCount;
       private PersistentState persistentState;
       private Timestamp completeTime;

       public QuestState(int questId)
       {
           this.questId = questId;
           status = QuestStatus.START;
           questVars = new QuestVars();
           completeCount = 0;
           persistentState = PersistentState.NEW;
       }

       public QuestState(int questId, QuestStatus status, int questVars, int completeCount)
       {
           this.questId = questId;
           this.status = status;
           this.questVars = new QuestVars(questVars);
           this.completeCount = completeCount;
           this.persistentState = PersistentState.NEW;
       }

       public QuestVars getQuestVars()
       {
    	   return questVars;
       }

       /**
        * @param id
        * @param var
        */
       public void setQuestVarById(int id, int var)
       {
    	   questVars.setVarById(id, var);
    	   setPersistentState(PersistentState.UPDATE_REQUIRED);
       }

       /**
        * @param id
        * @return Quest var by id.
        */
       public int getQuestVarById(int id)
       {
    	   return questVars.getVarById(id);
       }
       
       public void setQuestVar(int var)
       {
    	   questVars.setVar(var);
    	   setPersistentState(PersistentState.UPDATE_REQUIRED);
       }

       public QuestStatus getStatus()
       {
    	   return status;
       }

       public void setStatus(QuestStatus status)
       {
    	   if (status == QuestStatus.COMPLETE && this.status != QuestStatus.COMPLETE)
    		   updateCompleteTime();
    	   else if (status != QuestStatus.COMPLETE)
    		   completeTime = null;
    	   this.status = status;
    	   setPersistentState(PersistentState.UPDATE_REQUIRED);
       }
       
       public Timestamp getCompleteTime()
       {
    	   return completeTime;
       }
       
       public void setCompleteTime(Timestamp time)
       {
    	   completeTime = time;
       }
       
       public void updateCompleteTime()
       {
    	   completeTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
       }

       public int getQuestId()
       {
    	   return questId;
       }

       public void setCompliteCount(int completeCount)
       {
    	   this.completeCount = completeCount;
    	   setPersistentState(PersistentState.UPDATE_REQUIRED);
       }
       
       public boolean canRepeat(int maxCompleteCount)
       {
    	   if (this.status == QuestStatus.COMPLETE)
    	   {
        	   if (questId >= 80000 || maxCompleteCount == 255)
        		   return true;
    		   return completeCount < maxCompleteCount;  
    	   }
    	   return false;
       }

       public int getCompleteCount()
       {
    	   return completeCount;
       }
       
   	/**
   	 * @return the pState
   	 */
   	public PersistentState getPersistentState()
   	{
   		return persistentState;
   	}

   	/**
   	 * @param persistentState the pState to set
   	 */
   	public void setPersistentState(PersistentState persistentState)
   	{
		switch(persistentState)
		{
			case DELETED:
				if(this.persistentState == PersistentState.NEW)
					throw new IllegalArgumentException("Cannot change state to DELETED from NEW");
			case UPDATE_REQUIRED:
				if(this.persistentState == PersistentState.NEW)
			break;
			default:
				this.persistentState = persistentState;
		}
   	}
}