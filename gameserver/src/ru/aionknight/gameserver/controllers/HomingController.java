package ru.aionknight.gameserver.controllers;



import ru.aionknight.gameserver.controllers.movement.ActionObserver;
import ru.aionknight.gameserver.model.gameobjects.Homing;


/**
 * @author ATracer
 *
 */
public class HomingController extends NpcWithCreatorController
{
	@Override
	public Homing getOwner()
	{
		return (Homing)super.getOwner();
	}
	
	@Override
	public void onDelete()
	{
		ActionObserver observer = getOwner().getObserver();
		if (observer != null)
		{
			getOwner().getObserveController().removeObserver(observer);
			getOwner().setObserver(null);
		}
		getOwner().setCreator(null);
		getOwner().cancelDespawnTask();
		super.onDelete();
	}
}