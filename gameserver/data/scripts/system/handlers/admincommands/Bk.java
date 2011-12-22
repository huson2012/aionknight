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

package admincommands;

import commons.database.DB;
import commons.database.DatabaseFactory;
import commons.database.IUStH;
import commons.database.ParamReadStH;
import gameserver.ShutdownHook;
import gameserver.configs.administration.AdminConfig;
import gameserver.model.gameobjects.player.Player;
import gameserver.services.TeleportService;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

class Bookmark
{
	private String	name;
	private float	x;
	private float	y;
	private float	z;
	private int		world_id;

	public Bookmark(float x, float y, float z, int world_id, String name)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.world_id = world_id;
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the x
	 */
	public float getX()
	{
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY()
	{
		return y;
	}

	/**
	 * @return the z
	 */
	public float getZ()
	{
		return z;
	}

	/**
	 * @return the world_id
	 */
	public int getWorld_id()
	{
		return world_id;
	}
}

public class Bk extends AdminCommand
{
	ArrayList<Bookmark>			bookmarks		= new ArrayList<Bookmark>();
	private static final Logger	log				= Logger.getLogger(ShutdownHook.class);
	private String				bookmark_name	= "";

	Player						admin;

	public Bk()
	{
		super("bk");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{

		if (admin.getAccessLevel() < AdminConfig.COMMAND_BK)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //bk <add | del | tele | list>");
			return;
		}

		// set local class admin
		this.admin = admin;

		if (params[0].equals("add"))
		{
			try
			{
				bookmark_name = params[1].toLowerCase();
				if (isBookmarkExists(bookmark_name))
				{
					PacketSendUtility.sendMessage(admin, "Bookmark " + bookmark_name + " already exists !");
					return;
				}

				final float x = admin.getX();
				final float y = admin.getY();
				final float z = admin.getZ();
				final int char_id = admin.getObjectId();
				final int world_id = admin.getWorldId();

				DB.insertUpdate("INSERT INTO bookmark (" + "`name`,`char_id`, `x`, `y`, `z`,`world_id` )" + " VALUES "
					+ "(?, ?, ?, ?, ?, ?)", new IUStH()
				{
					@Override
					public void handleInsertUpdate(PreparedStatement ps) throws SQLException
					{
						ps.setString(1, bookmark_name);
						ps.setInt(2, char_id);
						ps.setFloat(3, x);
						ps.setFloat(4, y);
						ps.setFloat(5, z);
						ps.setInt(6, world_id);
						ps.execute();
					}
				});

				PacketSendUtility.sendMessage(admin, "Bookmark " + bookmark_name
					+ " sucessfully added to your bookmark list!");

				updateInfo();
			}
			catch (Exception e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //bk <add | del | tele> <bookmark name>");
				return;
			}
		}
		else if (params[0].equals("del"))
		{
			Connection con = null;
			try
			{
				bookmark_name = params[1].toLowerCase();
				con = DatabaseFactory.getConnection();

				PreparedStatement statement = con.prepareStatement("DELETE FROM bookmark WHERE name = ?");
				statement.setString(1, bookmark_name);
				statement.executeUpdate();
				statement.close();
			}
			catch (Exception e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //bk <add|del|tele> <bookmark name>");
				return;
			}
			finally
			{
				DatabaseFactory.close(con);
				PacketSendUtility.sendMessage(admin, "Bookmark " + bookmark_name
					+ " sucessfully removed from your bookmark list!");
				updateInfo();
			}
		}
		else if (params[0].equals("tele"))
		{
			try
			{

				if (params[1].equals("") || params[1] == null)
				{
					PacketSendUtility.sendMessage(admin, "syntax //bk <add|del|tele> <bookmark name>");
					return;
				}

				updateInfo();

				bookmark_name = params[1].toLowerCase();
				Bookmark tele_bk = null;
				try
				{
					tele_bk = selectByName(bookmark_name);
				}
				finally
				{
					if (tele_bk != null)
					{
						TeleportService.teleportTo(admin, tele_bk.getWorld_id(), tele_bk.getX(), tele_bk.getY(),
							tele_bk.getZ(), 0);
						PacketSendUtility.sendMessage(admin, "Teleported to bookmark " + tele_bk.getName()
							+ " location");
					}
				}

			}
			catch (Exception e)
			{
				PacketSendUtility.sendMessage(admin, "syntax //bk <add|del|tele> <bookmark name>");
				return;
			}

		}
		else if (params[0].equals("list"))
		{
			updateInfo();
			PacketSendUtility.sendMessage(admin, "=====Bookmark list begin=====");
			for (Bookmark b : bookmarks)
			{
				PacketSendUtility.sendMessage(admin, " = " + b.getName() + " =  -  ( " + b.getX() + " ," + b.getY()
					+ " ," + b.getZ() + " )");
			}
			PacketSendUtility.sendMessage(admin, "=====Bookmark list end=======");
		}
	}

	/**
	 * Reload bookmark list from db
	 */
	public void updateInfo()
	{
		bookmarks.clear();

		DB.select("SELECT * FROM `bookmark` where char_id= ?", new ParamReadStH()
		{
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, admin.getObjectId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				while (rset.next())
				{
					String name = rset.getString("name");
					float x = rset.getFloat("x");
					float y = rset.getFloat("y");
					float z = rset.getFloat("z");
					int world_id = rset.getInt("world_id");
					bookmarks.add(new Bookmark(x, y, z, world_id, name));
				}
			}
		});
	}

	/**
	 * @param bk_name - bookmark name
	 * @return Bookmark from bookmark name
	 */
	public Bookmark selectByName(String bk_name)
	{
		for (Bookmark b : bookmarks)
		{
			if (b.getName().equals(bk_name))
			{
				return b;
			}
		}
		return null;
	}

	/**
	 * @param bk_name - bookmark name
	 * @return true if bookmark exists
	 */
	public boolean isBookmarkExists(final String bk_name)
	{
		Connection con = null;
		int bkcount = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT count(id) as bkcount FROM bookmark WHERE ? = name AND char_id = ?");
			statement.setString(1, bk_name);
			statement.setInt(2, admin.getObjectId());
			ResultSet rset = statement.executeQuery();
			while (rset.next())
				bkcount = rset.getInt("bkcount");
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			log.error("Error in reading db", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return bkcount > 0;
	}
}
