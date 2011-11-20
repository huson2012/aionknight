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
package gameserver.network.aion.clientpackets;

import commons.database.dao.DAOManager;
import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_CMOTION;
import gameserver.utils.PacketSendUtility;
import gameserver.dao.PlayerCmotionListDAO;

public class CM_CMOTION extends AionClientPacket
{
	private int		act;
	private int 	cmid;
	private CmotionList	cmotionList;
	private int[] cell = {0,5,6,7,8,1,2,3,4};
	
	public CM_CMOTION(int opcode)
	{
		super(opcode);
	}
	

	@Override
    protected void readImpl()
    {
					  readC();
        cmid		= readH();
        act 		= readC();

    }

	@Override
	protected void runImpl() 
	{	
        Player player = getConnection().getActivePlayer();
        this.cmotionList 	= player.getCmotionList();
		for(Cmotion cmotion : cmotionList.getCmotions())
		{	
			int id = cmotion.getCmotionId();
			 if (cmid != 0)
			 {
				 if(cmid == id)
					 cmotion.setActive(true);
				 if(cell[cmid] == id)
					 cmotion.setActive(false);
			 }
			 if (cmid == 0)
			 {
				 switch (act)
				 {
					 case 1: if(id == 1 || id == 5) cmotion.setActive(false); continue;
					 case 2: if(id == 2 || id == 6) cmotion.setActive(false); continue;
					 case 3: if(id == 3 || id == 7) cmotion.setActive(false); continue;
					 case 4: if(id == 4 || id == 8) cmotion.setActive(false); continue;
				 }
			 }
		}        
        DAOManager.getDAO(PlayerCmotionListDAO.class).storeCmotions(player);//dataBase update
        PacketSendUtility.sendPacket(player, new SM_CMOTION(player, act, cmid));//Dialoge update	
	}
}