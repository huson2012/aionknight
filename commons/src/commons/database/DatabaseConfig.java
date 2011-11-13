/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package commons.database;

import java.io.File;
import commons.configuration.Property;

public class DatabaseConfig
{
	@Property(key = "database.url", defaultValue = "jdbc:mysql://localhost:3306/ak_server_ls")
	public static String DATABASE_URL;

	@Property(key = "database.driver", defaultValue = "com.mysql.jdbc.Driver")
	public static Class<?> DATABASE_DRIVER;

	@Property(key = "database.user", defaultValue = "root")
	public static String DATABASE_USER;

	@Property(key = "database.password", defaultValue = "root")
	public static String DATABASE_PASSWORD;

	@Property(key = "database.connections.min", defaultValue = "2")
	public static int DATABASE_CONNECTIONS_MIN;

	@Property(key = "database.connections.max", defaultValue = "10")
	public static int DATABASE_CONNECTIONS_MAX;

	@Property(key = "database.scriptcontext.descriptor", defaultValue = "./data/scripts/system/database/database.xml")
	public static File DATABASE_SCRIPTCONTEXT_DESCRIPTOR;
}