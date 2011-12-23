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

package mysql5;

import commons.database.DatabaseFactory;
import gameserver.dao.PlayerEmotionListDAO;
import gameserver.model.gameobjects.player.Emotion;
import gameserver.model.gameobjects.player.EmotionList;
import gameserver.model.gameobjects.player.Player;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class MySQL5PlayerEmotionListDAO extends PlayerEmotionListDAO
{
	private static final String LOAD_QUERY = "SELECT `emotion_id`, `emotion_expires_time`, `emotion_date` FROM `player_emotions` WHERE `player_id` = ?";
	private static final String INSERT_QUERY = "INSERT INTO `player_emotions`(`player_id`,`emotion_id`, `emotion_expires_time`, `emotion_date`) VALUES (?,?,?,?)";
	private static final String CHECK_QUERY = "SELECT `emotion_id` FROM `player_emotions` WHERE `player_id`= ? AND `emotion_id`= ?";
	private static final String DELETE_QUERY = "DELETE FROM `player_emotions` WHERE `player_id` = ? AND `emotion_id` = ?";

	private static final Logger log = Logger.getLogger(MySQL5PlayerEmotionListDAO.class);
	
	@Override
	public EmotionList loadEmotionList(int playerId)
	{
		EmotionList emotionList = new EmotionList();
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(LOAD_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int id = rset.getInt("emotion_id");
				long emotion_date = rset.getTimestamp("emotion_date").getTime();
				long emotion_expires_time = rset.getLong("emotion_expires_time");

				emotionList.add(id, emotion_date, emotion_expires_time);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		
		return emotionList;
	}

	@Override
	public boolean storeEmotions(Player player)
	{
		int playerId = player.getObjectId();

		if(player.getEmotionList() != null)
		{
			for(Emotion emotion : player.getEmotionList().getEmotions())
			{
				Connection con = null;
				try
				{
					con = DatabaseFactory.getConnection();
					PreparedStatement stmt = con.prepareStatement(CHECK_QUERY);
					stmt.setInt(1, playerId);
					stmt.setInt(2, emotion.getEmotionId());
					ResultSet rset = stmt.executeQuery();
					if (!rset.next())
					{
						Connection con2 = null;
						try
						{
							con2 = DatabaseFactory.getConnection();
							PreparedStatement stmt2 = con2.prepareStatement(INSERT_QUERY);
							stmt2.setInt(1, playerId);
							stmt2.setInt(2, emotion.getEmotionId());
							stmt2.setLong(3, emotion.getEmotionExpiresTime());
							stmt2.setTimestamp(4, new Timestamp(emotion.getEmotionDate()));

							stmt2.execute();
						}
						catch(Exception e)
						{
							log.error(e);
						}
						finally
						{
							DatabaseFactory.close(con2);
						}
					}
				}
				catch(Exception e)
				{
					log.error(e);
				}
				finally
				{
					DatabaseFactory.close(con);
				}

			}
		}
		return true;
	}

	@Override
	public void removeEmotion(int playerId, int emotionId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt2 = con.prepareStatement(DELETE_QUERY);
			stmt2.setInt(1, playerId);
			stmt2.setInt(2, emotionId);
			stmt2.execute();
		}
		catch(Exception e)
		{
			log.error(e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}