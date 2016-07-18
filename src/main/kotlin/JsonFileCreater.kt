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
    fun createFile(titleList: ArrayList<String>, valueListCollection: ArrayList<ArrayList<String>>, fileTitle: String){
        val stringWriter = StringWriter()
        val jsonWriter = JsonWriter(BufferedWriter(stringWriter))
        jsonWriter.beginObject()
        valueListCollection.forEach { valueList -> run{
                for(i in 0..valueList.size - 1){
                    jsonWriter.name(titleList[i]).value(valueList[i])
                }
            }
        }
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