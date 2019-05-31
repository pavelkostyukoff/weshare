package com.spacesofting.weshare.utils

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import com.spacesofting.weshare.common.ApplicationWrapper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec
import javax.security.auth.x500.X500Principal


object SecureUtils {
    private val CHARSET = Charset.forName("utf-8")

    private val AES_MODE = "AES/ECB/PKCS7Padding"
    private val SHARED_PREFENCE_NAME = "com.gpbdigital.wishninja.security"
    private val KEY = "com.gpbdigital.wishninja.key_rsa_private"

    private val KEY_AES = "com.gpbdigital.wishninja.KEY_AES"

    private val KEY_STORE = "AndroidKeyStore"

    private val RSA_MODE = "RSA/ECB/PKCS1Padding"

    private val STORE = KeyStore.getInstance(KEY_STORE)


    init {
        STORE.load(null);

        //make key if it does not exist
        if (!STORE.containsAlias(KEY)) {
            // Generate a key pair for encryption
            val start = Calendar.getInstance();

            val end = Calendar.getInstance();
            end.add(Calendar.YEAR, 30);

            val spec = KeyPairGeneratorSpec.Builder(ApplicationWrapper.INSTANCE)
                    .setAlias(KEY)
                    .setSubject(X500Principal("CN=" + KEY))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();

            val kpg = KeyPairGenerator.getInstance("RSA", KEY_STORE);
            kpg.initialize(spec);
            kpg.generateKeyPair();
        }
    }

    private fun rsaEncrypt(secret: ByteArray): ByteArray {
        val privateKeyEntry: KeyStore.PrivateKeyEntry = STORE.getEntry(KEY, null) as KeyStore.PrivateKeyEntry

        // Encrypt the text
        val inputCipher = Cipher.getInstance(RSA_MODE)
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());


        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(secret)
        cipherOutputStream.close()

        val vals = outputStream.toByteArray()
        return vals
    }

    private fun rsaDecrypt(encrypted: ByteArray): ByteArray {
        val privateKeyEntry = STORE.getEntry(KEY, null) as KeyStore.PrivateKeyEntry

        val output = Cipher.getInstance(RSA_MODE)
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

        val cipherInputStream = CipherInputStream(ByteArrayInputStream(encrypted), output)
        val values = ArrayList<Byte>()
        var nextByte: Int

        while (true) {
            nextByte = cipherInputStream.read()
            if (nextByte == -1) {
                break;
            }
            values.add(nextByte.toByte())
        }

        val bytes = ByteArray(values.size)
        for (i in bytes.indices) {
            bytes[i] = values.get(i)
        }
        return bytes
    }

    private fun getAES(): Key {
        val pref = ApplicationWrapper.INSTANCE.getSharedPreferences(SHARED_PREFENCE_NAME, Context.MODE_PRIVATE)
        var encryptedKeyB64 = pref.getString(KEY_AES, null)
        var key: ByteArray = ByteArray(16)
        if (encryptedKeyB64 == null) {
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(key)
            val encryptedKey = rsaEncrypt(key)
            encryptedKeyB64 = Base64.encodeToString(encryptedKey, Base64.DEFAULT)
            val edit = pref.edit()
            edit.putString(KEY_AES, encryptedKeyB64)
            edit.commit()
        } else {
            key = rsaDecrypt(Base64.decode(encryptedKeyB64, Base64.DEFAULT))
        }

        return SecretKeySpec(key, "AES")
    }

    /**
     * Encrypts String with RSA
     */
    fun encrypt(value: String?): String? {
        if(value == null){
            return null
        }

        val bytes = value.toByteArray(CHARSET)

        return encryptAES(bytes)
    }


    /**
     * Decrypt String encrypted with RSA
     */
    fun decrypt(value: String?): String? {
        if(value == null){
            return null
        }

        val encrypted = Base64.decode(value, Base64.DEFAULT)
        val decryptedBytes : ByteArray? = decryptAES(encrypted)
        if(decryptedBytes != null) {
            val decrypted = String(decryptedBytes, CHARSET)

            return decrypted
        }else{
            return null
        }
    }

    private fun encryptAES(input: ByteArray): String {
        val c = Cipher.getInstance(AES_MODE)
        c.init(Cipher.ENCRYPT_MODE, getAES())

        val encodedBytes = c.doFinal(input)
        val encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT)

        return encryptedBase64Encoded
    }


    private fun decryptAES(encrypted: ByteArray?): ByteArray? {
        if(encrypted == null){
            return null
        }

        val c = Cipher.getInstance(AES_MODE)
        c.init(Cipher.DECRYPT_MODE, getAES())

        val decodedBytes = c.doFinal(encrypted)

        return decodedBytes
    }
}