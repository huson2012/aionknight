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