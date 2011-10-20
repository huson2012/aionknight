/*
 * This file is part of Aion-Knight Emu.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aion-Knight Emu.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * author^
 * Fr0st;
 * Mr.Chayka.
*/

package ru.aionknight.gameserver.model.road;
 

import ru.aionknight.gameserver.ai.npcai.DummyAi;
import ru.aionknight.gameserver.controllers.RoadController;
import ru.aionknight.gameserver.model.gameobjects.Creature;
import ru.aionknight.gameserver.model.templates.road.RoadTemplate;
import ru.aionknight.gameserver.model.utils3d.Plane3D;
import ru.aionknight.gameserver.model.utils3d.Point3D;
import ru.aionknight.gameserver.utils.idfactory.IDFactory;
import ru.aionknight.gameserver.world.RoadKnownList;
import ru.aionknight.gameserver.world.World;

public class Road extends Creature {
	private RoadTemplate template = null;
	private String name = null;
	private Plane3D plane = null;
	private Point3D center = null;
	private Point3D p1 = null;
	private Point3D p2 = null;

  public Road(RoadTemplate template) {
		super(IDFactory.getInstance().nextId(), new RoadController(), null, null, World.getInstance().createPosition(template.getMap(), template.getCenter().getX(), template.getCenter().getY(), template.getCenter().getZ(), (byte)  0));

		((RoadController)getController()).setOwner(this);
		this.template = template;
		this.name = (template.getName() == null ? "ROAD" : template.getName());
		this.center = new Point3D(template.getCenter().getX(), template.getCenter().getY(), template.getCenter().getZ());
		this.p1 = new Point3D(template.getP1().getX(), template.getP1().getY(), template.getP1().getZ());
		this.p2 = new Point3D(template.getP2().getX(), template.getP2().getY(), template.getP2().getZ());
		this.plane = new Plane3D(center, p1, p2);
		setKnownlist(new RoadKnownList(this));
	}

	public Plane3D getPlane() {
		return plane;
	}

	public RoadTemplate getTemplate() {
		return template;
	}

	@Override
	public String getName() {
		return name;
	}

    @Override
	public byte getLevel() {
		return 0;
	}

    @Override
	public void initializeAi() {
		ai = new DummyAi();
		ai.setOwner(this);
	}

	public void spawn() {
		World w = World.getInstance();
		w.storeObject(this);
		w.spawn(this);
	}
}