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

package commons.scripting;

import java.util.Arrays;

/**
 * This class represents compilation result of script context
 * 
 * @author SoulKeeper
 */
public class CompilationResult
{
	/**
	 * List of classes that were compiled by compiler
	 */
	private final Class<?>[]			compiledClasses;

	/**
	 * Classloader that was used to load classes
	 */
	private final ScriptClassLoader	classLoader;

	/**
	 * Creates new instance of CompilationResult with classes that has to be parsed and classloader that was used to
	 * load classes
	 * 
	 * @param compiledClasses
	 *           classes compiled by compiler
	 * @param classLoader
	 *           classloader that was used by compiler
	 */
	public CompilationResult(Class<?>[] compiledClasses, ScriptClassLoader classLoader)
	{
		this.compiledClasses = compiledClasses;
		this.classLoader = classLoader;
	}

	/**
	 * Returns classLoader that was used by compiler
	 * 
	 * @return classloader that was used by compiler
	 */
	public ScriptClassLoader getClassLoader()
	{
		return classLoader;
	}

	/**
	 * Retunrs list of classes that were compiled
	 * 
	 * @return list of classes that were compiled
	 */
	public Class<?>[] getCompiledClasses()
	{
		return compiledClasses;
	}

	/** {@inheritDoc} */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append("CompilationResult");
		sb.append("{classLoader=").append(classLoader);
		sb.append(", compiledClasses=").append(
			compiledClasses == null ? "null" : Arrays.asList(compiledClasses).toString());
		sb.append('}');
		return sb.toString();
	}
}