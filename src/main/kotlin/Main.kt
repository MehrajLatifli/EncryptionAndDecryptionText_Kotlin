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
    var hash: String? = null
    var decryptedText: String? = null


    val encryptionJob = GlobalScope.launch {
        val (encryptedTextResult, hashResult) = encryptWithAsync(paragraphsData.joinToString("\n"), secretKey)
        encryptedText = encryptedTextResult
        hash = hashResult
        println("\nEncrypted text: $encryptedText")
    }


    delay(2000)


    val decryptionJob = GlobalScope.launch {
        decryptedText = decryptWithAsync(encryptedText!!, hash!!, secretKey)
        println("\nDecrypted text: $decryptedText")
    }

    encryptionJob.join()
    decryptionJob.join()
}


fun generateAESKey(): SecretKey {

    val keyGen = javax.crypto.KeyGenerator.getInstance("AES")
    keyGen.init(256)
    return keyGen.generateKey()
}