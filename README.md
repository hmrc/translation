# welsh-translation-tool


This tool is intended to make translation of English Play! Framework message files to other languages (specifically Welsh), simpler.

It will extract English messages, and any existing Welsh translations into a csv file, for translation.

Once the translation file has been populated and returned, the tool can extract the Welsh key/value pairs into a messages.cy file.

The tool may then be used to cross-reference the latest English messages file against a previous messages, within a specified git commit, to ensure sychnonisation. It will apply comments against each line, to indicate:
 - a new, untranslated English message
 - an existing changed English message
 - an existing unchanged English message, which already has a Welsh translation

## Pre-requisites to run
- Java 11
- sbt 1.x

# Running the app

> sbt run



## Git Msg to Csv (Compare current message versions, with a previous git commit)
1. Specify the git project you want to examine.
2. Specify the commit reference of the last time the Welsh translations were updated. If there are no previous Welsh translations, just put __main__, instead.
3. Click the button at the bottom, to create the csv file.

### Example Output csv

| Key | English | Welsh | Comments |
| ----------|----------|----------|----------|
| pages.form.field.description.transferor-income	| Confirm your annual income	| Cadarnhau eich incwm blynyddol	| English message unchanged |
| title.eligibility |	Your relationship	| |	No Welsh translation found |
| title.dateOfBirth	| Your date of birth	| |	No Welsh translation found |
| pages.form.field.last-name.error.error.maxLength	| Use up to or no more than {0} letters. |	Defnyddiwch hyd at, neu ddim mwy na {0} llythrennau.	| English message unchanged |
| pages.form.field.description.gender |	Confirm your spouse or civil partner''s gender	| Cadarnhau eich priod neu eich partner sifil 'yn rhyw	| English message unchanged |
| coc.end-reason.SYSTEM	| Ended by HMRC	|	| No Welsh translation found |


## Csv to Messages
1. Select Mode, to convert from csv to messages
2. Select the input and output files. (NB: the application will overwrite files, without confirmation)
3. Click the button at the bottom, to create the Welsh messages file.



# Process
 1. Create application, using Play i18n, with text in Messages file(s)
 2. Run **Message to Csv** conversion, to create a csv file, from the English Messages file.
 3. Convert the csv file to a spreadsheet, using your favourite spreadsheet application. You may need to adjust column widths, to make the file more readable.
 4. Send spreadsheet to translators.
 5. Receive spreadsheet back, with translations populated.
 6. Using your favourite spreadsheet application, export the spreadsheet to csv, using __TABs__ to separate fields.
 7. Extract the (Welsh) translations from the csv file, using **Csv to Messages**.
 8. In future, Run **Message to Csv** against the git commit ref, where the Welsh translations were last updated. If the newly created csv highlights any changes, this indicates that additional translations are required. I.e. the English file has changed since the specified git commit.
 
 
# Notes:
 1. Messages.en is the definitive hand-crafted list of messages in the project, so never auto generated.
 2. Messages.cy is a straight generation from a csv/spreadsheet file.
 3. This application uses some native code. To date, it has only been tested on Linux and Mac (thanks to Callum Cooper c33-cooper-hmrc). It should work with any Bash shell, but does not yet support Windoze (unless you have Cywin or Windows Bash shell installed).

# Running the tests

    sbt test

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

