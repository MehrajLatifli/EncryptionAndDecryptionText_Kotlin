import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import javax.crypto.SecretKey

fun main() = runBlocking<Unit> {
    val secretKey = generateAESKey()



    val jsonString = File("text.json").readText()
    val json = Json.decodeFromString<Map<String, List<String>>>(jsonString)

    val paragraphsData = json["paragraphs"] ?: error("Missing 'paragraphs' in JSON")

    var encryptedText: String? = null
    var decryptedText: String? = null

    val encryptionJob = GlobalScope.launch {
        encryptedText = encryptWithAsync(paragraphsData.joinToString("\n"), secretKey)
        println("\n\n")
        println("Encrypted text: $encryptedText")
    }



    val decryptionJob = GlobalScope.launch {
        delay(2000)
        println("\n\n")
        decryptedText = decryptWithAsync(encryptedText!!, secretKey)
        println("Decrypted text: $decryptedText")
    }

    encryptionJob.join()
    decryptionJob.join()
}

fun generateAESKey(): SecretKey {

    val keyGen = javax.crypto.KeyGenerator.getInstance("AES")
    keyGen.init(256)
    return keyGen.generateKey()
}