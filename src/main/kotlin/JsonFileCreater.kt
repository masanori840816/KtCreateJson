import com.google.gson.Gson
import com.google.gson.stream.JsonWriter
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException
import java.io.StringWriter
import java.util.ArrayList

/**
 * Created by masanori on 2016/07/18.
 */
class JsonFileCreater {
    fun createFile(toiletInfoList: ArrayList<ToiletInfo>, fileTitle: String){
        val stringWriter = StringWriter()
        val jsonWriter = JsonWriter(BufferedWriter(stringWriter))
        jsonWriter.setIndent("  ")
        jsonWriter.beginObject()

        val gson = Gson()
        jsonWriter.name("toiletInfo").jsonValue(gson.toJson(toiletInfoList))

        jsonWriter.endObject()
        jsonWriter.close()
        val createdJson = String(stringWriter.buffer)

        try{
            val splittedTitles = fileTitle.split('.')
            if(splittedTitles.size <= 0){
                return
            }
            val fileWriter = FileWriter(splittedTitles[0] + ".json")
            fileWriter.write(createdJson)
            fileWriter.close()
        }catch(e: IOException){

        }
    }
}