
package ru.aionknight.gameserver.network.aion.clientpackets;


import ru.aionknight.gameserver.model.gameobjects.player.Player;
import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.services.EmpyreanCrucibleService;


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
