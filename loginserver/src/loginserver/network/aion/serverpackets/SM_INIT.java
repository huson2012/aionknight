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
	private final int		sessionId;

	/**
	 * public Rsa key that client will use to encrypt login and password that will be send in RequestAuthLogin client
	 * packet.
	 */
	private final byte[]	publicRsaKey;
	/**
	 * blowfish key for packet encryption/decryption.
	 */
	private final byte[]	blowfishKey;

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
	 * @param publicRsaKey      Public RSA key
	 * @param blowfishKey       Blowfish key
	 * @param sessionId         Session identifier
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
