/**
 * Created by masanori on 2016/07/16.
 */
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.io.IOException

class MainForm : Application(){
    lateinit private var spreadsheetAccesser: SpreadsheetAccesser
    lateinit private var jsonFileCreator: JsonFileCreater

    lateinit private var loadFilePathField: TextField
    lateinit private var sheetNameCombobox: ComboBox<String>
    lateinit private var createButton: Button

    private var sheetNameList: ObservableList<String>? = null
    private var selectedFile: File? = null

    @Throws(IOException::class)
    override fun start(primaryStage: Stage) {

        spreadsheetAccesser = SpreadsheetAccesser()
        jsonFileCreator = JsonFileCreater()

        val findFileButton = Button()
        findFileButton.text = "参照"
        findFileButton.translateX = 250.0
        findFileButton.translateY = -150.0
        findFileButton.setOnAction { event -> run {
                val fileChooser = FileChooser()
                fileChooser.title = "ファイルを選択"
                fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Spreadsheet", "*.xlsx", "*.ods"))
                selectedFile = fileChooser.showOpenDialog(primaryStage)
                loadFilePathField.text = selectedFile.toString()
                setSheetNames()
            }
        }
        loadFilePathField = TextField()
        loadFilePathField.translateX = -35.0
        loadFilePathField.translateY = -150.0
        loadFilePathField.scaleX = 0.8
        loadFilePathField.setOnAction { event -> run{
            setSheetNames()
        } }

        sheetNameList = sheetNameList?: FXCollections.observableArrayList("")

// TODO: http://qiita.com/xaatw0/items/969ba45574f6a27e838fを参考にBinding.
        sheetNameCombobox = ComboBox()
        sheetNameCombobox.translateX = -200.0
        sheetNameCombobox.translateY = -70.0
        sheetNameCombobox.prefWidth = 160.0
        if(sheetNameList != null){
            sheetNameCombobox.items = sheetNameList
        }

        createButton = Button()
        createButton.text = "作成"
        createButton.translateX = 230.0
        createButton.translateY = 150.0
        createButton.setOnAction { event -> run{
            spreadsheetAccesser.loadFile(loadFilePathField.text, sheetNameCombobox.selectionModel.selectedItem)
            jsonFileCreator.createFile(spreadsheetAccesser.ColumnTitleList, spreadsheetAccesser.LoadedSheetItemList)
        } }

        val stackPane = StackPane()
        stackPane.children.addAll(findFileButton
                , loadFilePathField
                , sheetNameCombobox
                , createButton)

        val primaryScene = Scene(stackPane, 600.0, 400.0)
        primaryStage.setScene(primaryScene)
        primaryStage.show()
    }
    private fun setSheetNames(){
        if(!loadFilePathField.text.isNullOrEmpty()){
            sheetNameCombobox.items = null
            val gotItems = spreadsheetAccesser.getSheetNames(loadFilePathField.text)
            if(gotItems != null
                    && gotItems.size > 0){
                sheetNameCombobox.items = gotItems
                sheetNameCombobox.selectionModel.select(0)
            }
        }
    }
}