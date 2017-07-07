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

import util.{WrappedPrintWriter, CsvReader}
import scala.swing._

object ConvertMessages extends ConvertMessages with App {}

class UI extends MainFrame {
  title = "Messages Translation"
  preferredSize = new Dimension(320, 240)
  contents = new Label("Contents to go here!")
}

class ConvertMessages extends CsvReader with WrappedPrintWriter{

//    val ui = new UI
//    ui.visible = true

  val cwd = System.getProperty("user.dir")

    //compares messages.en with existingTranslations.csv, to create a new, marked-up out.csv

  val receivedTranslationsFile = s"$cwd/receivedTranslations.csv"
  val existingTranslationsFile = s"$cwd/existingTranslations.csv"

  //val outFile = s"$cwd/out.csv"
  val msgFile = s"$cwd/messages.cy"

    CompareCvs.messages2csv2(receivedTranslationsFile, existingTranslationsFile, existingTranslationsFile)

    //creates _messages.cy, from Translations.csv
    Csv2Message.csv2Messages(existingTranslationsFile, msgFile)
}
