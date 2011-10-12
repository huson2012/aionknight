
package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.EmpyreanCrucibleService;


public class CM_EXIT_EC extends AionClientPacket
{

	public CM_EXIT_EC(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{

	}

	@Override
	protected void runImpl()
	{	//TODO
		//final Player activePlayer = getConnection().getActivePlayer();
		//activePlayer.getPlayerGroup().getEmpyreanCrucible().doReward(activePlayer);
	}
}
