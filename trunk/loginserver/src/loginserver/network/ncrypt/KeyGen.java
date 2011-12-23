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

import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.spec.RSAKeyGenParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.apache.log4j.Logger;
import commons.utils.Rnd;

/**
 * Key generator. It generates keys or keyPairs for Blowfish and RSA
 */
public class KeyGen
{
	/**
	 * Logger for this class.
	 */
	protected static final Logger log = Logger.getLogger(KeyGen.class);

	/**
	 * Key generator for blowfish
	 */
	private static KeyGenerator	blowfishKeyGen;

	/**
	 * Public/Static RSA KeyPairs with encrypted modulus N
	 */
	private static EncryptedRSAKeyPair[] encryptedRSAKeyPairs;

    /**
	 * Initialize Key Generator (Blowfish keygen and RSA keygen)
	 *
	 * @throws GeneralSecurityException
	 */
	public static void init() throws GeneralSecurityException
	{
		log.info("Initializing Key Generator...");

		blowfishKeyGen = KeyGenerator.getInstance("Blowfish");

		KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");

		RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);

		rsaKeyPairGenerator.initialize(spec);

		encryptedRSAKeyPairs = new EncryptedRSAKeyPair[10];

		for(int i = 0; i < 10; i++)
		{
			encryptedRSAKeyPairs[i] = new EncryptedRSAKeyPair(rsaKeyPairGenerator.generateKeyPair());
		}

		// Pre-init RSA cipher.. saving about 300ms
		Cipher	rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");

		rsaCipher.init(Cipher.DECRYPT_MODE, encryptedRSAKeyPairs[0].getRSAKeyPair().getPrivate());
	}

	/**
	 * Generate and return blowfish key
	 *
	 * @return Random generated blowfish key
	 */
	public static SecretKey generateBlowfishKey()
	{
		return blowfishKeyGen.generateKey();
	}

	/**
	 * Get common RSA Public/Static Key Pair with encrypted modulus N
	 * @return encryptedRSAkeypairs
	 */
	public static EncryptedRSAKeyPair getEncryptedRSAKeyPair()
	{
		return encryptedRSAKeyPairs[Rnd.nextInt(10)];
	}
}