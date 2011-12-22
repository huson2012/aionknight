/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
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

package commons.scripting.scriptmanager;

import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Root element for script descriptors
 */
@XmlRootElement(name = "scriptlist")
@XmlAccessorType(XmlAccessType.NONE)
public class ScriptList
{
	/**
	 * List of Script descriptors
	 */
	@XmlElement(name = "scriptinfo", type = ScriptInfo.class)
	private Set<ScriptInfo>	scriptInfos;

	/**
	 * Returns list of script descriptors
	 * 
	 * @return list of script descriptors
	 */
	public Set<ScriptInfo> getScriptInfos()
	{
		return scriptInfos;
	}

	/**
	 * Sets list of script descriptors
	 * 
	 * @param scriptInfos
	 *           lisft of script descriptors
	 */
	public void setScriptInfos(Set<ScriptInfo> scriptInfos)
	{
		this.scriptInfos = scriptInfos;
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("ScriptList");
		sb.append("{scriptInfos=").append(scriptInfos);
		sb.append('}');
		return sb.toString();
	}
}