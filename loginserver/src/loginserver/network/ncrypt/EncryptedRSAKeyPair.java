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
    private final KeyPair RSAKeyPair;
    /**
     * Byte
     */
    private final byte[] encryptedModulus;

    /**
     * Default constructor. Stores RSA key pair and encrypts rsa modulus N
     *
     * @param RSAKeyPair standard RSA KeyPair generated
     *                   with standard KeyPairGenerator {@link java.security.KeyPairGenerator}
     */
    public EncryptedRSAKeyPair(KeyPair RSAKeyPair)
    {
        this.RSAKeyPair = RSAKeyPair;
        encryptedModulus = encryptModulus(((RSAPublicKey) this.RSAKeyPair.getPublic()).getModulus());
    }

    /**
     * Encrypt RSA modulus N
     *
     * @param modulus RSA modulus from public/private pairs (e,n), (d,n)
     * @return encrypted modulus
     */
    private static byte[] encryptModulus(BigInteger modulus)
    {
        byte[] encryptedModulus = modulus.toByteArray();

        if ((encryptedModulus.length == 0x81) && (encryptedModulus[0] == 0x00))
        {
            byte[] temp = new byte[0x80];

            System.arraycopy(encryptedModulus, 1, temp, 0, 0x80);

            encryptedModulus = temp;
        }

        for (int i = 0; i < 4; i++)
        {
            byte temp = encryptedModulus[i];

            encryptedModulus[i] = encryptedModulus[0x4d + i];
            encryptedModulus[0x4d + i] = temp;
        }

        for (int i = 0; i < 0x40; i++)
        {
            encryptedModulus[i] = (byte) (encryptedModulus[i] ^ encryptedModulus[0x40 + i]);
        }

        for (int i = 0; i < 4; i++)
        {
            encryptedModulus[0x0d + i] = (byte) (encryptedModulus[0x0d + i] ^ encryptedModulus[0x34 + i]);
        }

        for (int i = 0; i < 0x40; i++)
        {
            encryptedModulus[0x40 + i] = (byte) (encryptedModulus[0x40 + i] ^ encryptedModulus[i]);
        }

        return encryptedModulus;
    }

    /**
     * Get default RSA key pair
     *
     * @return RSAKeyPair
     */
    public KeyPair getRSAKeyPair()
    {
        return RSAKeyPair;
    }

    /**
     * Get encrypted modulus to be transferred on the net.
     *
     * @return encryptedModulus
     */
    public byte[] getEncryptedModulus()
    {
        return encryptedModulus;
    }
}
