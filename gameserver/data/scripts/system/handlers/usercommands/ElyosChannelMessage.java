/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package usercommands;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.utils.chathandlers.CustomChannel;
import ru.aionknight.gameserver.utils.i18n.CustomMessageId;
import ru.aionknight.gameserver.utils.i18n.LanguageHandler;


/**
 * @author blakawk
 *
 */
public class ElyosChannelMessage extends CustomChannel
{
	public ElyosChannelMessage ()
	{
		super(LanguageHandler.translate(CustomMessageId.CHANNEL_COMMAND_ELYOS), Player.CHAT_FIXED_ON_ELYOS);
	}
}
