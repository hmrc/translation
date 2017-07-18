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

import translate.Csv2Message

import scala.swing._

object Csv2MessageUI extends Csv2MessageUI

trait Csv2MessageUI {


  // #### Input Csv Input file...
  val tfExistingCsv = new TextField()
  val buttonExistingCsv = new Button {
    action = Action("open"){
      val fcExistingCsv = new FileChooser(new File(ProjectUI.tfProject.text))
      fcExistingCsv.showOpenDialog(tfExistingCsv)
      tfExistingCsv.text = fcExistingCsv.selectedFile.toString
    }
    text = "Input Csv file..."
    enabled = true
    tooltip = "Select the previously converted csv file, for comparison with your Messages File"
  }

  // #### Messages file...
  val tfMessagesOut = new TextField()
  val buttonMessagesOut = new Button {
    action = Action("open"){
      val fcMessagesOut = new FileChooser(new File(ProjectUI.tfProject.text))
      fcMessagesOut.showOpenDialog(tfMessagesOut)
      tfMessagesOut.text = fcMessagesOut.selectedFile.toString
    }
    text = "Output Message file..."
    enabled = true
    tooltip = "Select the English messages file you'd like to output to"
  }




  // ### Assemble button and Textfield panels...
  val panelButtons = new GridPanel(3,1){
    contents += buttonExistingCsv
    contents += buttonMessagesOut
  }

  val panelTextFields = new GridPanel(3,1){
    contents += tfExistingCsv
    contents += tfMessagesOut
  }


  // ## Build and assemble the action buttons...
  val go2MessagesButton = new Button {
    action = Action("open"){
      println("###### " + tfExistingCsv.text)
      Csv2Message.csv2Messages(tfExistingCsv.text, tfMessagesOut.text)
    }
    text = "Create Messages File"
    enabled = true
    tooltip = "Extracts the Welsh translations from a csv file, to create a Welsh Play! messages file."
  }

  val goPanel = new FlowPanel(){
    contents += go2MessagesButton

  }

}
