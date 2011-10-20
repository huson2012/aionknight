package ru.aionknight.gameserver.dao;

import java.util.List;

import ru.aionknight.gameserver.model.gameobjects.BrokerItem;


public abstract class BrokerDAO implements IDFactoryAwareDAO
{
	public abstract List<BrokerItem> loadBroker();
	
	public abstract boolean store(BrokerItem brokerItem);
	
	@Override
	public final String getClassName()
	{
		return BrokerDAO.class.getName();
	}
}
