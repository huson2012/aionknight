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