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

package chatserver.common.netty;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

public abstract class BaseClientPacket extends AbstractPacket
{
	private static final Logger log = Logger.getLogger(BaseClientPacket.class);
	private ChannelBuffer buf;
	
	/**
	 * @param channelBuffer
	 * @param opCode
	 */
	public BaseClientPacket(ChannelBuffer channelBuffer, int opCode)
	{
		super(opCode);
		this.buf = channelBuffer;
	}
	
	public int getRemainingBytes()
	{
		return buf.readableBytes();
	}
	
	/**
	 * Perform packet read
	 * 
	 * @return boolean
	 */
	public boolean read()
	{
		try
		{
			readImpl();
			if(getRemainingBytes() > 0)
				log.debug("Packet " + this + " not fully readed!");
			return true;
		}catch(Exception ex)
		{
			log.error("Reading failed for packet " + this, ex);
			return false;
		}
		
	}
	
	/**
	 * Perform packet action
	 */
	public void run()
	{
		try
		{
			runImpl();
		}catch(Exception ex)
		{
			log.error("Running failed for packet " + this, ex);
		}		
	}
	
	protected abstract void readImpl();

	protected abstract void runImpl();
	
	/**
	 * Read int from this packet buffer.
	 * 
	 * @return int
	 */
	protected final int readD()
	{
		try
		{
			return buf.readInt();
		}
		catch (Exception e)
		{
			log.error("Missing D for: " + this);
		}
		return 0;
	}

	/**
	 * Read byte from this packet buffer.
	 * 
	 * @return int
	 */
	protected final int readC()
	{
		try
		{
			return buf.readByte() & 0xFF;
		}
		catch (Exception e)
		{
			log.error("Missing C for: " + this);
		}
		return 0;
	}

	/**
	 * Read short from this packet buffer.
	 * 
	 * @return int
	 */
	protected final int readH()
	{
		try
		{
			return buf.readShort() & 0xFFFF;
		}
		catch (Exception e)
		{
			log.error("Missing H for: " + this);
		}
		return 0;
	}
	
	/**
	 * Read double from this packet buffer.
	 * 
	 * @return double
	 */
	protected final double readDF()
	{
		try
		{
			return buf.readDouble();
		}
		catch (Exception e)
		{
			log.error("Missing DF for: " + this);
		}
		return 0;
	}


	/**
	 * Read double from this packet buffer.
	 * 
	 * @return double
	 */
	protected final float readF()
	{
		try
		{
			return buf.readFloat();
		}
		catch (Exception e)
		{
			log.error("Missing F for: " + this);
		}
		return 0;
	}

	/**
	 * Read long from this packet buffer.
	 * 
	 * @return long
	 */
	protected final long readQ()
	{
		try
		{
			return buf.readLong();
		}
		catch (Exception e)
		{
			log.error("Missing Q for: " + this);
		}
		return 0;
	}

	/**
	 * Read String from this packet buffer.
	 * 
	 * @return String
	 */
	protected final String readS()
	{
		StringBuffer sb = new StringBuffer();
		char ch;
		try
		{
			while ((ch = buf.readChar()) != 0)
				sb.append(ch);
		}
		catch (Exception e)
		{
			log.error("Missing S for: " + this);
		}
		return sb.toString();

	}

	/**
	 * Read n bytes from this packet buffer, n = length.
	 * 
	 * @param length
	 * @return byte[]
	 */
	protected final byte[] readB(int length)
	{
		byte[] result = new byte[length];
		try
		{
			buf.readBytes(result);
		}
		catch (Exception e)
		{
			log.error("Missing byte[] for: " + this);
		}
		return result;
	}
}