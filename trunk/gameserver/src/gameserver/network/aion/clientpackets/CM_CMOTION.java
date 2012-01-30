/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package gameserver.network.aion.clientpackets;

import commons.database.dao.DAOManager;
import gameserver.dao.PlayerCmotionListDAO;
import gameserver.model.gameobjects.player.Cmotion;
import gameserver.model.gameobjects.player.CmotionList;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_CMOTION;
import gameserver.utils.PacketSendUtility;

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
					 case 4: if(id == 4 || id == 8) cmotion.setActive(false);
                 }
			 }
		}        
        DAOManager.getDAO(PlayerCmotionListDAO.class).storeCmotions(player);//dataBase update
        PacketSendUtility.sendPacket(player, new SM_CMOTION(act, cmid));//Dialoge update
	}
}
