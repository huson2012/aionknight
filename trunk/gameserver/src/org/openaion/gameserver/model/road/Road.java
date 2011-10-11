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

package org.openaion.gameserver.model.road;

import org.openaion.gameserver.ai.AI;
import org.openaion.gameserver.ai.npcai.DummyAi;
import org.openaion.gameserver.controllers.RoadController;
import org.openaion.gameserver.model.gameobjects.Creature;
import org.openaion.gameserver.model.templates.road.RoadPoint;
import org.openaion.gameserver.model.templates.road.RoadTemplate;
import org.openaion.gameserver.model.utils3d.Plane3D;
import org.openaion.gameserver.model.utils3d.Point3D;
import org.openaion.gameserver.utils.idfactory.IDFactory;
import org.openaion.gameserver.world.RoadKnownList;
import org.openaion.gameserver.world.World;

public class Road extends Creature
{

    public Road(RoadTemplate roadtemplate)
    {
        super(IDFactory.getInstance().nextId(), new RoadController(), null, null, World.getInstance().createPosition(roadtemplate.getMap(), roadtemplate.getCenter().getX(), roadtemplate.getCenter().getY(), roadtemplate.getCenter().getZ(), (byte)0));
        template = null;
        name = null;
        plane = null;
        center = null;
        p1 = null;
        p2 = null;
        ((RoadController)getController()).setOwner(this);
        template = roadtemplate;
        name = roadtemplate.getName() != null ? roadtemplate.getName() : "ROAD";
        center = new Point3D(roadtemplate.getCenter().getX(), roadtemplate.getCenter().getY(), roadtemplate.getCenter().getZ());
        p1 = new Point3D(roadtemplate.getP1().getX(), roadtemplate.getP1().getY(), roadtemplate.getP1().getZ());
        p2 = new Point3D(roadtemplate.getP2().getX(), roadtemplate.getP2().getY(), roadtemplate.getP2().getZ());
        plane = new Plane3D(center, p1, p2);
        setKnownlist(new RoadKnownList(this));
    }

    public Plane3D getPlane()
    {
        return plane;
    }

    public RoadTemplate getTemplate()
    {
        return template;
    }

    public String getName()
    {
        return name;
    }

    public byte getLevel()
    {
        return 0;
    }

    public void initializeAi()
    {
        ai = new DummyAi();
        ai.setOwner(this);
    }

    public void spawn()
    {
        World world = World.getInstance();
        world.storeObject(this);
        world.spawn(this);
    }

    private RoadTemplate template;
    private String name;
    private Plane3D plane;
    private Point3D center;
    private Point3D p1;
    private Point3D p2;
}