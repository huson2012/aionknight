/**
 * This file is part of Aion-Knight Dev. Team [http://aion-knight.ru]
 *
 * Aion-Knight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * Aion-Knight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a  copy  of the GNU General Public License
 * along with Aion-Knight. If not, see <http://www.gnu.org/licenses/>.
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