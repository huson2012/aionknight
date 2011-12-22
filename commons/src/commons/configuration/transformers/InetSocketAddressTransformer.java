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

package commons.configuration.transformers;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import commons.configuration.PropertyTransformer;
import commons.configuration.TransformationException;

public class InetSocketAddressTransformer implements PropertyTransformer<InetSocketAddress>
{
	public static final InetSocketAddressTransformer	SHARED_INSTANCE	= new InetSocketAddressTransformer();

	@Override
	public InetSocketAddress transform(String value, Field field) throws TransformationException
	{
		String[] parts = value.split(":");

		if(parts.length != 2)
		{
			throw new TransformationException("Can't transform property, must be in format \"address:port\"");
		}

		try
		{
			if("*".equals(parts[0]))
			{
				return new InetSocketAddress(Integer.parseInt(parts[1]));
			}
			else
			{
				InetAddress address = InetAddress.getByName(parts[0]);
				int port = Integer.parseInt(parts[1]);
				return new InetSocketAddress(address, port);
			}
		}
		catch(Exception e)
		{
			throw new TransformationException(e);
		}
	}
}