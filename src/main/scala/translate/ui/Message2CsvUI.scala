package translate.ui

import java.io.File

import translate.Message2Csv

import scala.swing._

object Message2CsvUI extends Message2CsvUI

trait Message2CsvUI {

  // #### Messages file...
  val fcMessagesIn = new FileChooser(new File("."))
  val tfMessagesIn = new TextField()
  val buttonMessagesIn = new Button {
    action = Action("open"){
      fcMessagesIn.showOpenDialog(tfMessagesIn)
      tfMessagesIn.text = fcMessagesIn.selectedFile.toString
    }
    text = "Input Message file..."
    enabled = true
    tooltip = "Select the English messages file you'd like to convert to csv"
  }


  // #### Input Csv Input file...
  val fcExistingCsv = new FileChooser(new File("."))
  val tfExistingCsv = new TextField()
  val buttonExistingCsv = new Button {
    action = Action("open"){
      fcExistingCsv.showOpenDialog(tfExistingCsv)
      tfExistingCsv.text = fcExistingCsv.selectedFile.toString
    }
    text = "Input Csv file..."
    enabled = true
    tooltip = "Select a previously converted csv file, for comparison with your Messages File"
  }


  // #### Output Csv Input file...
  val fcOuputCsv = new FileChooser(new File("."))
  val tfOuputCsv = new TextField()
  val buttonOuputCsv = new Button {
    action = Action("open"){
      fcOuputCsv.showOpenDialog(tfOuputCsv)
      tfOuputCsv.text = fcOuputCsv.selectedFile.toString
    }
    text = "Ouput Csv file..."
    enabled = true
    tooltip = "Select the file to output your csv to."
  }


  // ### Assemble button and Textfield panels...
  val panelButtons = new GridPanel(3,1){
    contents += buttonMessagesIn
    contents += buttonExistingCsv
    contents += buttonOuputCsv
  }

  val panelTextFields = new GridPanel(3,1){
    contents += tfMessagesIn
    contents += tfExistingCsv
    contents += tfOuputCsv
  }


  // ## Build and assemble the go Panel...
  val go2CsvButton = new Button {
    action = Action("go"){
      println("###### " + tfMessagesIn.text)
      Message2Csv.messages2csv(tfMessagesIn.text, tfExistingCsv.text, tfOuputCsv.text)
    }
    text = "Create Csv file"
    enabled = true
    tooltip = "Takes English messages and an existing set of translations, in order to create a new marked-up Csv, for translation."
  }

  val goPanel = new FlowPanel(){
    contents += go2CsvButton
  }

}
