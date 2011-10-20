package ru.aionknight.gameserver.dao;

import org.openaion.commons.database.dao.DAO;

import ru.aionknight.gameserver.model.gameobjects.player.EmotionList;
import ru.aionknight.gameserver.model.gameobjects.player.Player;



/**
 * @author ginho1
 * 
 */
public abstract class PlayerEmotionListDAO implements DAO
{
	@Override
	public final String getClassName()
	{
		 return PlayerEmotionListDAO.class.getName();
	}

	public abstract EmotionList loadEmotionList(int playerId);

	public abstract boolean storeEmotions(Player player);

	public abstract void removeEmotion(int playerId, int emotionId);
}