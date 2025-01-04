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
package io.inugami.framework.commons.security;

import io.inugami.framework.commons.files.FilesUtils;
import io.inugami.framework.interfaces.configurtation.JvmKeyValues;
import io.inugami.framework.interfaces.exceptions.Asserts;
import io.inugami.framework.interfaces.exceptions.FatalException;
import io.inugami.framework.interfaces.monitoring.logger.Loggers;
import io.inugami.framework.interfaces.tools.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

/**
 * EncryptionUtils
 *
 * @author patrick_guillerm
 * @since 27 oct. 2016
 */
@SuppressWarnings({"java:S2245", "java:S1168"})
public class EncryptionUtils {

    // =================================================================================================================
    // ATTRIBUTES
    // =================================================================================================================
    private static final Properties DEFAULT_PROPERTIES = loadDefaultProperties();

    //@formatter:off
    /**
     * @see io.inugami.framework.interfaces.configurtation.JvmKeyValues.SECURITY_CRYPTOGRAPHIC_KEYS
     */
    private static final  String[] CRYPTO_DEFINITION = initCryptoDefinition();


    /**
     * The Constant CHAR_DOUBLE_DOT.
     */
    protected static final String CHAR_MINUS = assertLenght(CRYPTO_DEFINITION[2], 1, "invalide char minus definition");


    /**
     * The Constant CHAR_AT.
     */
    protected static final String CHAR_AT = assertLenght(CRYPTO_DEFINITION[4], 1, "invalide char at definition");



    /**
     * @see io.inugami.framework.interfaces.configurtation.JvmKeyValues.SECURITY_TOKEN_MAX_SIZE
     */
    private static final int TOKEN_MAX_SIZE = Integer.parseInt(load(JvmKeyValues.SECURITY_TOKEN_MAX_SIZE));

    /**
     * @see io.inugami.framework.interfaces.configurtation.JvmKeyValues.SERCURITY_CIPHER
     */
    private static final String CIPHER_ALGORITHM = load(JvmKeyValues.SERCURITY_CIPHER_ALGO);

    private static final String KEY_ALGORITHM = load(JvmKeyValues.SERCURITY_CIPHER_ALGO_KEY);

    private static final Charset UTF_8 = Charset.forName(load(JvmKeyValues.SERCURITY_ENCODING));

    /**
     * @see io.inugami.framework.interfaces.configurtation.JvmKeyValues.SECURITY_AES_SECRET_KET
     */
    private static final byte[] SECRET_KEY = loadSecretKey();


    // =================================================================================================================
    // INIT
    // =================================================================================================================
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
        final Properties  result = new Properties();
        final InputStream stream = EncryptionUtils.class.getClassLoader().getResourceAsStream("META-INF/inugami_commons.properties");
        try {
            result.load(stream);
        } catch (final IOException e) {
            throw new FatalException(e.getMessage(), e);
        } finally {
            FilesUtils.close(stream);
        }
        return result;
    }

    private static String[] initCryptoDefinition() {
        final String       config = load(JvmKeyValues.SECURITY_CRYPTOGRAPHIC_KEYS);
        final List<String> result = new ArrayList<>();
        for (final char item : config.toCharArray()) {
            result.add(new StringBuilder().append(item).toString());
        }
        Asserts.assertTrue(result.size() == 6);
        return result.toArray(new String[]{});
    }

    // =================================================================================================================
    // Token
    // =================================================================================================================
    public synchronized String makeUniqueToken() {
        String result = null;

        final long   time     = System.currentTimeMillis();
        final long   timeNano = System.nanoTime();
        final String timeStr  = String.valueOf(time) + String.valueOf(timeNano);

        final String timeSha1;
        timeSha1 = encodeSha1(timeStr);

        String    randomToken = "";
        final int nbKeys      = TOKEN_MAX_SIZE - timeSha1.length();
        if (nbKeys > 0) {
            randomToken = UUID.randomUUID().toString();
        }

        result = encodeSha1(timeSha1 + randomToken);

        return result;
    }

    // =================================================================================================================
    // SHA 512
    // =================================================================================================================
    private static String encodeToSha1(final String value) {
        String result = null;
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(loadSecretKey());
            final byte[] bytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
            result = Hex.encodeHexString(bytes);
        } catch (final NoSuchAlgorithmException e) {
            Loggers.DEBUG.error(e.getMessage(), e);
        }
        return result;
    }


    public String encodeSha1(final String value) {
        return encodeToSha1(value);
    }


    // =================================================================================================================
    // AES
    // =================================================================================================================
    public String encodeAES(final String value) {
        byte[] data = null;
        try {
            final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY, KEY_ALGORITHM));
            data = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                       IllegalBlockSizeException
                       | BadPaddingException e) {
            throw new SecurityException(e.getMessage(), e);
        }

        return encodeBase64(data);
    }


    public String decodeAES(final String value) {
        final byte[] bytes = decodeBase64Bytes(value);
        try {
            final Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY, KEY_ALGORITHM));
            return new String(cipher.doFinal(bytes));
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                       IllegalBlockSizeException
                       | BadPaddingException e) {
            throw new SecurityException(e.getMessage(), e);
        }

    }



    // =================================================================================================================
    // BASE 64
    // =================================================================================================================
    public String encodeBase64(final String value) {
        return value == null ? null :  new String (Base64.getEncoder().encode(value.getBytes(StandardCharsets.UTF_8)));
    }

    public String encodeBase64(final byte[] value) {
        if (value == null) {
            return null;
        }
        return new String(Base64.getEncoder().encode(value));
    }


    public String decodeBase64(final String value) {
        return new String(decodeBase64Bytes(value));

    }

    public byte[] decodeBase64Bytes(final String value) {
        if (value == null) {
            return null;
        }
        return Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
    }

    // =================================================================================================================
    // Map<String,String> <-> String
    // =================================================================================================================
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
