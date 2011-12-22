/**
 * This file is part of Aion-Knight Dev. Team <Aion-Knight.org>.
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

package admincommands;

import gameserver.model.Race;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.chathandlers.AdminCommand;

public class ChangeRace extends AdminCommand {

    /**
     * @param commandName
     */
    public ChangeRace() {
        super("race");
    }

    @Override
    public void executeCommand(Player admin, String[] params) {
        if(admin.getCommonData().getRace() == Race.ASMODIANS)
            admin.getCommonData().setRace(Race.ELYOS);
        else
            admin.getCommonData().setRace(Race.ASMODIANS);
        
        PacketSendUtility.sendPacket(admin, new SM_PLAYER_INFO(admin, false));
    }

}
