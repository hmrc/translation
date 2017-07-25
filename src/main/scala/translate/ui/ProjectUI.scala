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

import scala.swing.BorderPanel.Position
import scala.swing._


object ProjectUI extends ProjectUI

trait ProjectUI {
  val tfProject = new TextField(".")
  val btnProject = new Button {
    action = Action("open") {
      val fcProject = new FileChooser(new File("."))
      fcProject.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
      fcProject.showOpenDialog(tfProject)
      if(fcProject.selectedFile != null) {tfProject.text = fcProject.selectedFile.toString}

      Csv2CsvUI.projectUpdated(fcProject.selectedFile.toString)
      Csv2MessageUI.projectUpdated(fcProject.selectedFile.toString)
      Message2CsvUI.projectUpdated(fcProject.selectedFile.toString)
    }
    text = "Project dir..."
    enabled = true
    tooltip = "Select the working directory, for the files below."
  }

  val projectPanel = new BorderPanel {
    layout(btnProject) = Position.West
    layout(tfProject) = Position.Center
  }

}
