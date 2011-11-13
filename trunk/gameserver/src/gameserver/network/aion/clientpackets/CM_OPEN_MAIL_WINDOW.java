package gameserver.network.aion.clientpackets;


import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_MAIL_SERVICE;
import gameserver.utils.PacketSendUtility;

/**
 * @author ginho1
 *
 */
public class CM_OPEN_MAIL_WINDOW extends AionClientPacket
{

	/**
	 * @param opcode
	 */
	public CM_OPEN_MAIL_WINDOW(int opcode)
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
		Player player = this.getConnection().getActivePlayer();
		PacketSendUtility.sendPacket(player, new SM_MAIL_SERVICE(player, player.getMailbox().getLetters()));
	}

}
