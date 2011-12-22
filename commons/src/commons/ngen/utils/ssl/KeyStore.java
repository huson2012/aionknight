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

package commons.ngen.utils.ssl;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeyStore {
	
	private java.security.KeyStore keyStore = null;
	private String privateKey = null;
	private String cert = null;
	private String caCert = null;
	private String crl = null;
	private boolean initialized = false;
	private CertificateAuthority ca;
	private PrivateKey pkey;
	
	private KeyStore (java.security.KeyStore keyStore)
	{
		this.keyStore = keyStore;
		this.initialized = false;
	}
	
	private void load () throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		pkey = PrivateKey.getInstance();
		ca = CertificateAuthority.getInstance();
		pkey.init(privateKey);
		ca.init(cert, caCert, crl);
		
		keyStore.load(null);
		keyStore.setCertificateEntry("ca.Aion-Knight.net", ca.getAuthorityCertificate());
		keyStore.setKeyEntry("lic.Aion-Knight.net", pkey.getKey(), PrivateKey.getPassword(), ca.getServerChain());
	}
	
	private static class SingletonHolder {
		private static KeyStore instance = null;
	}
	
	public java.security.KeyStore getStore ()
	{
		return keyStore;
	}
	
	public void init (String privateKey, String cert, String caCert, String crl)
	{
		if (initialized)
		{
			throw new RuntimeException("KeyStore already initialized");
		}
		this.privateKey = privateKey;
		this.cert = cert;
		this.caCert = caCert;
		this.crl = crl;
		
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static KeyStore getInstance ()
	{
		if (SingletonHolder.instance == null)
		{
			try
			{
				SingletonHolder.instance = new KeyStore (java.security.KeyStore.getInstance("JKS", "SUN"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return SingletonHolder.instance;
	}
	
	public CertificateAuthority getCertificateAuthority ()
	{
		return this.ca;
	}
}