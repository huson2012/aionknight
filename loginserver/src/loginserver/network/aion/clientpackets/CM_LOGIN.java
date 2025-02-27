/*
 * Emulator game server Aion 2.7 from the command of developers 'Aion-Knight Dev. Team' is
 * free software; you can redistribute it and/or modify it under the terms of
 * GNU affero general Public License (GNU GPL)as published by the free software
 * security (FSF), or to License version 3 or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranties related to
 * CONSUMER PROPERTIES and SUITABILITY FOR CERTAIN PURPOSES. For details, see
 * General Public License is the GNU.
 *
 * You should have received a copy of the GNU affero general Public License along with this program.
 * If it is not, write to the Free Software Foundation, Inc., 675 Mass Ave,
 * Cambridge, MA 02139, USA
 *
 * Web developers : http://aion-knight.ru
 * Support of the game client : Aion 2.7- 'Arena of Death' (Innova)
 * The version of the server : Aion-Knight 2.7 (Beta version)
 */

package loginserver.network.aion.clientpackets;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import loginserver.controller.AccountController;
import loginserver.network.aion.AionAuthResponse;
import loginserver.network.aion.AionClientPacket;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.SessionKey;
import loginserver.network.aion.AionConnection.State;
import loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;
import loginserver.network.aion.serverpackets.SM_LOGIN_OK;
import org.apache.log4j.Logger;

public class CM_LOGIN extends AionClientPacket
{
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(CM_LOGIN.class);

    /**
     * byte array contains encrypted login and password.
     */
    private byte[] data;

    /**
     * Constructs new instance of <tt>CM_LOGIN </tt> packet.
     *
     * @param buf
     * @param client
     */
    public CM_LOGIN(ByteBuffer buf, AionConnection client)
    {
        super(buf, client, 0x0b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void readImpl()
    {
        readD();
        if (getRemainingBytes() >= 163)
        {
            data = readB(163);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void runImpl()
    {
        if (data == null)
        {
            return;
        }

        byte[] decrypted;
        try
        {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.DECRYPT_MODE, getConnection().getRSAPrivateKey());
            decrypted = rsaCipher.doFinal(data, 0, 128);
        }
        catch (GeneralSecurityException e)
        {
            log.warn("Error while decripting data on user auth." + e, e);
            sendPacket(new SM_LOGIN_FAIL(AionAuthResponse.SYSTEM_ERROR));
            return;
        }

        String user = new String(decrypted, 64, 32).trim().toLowerCase();
        String password = new String(decrypted, 96, 32).trim();

        int ncotp = decrypted[0x7c];
        ncotp |= decrypted[0x7d] << 8;
        ncotp |= decrypted[0x7e] << 16;
        ncotp |= decrypted[0x7f] << 24;

        log.debug("AuthLogin: " + user + " pass: " + password + " ncotp: " + ncotp);

        AionConnection client = getConnection();
        AionAuthResponse response = AccountController.login(user, password, client);
        switch (response)
        {
            case AUTHED:
                client.setState(State.AUTHED_LOGIN);
                client.setSessionKey(new SessionKey(client.getAccount()));
                client.sendPacket(new SM_LOGIN_OK(client.getSessionKey()));
                break;

            default:
                client.close(new SM_LOGIN_FAIL(response), true);
                break;
        }
    }
}