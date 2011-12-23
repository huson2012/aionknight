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

package loginserver.network.ncrypt;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * This class is for storing standard RSA Public/Static keyPairs
 * The main difference that N (Modulus) is encrypted to be transfered
 * on the net with simple scrambling algorythm. So public pair (e, n), 
 * where e is exponent (usually static 3 or 65537) and n is modulus,
 * is encrypted and cannot be applied to cipher some data without deciphering
 * the modulus.
 */
public class EncryptedRSAKeyPair
{
	/**
	 * KeyPair
	 */
	private KeyPair RSAKeyPair;
	/**
	 * Byte
	 */
	private byte[]	encryptedModulus;

    /**
     * Default constructor. Stores RSA key pair and encrypts rsa modulus N 
     * @param RSAKeyPair standard RSA KeyPair generated
     * with standard KeyPairGenerator {@link java.security.KeyPairGenerator}
     */
    public EncryptedRSAKeyPair(KeyPair RSAKeyPair)
	{
		this.RSAKeyPair = RSAKeyPair;
		encryptedModulus = encryptModulus(((RSAPublicKey) this.RSAKeyPair.getPublic()).getModulus());
	}

    /**
     * Encrypt RSA modulus N
     * @param modulus RSA modulus from public/private pairs (e,n), (d,n)
     * @return encrypted modulus
     */
	private byte[] encryptModulus(BigInteger modulus)
	{
		byte[]	encryptedModulus = modulus.toByteArray();

		if((encryptedModulus.length == 0x81) && (encryptedModulus[0] == 0x00))
		{
			byte[]	temp = new byte[0x80];

			System.arraycopy(encryptedModulus, 1, temp, 0, 0x80);

			encryptedModulus = temp;
		}

		for(int i = 0; i < 4; i++)
		{
			byte	temp = encryptedModulus[i];

			encryptedModulus[i]        = encryptedModulus[0x4d + i];
			encryptedModulus[0x4d + i] = temp;
		}

		for(int i = 0; i < 0x40; i++)
		{
			encryptedModulus[i] = (byte) (encryptedModulus[i] ^ encryptedModulus[0x40 + i]);
		}

		for(int i = 0; i < 4; i++)
		{
			encryptedModulus[0x0d + i] = (byte) (encryptedModulus[0x0d + i] ^ encryptedModulus[0x34 + i]);
		}

		for(int i = 0; i < 0x40; i++)
		{
			encryptedModulus[0x40 + i] = (byte) (encryptedModulus[0x40 + i] ^ encryptedModulus[i]);
		}

		return encryptedModulus;
	}

    /**
     * Get default RSA key pair
     * @return RSAKeyPair
     */
	public KeyPair getRSAKeyPair()
	{
		return RSAKeyPair;
	}

    /**
     * Get encrypted modulus to be transferred on the net.
     * @return encryptedModulus
     */
	public byte[] getEncryptedModulus()
	{
		return encryptedModulus;
	}
}