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

package commons.objects.filter;

/**
 * This filter is used to combine a few ObjectFilters into one. Its acceptObject method returns true only if all
 * filters, that were passed through constructor return true
 * @param <T>
 */
public class AndObjectFilter<T> implements ObjectFilter<T>
{
	/** All filters that are used when running acceptObject() method */
	private ObjectFilter<? super T>[]	filters;

	/**
	 * Constructs new <tt>AndObjectFilter</tt> object, that uses given filters.
	 * 
	 * @param filters
	 */
	public AndObjectFilter(ObjectFilter<? super T>... filters)
	{
		this.filters = filters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean acceptObject(T object)
	{
		for(ObjectFilter<? super T> filter : filters)
		{
			if(filter != null && !filter.acceptObject(object))
				return false;
		}
		return true;
	}
}