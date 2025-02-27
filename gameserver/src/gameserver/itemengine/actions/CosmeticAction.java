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

package gameserver.itemengine.actions;

import gameserver.model.gameobjects.Item;
import gameserver.model.gameobjects.player.Player;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CosmeticAction")
public class CosmeticAction extends AbstractItemAction
{
	@XmlAttribute
	private String lips;

	@XmlAttribute
	private String eyes;

	@XmlAttribute
	private String face;

	@XmlAttribute
	private String hair;

	@XmlAttribute
	private int hairType;

	@XmlAttribute
	private int faceType;

	@XmlAttribute
	private int tattooType;

	@XmlAttribute
	private int makeupType;

	@XmlAttribute
	private int voiceType;

	@XmlAttribute
	private String preset;

	public void act(Player player, Item parentItem, Item targetItem)
	{
	}

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return false;
	}
	/**
	 * @return the lips
	 */
	public String getLipsColor()
	{
		return lips;
	}
	/**
	 * @return the eyes
	 */
	public String getEyesColor()
	{
		return eyes;
	}
	/**
	 * @return the face
	 */
	public String getFaceColor()
	{
		return face;
	}
	/**
	 * @return the hair
	 */
	public String getHairColor()
	{
		return hair;
	}
	/**
	 * @return the hairType
	 */
	public int getHairType()
	{
		return hairType;
	}
	/**
	 * @return the faceType
	 */
	public int getFaceType()
	{
		return faceType;
	}
	/**
	 * @return the tattooType
	 */
	public int getTattooType()
	{
		return tattooType;
	}
	/**
	 * @return the makeupType
	 */
	public int getMakeupType()
	{
		return makeupType;
	}
	/**
	 * @return the voiceType
	 */
	public int getVoiceType()
	{
		return voiceType;
	}
	/**
	 * @return the preset
	 */
	public String getPresetName()
	{
		return preset;
	}
}