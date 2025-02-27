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

package loginserver.network.aion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import loginserver.configs.Config;
import loginserver.controller.AccountController;
import loginserver.controller.AccountTimeController;
import loginserver.controller.FloodController;
import loginserver.model.Account;
import loginserver.network.aion.serverpackets.SM_INIT;
import loginserver.network.ncrypt.CryptEngine;
import loginserver.network.ncrypt.EncryptedRSAKeyPair;
import loginserver.network.ncrypt.KeyGen;
import org.apache.log4j.Logger;
import commons.network.AConnection;
import commons.network.Dispatcher;
import commons.network.PacketProcessor;

/**
 * Object representing connection between LoginServer and Aion Client.
 */
public class AionConnection extends AConnection
{
    /**
     * Logger for this class.
     */
    private static final Logger log = Logger.getLogger(AionConnection.class);
    /**
     * PacketProcessor for executing packets.
     */
    private final static PacketProcessor<AionConnection> processor = new PacketProcessor<AionConnection>(1, 8);
    private static final Pattern COMPILE = Pattern.compile(",");
    /**
     * Server Packet "to send" Queue
     */
    private final Deque<AionServerPacket> sendMsgQueue = new ArrayDeque<AionServerPacket>();

    /**
     * Unique Session Id of this connection
     */
    private final int sessionId = hashCode();

    /**
     * Account object for this connection. if state = AUTHED_LOGIN account cant be null.
     */
    private Account account;

    /**
     * Crypt to encrypt/decrypt packets
     */
    private CryptEngine cryptEngine;

    /**
     * True if this user is connecting to GS.
     */
    private boolean joinedGs;

    /**
     * Scrambled key pair for RSA
     */
    private EncryptedRSAKeyPair encryptedRSAKeyPair;

    /**
     * Session Key for this connection.
     */
    private SessionKey sessionKey;

    /**
     * Current state of this connection
     */
    private State state;

    /**
     * Possible states of AionConnection
     */
    public static enum State
    {
        /**
         * Means that client just connects
         */
        CONNECTED,

        /**
         * Means that clients GameGuard is authenticated
         */
        AUTHED_GG,

        /**
         * Means that client is logged in.
         */
        AUTHED_LOGIN
    }

    /**
     * Constructor
     *
     * @param sc
     * @param d
     * @throws java.io.IOException
     */
    public AionConnection(SocketChannel sc, Dispatcher d) throws IOException
    {
        super(sc, d);

        state = State.CONNECTED;

        String ip = getIP();

        // Anti-spamm
        List<String> IPsException = Arrays.asList(COMPILE.split(Config.FLOOD_CONTROLLER_EXCEPTIONS));

        if (!FloodController.exist(ip) || !FloodController.checkFlood(ip) || IPsException.contains(ip))
        {
            if (!FloodController.exist(ip))
            {
                FloodController.addIP(ip);
            }
            else
            {
                FloodController.addConnection(ip);
            }

            log.info("connection from: " + ip + "[" + FloodController.getConnection(ip) + "]");

            encryptedRSAKeyPair = KeyGen.getEncryptedRSAKeyPair();

            SecretKey blowfishKey = KeyGen.generateBlowfishKey();

            cryptEngine = new CryptEngine();

            cryptEngine.updateKey(blowfishKey.getEncoded());

            /** Send Init packet */
            sendPacket(new SM_INIT(this, blowfishKey));
        }
    }

    /**
     * Called by Dispatcher. ByteBuffer data contains one packet that should be processed.
     *
     * @param data
     * @return True if data was processed correctly, False if some error occurred and connection should be closed NOW.
     */
    @Override
    protected final boolean processData(ByteBuffer data)
    {
        if (!decrypt(data))
        {
            return false;
        }

        AionClientPacket pck = AionPacketHandler.handle(data, this);

        log.info("recived packet: " + pck);

        /**
         * Execute packet only if packet exist (!= null) and read was ok.
         */
        if ((pck != null) && pck.read())
        {
            processor.executePacket(pck);
        }

        return true;
    }

    /**
     * This method will be called by Dispatcher, and will be repeated till return false.
     *
     * @param data
     * @return True if data was written to buffer, False indicating that there are not any more data to write.
     */
    @Override
    protected boolean writeData(ByteBuffer data)
    {
        AionServerPacket packet = sendMsgQueue.pollFirst();

        if (packet == null)
        {
            return false;
        }

        packet.write(this, data);

        return true;
    }

    /**
     * This method is called by Dispatcher when connection is ready to be closed.
     *
     * @return time in ms after witch onDisconnect() method will be called. Always return 0.
     */
    @Override
    protected long getDisconnectionDelay()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDisconnect()
    {
        /**
         * Remove account only if not joined GameServer yet.
         */
        if ((account != null) && !joinedGs)
        {
            AccountController.removeAccountOnLS(account);
            AccountTimeController.updateOnLogout(account);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onServerClose()
    {
        // TODO mb some packet should be send to client before closing?
        close( /** packet, */true);
    }

    /**
     * Decrypt packet.
     *
     * @param buf
     * @return true if success
     */
    private boolean decrypt(ByteBuffer buf)
    {
        int size = buf.remaining();
        final int offset = buf.arrayOffset() + buf.position();
        boolean ret = cryptEngine.decrypt(buf.array(), offset, size);

        if (!ret)
        {
            log.warn("Wrong checksum from client: " + this);
        }

        return ret;
    }

    /**
     * Encrypt packet.
     *
     * @param buf
     * @return encrypted packet size.
     */
    public int encrypt(ByteBuffer buf)
    {
        int size = buf.limit() - 2;
        final int offset = buf.arrayOffset() + buf.position();

        size = cryptEngine.encrypt(buf.array(), offset, size);

        return size;
    }

    /**
     * Sends AionServerPacket to this client.
     *
     * @param bp AionServerPacket to be sent.
     */
    public void sendPacket(AionServerPacket bp)
    {
        /**
         * Connection is already closed or waiting for last (close packet) to be sent
         */
        if (isWriteDisabled())
        {
            return;
        }

        log.debug("sending packet: " + bp);
        sendMsgQueue.addLast(bp);
        enableWriteInterest();
    }

    /**
     * Its guaranted that closePacket will be sent before closing connection, but all past and future packets wont.
     * Connection will be closed [by Dispatcher Thread], and onDisconnect() method will be called to clear all other
     * things. forced means that server shouldn't wait with removing this connection.
     *
     * @param closePacket Packet that will be send before closing.
     * @param forced      have no effect in this implementation.
     */
    public void close(AionServerPacket closePacket, boolean forced)
    {
        if (isWriteDisabled())
        {
            return;
        }

        log.info("sending packet: " + closePacket + " and closing connection after that.");

        pendingClose = true;
        isForcedClosing = forced;

        sendMsgQueue.clear();
        sendMsgQueue.addLast(closePacket);
        enableWriteInterest();
    }

    /**
     * Return Scrambled modulus
     *
     * @return Scrambled modulus
     */
    public byte[] getEncryptedModulus()
    {
        return encryptedRSAKeyPair.getEncryptedModulus();
    }

    /**
     * Return RSA private key
     *
     * @return rsa private key
     */
    public RSAPrivateKey getRSAPrivateKey()
    {
        return (RSAPrivateKey) encryptedRSAKeyPair.getRSAKeyPair().getPrivate();
    }

    /**
     * Returns unique sessionId of this connection.
     *
     * @return SessionId
     */
    public int getSessionId()
    {
        return sessionId;
    }

    /**
     * Current state of this connection
     *
     * @return state
     */
    public State getState()
    {
        return state;
    }

    /**
     * Set current state of this connection
     *
     * @param state
     */
    public void setState(State state)
    {
        this.state = state;
    }

    /**
     * Returns Account object that this client logged in or null
     *
     * @return Account
     */
    public Account getAccount()
    {
        return account;
    }

    /**
     * Set Account object for this connection.
     *
     * @param account
     */
    public void setAccount(Account account)
    {
        this.account = account;
    }

    /**
     * Returns Session Key of this connection
     *
     * @return SessionKey
     */
    public SessionKey getSessionKey()
    {
        return sessionKey;
    }

    /**
     * Set Session Key for this connection
     *
     * @param sessionKey
     */
    public void setSessionKey(SessionKey sessionKey)
    {
        this.sessionKey = sessionKey;
    }

    /**
     * Set joinedGs value to true
     */
    public void setJoinedGs()
    {
        joinedGs = true;
    }

    /**
     * @return String info about this connection
     */
    @Override
    public String toString()
    {
        return (account != null) ? account + " " + getIP() : "not loged " + getIP();
    }

    /**
     * This method should no be modified, hashcode in this class is used to ensure that each connection hash unique id
     *
     * @return unique identifier
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}
