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
    private static final Logger log = Logger.getLogger(KeyGen.class);

    /**
     * Key generator for blowfish
     */
    private static KeyGenerator blowfishKeyGen;

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

        for (int i = 0; i < 10; i++)
        {
            encryptedRSAKeyPairs[i] = new EncryptedRSAKeyPair(rsaKeyPairGenerator.generateKeyPair());
        }

        // Pre-init RSA cipher.. saving about 300ms
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");

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
     *
     * @return encryptedRSAkeypairs
     */
    public static EncryptedRSAKeyPair getEncryptedRSAKeyPair()
    {
        return encryptedRSAKeyPairs[Rnd.nextInt(10)];
    }
}
