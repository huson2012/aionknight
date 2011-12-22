/**
 * �������� �������� ������� Aion 2.7 �� ������� ������������� 'Aion-Knight Dev. Team' �������� 
 * ��������� ����������� ������������; �� ������ �������������� �/��� �������� ��� �������� �������� 
 * ����������� ������������ �������� GNU (GNU GPL), �������������� ������ ���������� ������������ 
 * ����������� (FSF), ���� �������� ������ 3, ���� (�� ���� ����������) ����� ����� ������� 
 * ������.
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

package gameserver.utils;

import commons.utils.AEInfos;
import commons.versionning.Version;
import gameserver.GameServer;
import org.apache.log4j.Logger;

public class AEVersions
{
	private static final Logger log = Logger.getLogger(AEVersions.class);
	private static final Version commons = new Version(AEInfos.class);
	private static final Version gameserver	= new Version(GameServer.class);
	private static String getRevisionInfo(Version version)
	{
		return String.format("%-6s", version.getRevision());
	}

	private static String getDateInfo(Version version)
	{
		return String.format("[ %4s ]", version.getDate());
	}

	public static String[] getFullVersionInfo()
	{
		return new String[] { 
			"Commons Revision: " + getRevisionInfo(commons),
			"Commons Build Date: " + getDateInfo(commons), 
			"==================================================",
			"GS Revision: " + getRevisionInfo(gameserver),
			"GS Build Date: " + getDateInfo(gameserver),
			"=================================================="
		};
	}

	public static void printFullVersionInfo()
	{
		for(String line : getFullVersionInfo())
			log.info(line);
	}
}