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