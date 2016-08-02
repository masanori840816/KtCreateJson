import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileInputStream
import java.util.ArrayList

/**
 * Created by masanori on 2016/07/15.
 */

class SpreadsheetAccesser {

    lateinit var ToiletInfoList: ArrayList<ToiletInfo>
        get

    fun getSheetNames(targetFilePath: String): ObservableList<String>?{
        val fileStream = FileInputStream(targetFilePath)
        val currentWorkbook = WorkbookFactory.create(fileStream)

        if(currentWorkbook == null){
            return null
        }
        val sheetNameList: ObservableList<String> = FXCollections.observableArrayList()

        for (i in 0..currentWorkbook.numberOfSheets - 1){
            sheetNameList.add(currentWorkbook.getSheetName(i))
        }
        currentWorkbook.close()
        fileStream.close()
        return sheetNameList
    }
    fun loadFile(targetFilePath: String, targetSheetName: String){
        val fileStream = FileInputStream(targetFilePath)
        val currentWorkbook = WorkbookFactory.create(fileStream)

        if(currentWorkbook == null){
            return
        }
        val targetSheet: Sheet? = currentWorkbook.getSheet(targetSheetName)
        if(targetSheet == null){
            return
        }
        val rowCount = targetSheet.physicalNumberOfRows - 1
        if(rowCount < 0){
            return
        }
        // 最初の行から列数を取得する.
        if(targetSheet.getRow(0).physicalNumberOfCells >= 9) {
            ToiletInfoList = ArrayList<ToiletInfo>()

            // 最初の行は項目名なのでスキップ.
            for (i in 1..rowCount) {
                val toiletInfo = ToiletInfo()
                //　2. toiletName, 3. district, 4. municipality, 5. address,
                //  6. latitude, 7. longitude, 8.availableTime, 9.hasMultiPurposeToilet.
                toiletInfo.toiletName = targetSheet.getRow(i).getCell(1).stringCellValue
                toiletInfo.district = targetSheet.getRow(i).getCell(2).stringCellValue
                toiletInfo.municipality = targetSheet.getRow(i).getCell(3).stringCellValue
                toiletInfo.address = targetSheet.getRow(i).getCell(4).stringCellValue
                toiletInfo.latitude = targetSheet.getRow(i).getCell(5).numericCellValue
                toiletInfo.longitude = targetSheet.getRow(i).getCell(6).numericCellValue
                var availableTime: String? = targetSheet.getRow(i).getCell(7)?.stringCellValue
                availableTime = availableTime?: ""
                toiletInfo.availableTime = availableTime
                toiletInfo.hasMultiPurposeToilet = targetSheet.getRow(i).getCell(8).booleanCellValue

                ToiletInfoList.add(toiletInfo)
            }
        }
        currentWorkbook.close()
        fileStream.close()
    }
}