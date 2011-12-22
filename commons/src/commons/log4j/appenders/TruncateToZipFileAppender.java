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

package commons.log4j.appenders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.helpers.LogLog;
import commons.log4j.exceptions.AppenderInitializationError;

public class TruncateToZipFileAppender extends FileAppender
{
	private String backupDir = "log/backup";
	private String backupDateFormat	= "yyyy-MM-dd HH-mm-ss";

	@Override
	public void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException
	{
		if(!append)
		{
			truncate(new File(fileName));
		}

		super.setFile(fileName, append, bufferedIO, bufferSize);
	}
	protected void truncate(File file)
	{
		LogLog.debug("Compression of file: " + file.getAbsolutePath() + " started.");

		if(FileUtils.isFileOlder(file, ManagementFactory.getRuntimeMXBean().getStartTime()))
		{
			File backupRoot = new File(getBackupDir());
			if(!backupRoot.exists() && !backupRoot.mkdirs())
			{
				throw new AppenderInitializationError("Can't create backup dir for backup storage");
			}

			SimpleDateFormat df;
			try
			{
				df = new SimpleDateFormat(getBackupDateFormat());
			}
			catch(Exception e)
			{
				throw new AppenderInitializationError(
					"Invalid date formate for backup files: " + getBackupDateFormat(), e);
			}
			String date = df.format(new Date(file.lastModified()));

			File zipFile = new File(backupRoot, file.getName() + "." + date + ".zip");

			ZipOutputStream zos = null;
			FileInputStream fis = null;
			try
			{
				zos = new ZipOutputStream(new FileOutputStream(zipFile));
				ZipEntry entry = new ZipEntry(file.getName());
				entry.setMethod(ZipEntry.DEFLATED);
				entry.setCrc(FileUtils.checksumCRC32(file));
				zos.putNextEntry(entry);
				fis = FileUtils.openInputStream(file);

				byte[] buffer = new byte[1024];
				int readed;
				while((readed = fis.read(buffer)) != -1)
				{
					zos.write(buffer, 0, readed);
				}

			}
			catch(Exception e)
			{
				throw new AppenderInitializationError("Can't create zip file", e);
			}
			finally
			{
				if(zos != null)
				{
					try
					{
						zos.close();
					}
					catch(IOException e)
					{

						LogLog.warn("Can't close zip file", e);
					}
				}

				if(fis != null)
				{
					try
					{

						fis.close();
					}
					catch(IOException e)
					{
						LogLog.warn("Can't close zipped file", e);
					}
				}
			}

			if(!file.delete())
			{
				throw new AppenderInitializationError("Can't delete old log file " + file.getAbsolutePath());
			}
		}
	}

	public String getBackupDir()
	{
		return backupDir;
	}

	public void setBackupDir(String backupDir)
	{
		this.backupDir = backupDir;
	}

	public String getBackupDateFormat()
	{
		return backupDateFormat;
	}

	public void setBackupDateFormat(String backupDateFormat)
	{
		this.backupDateFormat = backupDateFormat;
	}
}