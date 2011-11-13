package gameserver.network.aion.clientpackets;


import gameserver.model.gameobjects.player.DeniedStatus;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.AionClientPacket;
import gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import gameserver.services.GroupService;
import gameserver.utils.Util;
import gameserver.world.World;

/**
 * 
 * @author ginho1
 * 
 */
public class CM_CHAT_RECRUIT_GROUP extends AionClientPacket
{
	private String name;
	
	public CM_CHAT_RECRUIT_GROUP(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		name = readS();
	}

	/**
	 * {@inheritDoc}n
	 */
	@Override
	protected void runImpl()
	{
		final String captainName = Util.convertName(name);

		final Player player = getConnection().getActivePlayer();
		final Player captain = World.getInstance().findPlayer(captainName);

		if(captain != null)
		{
			if(captain.getPlayerSettings().isInDeniedStatus(DeniedStatus.GROUP))
			{
				sendPacket(SM_SYSTEM_MESSAGE.STR_MSG_REJECTED_INVITE_PARTY(captain.getName()));
				return;
			}

			GroupService.getInstance().requestToGroup(player, captain);
		}
		else
			player.getClientConnection().sendPacket(SM_SYSTEM_MESSAGE.PLAYER_IS_OFFLINE(name));
	}
}
