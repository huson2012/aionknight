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
 
package chatserver;

import org.apache.log4j.Logger;
import chatserver.configs.Config;
import chatserver.network.netty.NettyServer;
import chatserver.utils.Util;
import commons.services.LoggingService;
import commons.utils.AEInfos;

public class ChatServer
{
	private static final Logger log = Logger.getLogger(ChatServer.class);
	
    public static void main(String[] args)
    {
    	long start = System.currentTimeMillis();
		
        LoggingService.init();
		log.info("Logging Initialized.");
		
        Util.printSection("Config Manager");
		Config.load();
        
		Util.printSection("Netty Manager");
        new NettyServer();	

        Util.printSection("System Manager");
        AEInfos.printAllInfos();
        
        Util.printSection("Log Manager");
        log.info("Total Boot Time: " + (System.currentTimeMillis() - start) / 1000 + " sec.");
    }
}