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

class Csv2MessageSpec extends FlatSpec with Matchers with CommonMethods {

  val inputCsvFile = "testCsvFilename"
  val outputMsgFile = "testMsgFilename"

  trait FakeWrappedPrintWriter extends WrappedPrintWriter{
    var output = ""

    override def writeFile(fileName: String, content: String): Unit = {
      output = content
    }
  }

  object testCsv2Message extends Csv2Message with FakeWrappedPrintWriter

  val allCsvFilePath: String = file("allTranslations.csv")

  "translate.Csv2Message" should
    "read and map the csv file values, separated by TABs" in {
      val result = testCsv2Message.readFromCsv(allCsvFilePath)
      result shouldBe List(
        "aaa.bbb.ccc" -> ("This is english text 1", "hwn yw testun Saesneg 1"),
        "aaa.ccc.ddd" -> ("This is english text 2", "hwn yw testun Saesneg 2"),
        "aaa.ddd.eee" -> ("This is english text 3", "hwn yw testun Saesneg 3"),
        "aaa.eee.fff" -> ("This is english text 4", "hwn yw testun Saesneg 4"),
        "aaa.fff.ggg" -> ("This is english text 5", "hwn yw testun Saesneg 5"),
        "aaa.ggg.hhh" -> ("This is english text 6", "hwn yw testun Saesneg 6"),
        "aaa.hhh.iii" -> ("This is english text 7", "hwn yw testun Saesneg 7"),
        "aaa.iii.jjj" -> ("This is english text 8", "hwn yw testun Saesneg 8"),
        "bbb.aaa.bbb" -> ("This is english text 9", "hwn yw testun Saesneg 9"),
        "bbb.bbb.ccc" -> ("This is english text 10", "hwn yw testun Saesneg 10"),
        "bbb.ccc.ddd" -> ("This is english text 11", "hwn yw testun Saesneg 11"),
        "bbb.ddd.eee" -> ("This is english text 12", "hwn yw testun Saesneg 12"),
        "bbb.eee.fff" -> ("This is english text 13", "hwn yw testun Saesneg 13"),
        "ccc.aaa.bbb" -> ("This is english text 4", "hwn yw testun Saesneg 4"),
        "ggg.aaa.bbb" -> ("This is english text 15", "hwn yw testun Saesneg 15"),
        "ggg.aaa.ccc" -> ("This is english text 16", "hwn yw testun Saesneg 16"),
        "ggg.aaa.eee" -> ("This is english text 17", "hwn yw testun Saesneg 17"),
        "ggg.aaa.fff" -> ("This is english text 18", "hwn yw testun Saesneg 18"))
  }

  "translate.Csv2Message" should
    "write to message files from values in the csv file values" in {
    val result = testCsv2Message.csv2Messages(allCsvFilePath, outputMsgFile)
    testCsv2Message.output shouldBe "" +
      "aaa.bbb.ccc=hwn yw testun Saesneg 1\n" +
      "aaa.ccc.ddd=hwn yw testun Saesneg 2\n" +
      "aaa.ddd.eee=hwn yw testun Saesneg 3\n" +
      "aaa.eee.fff=hwn yw testun Saesneg 4\n" +
      "aaa.fff.ggg=hwn yw testun Saesneg 5\n" +
      "aaa.ggg.hhh=hwn yw testun Saesneg 6\n" +
      "aaa.hhh.iii=hwn yw testun Saesneg 7\n" +
      "aaa.iii.jjj=hwn yw testun Saesneg 8\n" +
      "bbb.aaa.bbb=hwn yw testun Saesneg 9\n" +
      "bbb.bbb.ccc=hwn yw testun Saesneg 10\n" +
      "bbb.ccc.ddd=hwn yw testun Saesneg 11\n" +
      "bbb.ddd.eee=hwn yw testun Saesneg 12\n" +
      "bbb.eee.fff=hwn yw testun Saesneg 13\n" +
      "ccc.aaa.bbb=hwn yw testun Saesneg 4\n" +
      "ggg.aaa.bbb=hwn yw testun Saesneg 15\n" +
      "ggg.aaa.ccc=hwn yw testun Saesneg 16\n" +
      "ggg.aaa.eee=hwn yw testun Saesneg 17\n" +
      "ggg.aaa.fff=hwn yw testun Saesneg 18\n"
  }

  val existingCsvFilePath: String = file("existingTranslations.csv")

  "translate.Csv2Message" should
    "read and map the csv file values, separated by TABs for a file with quotes included" in {
      val result = testCsv2Message.readFromCsv(existingCsvFilePath)
      result shouldBe List(
        "aaa.bbb.ccc" -> ("This is english text 1", ""),
        "aaa.ccc.ddd" -> ("This is english text 2", "This is Welsh text 1"),
        "aaa.ddd.eee" -> ("This is english text 3", ""),
        "aaa.eee.fff" -> ("This is english text 4", "This is Welsh text 2"),
        "aaa.fff.ggg" -> ("This is english text 5", ""),
        "aaa.ggg.hhh" -> ("This is english text 6", "This is Welsh text 3"),
        "aaa.hhh.iii" -> ("This is english text 7", ""),
        "aaa.iii.jjj" -> ("This is english text 8", ""),
        "bbb.aaa.bbb" -> ("This is english text 9", ""),
        "bbb.bbb.ccc" -> ("This is english text 10", ""),
        "bbb.ccc.ddd" -> ("This is english text 11", ""),
        "bbb.ddd.eee" -> ("This is english text 12", "This is Welsh text 4 with circumflex ystâd"),
        "bbb.eee.fff" -> ("This is english text 13", "This is Welsh text 5"),
        "ccc.aaa.bbb" -> ("This is english text 4",  "This is Welsh text 6"),
        "ggg.aaa.bbb" -> ("This is english text 15", "This is Welsh text 9"),
        "ggg.aaa.ccc" -> ("This is english text 16", "This is Welsh text 10"),
        "ggg.aaa.eee" -> ("This is english text 17", "This is Welsh text 11"),
        "ggg.aaa.fff" -> ("This is english text 18", "\"This is Welsh text 12\""))
  }

  "translate.Csv2Message" should
    "write to message files from values in the csv file values, removing any quotes on Welsh translation" in {
    val result = testCsv2Message.csv2Messages(existingCsvFilePath, outputMsgFile)
    testCsv2Message.output shouldBe "aaa.ccc.ddd=This is Welsh text 1\n" +
                                    "aaa.eee.fff=This is Welsh text 2\n" +
                                    "aaa.ggg.hhh=This is Welsh text 3\n" +
                                    "bbb.ddd.eee=This is Welsh text 4 with circumflex ystâd\n" +
                                    "bbb.eee.fff=This is Welsh text 5\n" +
                                    "ccc.aaa.bbb=This is Welsh text 6\n" +
                                    "ggg.aaa.bbb=This is Welsh text 9\n" +
                                    "ggg.aaa.ccc=This is Welsh text 10\n" +
                                    "ggg.aaa.eee=This is Welsh text 11\n" +
                                    "ggg.aaa.fff=This is Welsh text 12\n"

  }
}


