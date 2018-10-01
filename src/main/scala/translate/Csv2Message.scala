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


object Csv2Message extends Csv2Message{}


trait Csv2Message extends CsvReader with WrappedPrintWriter{

  val newLine = "\n"
  val delimiter = "="

  private def firstLine(line: (String, (String, String))): Boolean = {
    line._1.equalsIgnoreCase("key") && line._2._2.equalsIgnoreCase("welsh")
  }

  def csv2Messages(csvFilename:String, outputFileName: String):Unit = {

    val existingTranslations = readFromCsv(csvFilename)

    val content = existingTranslations.filter(line => line._2._2.length > 0 && !firstLine(line)).map{translation =>
      translation._1 + delimiter  + performPunctuationChanges(translation._2._2)  + newLine
    }

    writeFile(outputFileName, content.fold("")((key,value) => key + value))
  }

  def performPunctuationChanges(str: String): String ={
    val sanitisedString = removeQuotes(str)
    applyApostrophesForMessagesFile(sanitisedString)
  }

  def removeQuotes(str: String) : String = {
    if (str.length() >= 2 && str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
      str.substring(1, str.length() - 1)
    }
    else {
      str
    }
  }

  def applyApostrophesForMessagesFile(str: String) : String = {
    str.replaceAll("''","'").replaceAll("'","''")
  }
}