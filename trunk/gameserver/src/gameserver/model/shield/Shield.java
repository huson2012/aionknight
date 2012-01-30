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

package gameserver.model.shield;

import gameserver.ai.npcai.DummyAi;
import gameserver.controllers.ShieldController;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.stats.modifiers.ShieldKnownList;
import gameserver.model.templates.shield.ShieldTemplate;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.World;

public class Shield extends Creature
{
	private ShieldTemplate template = null;
	private String name = null;
	
	public Shield (ShieldTemplate template)
	{
		super(IDFactory.getInstance().nextId(), new ShieldController(), null, null, World.getInstance().createPosition(template.getMap(), template.getX(), template.getY(), template.getZ(), (byte)0));
		
		((ShieldController)getController()).setOwner(this);
		this.template = template;
		this.name = template.getName();
		setKnownlist(new ShieldKnownList(this));
	}
	
	public ShieldTemplate getTemplate ()
	{
		return template;
	}
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public byte getLevel()
	{
		return 0;
	}

	@Override
	public void initializeAi()
	{
		ai = new DummyAi();
		ai.setOwner(this);
	}
	
	public void spawn ()
	{
		World w = World.getInstance();
		w.storeObject(this);
		w.spawn(this);
	}
}
