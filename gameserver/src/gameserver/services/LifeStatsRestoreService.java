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

package gameserver.services;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.state.CreatureState;
import gameserver.model.gameobjects.stats.CreatureLifeStats;
import gameserver.model.gameobjects.stats.PlayerLifeStats;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.zone.ZoneName;
import java.util.concurrent.Future;

public class LifeStatsRestoreService
{
	private static final int DEFAULT_DELAY = 6000;
	private static final int DEFAULT_FPREDUCE_DELAY	= 1000;
	private static final int DEFAULT_FPRESTORE_DELAY = 2000;
	private static LifeStatsRestoreService	instance = new LifeStatsRestoreService();

	/**
	 * HP and MP restoring task
	 * 
	 * @param creature
	 * @return Future<?>
	 */
	public Future<?> scheduleRestoreTask(CreatureLifeStats<? extends Creature> lifeStats)
	{
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new HpMpRestoreTask(lifeStats), 1700, DEFAULT_DELAY);
	}
	
	/**
	 * HP restoring task
	 * 
	 * @param lifeStats
	 * @return
	 */
	public Future<?> scheduleHpRestoreTask(CreatureLifeStats<? extends Creature> lifeStats)
	{
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new HpRestoreTask(lifeStats), 1700, DEFAULT_DELAY);
	}

	/**
	 * 
	 * @param lifeStats
	 * @param currentFlightZoneName 
	 * @return Future Task
	 */
	public Future<?> scheduleFpReduceTask(final PlayerLifeStats lifeStats, ZoneName currentFlightZoneName)
	{
		int delay = DEFAULT_FPREDUCE_DELAY;
		
		boolean flightAllowed = false;
		if (lifeStats.getOwner() != null)
			if (lifeStats.getOwner().getZoneInstance() != null)
				flightAllowed = lifeStats.getOwner().getZoneInstance().getTemplate().isFlightAllowed();

		if (lifeStats.getOwner().isInState(CreatureState.GLIDING) && flightAllowed)
			delay = 2000;
		
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new FpReduceTask(lifeStats, currentFlightZoneName), 2000, delay);
	}

	/**
	 * 
	 * @param lifeStats
	 * @return Future Task
	 */
	public Future<?> scheduleFpRestoreTask(PlayerLifeStats lifeStats)
	{
		return ThreadPoolManager.getInstance().scheduleAtFixedRate(new FpRestoreTask(lifeStats), 2000,
			DEFAULT_FPRESTORE_DELAY);
	}

	public static LifeStatsRestoreService getInstance()
	{
		return instance;
	}

	private static class HpRestoreTask implements Runnable
	{
		private CreatureLifeStats<?>	lifeStats;

		private HpRestoreTask(CreatureLifeStats<?> lifeStats)
		{
			this.lifeStats = lifeStats;
		}

		@Override
		public void run()
		{
			if(lifeStats.isAlreadyDead() || lifeStats.isFullyRestoredHp())
			{
				lifeStats.cancelRestoreTask();
			}
			else
			{
				lifeStats.restoreHp();
			}
		}
	}

	private static class HpMpRestoreTask implements Runnable
	{
		private CreatureLifeStats<?>	lifeStats;

		private HpMpRestoreTask(CreatureLifeStats<?> lifeStats)
		{
			this.lifeStats = lifeStats;
		}

		@Override
		public void run()
		{
			if(lifeStats.isAlreadyDead() || lifeStats.isFullyRestoredHpMp())
			{
				lifeStats.cancelRestoreTask();
			}
			else
			{
				if (!lifeStats.isFullyRestoredHp())
					lifeStats.restoreHp();
				
				if (!lifeStats.isFullyRestoredMp())
					lifeStats.restoreMp();
			}
		}
	}

	private static class FpReduceTask implements Runnable
	{
		private PlayerLifeStats	lifeStats;

		private ZoneName currentFlightZoneName;
		
		private FpReduceTask(PlayerLifeStats lifeStats, ZoneName currentFlightZoneName)
		{
			this.lifeStats = lifeStats;
			this.currentFlightZoneName = currentFlightZoneName;
		}

		@Override
		public void run()
		{
			if(lifeStats.isAlreadyDead())
				lifeStats.cancelFpReduce();

			if(lifeStats.getCurrentFp() == 0)
			{
				if(lifeStats.getOwner().getFlyState() > 0)
				{
					//NOTE exception for Prayer of Freedom
					if (!lifeStats.getOwner().getEffectController().hasAbnormalEffect(537))
						lifeStats.getOwner().getFlyController().endFly();
				}
				else
				{
					lifeStats.triggerFpRestore();
				}
			}
			else
			{
				int value = 1;
				if (lifeStats.getOwner().getZoneInstance() != null)
				{
					if (lifeStats.getOwner().isInState(CreatureState.GLIDING) && !lifeStats.getOwner().getZoneInstance().getTemplate().isFlightAllowed())
						value = 2;
				}

				lifeStats.reduceFp(value);	
				// Check for leaving flight zone
				if (this.currentFlightZoneName != null)
				{
					if (!ZoneService.getInstance().isInsideFlightZone(lifeStats.getOwner(), this.currentFlightZoneName) &&
						lifeStats.getOwner().getFlyState() == 1)
						lifeStats.getOwner().getFlyController().endFly();
				}
				
				lifeStats.specialrestoreFp();
			}
		}
	}

	private static class FpRestoreTask implements Runnable
	{
		private PlayerLifeStats	lifeStats;

		private FpRestoreTask(PlayerLifeStats lifeStats)
		{
			this.lifeStats = lifeStats;
		}

		@Override
		public void run()
		{
			if(lifeStats.isAlreadyDead() || lifeStats.isFlyTimeFullyRestored())
			{
				lifeStats.cancelFpRestore();
			}
			else
			{
				lifeStats.restoreFp();
			}
		}
	}
}
