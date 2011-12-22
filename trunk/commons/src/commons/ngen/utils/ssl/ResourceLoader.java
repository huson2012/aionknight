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

package commons.ngen.utils.ssl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class ResourceLoader
{
	public static InputStream loadResource (String name)
	{
		InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(name);
		DataInputStream dis = null;
		if (is==null)
		{
			FileInputStream fis;
			try {
				fis = new FileInputStream(name);
				dis = new DataInputStream(fis);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				dis = new DataInputStream(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
		    byte[] bytes = new byte[dis.available()];
	        dis.readFully(bytes);
	        is = new ByteArrayInputStream(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
}