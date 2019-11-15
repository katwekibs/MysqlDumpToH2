/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltoh2;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jeffseka
 */
public class MysqlToH2 {

    private ArrayList alterCommands = new ArrayList();

    public String removeComments(String contents) {
        return contents.replaceAll("\\/\\*.+\\*\\/;", "").trim();
    }

    public String removeCharSet(String contents) {
        return contents.replace("DEFAULT CHARSET=latin1", "").trim();
    }

    public String processAllQuery(String[] split) {
        String res = "";
        for (int i = 0; i < split.length; i++) {
            String query = split[i];
            String t = processSingleQuery(query);
            if (!t.equals("")) {
                res += t;
                res += ";\n\n";
            }
        }
        return res;
    }

    public String processSingleQuery(String query) {
        query = query.trim();

        // remove lock
        if (query.toLowerCase().contains("lock tables")) {
            return "";
        }

        if (query.toLowerCase().contains("unlock tables")) {
            return "";
        }

        if (!isCreateQuery(query)) {
            return query;
        }
        String removeDef = query.substring(query.indexOf("(") + 1, query.lastIndexOf(")"));
        String[] split = removeDef.split(",");
        String res = query;
        for (String line : split) {
            if (line.toLowerCase().contains("auto_increment")) {
                res = foundAutoIncrementField(line, res);
                continue;
            }

            if (line.toLowerCase().contains("primary key")) {
                res = foundPrimaryKeyField(line, res);
                continue;
            }

            if (line.toLowerCase().contains("unique key")) {
                res = foundUniqueKeyField(line, res);
                continue;
            }

            if (line.toLowerCase().trim().startsWith("key")) {
                res = foundKeyField(line, res);
                continue;
            }

            if (line.toLowerCase().contains("constraint")) {
                res = foundConstraintField(line, res);
                continue;
            }
        }
        return res;
    }

    public String processCommands() {
        String res = "";
        for (Object alterCommand : alterCommands) {
            res += "\n" + alterCommand + ";";
        }
        return res;
    }

    private String foundAutoIncrementField(String line, String query) {
        String res = query;

        // get table name
        String tableName = extractTableName(query);

        //get auto increment value
        String incrementValue = "";
        if (hasAutoIncrement(query)) {
            String[] split = query.split("AUTO_INCREMENT=");
            res = split[0];
            incrementValue = split[1];
        }

        if (incrementValue.contains(" ")) {
            incrementValue = incrementValue.substring(0, incrementValue.indexOf(" "));
        }
        //create auto increment alter command
        String aiNumber = incrementValue.equals("") ? "" : ", AUTO_INCREMENT=" + incrementValue;
        String alterCommand = "ALTER TABLE " + tableName + " MODIFY " + line.trim();// + " " + aiNumber;
        alterCommands.add(alterCommand);

        //remove the auto increment property from auto increment field
        res = res.replace("AUTO_INCREMENT", "");

        return res;
    }

    private String foundPrimaryKeyField(String line, String query) {
        String tableName = extractTableName(query);

        String alterCommand = "ALTER TABLE " + tableName + " ADD " + line.trim();
        alterCommands.add(alterCommand);

        //remove the auto increment property from auto increment field
        String res = removeField(query, line);

        return res;
    }

    private String foundUniqueKeyField(String line, String query) {
        String tableName = extractTableName(query);

        //h2 expects unique key names.
        //prefix our keyname with table name.
        String prifixedline = prefixColumnName(line,tableName);
        String alterCommand = "ALTER TABLE " + tableName + " ADD " + prifixedline.trim();
        alterCommands.add(alterCommand);

        //remove field
        String res = removeField(query, line);
        return res;
    }

    private String foundKeyField(String line, String query) {
        String tableName = extractTableName(query);

        String alterCommand = "ALTER TABLE " + tableName + " ADD " + line.trim();
        //alterCommands.add(alterCommand);

        //remove field
        String res = removeField(query, line);

        return res;
    }

    private String foundConstraintField(String line, String query) {
        String tableName = extractTableName(query);

        int index = line.trim().toLowerCase().indexOf("foreign key");
        String alterCommand = "ALTER TABLE " + tableName + " ADD " + line.trim().substring(index);
        alterCommands.add(alterCommand);

        //remove field
        String res = removeField(query, line);

        return res;
    }

    private boolean hasAutoIncrement(String query) {
        return query.toLowerCase().contains("auto_increment=");
    }

    private boolean isCreateQuery(String query) {
        return query.toLowerCase().contains("create table");
    }

    private String addTerminator(int i, String[] split) {
        return addTerminator(i, split, ";");
    }

    private String addTerminator(int i, String[] split, String terminator) {
        String res = "";
        if (i < split.length) {
            res = ",";
        }
        return res;
    }

    private String extractTableName(String query) {
        Pattern pattern = Pattern.compile("CREATE\\s+TABLE\\s+.?(\\w+).?\\s+\\(");
        Matcher matcher = pattern.matcher(query);
        matcher.find();
        return matcher.group(1);
    }

    private String removeField(String query, String line) {
        String res = query.replace(line, "").replace(",,", "");
        return res;
    }

    private String prefixColumnName(String line, String tableName) {
        String res = "";
        String[] split = line.trim().split(" ");
        String removedTicks = split[2].substring(1,split[2].length()-1);
        String keyName = tableName+"_"+removedTicks;
        res = line.replaceFirst(removedTicks, keyName);
        return res;
    }

}
