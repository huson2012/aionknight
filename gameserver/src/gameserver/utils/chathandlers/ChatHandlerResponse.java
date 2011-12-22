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

package gameserver.utils.chathandlers;

/**
 * Every {@link ChatHandler} as a result returns object of ChatHandlerResponse type. Objects of this class contains dual
 * information:
 * <ul>
 * <li>(maybe) Transformed message in accessible by {@link #getMessage()}</li>
 * <li>information whether handler blocked this message (it means, that it won't be sent to client(s)</li>
 * </ul>
 */
public class ChatHandlerResponse
{
	/** Single instance of <tt>ChatHandlerResponse</tt> representing response with blocked message */
	public static final ChatHandlerResponse	BLOCKED_MESSAGE	= new ChatHandlerResponse(true, "");

	private boolean							messageBlocked;
	private String							message;

	/**
	 * 
	 * @param messageBlocked
	 * @param message
	 */
	public ChatHandlerResponse(boolean messageBlocked, String message)
	{
		this.messageBlocked = messageBlocked;
		this.message = message;
	}

	/**
	 * A message (maybe) changed by handler.
	 * 
	 * @return a message
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * 
	 * @return if true, it means that handler blocked sending this message to client.
	 */
	public boolean isBlocked()
	{
		return messageBlocked;
	}
}
