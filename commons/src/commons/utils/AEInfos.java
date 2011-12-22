/**
 * Игровой эмулятор от команды разработчиков 'Aion-Knight Dev. Team' является свободным 
 * программным обеспечением; вы можете распространять и/или изменять его согласно условиям 
 * Стандартной Общественной Лицензии GNU (GNU GPL), опубликованной Фондом свободного 
 * программного обеспечения (FSF), либо Лицензии версии 3, либо (на ваше усмотрение) любой 
 * более поздней версии.
 *
 * Программа распространяется в надежде, что она будет полезной, но БЕЗ КАКИХ БЫ ТО НИ БЫЛО 
 * ГАРАНТИЙНЫХ ОБЯЗАТЕЛЬСТВ; даже без косвенных  гарантийных  обязательств, связанных с 
 * ПОТРЕБИТЕЛЬСКИМИ СВОЙСТВАМИ и ПРИГОДНОСТЬЮ ДЛЯ ОПРЕДЕЛЕННЫХ ЦЕЛЕЙ. Для подробностей смотрите 
 * Стандартную Общественную Лицензию GNU.
 * 
 * Вы должны были получить копию Стандартной Общественной Лицензии GNU вместе с этой программой. 
 * Если это не так, напишите в Фонд Свободного ПО (Free Software Foundation, Inc., 675 Mass Ave, 
 * Cambridge, MA 02139, USA
 * 
 * Веб-cайт разработчиков : http://aion-knight.ru
 * Поддержка клиента игры : Aion 2.7 - 'Арена Смерти' (Иннова)
 * Версия серверной части : Aion-Knight 2.7 (Beta version)
 */

package commons.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

public class AEInfos
{
	private static final Logger log	= Logger.getLogger(AEInfos.class);
	public static String[] getMemoryInfo()
	{
		double max = Runtime.getRuntime().maxMemory() / 1024; // maxMemory is the upper limit the jvm can use
		double allocated = Runtime.getRuntime().totalMemory() / 1024; // totalMemory the size of the current allocation pool
		double nonAllocated = max - allocated; // non allocated memory till jvm limit
		double cached = Runtime.getRuntime().freeMemory() / 1024; // freeMemory the unused memory in the allocation pool
		double used = allocated - cached; // really used memory
		double useable = max - used; // allocated, but non-used and non-allocated memory
		DecimalFormat df = new DecimalFormat(" (0.0000'%')");
		DecimalFormat df2 = new DecimalFormat(" # 'KB'");
		
		return new String[] {
		    "Global Memory Informations at " + getRealTime().toString() + ":",
			"==================================================",
			"Allowed Memory:" + df2.format(max),
			"Allocated Memory:" + df2.format(allocated) + df.format(allocated / max * 100),
			"Non-Allocated Memory:" + df2.format(nonAllocated) + df.format(nonAllocated / max * 100),
			"Allocated Memory:" + df2.format(allocated),
			"Used Memory:" + df2.format(used) + df.format(used / max * 100),
			"Unused (cached) Memory:" + df2.format(cached) + df.format(cached / max * 100),
			"Useable Memory:" + df2.format(useable) + df.format(useable / max * 100)
		};
	}

	public static String[] getCPUInfo()
	{
		return new String[] {
		    "Avaible CPU: " + Runtime.getRuntime().availableProcessors(),
			"=================================================="
		};
	}

	public static String[] getOSInfo()
	{
		return new String[] {
		    "OS: " + System.getProperty("os.name") + " Build: " + System.getProperty("os.version"),
			"OS Arch: " + System.getProperty("os.arch"),
			"=================================================="
		};
	}
	
	public static String[] getJREInfo()
	{
		return new String[] {
			"Java Platform Information",
			"==================================================",
			"Java Runtime: " + System.getProperty("java.runtime.name"),
			"Java Version: " + System.getProperty("java.version"),
			"Java Class Version: " + System.getProperty("java.class.version"),
			"=================================================="
		};
	}
	
	public static String[] getJVMInfo()
	{
		return new String[] {
			"Virtual Machine Information (JVM)",
			"==================================================",
			"JVM Name: " + System.getProperty("java.vm.name"),
			"JVM Version: " + System.getProperty("java.vm.version"),
			"JVM Vendor: " + System.getProperty("java.vm.vendor"),
			"JVM Info: " + System.getProperty("java.vm.info"),
			"=================================================="
		};
	}

	public static String getRealTime()
	{
		SimpleDateFormat String = new SimpleDateFormat("H:mm:ss");
		return String.format(new Date());
	}
	
	public static void printMemoryInfo()
	{
		for(String line : getMemoryInfo())
			log.info(line);
	}
	
	public static void printCPUInfo()
	{
		for(String line : getCPUInfo())
			log.info(line);
	}
	
	public static void printOSInfo()
	{
		for(String line : getOSInfo())
			log.info(line);
	}
	
	public static void printJREInfo()
	{
		for(String line : getJREInfo())
			log.info(line);
	}
	
	public static void printJVMInfo()
	{
		for(String line : getJVMInfo())
			log.info(line);
	}
	
	public static void printRealTime()
	{
		log.info(getRealTime().toString());
	}
	
	public static void printAllInfos()
	{
		printOSInfo();
		printCPUInfo();
		printJREInfo();
		printJVMInfo();
		printMemoryInfo();
	}
}