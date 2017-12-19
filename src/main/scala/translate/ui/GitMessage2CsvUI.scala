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

import translate.GitMessage2Csv
import util.PathParser

import scala.swing._

object GitMessage2CsvUI extends GitMessage2CsvUI

trait GitMessage2CsvUI extends PathParser{


  val tfGitRefIn = new TextField("https://github.com/hmrc/xxxxxxxxx.git")
  val lbGitRefIn = new Label("Git Clone Uri:"){
    tooltip = "The git project uri, to be examined"
  }


  val tfGitCommitRef = new TextField("aa12345")
  val lbGitCommitRef= new Label("Git Commit Ref:"){
    tooltip = "The commit where your messages were last translated into Welsh"
  }


  // #### Output Csv Output file...
  val tfOutputCsv = new TextField("messages_compared.csv")
  val buttonOutputCsv = new Button {
    action = Action("open"){
      val fcOutputCsv = new FileChooser(new File(extractPath(tfOutputCsv.text)))
      fcOutputCsv.showOpenDialog(tfOutputCsv)
      if(fcOutputCsv.selectedFile != null) {tfOutputCsv.text = fcOutputCsv.selectedFile.toString}
    }
    text = "Output Csv file..."
    enabled = true
    tooltip = "Select the file to output your csv to."
  }

  // ### Assemble label and Textfield panels...
  val panelButtons = new GridPanel(3,1){
    contents += lbGitRefIn
    contents += lbGitCommitRef
    contents += buttonOutputCsv
  }

  val panelTextFields = new GridPanel(3,1){
    contents += tfGitRefIn
    contents += tfGitCommitRef
    contents += tfOutputCsv
  }


  // ## Build and assemble the go Panel...
  val go2CsvButton = new Button {
    action = Action("go"){

      val lastPeriod:Int = tfGitRefIn.text.lastIndexOf('.')
      val lastSlash:Int = tfGitRefIn.text.lastIndexOf('/')
      val projectName = tfGitRefIn.text.substring(lastSlash+1, lastPeriod)
      GitMessage2Csv.fetchGitFiles(projectName, tfGitRefIn.text, tfGitCommitRef.text)
      GitMessage2Csv.messages2csv(tfOutputCsv.text)
    }
    text = "Create Csv file"
    enabled = true
    tooltip = "Compares current/previous English & Welsh messages from specified project, in order to create a new marked-up Csv, for translation."
  }

  val goPanel = new FlowPanel(){
    contents += go2CsvButton
  }
}
