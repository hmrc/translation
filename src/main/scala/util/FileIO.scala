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

package util

import java.io.PrintWriter
import scala.io.Source

trait KeyValueParser{
  def splitKeyValues(line:String, token:String): Map[String, (String, String)] = {
    val cols = line.split(token).toList
    val key = cols.headOption.getOrElse("").trim
    val value1 = cols.tail.headOption.getOrElse("").trim
    val value2 = cols.tail.tail.headOption.getOrElse("").trim
    Map(key -> (value1, value2))
  }
}


trait FileReader {
  lazy val source = Source

  def linesFromFile(fileName: String): Iterator[String] = {
    try {
      source.fromFile(fileName).getLines()
    }
    catch{
      case _: Throwable => println(s"Couldn't open file: $fileName"); Iterator(" = ")
    }
  }
}


trait CsvReader extends FileReader with KeyValueParser {
  def readFromCsv(translations:String):Map[String, (String, String)] = {
    val lines = for (line <- linesFromFile(translations)) yield line
     lines.flatMap{ line =>
       splitKeyValues(line, "\t")
    }.toMap
  }
}


trait WrappedPrintWriter{
  def writeFile(fileName: String, content: String): Unit  = {
    val pw = new PrintWriter(fileName)
    pw.println(content)
    pw.close()
  }
}