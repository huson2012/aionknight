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

package loginserver.dao;

import loginserver.model.AccountTime;
import commons.database.dao.DAO;

/**
 * DAO to manage account time
 */
public abstract class AccountTimeDAO implements DAO {
    /**
     * Updates @link loginserver.model.AccountTime data of account
     *
     * @param accountId
     *           account id
     * @param accountTime
     *           account time set
     * @return was update successfull or not
     */
    public abstract boolean updateAccountTime(int accountId, AccountTime accountTime);

    /**
     * Updates @link loginserver.model.AccountTime data of account
     *
     * @param accountId
     * @return AccountTime
     */
    public abstract AccountTime getAccountTime(int accountId);

    /**
     * Returns uniquire class name for all implementations
     *
     * @return uniquire class name for all implementations
     */
    @Override
    public final String getClassName()
    {
        return AccountTimeDAO.class.getName();
    }
}