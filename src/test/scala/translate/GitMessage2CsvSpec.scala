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


class GitMessage2CsvSpec extends FlatSpec with Matchers {

  trait FakeWrappedPrintWriter extends WrappedPrintWriter {
    var output: String = ""

    override def writeFile(fileName: String, content: String): Unit = {
      output = content
    }
  }

  object FakeGitMessage2Csv extends GitMessage2Csv with FakeWrappedPrintWriter with CommonMethods {

    override val currentEnglishMessages:String = file(s"test_current_messages")
    override val currentWelshMessages:String = file(s"test_current_messages.cy")
    override val oldEnglishMessages:String = file(s"test_old_messages")
  }

  object NewContent extends GitMessage2Csv with FakeWrappedPrintWriter with CommonMethods {

    override val currentEnglishMessages:String =  file(s"test_current_changes_messages")
    override val currentWelshMessages:String = file(s"test_current_messages.cy")
    override val oldEnglishMessages:String = file(s"test_old_messages")

  }

  object UpdatedEnglishContent extends GitMessage2Csv with FakeWrappedPrintWriter with CommonMethods {

    override val currentEnglishMessages:String =  file(s"test_updated_content_messages")
    override val currentWelshMessages:String = file(s"test_current_messages.cy")
    override val oldEnglishMessages:String = file(s"test_old_messages")

  }

  "Message2csv" should
  "read the current and old English message file and current Welsh message file, compare and write the result into a csv file " in {

    FakeGitMessage2Csv.messages2csv("")
    FakeGitMessage2Csv.output shouldBe "" +
      "Key\tEnglish\tWelsh\tComments\n" +
      "tractorBoots\tSisyphus off-the-runway lace up tractor boots\tSisyphus les i fyny esgidiau tractor oddi ar y rhedfa\tEnglish message unchanged\n" +
      "tractorSock\tSisyphus tractor sock sneakers\tHyfforddwyr sanau tractor Sisyphus\tEnglish message unchanged\n" +
      "dirtSandals\tSisyphus hiking sandals\tSandalau heicio Sisyphus\tEnglish message unchanged\n" +
      "obliqueSock\tSisyphus oblique runner stretch sock sneakers\tSisyphus hyfforddwyr sanau oblique ymestyn rhedwr\tEnglish message unchanged\n" +
      "geos\tSisyphus geobaskets\tGeobaskets Sisyphus\tEnglish message unchanged\n" +
      "creepers\tSisyphus lace-up creeper boots\tSisyphus esgidiau crafu tywallt\tEnglish message unchanged\n" +
      "stooges\tStooges cropped jacket\tSiaced croen Stooges\tEnglish message unchanged\n" +
      "bullet\tForever bullet jacket\tDaw'r siaced bwled\tEnglish message unchanged\n" +
      "intarsia\tForever intarsia high neck jacket\tSiaced gwddf uchel intarsia i gyd\tEnglish message unchanged\n" +
      "puffer\tSisyphus jumbo duvet coat\tCôt duvet Sisyphus jumbo\tEnglish message unchanged\n"
  }

  "Message2csv" should
  "read the current and old English message file and current Welsh message file, compare and write the result into a csv file" +
    "when there are changes to the current messages" in {

    NewContent.messages2csv("")
    NewContent.output shouldBe "" +
      "Key\tEnglish\tWelsh\tComments\n" +
      "tractorBoots\tSisyphus off-the-runway lace up tractor boots\tSisyphus les i fyny esgidiau tractor oddi ar y rhedfa\tEnglish message unchanged\n" +
      "tractorSock\tSisyphus tractor sock sneakers\tHyfforddwyr sanau tractor Sisyphus\tEnglish message unchanged\n" +
      "dirtSandals\tSisyphus hiking sandals\tSandalau heicio Sisyphus\tEnglish message unchanged\n" +
      "obliqueSock\tSisyphus oblique runner stretch sock sneakers\tSisyphus hyfforddwyr sanau oblique ymestyn rhedwr\tEnglish message unchanged\n" +
      "geos\tSisyphus geobaskets\tGeobaskets Sisyphus\tEnglish message unchanged\n" +
      "creepers\tSisyphus lace-up creeper boots\tSisyphus esgidiau crafu tywallt\tEnglish message unchanged\n" +
      "sockWedges\tSisyphus ruhlmann sock wedges\t\tNo Welsh translation found\n" +                                //Additional new content
      "stooges\tStooges cropped jacket\tSiaced croen Stooges\tEnglish message unchanged\n" +
      "bullet\tForever bullet jacket\tDaw'r siaced bwled\tEnglish message unchanged\n" +
      "intarsia\tForever intarsia high neck jacket\tSiaced gwddf uchel intarsia i gyd\tEnglish message unchanged\n" +
      "puffer\tSisyphus jumbo duvet coat\tCôt duvet Sisyphus jumbo\tEnglish message unchanged\n"
  }

  "Message2csv" should
  "read the current and old English message file and current Welsh message file, compare and write the result into a csv file" +
    "when existing content has changed" in {

    UpdatedEnglishContent.messages2csv("")
    UpdatedEnglishContent.output shouldBe "" +
      "Key\tEnglish\tWelsh\tComments\n" +
      "tractorBoots\tSisyphus off-the-runway lace up tractor boots\tSisyphus les i fyny esgidiau tractor oddi ar y rhedfa\tEnglish message unchanged\n" +
      "tractorSock\tSisyphus tractor sock sneakers\tHyfforddwyr sanau tractor Sisyphus\tEnglish message unchanged\n" +
      "dirtSandals\tSisyphus hiking sandals\tSandalau heicio Sisyphus\tEnglish message unchanged\n" +
      "obliqueSock\tSisyphus oblique runner stretch sock sneakers\tSisyphus hyfforddwyr sanau oblique ymestyn rhedwr\tEnglish message unchanged\n" +
      "geos\tSisyphus geobaskets\tGeobaskets Sisyphus\tEnglish message unchanged\n" +
      "creepers\tSisyphus lace-up creeper boots\tSisyphus esgidiau crafu tywallt\tEnglish message unchanged\n" +
      "stooges\tStooges FW18 cropped jacket\t\tMessage changed (previous message was: Stooges cropped jacket / Siaced croen Stooges)\n" +    //Updated content
      "bullet\tForever bullet jacket\tDaw'r siaced bwled\tEnglish message unchanged\n" +
      "intarsia\tForever intarsia high neck jacket\tSiaced gwddf uchel intarsia i gyd\tEnglish message unchanged\n" +
      "puffer\tSisyphus jumbo puffer jacket\t\tMessage changed (previous message was: Sisyphus jumbo duvet coat / Côt duvet Sisyphus jumbo)\n"  //Updated content
  }
}




