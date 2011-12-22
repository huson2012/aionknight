/**   
 * Эмулятор игрового сервера Aion 2.7 от команды разработчиков 'Aion-Knight Dev. Team' является 
 * свободным программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного программного 
 * обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой более поздней 
 * версии.
 * 
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова) 
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package gameserver.controllers;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Summon;
import gameserver.model.gameobjects.Trap;
import gameserver.model.gameobjects.player.Player;
import gameserver.model.siege.ArtifactProtector;
import gameserver.services.SiegeService;

public class ArtifactProtectorController extends NpcController
{

	@Override
	public void onRespawn()
	{
		super.onRespawn();
	}
	
	@Override
	public void onStartMove()
	{
		super.onStartMove();
	}
	
	@Override
	public void onMove()
	{
		super.onMove();
	}
	
	@Override
	public void onStopMove()
	{
		super.onStopMove();
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		if(lastAttacker instanceof Player || lastAttacker instanceof Summon || lastAttacker instanceof Trap)
		{
			Player taker;
			if(lastAttacker instanceof Player)
				taker = (Player)lastAttacker;
			else if(lastAttacker instanceof Summon)
				taker = ((Summon)lastAttacker).getMaster();
			else if(lastAttacker instanceof Trap)
				taker = (Player)((Trap)lastAttacker).getCreator();
			else
				taker = null;
			
			if(taker != null)			
				SiegeService.getInstance().onArtifactCaptured(getOwner().getArtifact(), taker);
		}
		else
		{
			// Taken by Balaur
			if(lastAttacker != null)
				SiegeService.getInstance().onArtifactCaptured(getOwner().getArtifact());
		}
		super.onDie(lastAttacker);
	}

	@Override
	public ArtifactProtector getOwner()
	{
		return (ArtifactProtector) super.getOwner();
	}
}
