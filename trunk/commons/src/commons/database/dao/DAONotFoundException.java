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

package commons.database.dao;

public class DAONotFoundException extends DAOException
{
	private static final long	serialVersionUID	= 4241980426435305296L;

	public DAONotFoundException()
	{
	}

	public DAONotFoundException(String message)
	{
		super(message);
	}

	public DAONotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DAONotFoundException(Throwable cause)
	{
		super(cause);
	}
}