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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import com.sun.tools.javac.file.BaseFileObject;

/**
 * This class is just a hack to make javac compiler work with classes loaded by prevoius classloader. Also it's used as
 * container for loaded class
 */
public class BinaryClass extends BaseFileObject
{

	/**
	 * ClassName
	 */
	private final String				name;

	/**
	 * Class data will be written here
	 */
	private final ByteArrayOutputStream	baos	= new ByteArrayOutputStream();

	/**
	 * Locaded class will be set here
	 */
	private Class<?>					definedClass;

	/**
	 * Constructor that accepts class name as parameter
	 * 
	 * @param name
	 *           class name
	 */
	protected BinaryClass(String name)
	{
		super(null);
		this.name = name;
	}

	/**
	 * Throws {@link UnsupportedOperationException}
	 * 
	 * @return nothing
	 */
	@Override
	public URI toUri()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns name of this class with ".class" suffix
	 * 
	 * @return name of this class with ".class" suffix
	 * 
	 * @deprecated
	 */
	@Deprecated
	@Override
	public String getName()
	{
		return name + ".class";
	}

	/**
	 * Creates new ByteArrayInputStream, it just wraps class binary data
	 * 
	 * @return input stream for class data
	 * @throws IOException
	 *            never thrown
	 */
	@Override
	public InputStream openInputStream() throws IOException
	{
		return new ByteArrayInputStream(baos.toByteArray());
	}

	/**
	 * Opens ByteArrayOutputStream for class data
	 * 
	 * @return output stream
	 * @throws IOException
	 *            never thrown
	 */
	@Override
	public OutputStream openOutputStream() throws IOException
	{
		return baos;
	}

	/**
	 * Throws {@link UnsupportedOperationException}
	 * 
	 * @return nothing
	 */
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Throws {@link UnsupportedOperationException}
	 * 
	 * @return nothing
	 */
	@Override
	public Writer openWriter() throws IOException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported operation, always reutrns 0
	 * 
	 * @return 0
	 */
	@Override
	public long getLastModified()
	{
		return 0;
	}

	/**
	 * Unsupported operation, returns false
	 * 
	 * @return false
	 */
	@Override
	public boolean delete()
	{
		return false;
	}

	/**
	 * Returns class name
	 * 
	 * @param path
	 *           doesn't matter
	 * @return class name
	 */
	@Override
	protected String inferBinaryName(Iterable<? extends File> path)
	{
		return name;
	}

	/**
	 * Returns true if {@link javax.tools.JavaFileObject.Kind#CLASS}
	 * 
	 * @param simpleName
	 *           doesn't matter
	 * @param kind
	 *           kind to compare
	 * @return true if Kind is {@link javax.tools.JavaFileObject.Kind#CLASS}
	 */
	@Override
	public boolean isNameCompatible(String simpleName, Kind kind)
	{
		return Kind.CLASS.equals(kind);
	}

	/**
	 * Returns bytes of class
	 * 
	 * @return bytes of class
	 */
	public byte[] getBytes()
	{
		return baos.toByteArray();
	}

	/**
	 * Returns class that was loaded from binary data of this object
	 * 
	 * @return loaded class
	 */
	public Class<?> getDefinedClass()
	{
		return definedClass;
	}

	/**
	 * Sets class that was loaded by this object
	 * 
	 * @param definedClass
	 *           class that was loaded
	 */
	public void setDefinedClass(Class<?> definedClass)
	{
		this.definedClass = definedClass;
	}
}
