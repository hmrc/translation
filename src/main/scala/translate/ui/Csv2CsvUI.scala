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
import scala.swing._

object Csv2CsvUI extends Csv2CsvUI

trait Csv2CsvUI {
  // #### Input New Csv Input file...
  val tfReceivedCsv = new TextField()
  val buttonReceivedCsv = new Button {
    action = Action("open"){
      val fcReceivedCsv = new FileChooser(new File(ProjectUI.tfProject.text))
      fcReceivedCsv.showOpenDialog(tfReceivedCsv)
      tfReceivedCsv.text = fcReceivedCsv.selectedFile.toString
    }
    text = "Received Input Csv file..."
    enabled = true
    tooltip = "Select the newly received csv file, for comparison with your previous csv File"
  }

  // #### Input Csv Input file...
  val tfExistingCsv = new TextField()
  val buttonExistingCsv = new Button {
    action = Action("open"){
      val fcExistingCsv = new FileChooser(new File(ProjectUI.tfProject.text))
      fcExistingCsv.showOpenDialog(tfExistingCsv)
      tfExistingCsv.text = fcExistingCsv.selectedFile.toString
    }
    text = "Existing Input Csv file..."
    enabled = true
    tooltip = "Select the previously converted csv file, for comparison with your newly received csv file"
  }

  // #### Output Csv Input file...
  val tfOuputCsv = new TextField()
  val buttonOuputCsv = new Button {
    action = Action("open"){
      val fcOuputCsv = new FileChooser(new File(ProjectUI.tfProject.text))
      fcOuputCsv.showOpenDialog(tfOuputCsv)
      tfOuputCsv.text = fcOuputCsv.selectedFile.toString
    }
    text = "Ouput Csv file..."
    enabled = true
    tooltip = "Select the file to output your csv to."
  }


  // ### Assemble button and Textfield panels...
  val panelButtons = new GridPanel(3,1){
    contents += buttonExistingCsv
    contents += buttonReceivedCsv
    contents += buttonOuputCsv
  }

  val panelTextFields = new GridPanel(3,1){
    contents += tfExistingCsv
    contents += tfReceivedCsv
    contents += tfOuputCsv
  }


  // ## Build and assemble the action buttons...
  val go2MessagesButton = new Button {
    action = Action("open"){
      println("###### " + tfExistingCsv.text)
//      Csv2Message.csv2csv(tfExistingCsv.text, tfReceivedCsv.text, tfOuputCsv.text)
    }
    text = "Create New Csv"
    enabled = true
    tooltip = "Takes an existing set of translations, and compares with new translations and outputs a new csv"
  }

  val goPanel = new FlowPanel(){
    contents += go2MessagesButton
  }

  def projectUpdated(projDir: String):Unit = {
    tfExistingCsv.text = projDir+"/conf/existingMessages.csv"
    tfReceivedCsv.text = projDir+"/conf/newMessages.csv"
    tfOuputCsv.text = projDir+"/conf/difference.csv"
  }

}
