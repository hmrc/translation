# translation

This tool is intended to make translation of English Play! Fraemwork messages files to other languages (specifically Welsh), simpler.
On first run, it will extract English messages (key/value pairs) into a csv file, for translation.

Once the translations have been populated and returned, the tool can extract the Welsh key/value pairs into a messages.cy file.
The tool may then be used to cross-reference the latest English messages file against a translated csv file, to ensure sychnonisation. It will apply comments against each line, to indicate:
 - a new, untranslated English message
 - an existing, changed English message
 - an existing, unchanged English message, which already has a Welsh translation

# Executing
At the moment, just performs both conversions from hardcoded filenames:
takes messages.en & existingTranslations.csv, outputs to out.csv
takes Translations.csv and outputs to _messages.cy

These input files need to be in the project home directory. Plans is to create a UI, so that all files can be specified at runtime.

> sbt run



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
 3. When generating new csv, if cy line is found in old csv?, compare line in csv with line in messages.en. Outcome: [New message / Message changed / Already translated]
 4. Messages.cy is always a straight generation from a csv file. New/Untranslated En messages are ignored (for now).
