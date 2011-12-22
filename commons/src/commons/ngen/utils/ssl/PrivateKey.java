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

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class PrivateKey {
	
	private final static String password = "3O4npw4xC2zDkvnoj6Fv769SlWe/un2AVuNqrGWCJ4M=";
	
	private Key key = null;
	
	private void load (InputStream is) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
	{
        byte[] bytes = new byte[is.available()];
        is.read(bytes, 0, is.available());
        is.close();
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec peks = new PKCS8EncodedKeySpec(bytes);
        key = kf.generatePrivate(peks);
	}
	
	public Key getKey ()
	{
		return key;
	}
	
	public static char[] getPassword ()
	{
		return password.toCharArray();
	}
	
	public void init (String privateKey)
	{
		try 
		{
			InputStream is = ResourceLoader.loadResource(privateKey);
			load(is);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static class SingletonHolder {
		protected static PrivateKey instance = null;
	}
	
	public static final PrivateKey getInstance ()
	{
		if (SingletonHolder.instance == null)
		{
			SingletonHolder.instance = new PrivateKey ();
		}
		return SingletonHolder.instance;
	}
}