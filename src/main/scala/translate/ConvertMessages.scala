package translate

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

import scala.swing._
object ConvertMessages extends ConvertMessages with App {}

class UI extends MainFrame {
  title = "Messages Translation"
  preferredSize = new Dimension(320, 240)
  contents = new Label("Contents to go here!")
}


class ConvertMessages {

//    val ui = new UI
//    ui.visible = true

    //compares messages.en with existingTranslations.csv, to create a new, marked-up out.csv

    Message2Csv.messages2csv("messages.en", "existingTranslations.csv", "out.csv")

    //creates _messages.cy, from Translations.csv
    Csv2Message.csv2Messages("Translations.csv", "_messages.cy")
}
