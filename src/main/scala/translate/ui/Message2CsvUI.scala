package translate.ui
/*
 * Copyright 2015-2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File

import translate.Message2Csv
import util.PathParser

import scala.swing._

object Message2CsvUI extends Message2CsvUI

trait Message2CsvUI extends PathParser{

  // #### Messages file...
  val tfMessagesIn = new TextField()
  val buttonMessagesIn = new Button {
    action = Action("open"){
      val fcMessagesIn = new FileChooser(new File(extractPath(tfMessagesIn.text)))
      fcMessagesIn.showOpenDialog(tfMessagesIn)
      if(fcMessagesIn.selectedFile != null) {tfMessagesIn.text = fcMessagesIn.selectedFile.toString}
    }
    text = "Input Message file..."
    enabled = true
    tooltip = "Select the English messages file you'd like to convert to csv"
  }


  // #### Input Csv Input file...
    val tfExistingCsv = new TextField()
  val buttonExistingCsv = new Button {
    action = Action("open"){
      val fcExistingCsv = new FileChooser(new File(extractPath(tfExistingCsv.text)))
      fcExistingCsv.showOpenDialog(tfExistingCsv)
      if(fcExistingCsv.selectedFile != null) {tfExistingCsv.text = fcExistingCsv.selectedFile.toString}
    }
    text = "Input Csv file..."
    enabled = true
    tooltip = "Select a previously converted csv file, for comparison with your Messages File"
  }


  // #### Output Csv Output file...
  val tfOuputCsv = new TextField()
  val buttonOuputCsv = new Button {
    action = Action("open"){
      val fcOuputCsv = new FileChooser(new File(extractPath(tfOuputCsv.text)))
      fcOuputCsv.showOpenDialog(tfOuputCsv)
      if(fcOuputCsv.selectedFile != null) {tfOuputCsv.text = fcOuputCsv.selectedFile.toString}
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
      Message2Csv.messages2csv(tfMessagesIn.text, tfExistingCsv.text, tfOuputCsv.text)
    }
    text = "Create Csv file"
    enabled = true
    tooltip = "Takes English messages and an existing set of translations, in order to create a new marked-up Csv, for translation."
  }

  val goPanel = new FlowPanel(){
    contents += go2CsvButton
  }

  def projectUpdated(projDir: String):Unit = {
    tfMessagesIn.text = projDir+"/conf/messages"
    tfExistingCsv.text = projDir+"/conf/existingMessages.csv"
    tfOuputCsv.text = projDir+"/conf/newMessages.csv"
  }
}
