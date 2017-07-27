package util

import java.io.{BufferedReader, File, IOException, InputStreamReader}

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

trait Commands {

  def executeCommand(cmd: String):Unit = {
    executeCommand(cmd, ".")
  }

  def executeCommand(cmd: String, dir:String):Unit = {
    executeCommand(cmd, null, dir)
  }

  def executeCommand(cmd: String, params: Array[String], strDir:String):Unit = {

    println("Running " + cmd)
    try {
      val dir = new File(strDir)
      val p: Process = Runtime.getRuntime.exec(cmd, params, dir)
      val reader: BufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream))

//      readLines(reader)
      var line: String = reader.readLine()
      while (line != null) {
        println(line)
        line = reader.readLine()
      }
      p.waitFor()
    }
    catch {
      case e1: IOException =>
      case e2: InterruptedException =>
    }
    println("-------> Done.")
  }

  def readLines(reader: BufferedReader):Unit = {
    val line = reader.readLine()
    if (line != null){
      println(line)
      readLines(reader)
    }
  }

}
