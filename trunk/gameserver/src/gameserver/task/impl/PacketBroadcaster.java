/**   
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
 * 
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������) 
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package gameserver.task.impl;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.task.AbstractFIFOPeriodicTaskManager;

public final class PacketBroadcaster extends AbstractFIFOPeriodicTaskManager<Creature>
{
	private static final class SingletonHolder
	{
		private static final PacketBroadcaster	INSTANCE	= new PacketBroadcaster();
	}

	public static PacketBroadcaster getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private PacketBroadcaster()
	{
		super(100);
	}

	public static enum BroadcastMode
	{
		UPDATE_PLAYER_HP_STAT {
			@Override
			public void sendPacket(Creature creature)
			{
				((Player) creature).getLifeStats().sendHpPacketUpdateImpl();
			}
		},
		UPDATE_PLAYER_MP_STAT {
			@Override
			public void sendPacket(Creature creature)
			{
				((Player) creature).getLifeStats().sendMpPacketUpdateImpl();
			}
		},
		UPDATE_PLAYER_EFFECT_ICONS {
			@Override
			public void sendPacket(Creature creature)
			{
				((Player) creature).getEffectController().updatePlayerEffectIconsImpl();
			}
		},
		UPDATE_NEARBY_QUEST_LIST {
			@Override
			public void sendPacket(Creature creature)
			{
				((Player) creature).getController().updateNearbyQuestListImpl();
			}
		},
		
		UPDATE_PLAYER_FLY_TIME {
			@Override
			public void sendPacket(Creature creature)
			{
				((Player) creature).getLifeStats().sendFpPacketUpdateImpl();
			}
		},

		BROAD_CAST_EFFECTS {
			@Override
			public void sendPacket(Creature creature)
			{
				creature.getEffectController().broadCastEffectsImp();
			}
		};

		// TODO: more packets
		

		private final byte	MASK;

		private BroadcastMode()
		{
			MASK = (byte) (1 << ordinal());
		}

		public byte mask()
		{
			return MASK;
		}

		protected abstract void sendPacket(Creature creature);

		protected final void trySendPacket(final Creature creature, byte mask)
		{
			if((mask & mask()) == mask())
			{
				sendPacket(creature);
				creature.removePacketBroadcastMask(this);
			}
		}
	}

	private static final BroadcastMode[]	VALUES	= BroadcastMode.values();

	@Override
	protected void callTask(Creature creature)
	{
		for(byte mask; (mask = creature.getPacketBroadcastMask()) != 0;)
		{
			for(BroadcastMode mode : VALUES)
			{
				mode.trySendPacket(creature, mask);
			}
		}
	}

	@Override
	protected String getCalledMethodName()
	{
		return "packetBroadcast()";
	}
}