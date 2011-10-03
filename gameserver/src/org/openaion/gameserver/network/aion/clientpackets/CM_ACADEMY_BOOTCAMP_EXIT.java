package org.openaion.gameserver.network.aion.clientpackets;

import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.network.aion.AionClientPacket;
import org.openaion.gameserver.services.AcademyBootcampService;
import org.openaion.gameserver.services.TeleportService;

public class CM_ACADEMY_BOOTCAMP_EXIT extends AionClientPacket
{
	public CM_ACADEMY_BOOTCAMP_EXIT(int opcode)
	{
		super(opcode);
	}
	
	@Override
	protected void readImpl()
	{

	}
	
	@Override
	protected void runImpl()
	{
		Player player = getConnection().getActivePlayer();
		
		if(AcademyBootcampService.isAcademyBootcamp(player.getWorldId()))
		{
			TeleportService.teleportToInstanceExit(player, player.getWorldId(), player.getInstanceId(), 0);
		}		
	}
}
