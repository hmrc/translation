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


  //### Build and assemble the mode buttons.
  val btnChooseMsg2Csv = new Button {
    action = Action("select"){
      showMsg2Csv()
      updateButons(this)
      ProjectUI.enable(true)
    }

    text = "Messages to Csv"
    enabled = true
    tooltip = "Takes English messages and an existing set of translations, in order to create a new marked-up Csv, for translation."
  }

  val btnChooseCsv2Msg = new Button {
    action = Action("select"){
      showCsv2Msg()
      updateButons(this)
      ProjectUI.enable(true)
    }
    text = "Csv to Messages"
    enabled = true
    tooltip = "Takes English messages and an existing set of translations, in order to create a new marked-up Csv, for translation."
  }

  val btnChooseGitMsg2Csv = new Button {
    action = Action("select"){
      showGitMsg2Csv()
      updateButons(this)
      ProjectUI.enable(false)
    }
    text = "Git Msg to Csv"
    enabled = false
    tooltip = "Takes an existing set of translations, and compares with new translations and outputs a new csv"
  }

  val btnChooseCsv2Csv = new Button {
    action = Action("select"){
      showCsv2Csv()
      updateButons(this)
      ProjectUI.enable(true)
    }
    text = "Csv to Csv (TBD)"
    enabled = true
    tooltip = "Takes an existing set of translations, and compares with new translations and outputs a new csv"
  }

  def updateButons(current: Button):Unit = {
    btnChooseMsg2Csv.enabled = true
    btnChooseCsv2Msg.enabled = true
    btnChooseGitMsg2Csv.enabled = true
    btnChooseCsv2Csv.enabled = true
    current.enabled = false
  }


  val modePanel = new FlowPanel(){
    contents += new Label("Choose a mode: ")
    contents += btnChooseGitMsg2Csv
    contents += btnChooseCsv2Msg
    contents += btnChooseMsg2Csv


    //Disabled until IHT merge in their branch
    //    contents += btnChooseCsv2Csv
  }

  val projectAndModePanel = new BorderPanel {
    layout(ProjectUI.projectPanel) = Position.North
    layout(modePanel) = Position.South
  }


  def showMsg2Csv(): Unit = {
    val borderPanel = new BorderPanel {
      layout(projectAndModePanel) = Position.North
      layout(Message2CsvUI.panelButtons) = Position.West
      layout(Message2CsvUI.panelTextFields) = Position.Center
      layout(Message2CsvUI.goPanel) = Position.South
    }
    contents = borderPanel
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
      layout(Csv2CsvUI.panelButtons) = Position.West
      layout(Csv2CsvUI.panelTextFields) = Position.Center
      layout(Csv2CsvUI.goPanel) = Position.South
    }
    contents = borderPanel
  }

  showGitMsg2Csv()
  ProjectUI.enable(false)
}
