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

package gameserver.model.instances;

import javolution.util.FastMap;
import gameserver.world.WorldMap;
import gameserver.world.WorldMapInstance;

public class EmpyreanCrucible extends WorldMapInstance
{
    private int stage = 0;
    private int round = 0;
    private int spawnedCount = 0;
    private int killedCount = 0;
    private boolean isStageAllSpawned = true;
    private boolean isRewarded = false;
    private FastMap<Integer, Integer> points = new FastMap<Integer, Integer>();
    private FastMap<Integer, Boolean> readyRoomPlayers = new FastMap<Integer, Boolean>();
    
    public EmpyreanCrucible(WorldMap parent, int instanceId)
    {
        super(parent, instanceId);
    }
    
    public void setStageRound(int stage, int round)
    {
        this.stage = stage;
        this.round = round;
    }
    
    @Override
    public void register(int objectId)
    {
    	
    	points.put(objectId, 0);
    	readyRoomPlayers.put(objectId, false);
    }
    
    public void addPoints(int objectId, int addValue)
    {
    	if(points.getEntry(objectId) == null)
    		register(objectId);
    	
    	int value = points.get(objectId);
    	points.getEntry(objectId).setValue(value + addValue);
    }
    
    public void addToReadyRoom(int objectId)
    {
    	if(readyRoomPlayers.getEntry(objectId) == null)
    		register(objectId);

    	readyRoomPlayers.getEntry(objectId).setValue(true);
    }
    
    public boolean isInReadyRoom(int objectId)
    {
    	if(readyRoomPlayers.getEntry(objectId) == null)
    	{
    		register(objectId);
    		return false;
    	}
    	
    	return readyRoomPlayers.getEntry(objectId).getValue();
    }
    
    public void removeFromReadyRoom(int objectId)
    {
    	if(readyRoomPlayers.getEntry(objectId) == null)
    		register(objectId);

    	readyRoomPlayers.getEntry(objectId).setValue(false);
    }
    
    public FastMap<Integer, Integer> getArenaPoints()
    {
    	return points;
    }
    
    public int getStage()
    {
        return stage;
    }
    
    public int getRound()
    {
        return round;
    }
    
    public void setStageAllSpawned(boolean isStageAllSpawned)
    {
    	this.isStageAllSpawned = isStageAllSpawned;
    }
    
    public int getTotalSpawned()
    {
    	return spawnedCount;
    }
    
    public int getTotalKilled()
    {
    	return killedCount;
    }
    
    public boolean isStageAllSpawned()
    {
    	return isStageAllSpawned;
    }
    
    public void addSpawnedCount(int count)
    {
    	spawnedCount += count;
    }
    
    public void onReward()
    {
    	killedCount += 1;
    }
    
    public void setRewarded(boolean value)
    {
    	this.isRewarded = value;    	
    }
    
    public boolean isRewarded()
    {
    	return isRewarded;
    }
    
    public boolean isStageAllDead()
    {
    	/*boolean isAllDead = true;
    	for(Npc npc : getNpcs())
    	{
    		if((npc.getObjectTemplate().getNpcType() == NpcType.ATTACKABLE || npc.getObjectTemplate().getNpcType() == NpcType.AGGRESSIVE) && !npc.isInState(CreatureState.DEAD))
    			isAllDead = false;
    	}*/
    	
    	return killedCount >= spawnedCount;
    }
    
    public boolean isStageDone()
    {
        return isStageAllSpawned && isStageAllDead();
    }
}
