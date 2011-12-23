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
import gameserver.dao.PlayerSkillListDAO;
import gameserver.model.gameobjects.PersistentState;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.player.SkillList;
import gameserver.model.gameobjects.player.SkillListEntry;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MySQL5PlayerSkillListDAO extends PlayerSkillListDAO
{
	private static final Logger log = Logger.getLogger(MySQL5PlayerSkillListDAO.class);

	public static final String INSERT_QUERY = "INSERT INTO `player_skills` (`player_id`, `skillId`, `skillLevel`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE `skillLevel` = ?;";
	public static final String UPDATE_QUERY = "UPDATE `player_skills` set skillLevel=? where player_id=? AND skillId=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_skills` WHERE `player_id`=? AND skillId=?";
	public static final String SELECT_QUERY = "SELECT `skillId`, `skillLevel` FROM `player_skills` WHERE `player_id`=?";


	/** {@inheritDoc} */
	@Override
	public SkillList loadSkillList(final int playerId)
	{
		Map<Integer, SkillListEntry> skills = new HashMap<Integer, SkillListEntry>();
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			ResultSet rset = stmt.executeQuery();
			while(rset.next())
			{
				int id = rset.getInt("skillId");
				int lv = rset.getInt("skillLevel");

				skills.put(id, new SkillListEntry(id, false, lv, PersistentState.UPDATED));
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.fatal("Could not restore SkillList data for player: " + playerId + " from DB: "+e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return new SkillList(skills);
	}
	/**
	 * Stores all player skills according to their persistence state
	 */
	@Override
	public boolean storeSkills(Player player)
	{
		SkillListEntry[] skillsActive = player.getSkillList().getAllSkills();
		SkillListEntry[] skillsDeleted = player.getSkillList().getDeletedSkills();
		store(player, skillsActive);
		store(player, skillsDeleted);
		
		return true;
	}
	/**
	 * 
	 * @param player
	 * @param skills
	 */
	private void store(Player player, SkillListEntry[] skills)
	{
		for(int i = 0; i < skills.length ; i++)
		{
			SkillListEntry skill = skills[i];
			switch(skill.getPersistentState())
			{
				case NEW:
					addSkill(player.getObjectId(), skill.getSkillId(), skill.getSkillLevel());
					break;
				case UPDATE_REQUIRED:
					updateSkill(player.getObjectId(), skill.getSkillId(), skill.getSkillLevel());
					break;
				case DELETED:
					deleteSkill(player.getObjectId(), skill.getSkillId());
					break;
			}
			skill.setPersistentState(PersistentState.UPDATED);
		}
	}
	
	/**
	 * Add a skill information into database
	 *
	 * @param playerId player object id
	 * @param skill    skill contents.
	 */
	private void addSkill(final int playerId, final int skillId, final int skillLevel)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, skillId);
			stmt.setInt(3, skillLevel);
			stmt.setInt(4, skillLevel);
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
	 * Updates skill in database (after level change)
	 * 
	 * @param playerId
	 * @param skillId
	 * @param skillLevel
	 */
	private void updateSkill(final int playerId, final int skillId, final int skillLevel)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, skillLevel);
			stmt.setInt(2, playerId);
			stmt.setInt(3, skillId);
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
	 * Deletes skill from database
	 * 
	 * @param playerId
	 * @param skillId
	 */
	private void deleteSkill(final int playerId, final int skillId)
	{		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, skillId);
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
	
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}