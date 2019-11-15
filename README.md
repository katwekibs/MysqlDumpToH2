# MysqlDumpToH2
A tool that transforms mysqldump sql to H2 database sql. This tool Convert MySQL script to H2
This tool can be used in both gralphical environment and within code as an api importing MysqlToH2.jar as a dependancy. 


1. Download the .jar file from the dist folder on this repo,
2. Make sure you have JRE 7 and above installed on your machine.
3. Double click on the MysqlToH2.jar file to open GUI.
4. Select your mysqldump file
5. Provide the destination folder for the H2 database SQL script.
6. Click let's go.
7. The tool will display "processing.." once this label hides, it means processing is done.
8. Check the destination folder for a file called h2.sql.

cheers.

Using this tool within your code. Add the MysqlToH2.jar jar file to your project dependancies.
```
MysqlToH2 api = new MysqlToH2();
String contents = new String(Files.readAllBytes(Paths.get(mysqlDumpTextField.getText())));
contents = api.removeComments(contents);
contents = api.removeCharSet(contents);
String[] split = contents.split(";");
String res = api.processAllQuery(split);
res += "\n";
res += api.processCommands();
```
