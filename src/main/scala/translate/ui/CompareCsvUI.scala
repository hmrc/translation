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

import translate.{CompareCsv, Csv2Message}
import util.PathParser

import scala.swing._

object CompareCsvUI extends CompareCsvUI

trait CompareCsvUI extends PathParser{

  // #### Input Csv Input file...
  val tfExistingCsv = new TextField()
  val buttonExistingCsv = new Button {
    action = Action("open"){
      val fcExistingCsv = new FileChooser(new File(extractPath(tfExistingCsv.text)))
      fcExistingCsv.showOpenDialog(tfExistingCsv)
      if(fcExistingCsv.selectedFile != null){tfExistingCsv.text = fcExistingCsv.selectedFile.toString}
    }
    text = "Existing Csv file..."
    enabled = true
    tooltip = "Select the existing csv with Welsh translations"
  }

  // #### New Csv file...
  val tfNewCsv = new TextField()
  val buttonNewCsv = new Button {
    action = Action("open"){
      val fcNewCsv = new FileChooser(new File(extractPath(tfNewCsv.text)))
      fcNewCsv.showOpenDialog(tfNewCsv)
      if(fcNewCsv.selectedFile != null) {tfNewCsv.text = fcNewCsv.selectedFile.toString}
    }
    text = "New Csv file..."
    enabled = true
    tooltip = "Select the newly received csv with Welsh translations"
  }

  // #### Output Csv file...
  val tfOutputCsv = new TextField()
  val buttonCsvOut = new Button {
    action = Action("open"){
      val fcMessagesOut = new FileChooser(new File(extractPath(tfOutputCsv.text)))
      fcMessagesOut.showOpenDialog(tfOutputCsv)
      if(fcMessagesOut.selectedFile != null) {tfOutputCsv.text = fcMessagesOut.selectedFile.toString}
    }
    text = "Output csv file..."
    enabled = true
    tooltip = "Select the csv file you'd like to output to"
  }




  // ### Assemble button and Textfield panels...
  val panelButtons = new GridPanel(3,1){
    contents += buttonExistingCsv
    contents += buttonNewCsv
    contents += buttonCsvOut
  }

  val panelTextFields = new GridPanel(3,1){
    contents += tfExistingCsv
    contents += tfNewCsv
    contents += tfOutputCsv
  }


  // ## Build and assemble the action buttons...
  val go2MessagesButton = new Button {
    action = Action("open"){
      println("###### " + tfExistingCsv.text)
      val cwd = System.getProperty("user.dir")
      CompareCsv.csv2csv(tfNewCsv.text, tfExistingCsv.text, tfOutputCsv.text )
    }
    text = "Merge existing and new csv Files"
    enabled = true
    tooltip = "Merges the newly received csv file and existing csv file, into the specified output file"
  }

  val goPanel = new FlowPanel(){
  contents += go2MessagesButton

  }

  def projectUpdated(projDir: String):Unit = {
    tfExistingCsv.text = projDir+"/conf/existingMessages.csv"
    tfNewCsv.text = projDir+"/conf/messages_created.cy"
  }

}
