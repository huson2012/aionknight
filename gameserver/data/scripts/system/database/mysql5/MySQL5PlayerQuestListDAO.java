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

package mysql5;

import commons.database.DatabaseFactory;
import gameserver.dao.PlayerQuestListDAO;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.QuestStateList;
import gameserver.quest.model.QuestState;
import gameserver.quest.model.QuestStatus;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class MySQL5PlayerQuestListDAO extends PlayerQuestListDAO
{
	private static final Logger log = Logger.getLogger(MySQL5PlayerQuestListDAO.class);

	public static final String	SELECT_QUERY	= "SELECT `quest_id`, `status`, `quest_vars`, `complete_count`, `complete_time` FROM `player_quests` WHERE `player_id`=?";
	public static final String	UPDATE_QUERY	= "UPDATE `player_quests` SET `status`=?, `quest_vars`=?, `complete_count`=? where `player_id`=? AND `quest_id`=?";
	public static final String	UPDATE_QUERY2	= "UPDATE `player_quests` SET `status`=?, `quest_vars`=?, `complete_count`=?, `complete_time`=? where `player_id`=? AND `quest_id`=?";
	public static final String	DELETE_QUERY	= "DELETE FROM `player_quests` WHERE `player_id`=? AND `quest_id`=?";
	public static final String	INSERT_QUERY	= "INSERT INTO `player_quests` (`player_id`, `quest_id`, `status`, `quest_vars`, `complete_count`) VALUES (?,?,?,?,?)";
	public static final String	INSERT_QUERY2	= "INSERT INTO `player_quests` (`player_id`, `quest_id`, `status`, `quest_vars`, `complete_count`, `complete_time`) VALUES (?,?,?,?,?,?)";

	/**
	 * (non-Javadoc)
	 * @see gameserver.dao.PlayerQuestListDAO#load(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public QuestStateList load(final Player player)
	{
		QuestStateList questStateList = new QuestStateList();

		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int questId = rset.getInt("quest_id");
				int questVars = rset.getInt("quest_vars");
				int completeCount = rset.getInt("complete_count");
				QuestStatus status = QuestStatus.valueOf(rset.getString("status"));
				QuestState questState = new QuestState(questId, status, questVars, completeCount);
				try 
				{
					questState.setCompleteTime(rset.getTimestamp("complete_time"));
				}
				catch (Exception e)
				{
				}
				questState.setPersistentState(PersistentState.UPDATED);
				questStateList.addQuest(questId, questState);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore QuestStateList data for player: " + player.getObjectId() + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return questStateList;
	}

	/**
	 * (non-Javadoc)
	 * @see gameserver.dao.PlayerQuestListDAO#store(gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void store(final Player player)
	{
		for(QuestState qs : player.getQuestStateList().getAllQuestState())
		{
			switch(qs.getPersistentState())
			{
				case NEW:
					addQuest(player.getObjectId(), qs);
					break;
				case UPDATE_REQUIRED:
					updateQuest(player.getObjectId(), qs);
					break;
				case DELETED:
					deleteQuest(player.getObjectId(), qs.getQuestId());
					break;
			}
			qs.setPersistentState(PersistentState.UPDATED);
		}
	}

	/**
	 * @param playerId
	 * @param QuestState
	 */
	private void addQuest(final int playerId, final QuestState qs)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			Timestamp ts = qs.getCompleteTime();
			PreparedStatement stmt = null;
			if (ts == null)
				stmt = con.prepareStatement(INSERT_QUERY);
			else
				stmt = con.prepareStatement(INSERT_QUERY2);
			stmt.setInt(1, playerId);
			stmt.setInt(2, qs.getQuestId());
			stmt.setString(3, qs.getStatus().toString());
			stmt.setInt(4, qs.getQuestVars().getQuestVars());
			stmt.setInt(5, qs.getCompleteCount());
			if (ts != null) 
				stmt.setTimestamp(6, ts);
			stmt.execute();
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

	/**
	 * @param playerId
	 * @param qs
	 */
	private void updateQuest(final int playerId, final QuestState qs)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			Timestamp ts = qs.getCompleteTime();
			PreparedStatement stmt = null;
			if (ts == null)
				stmt = con.prepareStatement(UPDATE_QUERY);
			else
				stmt = con.prepareStatement(UPDATE_QUERY2);
			stmt.setString(1, qs.getStatus().toString());
			stmt.setInt(2, qs.getQuestVars().getQuestVars());
			stmt.setInt(3, qs.getCompleteCount());
			if (ts != null)
			{
				stmt.setTimestamp(4, ts);
				stmt.setInt(5, playerId);
				stmt.setInt(6, qs.getQuestId());
			}
			else
			{
				stmt.setInt(4, playerId);
				stmt.setInt(5, qs.getQuestId());				
			}

			stmt.execute();
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

	/**
	 * @param playerId The playerObjectId of the player who's quest needs to be deleted
	 * @param questId The questId that needs to be deleted
	 */
	public void deleteQuest(final int playerId, final int questId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, questId);
			stmt.execute();
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}