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

import org.scalatest.{FlatSpec, Matchers}
import util.WrappedPrintWriter

class Csv2MessageSpec extends FlatSpec with Matchers {

  val inputCsvFile = "testCsvFilename"
  val outputMsgFile = "testMsgFilename"

  trait FakeWrappedPrintWriter extends WrappedPrintWriter{
    var output = ""

    override def writeFile(fileName: String, content: String): Unit = {
      output = content
    }
  }

  object testCsv2Message extends Csv2Message with FakeWrappedPrintWriter{

    override def linesFromFile(fileName: String):Iterator[String] = {
      val line1 = "key 1 \t English 1 \t Welsh 1"
      val line2 = "key 2 \t English 2"
      val line3 = "key 300 \t English 3 \t Welsh 3"
      Iterator(line1, line2, line3)
    }
  }


  "translate.Csv2Message" should
    "read and map the csv file values, separated by TABs" in {
      val result = testCsv2Message.readFromCsv("en")
      result shouldBe Map(
        "key 1" -> ("English 1", "Welsh 1"),
        "key 2" ->   ("English 2", ""),
        "key 300" -> ("English 3", "Welsh 3"))
  }


  "translate.Csv2Message" should
    "write to message files from values in the csv file values" in {
    val result = testCsv2Message.csv2Messages(inputCsvFile, outputMsgFile)
    testCsv2Message.output shouldBe "key 1=Welsh 1\n" +
                                    "key 300=Welsh 3\n"

  }
}


