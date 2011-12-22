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

import java.io.File;
import commons.scripting.impl.ScriptContextImpl;

/**
 * This class is script context provider. We can switch to any other ScriptContext implementation later, so it's good to
 * have factory class
 */
public final class ScriptContextFactory
{
	/**
	 * Creates script context, sets the root context. Adds child context if needed
	 * 
	 * @param root
	 *           file that will be threated as root for compiler
	 * @param parent
	 *           parent of new ScriptContext
	 * @return ScriptContext with presetted root file
	 * @throws InstantiationException
	 *            if java compiler is not aviable
	 */
	public static ScriptContext getScriptContext(File root, ScriptContext parent) throws InstantiationException
	{
		ScriptContextImpl ctx;
		if(parent == null)
		{
			ctx = new ScriptContextImpl(root);
		}
		else
		{
			ctx = new ScriptContextImpl(root, parent);
			parent.addChildScriptContext(ctx);
		}
		return ctx;
	}
}