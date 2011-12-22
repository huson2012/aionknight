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

package commons.ngen.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.log4j.Logger;

public abstract class Connection
{
	private static final Logger _log = Logger.getLogger(Connection.class);
	protected Logger log;
	private SocketChannel sc;
	private Writer writer;
	public boolean initialized = false;
	private boolean alreadyClosed = false;
	private boolean closeInProgress = false;
	protected boolean pendingClose = false;
	protected boolean isForcedClosing = false;
	private String src;
	private Mode mode = Mode.BINARY;
	protected boolean debugEnabled = false;
	
	public enum Mode {
		TEXT,
		BINARY
	}
	
	public Connection (SocketChannel sc, boolean debugEnabled)
	{
		this.sc     = sc;
		this.writer = null;
		this.src = sc.socket().getInetAddress().getHostAddress()+":"+sc.socket().getPort();
		this.log = Logger.getLogger(getClass());
		this.debugEnabled = debugEnabled;
	}
	
	public Connection (SocketChannel sc, boolean debugEnabled, Mode mode)
	{
		this(sc, debugEnabled);
		this.mode = mode;
	}
	
	public void setWriter (Writer writer)
	{
		this.writer = writer;
	}
	
	public SocketChannel channel ()
	{
		return sc;
	}
	
	protected void enableWriteInterest ()
	{
		if (alreadyClosed || closeInProgress)
			return;
		
		if (!initialized)
		{
			synchronized (this)
			{
				try { wait(); } catch (Exception e) { }
			}
		}
		
		writer.wakeup(this);
	}
	
	public Mode getMode ()
	{
		return mode;
	}
	
	protected boolean isWriteDisabled ()
	{
		return alreadyClosed || closeInProgress;
	}
	
	public void close (boolean serverClose)
	{
		if (alreadyClosed || closeInProgress)
			return;
		
		closeInProgress = true;
		
		writer.remove();
		
		if (sc.isOpen())
		{
			try {
				sc.socket().shutdownOutput();
				sc.close();
			}
			catch (IOException e)
			{
				if (debugEnabled)
					_log.error("IOException on closing socket for connection "+this, e);
			}
		}

		try {
			if (serverClose)
			{
				onServerClose();
			}
			else
			{
				onDisconnect();
			}
		} catch (Exception e) {
			if (debugEnabled)
				_log.error(e.getClass().getSimpleName()+" while closing connection "+this, e);
		}
		
		alreadyClosed = true;
	}
	
	public String getSource ()
	{
		return src;
	}
	
	public String toString ()
	{
		return src;
	}
	
	public boolean isPendingClose ()
	{
		return pendingClose;
	}
	
	abstract protected void onInit ();	
	abstract protected void onDisconnect ();	
	abstract protected void onServerClose ();
	abstract protected boolean processData(ByteBuffer data);	
	abstract protected boolean writeData(ByteBuffer data);
}