/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
 */

package gameserver.model.siege;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.gameobjects.stats.modifiers.Executor;
import gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import gameserver.services.SiegeService;
import gameserver.utils.PacketSendUtility;
import gameserver.world.World;

public class Influence
{
	private float elyos = 0; 
	private float asmos = 0; 
	private float balaur = 0;
	private float elyosGelkmaros = 0; 
	private float asmosGelkmaros = 0; 
	private float balaurGelkmaros = 0; 
	private float elyosInggison = 0; 
	private float asmosInggison = 0; 
	private float balaurInggison = 0; 
	private float elyosAbyss = 0; 
	private float asmosAbyss = 0; 
	private float balaurAbyss = 0; 
	
	private Influence()
	{
		calculateInfluence();
	}
	
	public static final Influence getInstance()
	{
		return SingletonHolder.instance;
	}

	public void recalculateInfluence()
	{
		calculateInfluence();

		broadcastInfluencePacket();
	}

	private void calculateInfluence()
	{
		int total = 0;
		int asmos = 0;
		int elyos = 0;
		int balaur = 0;
		elyosGelkmaros = 0; 
		asmosGelkmaros = 0; 
		balaurGelkmaros = 0; 
		elyosInggison = 0; 
		asmosInggison = 0; 
		balaurInggison = 0;
		
		for(SiegeLocation sLoc : SiegeService.getInstance().getSiegeLocations().values())
		{
			int bonus = 0;
			switch(sLoc.getSiegeType())
			{
				case ARTIFACT: bonus = 1; break;
				case FORTRESS: bonus = 10; break;
				default: break;
			}
			total += bonus;
			switch(sLoc.getRace())
			{
				case BALAUR:
					balaur += bonus;
					break;
				case ASMODIANS:
					asmos += bonus;
					break;
				case ELYOS:
					elyos += bonus;
					break;
			}
			calculateAbyssInfluence(sLoc);
			calculateBalaureaInfluence(sLoc);
		}
		
		this.balaur = (float)balaur / total;
		this.elyos = (float)elyos / total;
		this.asmos = (float)asmos / total;
		
		this.balaurAbyss /= 9;
		this.elyosAbyss /= 9;
		this.asmosAbyss /= 9;
	}
	
	private void calculateBalaureaInfluence(SiegeLocation sLoc)
	{
		if (sLoc.getLocationId() == 3011 || sLoc.getLocationId() == 3021)
		{
			switch(sLoc.getRace())
			{
				case BALAUR:
					balaurGelkmaros += 0.5f;
					break;
				case ASMODIANS:
					asmosGelkmaros += 0.5f;
					break;
				case ELYOS:
					elyosGelkmaros += 0.5f;
					break;
			}
		}
		
		if (sLoc.getLocationId() == 2011 || sLoc.getLocationId() == 2021)
		{
			switch(sLoc.getRace())
			{
				case BALAUR:
					balaurInggison += 0.5f;
					break;
				case ASMODIANS:
					asmosInggison += 0.5f;
					break;
				case ELYOS:
					elyosInggison += 0.5f;
					break;
			}
		}
	}
	
	private void calculateAbyssInfluence(SiegeLocation sLoc)
	{
		switch(sLoc.getLocationId())
		{
			case 1221:
			case 1231:
			case 1241:
			case 1132:
			case 1251:
			case 1131:
			case 1141:
			case 1011:
			case 1211:
				switch(sLoc.getRace())
				{
					case BALAUR:
						balaurAbyss += 1.0f;
						break;
					case ASMODIANS:
						asmosAbyss += 1.0f;
						break;
					case ELYOS:
						elyosAbyss += 1.0f;
						break;
				}
				break;
			default:
				break;
		}
	}

	private void broadcastInfluencePacket()
	{
		final SM_INFLUENCE_RATIO pkt = new SM_INFLUENCE_RATIO();
		
		World.getInstance().doOnAllPlayers(new Executor<Player>(){
			@Override
			public boolean run(Player player)
			{
				PacketSendUtility.sendPacket(player, pkt);
				return true;
			}
		});
	}

	public float getElyos()
	{
		return this.elyos;
	}

	public float getAsmos()
	{
		return this.asmos;
	}

	public float getBalaur()
	{
		return this.balaur;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final Influence instance = new Influence();
	}

	public float getElyosGelkmaros()
	{
		return elyosGelkmaros;
	}

	public float getAsmosGelkmaros()
	{
		return asmosGelkmaros;
	}

	public float getBalaurGelkmaros()
	{
		return balaurGelkmaros;
	}

	public float getElyosInggison()
	{
		return elyosInggison;
	}

	public float getAsmosInggison()
	{
		return asmosInggison;
	}

	public float getBalaurInggison()
	{
		return balaurInggison;
	}

	public float getElyosAbyss()
	{
		return elyosAbyss;
	}

	public float getAsmosAbyss()
	{
		return asmosAbyss;
	}

	public float getBalaurAbyss()
	{
		return balaurAbyss;
	}

}