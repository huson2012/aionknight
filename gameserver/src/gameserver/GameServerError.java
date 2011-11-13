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

package gameserver;

public class GameServerError extends Error
{
	private static final long serialVersionUID = -7445873741878754767L;

	public GameServerError()
	{
	}

	public GameServerError(Throwable cause)
	{
		super(cause);
	}

	public GameServerError(String message)
	{
		super(message);
	}

	public GameServerError(String message, Throwable cause)
	{
		super(message, cause);
	}
}