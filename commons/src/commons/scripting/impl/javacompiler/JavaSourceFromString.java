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

package commons.scripting.impl.javacompiler;

import java.net.URI;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

/**
 * This class allows us to compile sources that are located only in memory.
 * 
 * @author SoulKeeper
 */
public class JavaSourceFromString extends SimpleJavaFileObject
{
	/**
	 * Source code of the class
	 */
	private final String	code;

	/**
	 * Creates new object that contains sources of java class
	 * 
	 * @param className
	 *           class name of class
	 * @param code
	 *           source code of class
	 */
	public JavaSourceFromString(String className, String code)
	{
		super(URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension),
			JavaFileObject.Kind.SOURCE);
		this.code = code;
	}

	/**
	 * Returns class source code
	 * 
	 * @param ignoreEncodingErrors
	 *           not used
	 * @return class source code
	 */
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors)
	{
		return code;
	}
}