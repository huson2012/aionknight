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

import java.io.File;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import commons.scripting.impl.javacompiler.ScriptCompilerImpl;

/**
 * Simple class that represents script info.<br>
 * <br>
 * It contains Script root, list of libraries and list of child contexes
 */
@XmlRootElement(name = "scriptinfo")
@XmlAccessorType(XmlAccessType.NONE)
public class ScriptInfo
{

	/**
	 * Root of this script context. Child directories of root will be scanned for script files
	 */
	@XmlAttribute(required = true)
	private File				root;

	/**
	 * List of libraries of this script context
	 */
	@XmlElement(name = "library")
	private List<File>			libraries;

	/**
	 * List of child contexts
	 */
	@XmlElement(name = "scriptinfo")
	private List<ScriptInfo>	scriptInfos;

	/**
	 * Default compiler class name.
	 */
	@XmlElement(name = "compiler")
	private String				compilerClass	= ScriptCompilerImpl.class.getName();

	/**
	 * Returns root of script context
	 * 
	 * @return root of script context
	 */
	public File getRoot()
	{
		return root;
	}

	/**
	 * Sets root for script context
	 * 
	 * @param root
	 *           root for script context
	 */
	public void setRoot(File root)
	{
		this.root = root;
	}

	/**
	 * Returns list of libraries that will be used byscript context and it's children
	 * 
	 * @return lib of libraries
	 */
	public List<File> getLibraries()
	{
		return libraries;
	}

	/**
	 * Sets list of libraries that will be used by script context and it's children
	 * 
	 * @param libraries
	 *           sets list of libraries
	 */
	public void setLibraries(List<File> libraries)
	{
		this.libraries = libraries;
	}

	/**
	 * Return list of child context descriptors
	 * 
	 * @return list of child context descriptors
	 */
	public List<ScriptInfo> getScriptInfos()
	{
		return scriptInfos;
	}

	/**
	 * Sets list of child context descriptors
	 * 
	 * @param scriptInfos
	 *           list of child context descriptors
	 */
	public void setScriptInfos(List<ScriptInfo> scriptInfos)
	{
		this.scriptInfos = scriptInfos;
	}

	/**
	 * Returns compiler class name
	 * 
	 * @return name of compiler class
	 */
	public String getCompilerClass()
	{
		return compilerClass;
	}

	/**
	 * Sets compiler class name
	 * 
	 * @param compilerClass
	 *           name of compiler class
	 */
	public void setCompilerClass(String compilerClass)
	{
		this.compilerClass = compilerClass;
	}

	/**
	 * Returns true if roots are quals
	 * 
	 * @param o
	 *           object to compare with
	 * @return true if this ScriptInfo and anothers ScriptInfo has same root
	 */
	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(o == null || getClass() != o.getClass())
			return false;

		ScriptInfo that = (ScriptInfo) o;

		return root.equals(that.root);

	}

	/**
	 * Returns hashcode of root
	 * 
	 * @return hashcode of root
	 */
	@Override
	public int hashCode()
	{
		return root.hashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("ScriptInfo");
		sb.append("{root=").append(root);
		sb.append(", libraries=").append(libraries);
		sb.append(", compilerClass='").append(compilerClass).append('\'');
		sb.append(", scriptInfos=").append(scriptInfos);
		sb.append('}');
		return sb.toString();
	}
}