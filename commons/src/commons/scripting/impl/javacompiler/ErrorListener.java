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

import java.util.Locale;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileObject;
import org.apache.log4j.Logger;

/**
 * This class is simple compiler error listener that forwards errors to log4j logger
 */
public class ErrorListener implements DiagnosticListener<JavaFileObject>
{
	/**
	 * Logger for this class
	 */
	private static final Logger	log	= Logger.getLogger(ErrorListener.class);

	/**
	 * Reports compilation errors to log4j
	 * 
	 * @param diagnostic
	 *           compiler errors
	 */
	@Override
	public void report(Diagnostic<? extends JavaFileObject> diagnostic)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("Java Compiler ").append(diagnostic.getKind()).append(": ").append(
			diagnostic.getMessage(Locale.ENGLISH)).append("\n").append("Source: ").append(
			diagnostic.getSource().getName()).append("\n").append("Line: ").append(diagnostic.getLineNumber()).append(
			"\n").append("Column: ").append(diagnostic.getColumnNumber());
		log.error(buffer.toString());
	}
}