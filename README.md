# translation

This tool is intended to make translation of English Play! Framework messages files to other languages (specifically Welsh), simpler.
It will extract English messages (key/value pairs) into a csv file, for translation.

Once the csv translatiofile has been populated and returned, the tool can extract the Welsh key/value pairs into a messages.cy file.

The tool may then be used to cross-reference the latest English messages file against the previously translated csv file, to ensure sychnonisation. It will apply comments against each line, to indicate:
 - a new, untranslated English message
 - an existing, changed English message
 - an existing, unchanged English message, which already has a Welsh translation

# Executing

> sbt run


# Example Output csv

| Key | English | Welsh | Comments |
| ----------|----------|----------|----------|
| pages.form.field.description.transferor-income	| Confirm your annual income	| Cadarnhau eich incwm blynyddol	| English message unchanged |
| title.eligibility |	Your relationship	| |	No Welsh translation found |
| title.dateOfBirth	| Your date of birth	| |	No Welsh translation found |
| pages.form.field.last-name.error.error.maxLength	| Use up to or no more than {0} letters. |	Defnyddiwch hyd at, neu ddim mwy na {0} llythrennau.	| English message unchanged |
| pages.form.field.description.gender |	Confirm your spouse or civil partner''s gender	| Cadarnhau eich priod neu eich partner sifil 'yn rhyw	| English message unchanged |
| coc.end-reason.SYSTEM	| Ended by HMRC	|	| No Welsh translation found |



# Process
 1. Create application, using Play i18n, with text in Messages file(s)
 2. Run conversion, to create a csv file, from the English Messages file (plus any translations from  previous csv)
 3. Track (new) csv in Git and send file to translators.
 4. Receive csv back, with translations populated.
 5. Check this new csv into Git, superseeding the one in step 3.
 6. Extract the (Welsh) translations from the file, using this tool.
 7. Run the Tool against the messages.en / baselined csv. If the newly created csv highlights any changes,
     this indicates that additional translations are required. I.e. the English file has changed since the last csv file was saved / baselined.
 
 
# Notes:
 1. Messages.en is the definitive hand-crafted list of messages in the project, so never auto generated.
 2. csv file is generated from messages.en, plus matched cy content, from tracked csv file (when available).
 3. When generating new csv, if cy line is found in old csv?, it will compare the English message in csv with line in messages.en. This will result in the one of the outcomes: Message changed / Already translated.
 4. Messages.cy is always a straight generation from a csv file. New/Untranslated En messages are ignored (for now).
