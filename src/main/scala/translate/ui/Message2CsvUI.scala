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


  // #### Csv Input file...
  val tfInputCsv = new TextField("previous_messages.csv")
  val buttonInputCsv = new Button {
    action = Action("open"){
      val fcInputCsv = new FileChooser(new File(extractPath(tfInputCsv.text)))
      fcInputCsv.showOpenDialog(tfInputCsv)
      if(fcInputCsv.selectedFile != null) {tfInputCsv.text = fcInputCsv.selectedFile.toString}
    }
    text = "Previous Csv file..."
    enabled = true
    tooltip = "Select the file to read your previous csv from."
  }


  // #### Csv Input file...
  val tfInputMessage = new TextField("messages")
  val buttonInputMessage = new Button {
    action = Action("open"){
      val fcInputMessage = new FileChooser(new File(extractPath(tfInputMessage.text)))
      fcInputMessage.showOpenDialog(tfInputMessage)
      if(fcInputMessage.selectedFile != null) {tfInputMessage.text = fcInputMessage.selectedFile.toString}
    }
    text = "Current messages file..."
    enabled = true
    tooltip = "Select your current English messsages file."
  }





  // #### Output Csv Output file...
  val tfOuputCsv = new TextField("messages_compared.csv")
  val buttonOuputCsv = new Button {
    action = Action("open"){
      val fcOuputCsv = new FileChooser(new File(extractPath(tfOuputCsv.text)))
      fcOuputCsv.showOpenDialog(tfOuputCsv)
      if(fcOuputCsv.selectedFile != null) {tfOuputCsv.text = fcOuputCsv.selectedFile.toString}
    }
    text = "Output Csv file..."
    enabled = true
    tooltip = "Select the file to output your csv to."
  }

  // ### Assemble label and Textfield panels...
  val panelButtons = new GridPanel(3,1){
    contents += buttonInputCsv
    contents += buttonInputMessage
    contents += buttonOuputCsv
  }

  val panelTextFields = new GridPanel(3,1){
    contents += tfInputCsv
    contents += tfInputMessage
    contents += tfOuputCsv
  }


  // ## Build and assemble the go Panel...
  val go2CsvButton = new Button {
    action = Action("go"){
      Message2Csv.messages2csv(tfInputMessage.text, tfInputCsv.text, tfOuputCsv.text)
    }
    text = "Create Csv file"
    enabled = true
    tooltip = "Compares specified English messages with a specified translated cvs file, in order to create a new marked-up Csv."
  }

  val goPanel = new FlowPanel(){
    contents += go2CsvButton
  }
}
