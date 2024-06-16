import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


suspend fun encryptWithAsync(plainText: String, key: SecretKey): String = withContext(Dispatchers.Default) {
    val iv = IvParameterSpec(key.encoded.copyOf(16))
    val plainBytes = plainText.toByteArray()

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, key, iv)

    val encryptedBytes = cipher.doFinal(plainBytes)
    Base64.getEncoder().encodeToString(encryptedBytes)
}

suspend fun decryptWithAsync(encryptedText: String, key: SecretKey): String = withContext(Dispatchers.Default) {
    val iv = IvParameterSpec(key.encoded.copyOf(16))
    val encryptedBytes = Base64.getDecoder().decode(encryptedText)

    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, iv)

    val decryptedBytes = cipher.doFinal(encryptedBytes)
    String(decryptedBytes)
}

