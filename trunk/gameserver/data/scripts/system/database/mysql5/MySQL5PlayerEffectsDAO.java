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
import gameserver.configs.main.CustomConfig;
import gameserver.dao.PlayerEffectsDAO;
import gameserver.model.gameobjects.player.Player;
import gameserver.skill.model.Effect;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

public class MySQL5PlayerEffectsDAO extends PlayerEffectsDAO
{
	public static final String INSERT_QUERY = "INSERT INTO `player_effects` (`player_id`, `skill_id`, `delay_id`, `skill_lvl`, `current_time`, `reuse_delay`) VALUES (?,?,?,?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `player_effects` WHERE `player_id`=?";
	public static final String SELECT_QUERY = "SELECT `skill_id`, `delay_id`, `skill_lvl`, `current_time`, `reuse_delay` FROM `player_effects` WHERE `player_id`=?";

	private static final Logger log = Logger.getLogger(MySQL5PlayerEffectsDAO.class);
	
	@Override
	public void loadPlayerEffects(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, player.getObjectId());
			ResultSet rset = stmt.executeQuery();
			int timeLaunched = 0;
			int skillLaunched = 0;
			int skillLvlLaunched = 0;
			while(rset.next())
			{
				int skillId = rset.getInt("skill_id");
				int delayId = rset.getInt("delay_id");
				int skillLvl = rset.getInt("skill_lvl");
				int elapsedTime = rset.getInt("current_time");//time till end
				long reuseDelay = rset.getLong("reuse_delay");
				
				if(reuseDelay > System.currentTimeMillis())
					player.setSkillCoolDown(delayId, reuseDelay);
				if(elapsedTime > 0)
				{
					if (CustomConfig.ABYSS_XFORM_DURATION_AFTER_LOGOUT)
					{
						//custom for abyss xforms, duration is counting even after log out
						if (skillId >= 11885 && skillId <= 11894)
						{
							elapsedTime = (int)(reuseDelay - System.currentTimeMillis() - 110*60*1000);
							if (elapsedTime < 0 || elapsedTime > 10*60*1000)
								continue;
							else
								timeLaunched = elapsedTime;
						}
						else if(skillId >= 11907 && skillId <= 11916 )
						{
							skillLaunched = skillId;
							skillLvlLaunched = skillLvl;
							continue;
						}
					}
					
					player.getEffectController().addSavedEffect(skillId, skillLvl, elapsedTime);
				}
				
			}
		
			if (CustomConfig.ABYSS_XFORM_DURATION_AFTER_LOGOUT)
			{
				//add launched skill for abyss xform
				if (timeLaunched > 0 && skillLaunched != 0)
				{
					player.getEffectController().addSavedEffect(skillLaunched, skillLvlLaunched, timeLaunched);
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
		player.getEffectController().broadCastEffects();
	}

	@Override
	public void storePlayerEffects(final Player player)
	{
		deletePlayerEffects(player);
		Iterator<Effect> iterator = player.getEffectController().iterator();		
		
		while(iterator.hasNext())
		{
			final Effect effect = iterator.next();
			final int elapsedTime = effect.getElapsedTime();
			
			if(elapsedTime < 60000)
				continue;
			
			Connection con = null;
			try
			{
				con = DatabaseFactory.getConnection();
				PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, effect.getSkillId());
				stmt.setInt(3, effect.getSkillTemplate().getDelayId());
				stmt.setInt(4, effect.getSkillLevel());
				stmt.setInt(5, elapsedTime);//time till end
				
				long reuseTime = player.getSkillCoolDown(effect.getSkillTemplate().getDelayId());
				player.removeSkillCoolDown(effect.getSkillTemplate().getDelayId());
				
				stmt.setLong(6, reuseTime);
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
		
		final Map<Integer, Long> cooldowns = player.getSkillCoolDowns();
		if(cooldowns != null)
		{
			for(Map.Entry<Integer, Long> entry : cooldowns.entrySet())
			{
				final int delayId = entry.getKey();
				final long reuseTime = entry.getValue();
				if(reuseTime - System.currentTimeMillis() < 60000)
					continue;
				
				Connection con = null;
				try
				{
					con = DatabaseFactory.getConnection();
					PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
					stmt.setInt(1, player.getObjectId());
					stmt.setInt(2, 0);
					stmt.setInt(3, delayId);
					stmt.setInt(4, 0);
					stmt.setInt(5, 0);											
					stmt.setLong(6, reuseTime);
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
		}
	}
	
	private void deletePlayerEffects(final Player player)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, player.getObjectId());
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
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}
