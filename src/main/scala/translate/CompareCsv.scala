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

import util.{CsvReader, WrappedPrintWriter}

object CompareCvs extends CompareCsv{}

trait CompareCsv extends CsvReader with WrappedPrintWriter{

  val delimiter = "\t"
  val newLine = "\n"


  // IHT version
  def messages2csv2(receivedInputFileName: String, existingInputFileName: String, csvOutputFileName: String): Unit = {
    val receivedMap = readFromCsv(receivedInputFileName)
    val existingMap = readFromCsv(existingInputFileName)

    def outputLine(key: String, receivedEnglish: String, receivedWelsh: String, message:String) = {
      key + delimiter + receivedEnglish + delimiter + receivedWelsh + delimiter + message
    }
    val receivedLines = receivedMap.map(receivedItem => {
      val key = receivedItem._1
      val receivedEnglish = receivedItem._2._1
      val receivedWelsh = receivedItem._2._2

      val result = existingMap.find(existingItem => receivedItem._1 == existingItem._1) match {
        case None => outputLine(key, receivedEnglish, receivedWelsh, "Added.")
        case Some(matchedExistingItem) =>
          val matchedEnglish = matchedExistingItem._2._1
          val matchedWelsh = matchedExistingItem._2._2
          val newOutput = ChooseContent.chooseContent(receivedEnglish, receivedWelsh, matchedEnglish, matchedWelsh)
          outputLine(key, newOutput._1, newOutput._2, newOutput._3)
      }
      result + newLine
    }
    )
    val unaffectedItems: Map[String, (String, String)] = existingMap.filter(existingItem => !receivedMap.exists(receivedItem => receivedItem._1 == existingItem._1))
    val existingLinesUnaffected = unaffectedItems.map( xx => outputLine( xx._1, xx._2._1, xx._2._2, "Unchanged." ) + newLine)

    val ee = receivedLines.fold("")((key, value) => key + value) +
      existingLinesUnaffected.fold("")((key, value) => key + value)

    writeFile(csvOutputFileName, ee)
  }

}
