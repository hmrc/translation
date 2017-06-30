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

import util.{CsvReader, FileReader, KeyValueParser, WrappedPrintWriter}

object Message2Csv extends Message2Csv{}

trait Message2Csv extends KeyValueParser with FileReader with CsvReader with WrappedPrintWriter{

  val csvHeader = "Key\tEnglish\tWelsh\tComments"
  val delimiter = "\t"
  val token = "="
  val noWelshFound = "No Welsh translation found"
  val noEnglishFound = "No English messages found"
  val englishUnchanged = "English message unchanged"
  val englishChanged = "Message changed (previous message was: "
  val separator = " / "
  val englishChangedEnd = ")"
  val newLine = "\n"

  type translationLine = (String,(String, String))
  type messageLine = (String, String)



  def messages2csv(englishMessagesFileName: String, csvInputFileName: String, csvOutputFileName: String):Unit = {

    val enMap = fetchMessages(englishMessagesFileName)
    val existingTranslations = readFromCsv(csvInputFileName)


    val csvLines = enMap.map{ enMessage =>

      val oExistingTranslation = existingTranslations.find(translation => enMessage._1 == translation._1)

      oExistingTranslation.fold(enMessage._1 + delimiter + enMessage._2 + delimiter + delimiter + noWelshFound)
      {existingTranslation =>
        checkEnglishMessageChanged(existingTranslation, enMessage)
      } + newLine
    }

    writeFile(csvOutputFileName, csvHeader + newLine + csvLines.fold("")((key,value) => key + value))
  }

  private def checkEnglishMessageChanged(translation: translationLine, enMessage: messageLine): String = {
    if(translation._2._1 == enMessage._2){
      if(translation._2._2 == ""){
        translation._1 + delimiter + enMessage._2 + delimiter + delimiter + noWelshFound
      }
      else {
        translation._1 + delimiter + translation._2._1 + delimiter + translation._2._2  + delimiter + englishUnchanged
      }
    }
    else{
      translation._1 + "\t" + enMessage._2 + delimiter + delimiter + englishChanged+ translation._2._1+ separator +translation._2._2 + englishChangedEnd
    }
  }


  def fetchMessages(lang:String):Map[String, String] = {
    val lines = for (line <- linesFromFile(lang)) yield line
    lines.flatMap{ line =>
      splitKeyValues(line, token).map(line => line._1 -> line._2._1)
    }.toMap
  }
}