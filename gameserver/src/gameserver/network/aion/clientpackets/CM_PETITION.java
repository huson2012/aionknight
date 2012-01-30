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

import gameserver.model.Petition;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_PETITION;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.PetitionService;
import java.util.regex.Pattern;

public class CM_PETITION extends AionClientPacket 
{
    private static final Pattern COMPILE = Pattern.compile("/");
    private int action;
	private String title = "";
	private String text = "";
	private String additionalData = "";
	
    public CM_PETITION(int opcode) 
    {
        super(opcode);
    }

    @Override
    protected void readImpl() 
    {
        action = readH();
        if(action == 2)
        {
        	readD();
        }
        else
        {
	        String data = readS();
	        String[] dataArr = COMPILE.split(data, 3);
	        title = dataArr[0];
	        text = dataArr[1];
	        additionalData = dataArr[2];
        }
    }

    @Override
    protected void runImpl() 
    {
    	int playerObjId = getConnection().getActivePlayer().getObjectId();
    	if(action == 2)
    	{
    		if(PetitionService.getInstance().hasRegisteredPetition(playerObjId))
    		{
    			int petitionId = PetitionService.getInstance().getPetition(playerObjId).getPetitionId();
    			PetitionService.getInstance().deletePetition(playerObjId);
        		sendPacket(new SM_SYSTEM_MESSAGE(1300552, petitionId));
        		sendPacket(new SM_SYSTEM_MESSAGE(1300553, 49));
        		return;
    		}
    		
    	}
    		
    	if(!PetitionService.getInstance().hasRegisteredPetition(getConnection().getActivePlayer().getObjectId()))
    	{
    		Petition petition = PetitionService.getInstance().registerPetition(getConnection().getActivePlayer(), action, title, text, additionalData);
    		sendPacket(new SM_PETITION(petition));
    	}    	
	}
}
