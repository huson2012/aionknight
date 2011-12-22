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