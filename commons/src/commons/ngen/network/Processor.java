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
import java.nio.ByteOrder;
import org.apache.log4j.Logger;

public abstract class Processor extends Thread
{
	private boolean      idle;
	private boolean      running;
	protected Logger     log;
	protected ByteBuffer buffer;
	
	public Processor (String name, boolean direct) throws IOException
	{
		super(name);
		
		this.idle = true;
		this.running = false;
		if (direct)
		{
			this.buffer = ByteBuffer.allocateDirect(8192 * 2).order(ByteOrder.LITTLE_ENDIAN);
		}
		else
		{
			this.buffer = ByteBuffer.allocate(8192 * 2).order(ByteOrder.LITTLE_ENDIAN);
		}
		this.log = Logger.getLogger(getClass());
	}
	
	public Processor (String name) throws IOException
	{
		this (name, false);
	}
	
	abstract void manage (Connection conn) throws RuntimeException;
	abstract int getNumberOfConnections ();
	
	public void close ()
	{
		running = false;
	}
	
	public boolean isIdle ()
	{
		return idle;
	}
	
	protected void imIdle ()
	{
		this.idle = true;
	}
	
	protected void imBusy ()
	{
		this.idle = false;
	}
	
	protected void imRunning ()
	{
		this.running = true;
	}
	
	protected boolean running ()
	{
		return this.running;
	}
}