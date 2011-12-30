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

package gameserver.network.aion.serverpackets;

import gameserver.model.gameobjects.player.ToyPet;
import gameserver.network.aion.AionConnection;
import gameserver.network.aion.AionServerPacket;
import java.nio.ByteBuffer;
import java.util.List;

public class SM_PET extends AionServerPacket
{
	private int actionId;
	private int feedActionId;
	private ToyPet pet;
	private List<ToyPet> pets;
	private int petUniqueId;
	private String petName;
	private int foodObjId;
	private int foodAmount;
	
	public SM_PET(int actionId)
	{
		this.actionId = actionId;
	}
	
	public SM_PET(int actionId, int petUniqueId)
	{
		this.actionId = actionId;
		this.petUniqueId = petUniqueId;
	}
	
	public SM_PET(int actionId, ToyPet pet)
	{
		this.actionId = actionId;
		this.pet = pet;
	}
	
	public SM_PET(int actionId, List<ToyPet> pets)
	{
		this.actionId = actionId;
		this.pets = pets;
	}

	public SM_PET(int actionId, int petUniqueId, String petName)
	{
		this.actionId = actionId;
		this.petUniqueId = petUniqueId;
		this.petName = petName;
	}
	
	public SM_PET(int actionId, int feedActionId, int foodObjId, int foodAmount, ToyPet pet)
	{
		this.actionId = actionId;
		this.feedActionId = feedActionId;
		this.pet = pet;
		this.foodObjId = foodObjId;
		this.foodAmount = foodAmount;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, actionId);
		switch(actionId)
		{
			case 0:
				// load list on login
				int counter = 0;
				writeC(buf, 0); // unk
				writeH(buf, pets.size());
				for(ToyPet p : pets)
				{
 					counter++;
 					writeS(buf, p.getName());
 					writeD(buf, p.getPetId());
					writeD(buf, p.getUid());
					writeD(buf, p.getMaster().getObjectId()); //unk
 					writeD(buf, 0); //unk
 					writeD(buf, 0); //unk
					writeD(buf, (int)(p.getBirthDay().getTime() / 1000)); //unix timestamp
					
					writeC(buf, 1); //unk
					writeC(buf, 0); //last feedActionId or dynamic value for the feeding
					writeC(buf, p.getExp());
					writeC(buf, p.getLoveCount());
					writeC(buf, p.getFeedCount());
					writeD(buf, p.getFullRemainingTime());
					
					writeC(buf, 0); //??
					writeC(buf, 0); //??
					writeC(buf, 0); //??
					writeD(buf, 0); // TODO: features1 override ???
					writeD(buf, 0); //unk 
					writeC(buf, 1); // hasFunction? 1 / 0
					
 				}
				break;
			case 1:
				// adopt
				writeS(buf, pet.getName());
				writeD(buf, pet.getPetId());
				writeD(buf, pet.getUid()); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeC(buf, 0); //unk +
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeC(buf, 0); //unk +
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeC(buf, 0); //unk +
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				break;
			case 2:
				// surrender
				writeD(buf, pet.getPetId());
				writeD(buf, pet.getUid()); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				break;
			case 3:
				// spawn
				writeS(buf, pet.getName());
				writeD(buf, pet.getPetId());
				writeD(buf, pet.getUid());
				
				if(pet.getX1() == 0 && pet.getY1() == 0 && pet.getZ1() == 0)
				{
					writeF(buf, pet.getMaster().getX());
					writeF(buf, pet.getMaster().getY());
					writeF(buf, pet.getMaster().getZ());
					
					writeF(buf, pet.getMaster().getX());
					writeF(buf, pet.getMaster().getY());
					writeF(buf, pet.getMaster().getZ());
					
					writeC(buf, pet.getMaster().getHeading());
				}
				else
				{
					writeF(buf, pet.getX1());
					writeF(buf, pet.getY1());
					writeF(buf, pet.getZ1());
					
					writeF(buf, pet.getX2());
					writeF(buf, pet.getY2());
					writeF(buf, pet.getZ2());
					
					writeC(buf, pet.getH());
				}
				
				writeD(buf, pet.getMaster().getObjectId()); //unk
				writeC(buf, 1); //unk
				writeD(buf, 0); //unk
				
				writeD(buf, pet.getDecoration());
				writeD(buf, 0); //wings ID if customize_attach = 1
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				break;
			case 4:
				// dismiss
				writeD(buf, petUniqueId);
				writeC(buf, 0x01);
				break;
			case 9:
				// feed
				writeH(buf, 0x01);
				writeC(buf, 0x01);
				writeC(buf, feedActionId);
				final int state = 0x05; // dynamic value, unknown; seen 0x04, 0x05 and 0x0D
				switch(feedActionId)
				{
					case 1:
						// eat
						writeC(buf, state);
						writeC(buf, pet.getExp());
						writeC(buf, pet.getLoveCount());
						writeC(buf, pet.getFeedCount());
						writeD(buf, 0x00);
						writeD(buf, foodObjId);
						writeD(buf, foodAmount);
						break;
					case 2:
						// eating successful
						writeC(buf, state);
						writeC(buf, pet.getExp());
						writeC(buf, pet.getLoveCount());
						writeC(buf, pet.getFeedCount());
						writeD(buf, 0x00);
						writeD(buf, foodObjId);
						writeD(buf, foodAmount);
						writeD(buf, 0x00);
						break;
					case 3:
						// non eatable item
						writeC(buf, state);
						writeC(buf, pet.getExp());
						writeC(buf, pet.getLoveCount());
						writeC(buf, pet.getFeedCount());
						writeD(buf, 0x00);
						break;
					case 4:
						// cancel feed
						writeC(buf, state);
						writeC(buf, pet.getExp());
						writeC(buf, pet.getLoveCount());
						writeC(buf, pet.getFeedCount());
						writeD(buf, 0x00);
						break;
					case 5:
						// clean feed task; before reward set exp, love and count to zero
						writeC(buf, state);
						writeC(buf, pet.getExp());
						writeC(buf, pet.getLoveCount());
						writeC(buf, pet.getFeedCount());
						writeD(buf, 0x00);
						break;
					case 6:
						// give item
						writeC(buf, state);
						writeC(buf, 0x00);
						writeC(buf, 0x00);
						writeC(buf, 0x00);
						writeD(buf, 0x00);
						writeD(buf, foodObjId); // item id of reward
						writeC(buf, 0x00);
						break;
					case 7:
						// present notification
						writeC(buf, state);
						writeC(buf, 0x00);
						writeC(buf, 0x00);
						writeC(buf, 0x00);
						writeD(buf, 600); //remaining time (10min)
						writeD(buf, foodObjId);
						writeD(buf, 0x00);
						break;
					case 8:
						// not hungry
						writeC(buf, state);
						writeC(buf, 0x00);
						writeC(buf, 0x00);
						writeC(buf, 0x00);
						writeD(buf, pet.getFullRemainingTime()); //remaining time
						writeD(buf, foodObjId);
						writeD(buf, foodAmount);
						break;
				}
				break;
			case 10:
				// rename
				writeD(buf, petUniqueId);
				writeS(buf, petName);
				break;
			default:
				break;
		}
	}
}
