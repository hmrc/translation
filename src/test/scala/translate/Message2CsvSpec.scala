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


class Message2CsvSpec extends FlatSpec with Matchers with CommonMethods {

  trait FakeWrappedPrintWriter extends WrappedPrintWriter {
    var output: String = ""

    override def writeFile(fileName: String, content: String): Unit = {
      output = content
    }
  }

  object FakeMessage2Csv extends Message2Csv with FakeWrappedPrintWriter

  val messages: String = file("sampleMessages")
  val csvFilePath: String = file("existingTranslations.csv")

  "Message2csv" should
    "read the en message file, find message in tracked csv, and write the result into a new csv file " in {

    FakeMessage2Csv.messages2csv(messages, csvFilePath, "test")
    FakeMessage2Csv.output shouldBe
      "Key\tEnglish\tWelsh\tComments\n" +
        "aaa.bbb.ccc\tThis is english text 1\t\tNo Welsh translation found\n" +
        "aaa.ccc.ddd\tThis is english text 2\tThis is Welsh text 1\tEnglish message unchanged\n" +
        "aaa.ddd.eee\tThis is english text 3\t\tNo Welsh translation found\n" +
        "aaa.eee.fff\tThis is english text 4\tThis is Welsh text 2\tEnglish message unchanged\n" +
        "aaa.fff.ggg\tThis is english text 5\t\tNo Welsh translation found\n" +
        "aaa.ggg.hhh\tThis is english text 6\tThis is Welsh text 3\tEnglish message unchanged\n" +
        "aaa.hhh.iii\tThis is english text 7\t\tNo Welsh translation found\n" +
        "aaa.iii.jjj\tThis is english text 8\t\tNo Welsh translation found\n" +
        "bbb.aaa.bbb\tThis is english text 9\t\tNo Welsh translation found\n" +
        "bbb.bbb.ccc\tThis is english text 10\t\tNo Welsh translation found\n" +
        "bbb.ccc.ddd\tThis is english text 11\t\tNo Welsh translation found\n" +
        "bbb.ddd.eee\tThis is english text 12\tThis is Welsh text 4 with circumflex ystâd\tEnglish message unchanged\n" +
        "bbb.eee.fff\tThis is english text 13\tThis is Welsh text 5\tEnglish message unchanged\n" +
        "ccc.aaa.bbb\tThis is english text 4\tThis is Welsh text 6\tEnglish message unchanged\n" +
        "ggg.aaa.bbb\tThis is english text 15\tThis is Welsh text 9\tEnglish message unchanged\n" +
        "ggg.aaa.ccc\tThis is english text 16\tThis is Welsh text 10\tEnglish message unchanged\n" +
        "ggg.aaa.eee\tThis is english text 17\tThis is Welsh text 11\tEnglish message unchanged\n" +
        "ggg.aaa.fff\tThis is english text 18\t\"This is Welsh text 12\"\tEnglish message unchanged\n"
  }

  val noExistingTranslations: String = file("noExistingTranslations.csv")

  "Message2csv" should
    "read the en message file, find message in tracked csv, and write the result into a new csv file" +
      "and have No welsh found for all lines when it is the initial conversion" in {

    FakeMessage2Csv.messages2csv(messages, noExistingTranslations, "test")
    FakeMessage2Csv.output shouldBe
      "Key\tEnglish\tWelsh\tComments\n" +
        "aaa.bbb.ccc\tThis is english text 1\t\tNo Welsh translation found\n" +
        "aaa.ccc.ddd\tThis is english text 2\t\tNo Welsh translation found\n" +
        "aaa.ddd.eee\tThis is english text 3\t\tNo Welsh translation found\n" +
        "aaa.eee.fff\tThis is english text 4\t\tNo Welsh translation found\n" +
        "aaa.fff.ggg\tThis is english text 5\t\tNo Welsh translation found\n" +
        "aaa.ggg.hhh\tThis is english text 6\t\tNo Welsh translation found\n" +
        "aaa.hhh.iii\tThis is english text 7\t\tNo Welsh translation found\n" +
        "aaa.iii.jjj\tThis is english text 8\t\tNo Welsh translation found\n" +
        "bbb.aaa.bbb\tThis is english text 9\t\tNo Welsh translation found\n" +
        "bbb.bbb.ccc\tThis is english text 10\t\tNo Welsh translation found\n" +
        "bbb.ccc.ddd\tThis is english text 11\t\tNo Welsh translation found\n" +
        "bbb.ddd.eee\tThis is english text 12\t\tNo Welsh translation found\n" +
        "bbb.eee.fff\tThis is english text 13\t\tNo Welsh translation found\n" +
        "ccc.aaa.bbb\tThis is english text 4\t\tNo Welsh translation found\n" +
        "ggg.aaa.bbb\tThis is english text 15\t\tNo Welsh translation found\n" +
        "ggg.aaa.ccc\tThis is english text 16\t\tNo Welsh translation found\n" +
        "ggg.aaa.eee\tThis is english text 17\t\tNo Welsh translation found\n" +
        "ggg.aaa.fff\tThis is english text 18\t\tNo Welsh translation found\n"
  }

  val allTranslations: String = file("allTranslations.csv")

  "Message2csv" should
    "read the en message file, find message in tracked csv, and write the result into a new csv file" +
      "and say that no english content has changed when csv includes welsh translations and no content changes" in {

    FakeMessage2Csv.messages2csv(messages, allTranslations, "test")
    FakeMessage2Csv.output shouldBe
      "Key\tEnglish\tWelsh\tComments\n" +
        "aaa.bbb.ccc\tThis is english text 1\thwn yw testun Saesneg 1\tEnglish message unchanged\n" +
        "aaa.ccc.ddd\tThis is english text 2\thwn yw testun Saesneg 2\tEnglish message unchanged\n" +
        "aaa.ddd.eee\tThis is english text 3\thwn yw testun Saesneg 3\tEnglish message unchanged\n" +
        "aaa.eee.fff\tThis is english text 4\thwn yw testun Saesneg 4\tEnglish message unchanged\n" +
        "aaa.fff.ggg\tThis is english text 5\thwn yw testun Saesneg 5\tEnglish message unchanged\n" +
        "aaa.ggg.hhh\tThis is english text 6\thwn yw testun Saesneg 6\tEnglish message unchanged\n" +
        "aaa.hhh.iii\tThis is english text 7\thwn yw testun Saesneg 7\tEnglish message unchanged\n" +
        "aaa.iii.jjj\tThis is english text 8\thwn yw testun Saesneg 8\tEnglish message unchanged\n" +
        "bbb.aaa.bbb\tThis is english text 9\thwn yw testun Saesneg 9\tEnglish message unchanged\n" +
        "bbb.bbb.ccc\tThis is english text 10\thwn yw testun Saesneg 10\tEnglish message unchanged\n" +
        "bbb.ccc.ddd\tThis is english text 11\thwn yw testun Saesneg 11\tEnglish message unchanged\n" +
        "bbb.ddd.eee\tThis is english text 12\thwn yw testun Saesneg 12\tEnglish message unchanged\n" +
        "bbb.eee.fff\tThis is english text 13\thwn yw testun Saesneg 13\tEnglish message unchanged\n" +
        "ccc.aaa.bbb\tThis is english text 4\thwn yw testun Saesneg 4\tEnglish message unchanged\n" +
        "ggg.aaa.bbb\tThis is english text 15\thwn yw testun Saesneg 15\tEnglish message unchanged\n" +
        "ggg.aaa.ccc\tThis is english text 16\thwn yw testun Saesneg 16\tEnglish message unchanged\n" +
        "ggg.aaa.eee\tThis is english text 17\thwn yw testun Saesneg 17\tEnglish message unchanged\n" +
        "ggg.aaa.fff\tThis is english text 18\thwn yw testun Saesneg 18\tEnglish message unchanged\n"
  }

  val messagesContentChanges: String = file("sampleMessagesContentChange")

  "Message2csv" should
    "read the en message file, find message in tracked csv, and write the result into a new csv file" +
      "and say that english content has changed when csv includes welsh translations and content changes" in {

    FakeMessage2Csv.messages2csv(messagesContentChanges, allTranslations, "test")
    FakeMessage2Csv.output shouldBe
      "Key\tEnglish\tWelsh\tComments\n" +
        "aaa.bbb.ccc\tThis is new english text 1\t\tMessage changed (previous message was: This is english text 1 / hwn yw testun Saesneg 1)\n" +
        "aaa.ccc.ddd\tThis is new english text 2\t\tMessage changed (previous message was: This is english text 2 / hwn yw testun Saesneg 2)\n" +
        "aaa.ddd.eee\tThis is new english text 3\t\tMessage changed (previous message was: This is english text 3 / hwn yw testun Saesneg 3)\n" +
        "aaa.eee.fff\tThis is new english text 4\t\tMessage changed (previous message was: This is english text 4 / hwn yw testun Saesneg 4)\n" +
        "aaa.fff.ggg\tThis is new english text 5\t\tMessage changed (previous message was: This is english text 5 / hwn yw testun Saesneg 5)\n" +
        "aaa.ggg.hhh\tThis is new english text 6\t\tMessage changed (previous message was: This is english text 6 / hwn yw testun Saesneg 6)\n" +
        "aaa.hhh.iii\tThis is new english text 7\t\tMessage changed (previous message was: This is english text 7 / hwn yw testun Saesneg 7)\n" +
        "aaa.iii.jjj\tThis is new english text 8\t\tMessage changed (previous message was: This is english text 8 / hwn yw testun Saesneg 8)\n" +
        "bbb.aaa.bbb\tThis is new english text 9\t\tMessage changed (previous message was: This is english text 9 / hwn yw testun Saesneg 9)\n" +
        "bbb.bbb.ccc\tThis is new english text 10\t\tMessage changed (previous message was: This is english text 10 / hwn yw testun Saesneg 10)\n" +
        "bbb.ccc.ddd\tThis is new english text 11\t\tMessage changed (previous message was: This is english text 11 / hwn yw testun Saesneg 11)\n" +
        "bbb.ddd.eee\tThis is new english text 12\t\tMessage changed (previous message was: This is english text 12 / hwn yw testun Saesneg 12)\n" +
        "bbb.eee.fff\tThis is new english text 13\t\tMessage changed (previous message was: This is english text 13 / hwn yw testun Saesneg 13)\n" +
        "ccc.aaa.bbb\tThis is new english text 4\t\tMessage changed (previous message was: This is english text 4 / hwn yw testun Saesneg 4)\n" +
        "ggg.aaa.bbb\tThis is new english text 15\t\tMessage changed (previous message was: This is english text 15 / hwn yw testun Saesneg 15)\n" +
        "ggg.aaa.ccc\tThis is new english text 16\t\tMessage changed (previous message was: This is english text 16 / hwn yw testun Saesneg 16)\n" +
        "ggg.aaa.eee\tThis is new english text 17\t\tMessage changed (previous message was: This is english text 17 / hwn yw testun Saesneg 17)\n" +
        "ggg.aaa.fff\tThis is new english text 18\t\tMessage changed (previous message was: This is english text 18 / hwn yw testun Saesneg 18)\n"
  }
}




