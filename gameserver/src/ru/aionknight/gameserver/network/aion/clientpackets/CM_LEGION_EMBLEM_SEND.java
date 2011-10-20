package ru.aionknight.gameserver.network.aion.clientpackets;


import ru.aionknight.gameserver.model.legion.Legion;
import ru.aionknight.gameserver.model.legion.LegionEmblem;
import ru.aionknight.gameserver.network.aion.AionClientPacket;
import ru.aionknight.gameserver.services.LegionService;

/**
 * @author LokiReborn
 */
public class CM_LEGION_EMBLEM_SEND extends AionClientPacket
{
	
	private int	legionId;

	public CM_LEGION_EMBLEM_SEND(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		legionId = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		Legion legion = LegionService.getInstance().getLegion(legionId);
		if (legion != null)
		{
			LegionEmblem legionEmblem = legion.getLegionEmblem();
			if(legionEmblem.getCustomEmblemData() == null) return;
				LegionService.getInstance().sendCustomLegionPacket(getConnection().getActivePlayer(), legionEmblem, legionId, legion.getLegionName());
		}
	}
}
