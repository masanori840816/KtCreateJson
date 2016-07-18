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

    lateinit var ColumnTitleList: ArrayList<String>
        get
    lateinit var LoadedSheetItemList: ArrayList<ArrayList<String>>
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
        val columnCount = targetSheet.getRow(0).physicalNumberOfCells - 1

        ColumnTitleList = ArrayList()
        for(cell in targetSheet.getRow(0)){
            ColumnTitleList.add(getCellValue(cell))
        }
        LoadedSheetItemList = ArrayList<ArrayList<String>>()

        for(i in 1..rowCount){
            val loadedRowItemList = ArrayList<String>()
            loadedRowItemList.add(i.toString())

            for(t in 1..columnCount){
                loadedRowItemList.add(getCellValue(targetSheet.getRow(i).getCell(t)))
            }
            LoadedSheetItemList.add(loadedRowItemList)
        }
        currentWorkbook.close()
        fileStream.close()
    }
    fun getCellValue(targetCell: Cell?): String{
        var result = ""
        if(targetCell == null){
            return result
        }
        // SpreadsheetにおけるCellの型によらず、一律でStringとしてCellの値を取得.
        when(targetCell.cellType){
            Cell.CELL_TYPE_BOOLEAN -> return targetCell.booleanCellValue.toString()
            Cell.CELL_TYPE_NUMERIC -> return targetCell.numericCellValue.toString()
            Cell.CELL_TYPE_STRING -> return targetCell.stringCellValue.toString()
            Cell.CELL_TYPE_FORMULA -> return targetCell.cellFormula.toString()
        }
        return result
    }
}