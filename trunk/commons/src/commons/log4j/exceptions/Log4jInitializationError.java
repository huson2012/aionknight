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

package commons.log4j.exceptions;

public class Log4jInitializationError extends Error
{
	private static final long	serialVersionUID	= -628697707807736993L;

	public Log4jInitializationError()
	{
	}

	public Log4jInitializationError(String message)
	{
		super(message);
	}

	public Log4jInitializationError(String message, Throwable cause)
	{
		super(message, cause);
	}

	public Log4jInitializationError(Throwable cause)
	{
		super(cause);
	}
}