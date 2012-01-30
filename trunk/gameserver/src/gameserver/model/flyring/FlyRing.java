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

package gameserver.model.flyring;

import gameserver.ai.npcai.DummyAi;
import gameserver.controllers.FlyRingController;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.stats.modifiers.FlyRingKnownList;
import gameserver.model.templates.flyring.FlyRingTemplate;
import gameserver.model.utils3d.Plane3D;
import gameserver.model.utils3d.Point3D;
import gameserver.utils.idfactory.IDFactory;
import gameserver.world.World;

public class FlyRing extends Creature
{
	private FlyRingTemplate template = null;
	private String name = null;
	private Plane3D plane = null;
	private Point3D center = null;
	private Point3D p1 = null;
	private Point3D p2 = null;
	
	public FlyRing (FlyRingTemplate template)
	{
		super(IDFactory.getInstance().nextId(), new FlyRingController(), null, null,
			World.getInstance().createPosition(template.getMap(), template.getCenter().getX(), 
			template.getCenter().getY(), template.getCenter().getZ(), (byte)0));
		
		((FlyRingController)getController()).setOwner(this);
		this.template = template;
		this.name = (template.getName()==null)?"FLY_RING":template.getName();
		this.center = new Point3D(template.getCenter().getX(), template.getCenter().getY(), template.getCenter().getZ());
		this.p1 = new Point3D(template.getP1().getX(), template.getP1().getY(), template.getP1().getZ());
		this.p2 = new Point3D(template.getP2().getX(), template.getP2().getY(), template.getP2().getZ());
		this.plane = new Plane3D(center, p1, p2);
		setKnownlist(new FlyRingKnownList(this));
	}
	
	public Plane3D getPlane()
	{
		return plane;
	}
	
	public FlyRingTemplate getTemplate ()
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
