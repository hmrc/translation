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
import java.awt.{Color, Dimension}
import java.io.File

import scala.swing.BorderPanel.Position
import scala.swing._



class UI extends MainFrame {
  title = "Messages Translation"
  preferredSize = new Dimension(900, 190)
  visible = true


  val btnChooseCsv2Msg = new Button {
    action = Action("select"){
      showCsv2Msg()
      updateButons(this)
    }
    text = "Csv to Messages"
    enabled = true
    tooltip = "Takes translated csv file, and creates a messages messages file."
  }

  val btnChooseGitMsg2Csv = new Button {
    action = Action("select"){
      showGitMsg2Csv()
      updateButons(this)
    }
    text = "Messages to Csv"
    enabled = false
    tooltip = "Compares previous messages files with with current message files, to create a csv report, for possible translation"
  }

  val btnChooseCsv2Csv = new Button {
    action = Action("select"){
      showCsv2Csv()
      updateButons(this)
    }
    text = "Merge Csv files"
    enabled = true
    tooltip = "Merges the newly received csv file and existing csv file, into the specified output file"
  }


  def updateButons(current: Button):Unit = {
    btnChooseCsv2Msg.enabled = true
    btnChooseGitMsg2Csv.enabled = true
    btnChooseCsv2Csv.enabled = true
    current.enabled = false
  }


  val modePanel = new FlowPanel(){
    contents += new Label("Choose a mode: ")
    contents += btnChooseGitMsg2Csv
    contents += btnChooseCsv2Msg
    contents += btnChooseCsv2Csv
  }

  val projectAndModePanel = new BorderPanel {
    layout(modePanel) = Position.South
  }



  def showCsv2Msg(): Unit = {
    val borderPanel = new BorderPanel {
      layout(projectAndModePanel) = Position.North
      layout(Csv2MessageUI.panelButtons) = Position.West
      layout(Csv2MessageUI.panelTextFields) = Position.Center
      layout(Csv2MessageUI.goPanel) = Position.South
    }
    contents = borderPanel
  }

  def showGitMsg2Csv(): Unit = {
    val borderPanel = new BorderPanel {
      layout(projectAndModePanel) = Position.North
      layout(GitMessage2CsvUI.panelButtons) = Position.West
      layout(GitMessage2CsvUI.panelTextFields) = Position.Center
      layout(GitMessage2CsvUI.goPanel) = Position.South
    }
    contents = borderPanel
  }

  def showCsv2Csv(): Unit = {
    val borderPanel = new BorderPanel {
      layout(projectAndModePanel) = Position.North
      layout(CompareCsvUI.panelButtons) = Position.West
      layout(CompareCsvUI.panelTextFields) = Position.Center
      layout(CompareCsvUI.goPanel) = Position.South
    }
    contents = borderPanel
  }



  showGitMsg2Csv()
}
