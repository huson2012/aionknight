/**
 * ������� �������� �� ������� ������������� 'Aion-Knight Dev. Team' �������� ��������� 
 * ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� 
 * ������������ ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� 
 * ����� ������� ������.
 *
 * ��������� ���������������� � �������, ��� ��� ����� ��������, �� ��� ����� �� �� �� ���� 
 * ����������� ������������; ���� ��� ���������  �����������  ������������, ��������� � 
 * ���������������� ���������� � ������������ ��� ������������ �����. ��� ������������ �������� 
 * ����������� ������������ �������� GNU.
 * 
 * �� ������ ���� �������� ����� ����������� ������������ �������� GNU ������ � ���� ����������. 
 * ���� ��� �� ���, �������� � ���� ���������� �� (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * ���-c��� ������������� : http://aion-knight.ru
 * ��������� ������� ���� : Aion 2.7 - '����� ������' (������)
 * ������ ��������� ����� : Aion-Knight 2.7 (Beta version)
 */

package commons.scripting;

import java.io.File;

/**
 * This interface reperesents common functionality list that should be available for any commpiler that is going to be
 * used with scripting engine. For instance, groovy can be used, hoever it produces by far not the best bytecode so by
 * default javac from sun is used.
 */
public interface ScriptCompiler
{
	/**
	 * Sets parent class loader for this compiler.<br>
	 * <br>
	 * <font color="red">Warning, for now only</font>
	 * 
	 * @param classLoader
	 *           ScriptClassLoader that will be used as parent
	 */
	public void setParentClassLoader(ScriptClassLoader classLoader);

	/**
	 * List of jar files that are required for compilation
	 * 
	 * @param files
	 *           list of jar files
	 */
	public void setLibraires(Iterable<File> files);

	/**
	 * Compiles single class that is represented as string
	 * 
	 * @param className
	 *           class name
	 * @param sourceCode
	 *           class sourse code
	 * @return {@link commons.scripting.CompilationResult}
	 */
	public CompilationResult compile(String className, String sourceCode);

	/**
	 * Compiles classes that are represented as strings
	 * 
	 * @param className
	 *           class names
	 * @param sourceCode
	 *           class sources
	 * @return {@link commons.scripting.CompilationResult}
	 * @throws IllegalArgumentException
	 *            if number of class names != number of sources
	 */
	public CompilationResult compile(String[] className, String[] sourceCode) throws IllegalArgumentException;

	/**
	 * Compiles list of files
	 * 
	 * @param compilationUnits
	 *           list of files
	 * @return {@link commons.scripting.CompilationResult}
	 */
	public CompilationResult compile(Iterable<File> compilationUnits);

	/**
	 * Returns array of supported file types. This files will be threated as source files.
	 * 
	 * @return array of supported file types.
	 */
	public String[] getSupportedFileTypes();
}