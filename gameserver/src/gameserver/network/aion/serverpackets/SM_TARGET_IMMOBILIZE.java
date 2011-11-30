package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.Creature;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;

import java.nio.ByteBuffer;


/**
 * @author Sweetkr
 */
public class SM_TARGET_IMMOBILIZE extends AionServerPacket
{
	private Creature creature;

	public SM_TARGET_IMMOBILIZE(Creature creature)
	{
		this.creature = creature;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, creature.getObjectId());
		writeF(buf, creature.getX());
		writeF(buf, creature.getY());
		writeF(buf, creature.getZ());
		writeC(buf, creature.getHeading());
	}
}
