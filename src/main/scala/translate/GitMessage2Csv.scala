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

import java.io.File

import util._

object GitMessage2Csv extends GitMessage2Csv{}

trait GitMessage2Csv extends Message2Csv with KeyValueParser with FileReader with WrappedPrintWriter with Commands{

  val currentEnglishMessages = "current_messages"
  val currentWelshMessages = "current_messages.cy"
  val oldEnglishMessages = "old_messages"
  val oldWelshMessages = "old_messages.cy"

  def messages2csv(csvOutputFileName: String):Unit = {

    val enMap = fetchMessages(currentEnglishMessages)
    val cyMap = fetchMessages(currentWelshMessages)
    val oldEnMap = fetchMessages(oldEnglishMessages)


    val outputCsvLines = enMap.map{ enMessage =>

      val oOldEnMsg = oldEnMap.find(oldEnMessage => enMessage._1 == oldEnMessage._1)
      val oCyMsg = cyMap.find(cyMessage => enMessage._1 == cyMessage._1).map(cyMsg => cyMsg._2)

      oCyMsg.fold(enMessage._1 + delimiter + enMessage._2 + delimiter + delimiter + noWelshFound)
      {cyMsg =>
        checkEnglishMessageChanged(enMessage._1, enMessage._2, oOldEnMsg.getOrElse(("",""))._2, cyMsg)
      } + newLine
    }

    writeFile(csvOutputFileName, csvHeader + newLine + outputCsvLines.fold("")((key,value) => key + value))
  }

  private def checkEnglishMessageChanged(key: String, enMessage: String, oldEnMsg: String, cyMsg: String): String = {

    if(oldEnMsg == enMessage){
      if(cyMsg == ""){
        key + delimiter + enMessage + delimiter + delimiter + noWelshFound
      }
      else {
        key + delimiter + enMessage + delimiter + cyMsg  + delimiter + englishUnchanged
      }
    }
    else{
      key + "\t" + enMessage + delimiter + delimiter + englishChanged+ oldEnMsg + separator + cyMsg + englishChangedEnd
    }
  }


  def fetchGitFiles(projectDir: String, gitCloneRef: String, gitCommitRef: String):Unit = {

    val pr = System.getProperties()
    // Windows / Linux / MacOS / Other
    pr.get("os.name") match {
      case "Linux" => executeCommand(s"./gitRetrieve.sh $projectDir $gitCloneRef $gitCommitRef")
      case "Windows" => println("Only bash/Linux script created so far. Windows bat file will be added, or please feel free to add it! :)")
      case _ => println("Only bash/Linux script created so far. ")
    }
  }

}