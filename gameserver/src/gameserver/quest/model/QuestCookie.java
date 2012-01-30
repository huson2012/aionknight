/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.quest.model;

import gameserver.model.gameobjects.Gatherable;
import gameserver.model.gameobjects.Npc;
import gameserver.model.gameobjects.StaticObject;
import gameserver.model.gameobjects.VisibleObject;
import gameserver.model.gameobjects.player.Player;

public class QuestCookie
{
	private VisibleObject visibleObject;
	private Player player;
	private Integer questId;
	private Integer dialogId;
	private int questVars;
	private int workVar;
	private int targetId;
	
	/**
	 * @param creature
	 * @param player
	 * @param questId
	 */
	public QuestCookie(VisibleObject visibleObject, Player player, int questId, int dialogId)
	{
		super();
		this.visibleObject = visibleObject;
		this.player = player;
		this.questId = questId;
		this.dialogId = dialogId;
		
		if (player.getQuestCookie() == null)
			this.player.setQuestCookie(this);
		
		if (visibleObject == null)
		{
			this.targetId = 0;
		}
		else if (visibleObject instanceof Npc)
		{
			this.targetId = ((Npc)visibleObject).getNpcId();
		}
		else if (visibleObject instanceof Gatherable)
		{
			this.targetId = ((Gatherable)visibleObject).getObjectTemplate().getTemplateId();
		}
		else if (visibleObject instanceof StaticObject)
		{
			this.targetId = ((StaticObject)visibleObject).getObjectTemplate().getTemplateId();
		}
		
		if (player.getQuestCookie().questId == this.questId)
		{
			this.questVars = player.getQuestCookie().questVars;
			this.workVar = player.getQuestCookie().workVar;
		}
		else if (this.questId == 0)
		{
			this.questVars = 0;
			this.workVar = 0;
		}
		
		this.player.setQuestCookie(this);
	}

	/**
	 * @return the visibleObject
	 */
	public VisibleObject getVisibleObject()
	{
		return visibleObject;
	}

	/**
	 * @param visibleObject the visibleObject to set
	 */
	public void setVisibleObject(VisibleObject visibleObject)
	{
		this.visibleObject = visibleObject;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer()
	{
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player)
	{
		this.player = player;
	}

	/**
	 * @return the questId
	 */
	public Integer getQuestId()
	{
		return questId;
	}

	/**
	 * @param questId the questId to set
	 */
	public void setQuestId(Integer questId)
	{
		this.questId = questId;
	}

	/**
	 * @return the dialogId
	 */
	public Integer getDialogId()
	{
		return dialogId;
	}

	/**
	 * @param dialogId the dialogId to set
	 */
	public void setDialogId(Integer dialogId)
	{
		this.dialogId = dialogId;
	}
	
	/**
	 * @return the questVars
	 */
	public int getQuestVars()
	{
		return questVars;
	}

	/**
	 * @param questVars the questVars to set
	 */
	public void setQuestVars(int questVars)
	{
		this.questVars = questVars;
	}
	
	/**
	 * @return the workVar which is the number of active var
	 */
	public int getQuestVarNum()
	{
		return workVar;
	}

	/**
	 * @param questVarNum the workVar to set
	 */
	public void setQuestWorkVar(int questVarNum)
	{
		this.workVar = questVarNum;
	}
	
	/**
	 * @return the targetId
	 */
	public int getTargetId()
	{
		return targetId;
	}

	/**
	 * @param targetId the targetId to set
	 */
	public void setTargetId(int targetId)
	{
		this.targetId = targetId;
	}
}