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

// Assume:-
// If English changed and not to blank then come from latest content file so use new English and blank the Welsh
// Else:-
//  If English changed to blank then just use existing English
//  If Welsh changed and not to blank then come from WLU so use new Welsh
//  If Welsh changed to blank then just use existing Welsh

class ChooseContentSpec extends FlatSpec with Matchers {
  "ChooseContentSpec" must
    "yield received welsh when english has not changed and welsh has changed and not to blank" in { // I.e. from WLU
    ChooseContent.chooseContent(
      receivedEnglish = "english1",
      receivedWelsh = "welsh1",
      matchedEnglish = "english1",
      matchedWelsh = "welsh2"
    ) shouldBe("english1", "welsh1", "English not updated. Welsh updated.")
  }

  "ChooseContentSpec" must
    "yield received english and blank welsh when english has changed and not to blank" in { // I.e. from David's file
    ChooseContent.chooseContent(
      receivedEnglish = "english2",
      receivedWelsh = "welsh1",
      matchedEnglish = "english1",
      matchedWelsh = "welsh2"
    ) shouldBe("english2", "", "English updated. Welsh blanked.")
  }

  "ChooseContentSpec" must
    "yield existing english and existing welsh when english has changed to blank and welsh has not changed" in {
    ChooseContent.chooseContent(
      receivedEnglish = "",
      receivedWelsh = "welsh1",
      matchedEnglish = "english1",
      matchedWelsh = "welsh1"
    ) shouldBe("english1", "welsh1", "English not updated. Welsh not updated.")
  }

  "ChooseContentSpec" must
    "yield existing english and existing welsh when english has not changed and welsh has changed to blank" in {
    ChooseContent.chooseContent(
      receivedEnglish = "english1",
      receivedWelsh = "",
      matchedEnglish = "english1",
      matchedWelsh = "welsh1"
    ) shouldBe("english1", "welsh1", "English not updated. Welsh not updated.")
  }

  "ChooseContentSpec" must
    "yield existing english and received welsh when english has changed to blank and welsh has changed and not to blank" in {
    ChooseContent.chooseContent(
      receivedEnglish = "",
      receivedWelsh = "welsh2",
      matchedEnglish = "english1",
      matchedWelsh = "welsh1"
    ) shouldBe("english1", "welsh2", "English not updated. Welsh updated.")
  }
}
