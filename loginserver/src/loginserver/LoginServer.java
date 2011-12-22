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

package loginserver;

import loginserver.configs.Config;
import loginserver.controller.BannedIpController;
import loginserver.network.IOServer;
import loginserver.network.ncrypt.KeyGen;
import loginserver.utils.DeadLockDetector;
import loginserver.utils.ThreadPoolManager;
import loginserver.utils.Util;
import org.apache.log4j.Logger;
import commons.database.DatabaseFactory;
import commons.database.dao.DAOManager;
import commons.services.LoggingService;
import commons.utils.AEInfos;
import commons.utils.ExitCode;

public class LoginServer
{
    private static final Logger	log = Logger.getLogger(LoginServer.class);
    public static void main(String[] args)
    {
    	long start = System.currentTimeMillis();
    	
        LoggingService.init();
		Config.load();

		Util.printSection("DB Manager");
		DatabaseFactory.init("./config/loginserver.ini");
		DAOManager.init();

        new DeadLockDetector(60, DeadLockDetector.RESTART).start();
        ThreadPoolManager.getInstance();

        try
        {
        	Util.printSection("Key Manager");
            KeyGen.init();
        }
        catch (Exception e)
        {
            log.fatal("Failed initializing Key Generator. Reason: " + e.getMessage(), e);
            System.exit(ExitCode.CODE_ERROR);
        }

        Util.printSection("GS Manager");
        GameServerTable.load();
		
        Util.printSection("Ban Manager");
        BannedIpController.load();

        Util.printSection("I/O Manager");
        IOServer.getInstance().connect();
        Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

        Util.printSection("System Manager");
        AEInfos.printAllInfos();
        
        Util.printSection("Log Manager");
        log.info("Server started in " + (System.currentTimeMillis() - start) / 1000 + " sec.");
    }
}