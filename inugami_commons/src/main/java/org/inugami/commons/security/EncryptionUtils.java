/* --------------------------------------------------------------------
 *  Inugami  
 * --------------------------------------------------------------------
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.inugami.commons.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.inugami.api.constants.JvmKeyValues;
import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.FatalException;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.commons.files.FilesUtils;

import com.google.common.hash.Hashing;

/**
 * EncryptionUtils
 * 
 * @author patrick_guillerm
 * @since 27 oct. 2016
 */
public class EncryptionUtils {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final static Properties DEFAULT_PROPERTIES = loadDefaultProperties();
    
    //@formatter:off
    /**
     * @see org.inugami.api.constants.JvmKeyValues.SECURITY_CRYPTOGRAPHIC_KEYS
     */
    private final static String[]   CRYPTO_DEFINITION  = initCryptoDefinition();
    
    /** The Constant CHAR_DOT. */
    protected static final String   CHAR_UNDERSCORE    = assertLenght(CRYPTO_DEFINITION[0], 1,"invalide char underscore definition");
    
    /** The Constant CHAR_AND. */
    protected static final String   CHAR_AND           = assertLenght(CRYPTO_DEFINITION[1], 1,"invalide char and definition");
    
    /** The Constant CHAR_DOUBLE_DOT. */
    protected static final String   CHAR_MINUS         = assertLenght(CRYPTO_DEFINITION[2], 1,"invalide char minus definition");
    
    /** The Constant CHAR_PLUS. */
    protected static final String   CHAR_PLUS          = assertLenght(CRYPTO_DEFINITION[3], 1,"invalide char plus definition");
    
    /** The Constant CHAR_AT. */
    protected static final String   CHAR_AT            = assertLenght(CRYPTO_DEFINITION[4], 1,"invalide char at definition");
    
    /** The Constant CHAR_SLASH. */
    protected static final String   CHAR_SLASH         = assertLenght(CRYPTO_DEFINITION[5], 1,"invalide char slash definition");
    //@formatter:on
    
    private static final Random  RANDOM           = new Random((new Date()).getTime());
    
    /**
     * @see org.inugami.api.constants.JvmKeyValues.SECURITY_TOKEN_MAX_SIZE
     */
    private static final int     TOKEN_MAX_SIZE   = Integer.parseInt(load(JvmKeyValues.SECURITY_TOKEN_MAX_SIZE));
    
    /**
     * @see org.inugami.api.constants.JvmKeyValues.SERCURITY_CIPHER
     */
    private static final String  CIPHER_ALGORITHM = load(JvmKeyValues.SERCURITY_CIPHER_ALGO);
    
    private static final String  KEY_ALGORITHM    = load(JvmKeyValues.SERCURITY_CIPHER_ALGO_KEY);
    
    private static final Charset UTF_8            = Charset.forName(load(JvmKeyValues.SERCURITY_ENCODING));
    
    /**
     * @see org.inugami.api.constants.JvmKeyValues.SECURITY_AES_SECRET_KET
     */
    private static final byte[]  SECRET_KEY       = loadSecretKey();
    
    // =========================================================================
    // INIT
    // =========================================================================
    private static byte[] loadSecretKey() {
        final String config = JvmKeyValues.SECURITY_AES_SECRET_KEY.or("16BYTESSECRETKEY");
        return config.getBytes(UTF_8);
    }
    
    private static String assertLenght(final String value, final int size, final String message) {
        if ((value == null) || (value.length() != size)) {
            throw new FatalException(message);
        }
        return value;
    }
    
    private static String load(final JvmKeyValues key) {
        return key.or(DEFAULT_PROPERTIES.get(key.getKey()));
    }
    
    private static Properties loadDefaultProperties() {
        final Properties result = new Properties();
        final InputStream stream = EncryptionUtils.class.getClassLoader().getResourceAsStream("META-INF/inugami_commons.properties");
        try {
            result.load(stream);
        }
        catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        }
        finally {
            FilesUtils.close(stream);
        }
        return result;
    }
    
    private static String[] initCryptoDefinition() {
        final String config = load(JvmKeyValues.SECURITY_CRYPTOGRAPHIC_KEYS);
        final List<String> result = new ArrayList<>();
        for (final char item : config.toCharArray()) {
            result.add(new StringBuilder().append(item).toString());
        }
        Asserts.isTrue(result.size() == 6);
        return result.toArray(new String[] {});
    }
    
    // =========================================================================
    // Token
    // =========================================================================
    /**
     * Make unique token.
     * 
     * @return the string
     */
    public synchronized String makeUniqueToken() {
        String result = null;
        
        final long time = System.currentTimeMillis();
        final long timeNano = System.nanoTime();
        final String timeStr = String.valueOf(time) + String.valueOf(timeNano);
        
        String timeSha1;
        timeSha1 = encodeSha1(timeStr);
        
        String randomToken = "";
        final int nbKeys = TOKEN_MAX_SIZE - timeSha1.length();
        if (nbKeys > 0) {
            randomToken = UUID.randomUUID().toString();
        }
        
        result = encodeSha1(timeSha1 + randomToken);
        
        return result;
    }
    
    // =========================================================================
    // SHA 1
    // =========================================================================
    /**
     * Encode to sha1.
     * 
     * @param value the value
     * @return the string
     * @throws TechnicalException the technical exception
     */
    private static String encodeToSha1(final String value) {
        return Hashing.sha512().hashString(value, Charsets.UTF_8).toString();
        
    }
    
    /**
     * Encode sha1.
     * 
     * @param value the value
     * @return the string
     * @throws TechnicalException the technical exception
     */
    public String encodeSha1(final String value) {
        return encodeToSha1(value);
    }
    
    // =========================================================================
    // AES
    // =========================================================================
    
    /**
     * Encode aes.
     * 
     * @param value the value
     * @return the string
     * @throws TechnicalException the technical exception
     */
    public String encodeAES(final String value) {
        byte[] data = null;
        try {
            final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY, KEY_ALGORITHM));
            data = cipher.doFinal(value.getBytes(Charsets.UTF_8));
            
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new SecurityException(e.getMessage(), e);
        }
        
        return encodeBase64(data);
    }
    
    /**
     * Decode aes.
     * 
     * @param value the value
     * @return the string
     * @throws TechnicalException the technical exception
     * @throws DecoderException
     */
    public String decodeAES(final String value) {
        
        try {
            final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY, KEY_ALGORITHM));
            return new String(cipher.doFinal(Base64.decodeBase64(value)), Charsets.UTF_8);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new SecurityException(e.getMessage(), e);
        }
        
    }
    
    // =========================================================================
    // AES
    // =========================================================================
    /**
     * Encode hexa.
     * 
     * @param value the value
     * @return the string
     */
    public String encodeHexa(final String value) {
        return Hex.encodeHexString(value.getBytes());
    }
    
    /**
     * Decode hexa.
     * 
     * @param value the value
     * @return the string
     * @throws DecoderException the decoder exception
     */
    public String decodeHexa(final String value) throws DecoderException {
        return new String(Hex.decodeHex(value.toCharArray()));
    }
    
    // =========================================================================
    // BASE 64
    // =========================================================================
    /**
     * Encode base64.
     * 
     * @param value the value
     * @return the string
     */
    public String encodeBase64(final String value) {
        return encodeBase64(value == null ? null : value.getBytes(UTF_8));
    }
    
    public String encodeBase64(final byte[] value) {
        if (value == null) {
            return null;
        }
        return Base64.encodeBase64URLSafeString(value);
    }
    
    /**
     * Decode base64.
     * 
     * @param value the value
     * @return the string
     */
    public String decodeBase64(final String value) {
        return new String(decodeBase64Bytes(value));
        
    }
    
    public byte[] decodeBase64Bytes(final String value) {
        if (value == null) {
            return null;
        }
        return Base64.decodeBase64(value.getBytes(UTF_8));
    }
    
    // =========================================================================
    // Map<String,String> <-> String
    // =========================================================================
    /**
     * Encode map.
     * 
     * @param value the value
     * @return the string
     */
    public String encodeMap(final Map<String, String> value) {
        final StringBuilder result = new StringBuilder();
        if ((value == null) || value.isEmpty()) {
            return result.toString();
        }
        
        final Iterator<Entry<String, String>> iterator = value.entrySet().iterator();
        while (iterator.hasNext()) {
            final Entry<String, String> entry = iterator.next();
            result.append(entry.getKey());
            result.append(CHAR_MINUS);
            result.append(entry.getValue());
            
            if (iterator.hasNext()) {
                result.append(CHAR_AT);
            }
        }
        return result.toString();
    }
    
    /**
     * Decode map.
     * 
     * @param value the value
     * @return the map
     */
    public Map<String, String> decodeMap(final String value) {
        final Map<String, String> result = new HashMap<>();
        
        final String[] values = value.split(CHAR_AT);
        for (final String item : values) {
            final String[] composite = item.split(CHAR_MINUS);
            if (composite.length == 2) {
                result.put(composite[0], composite[1]);
            }
        }
        return result;
    }
}
