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

package commons.utils;

public class Rnd
{
	private static final MTRandom	rnd	= new MTRandom();

	public static float get()
	{
		return rnd.nextFloat();
	}

	public static int get(int n)
	{
		return (int) Math.floor(rnd.nextDouble() * n);
	}

	public static int get(int min, int max)

	{
		return min + (int) Math.floor(rnd.nextDouble() * (max - min + 1));
	}

	public static int nextInt(int n)
	{
		return (int) Math.floor(rnd.nextDouble() * n);
	}

	public static int nextInt()
	{
		return rnd.nextInt();
	}

	public static double nextDouble()
	{
		return rnd.nextDouble();
	}

	public static double nextGaussian()
	{
		return rnd.nextGaussian();
	}

	public static boolean nextBoolean()
	{
		return rnd.nextBoolean();
	}
}