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

import org.scalatest._
import util.WrappedPrintWriter


class Message2CsvSpec extends FlatSpec with Matchers {

  val inputCsvFile = "existingTranslations.csv"
  val inputMessagesFile = "Messages.en"

  trait FakeWrappedPrintWriter extends WrappedPrintWriter {
    var output: String = ""

    override def writeFile(fileName: String, content: String): Unit = {
      output = content
    }
  }


  object FakeMessage2Csv extends Message2Csv with FakeWrappedPrintWriter {

    override def readFromCsv(fileName: String): Map[String, (String, String)] = {
      Map("key 1" -> ("English message 1","Welsh message 1"),
          "key 2" -> ("English message 2","Welsh message 2"),
          "key 3" -> ("English message 3",""))
    }

    override def fetchMessages(lang:String):Map[String, String] = {
      Map(("key 1" -> "English message 1"),
          ("key 2" -> "English updated message 2"),
          ("key 3" -> "English message 3"),
          ("key 4" -> "English message 4"))
    }
  }


  "Message2csv" should
  "read the en message file, find message in tracked csv, and write the result into a new csv file " in {
    val result = FakeMessage2Csv.messages2csv("fake","fake","fake")
    FakeMessage2Csv.output shouldBe "Key\tEnglish\tWelsh\tComments\n" +
                                            "key 1\tEnglish message 1\tWelsh message 1\tEnglish message unchanged\n" +    // Already translated
                                            "key 2\tEnglish updated message 2\t\tMessage changed (previous message was: English message 2 / Welsh message 2)\n" +   // Message changed
                                            "key 3\tEnglish message 3\t\tNo Welsh translation found\n" +                  // Welsh empty in csv
                                            "key 4\tEnglish message 4\t\tNo Welsh translation found\n"                    // New Message
  }
}




