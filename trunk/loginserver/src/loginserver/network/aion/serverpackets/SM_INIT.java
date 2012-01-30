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

package loginserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import javax.crypto.SecretKey;
import loginserver.network.aion.AionConnection;
import loginserver.network.aion.AionServerPacket;

/**
 * Format: dd b dddd s d: session id d: protocol revision b: 0x90 bytes : 0x80 bytes for the scrambled RSA public key
 * 0x10 bytes at 0x00 d: unknow d: unknow d: unknow d: unknow s: blowfish key
 */
public final class SM_INIT extends AionServerPacket
{
    /**
     * Session Id of this connection
     */
    private final int sessionId;

    /**
     * public Rsa key that client will use to encrypt login and password that will be send in RequestAuthLogin client
     * packet.
     */
    private final byte[] publicRsaKey;
    /**
     * blowfish key for packet encryption/decryption.
     */
    private final byte[] blowfishKey;

    /**
     * Constructor
     *
     * @param client
     * @param blowfishKey
     */
    public SM_INIT(AionConnection client, SecretKey blowfishKey)
    {
        this(client.getEncryptedModulus(), blowfishKey.getEncoded(), client.getSessionId());
    }

    /**
     * Creates new instance of <tt>SM_INIT</tt> packet.
     *
     * @param publicRsaKey Public RSA key
     * @param blowfishKey  Blowfish key
     * @param sessionId    Session identifier
     */
    private SM_INIT(byte[] publicRsaKey, byte[] blowfishKey, int sessionId)
    {
        super(0x00);

        this.sessionId = sessionId;
        this.publicRsaKey = publicRsaKey;
        this.blowfishKey = blowfishKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void writeImpl(AionConnection con, ByteBuffer buf)
    {
        writeC(buf, getOpcode()); // init packet id

        writeD(buf, sessionId); // session id
        writeD(buf, 0x0000c621); // protocol revision
        writeB(buf, publicRsaKey); // RSA Public Key
        // unk
        writeD(buf, 0x00);
        writeD(buf, 0x00);
        writeD(buf, 0x00);
        writeD(buf, 0x00);

        writeB(buf, blowfishKey); // BlowFish key
        writeD(buf, 197635); // unk
        writeD(buf, 2097152); // unk

    }
}
