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