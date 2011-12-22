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

import gameserver.model.ChatType;
import gameserver.model.gameobjects.player.Player;

/**
 * ChatHandler is called every time when player is trying to send a message using chat. ChatHandler can decide whether
 * message should be send later to players (i.e. admin command handler will block it) and can also change the content of
 * the message ( for example censor may put *** in place of vulgar words)
 */
public interface ChatHandler
{
	/**
	 * This method may check content of message and take proper actions based on it. The message can be changed and also
	 * blocked to forwarding to players.
	 * 
	 * @param chatType
	 * @param message
	 * @param sender
	 * @return response {@link ChatHandlerResponse}
	 */
	public ChatHandlerResponse handleChatMessage(ChatType chatType, String message, Player sender);
}
