package translate

/**
  * Created by grant on 05/07/17.
  */
object ChooseContent {

  private def chooseWelsh(receivedWelsh: String, matchedWelsh: String): (String, String) = {
    if (receivedWelsh != matchedWelsh) {
      if (receivedWelsh.nonEmpty) {
        (receivedWelsh, "Welsh updated.")
      } else {
        (matchedWelsh, "Welsh not updated.")
      }
    } else {
      (receivedWelsh, "Welsh not updated.")
    }
  }

  // Assume:-
  // If English changed and not to blank then come from latest content file so use new English and blank the Welsh
  // Else:-
  //  If English changed to blank then just use existing English
  //  If Welsh changed and not to blank then come from WLU so use new Welsh
  //  If Welsh changed to blank then just use existing Welsh

  def chooseContent(receivedEnglish:String, receivedWelsh:String, matchedEnglish:String, matchedWelsh:String):(String,String,String) = {
    if (receivedEnglish != matchedEnglish) {
      if (receivedEnglish.nonEmpty) {
        (receivedEnglish, "", "English updated. Welsh blanked.")
      } else {
        val welsh = chooseWelsh(receivedWelsh, matchedWelsh)
        (matchedEnglish, welsh._1, "English not updated. " + welsh._2)
      }
    } else {
      val welsh = chooseWelsh(receivedWelsh, matchedWelsh)
      (receivedEnglish, welsh._1, "English not updated. " + welsh._2)
    }
  }
}
