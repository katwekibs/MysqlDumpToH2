/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltoh2;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author muhamedkakembo
 */
public class Prefs {

    private final String fileName;
    private Map properties;

    public Prefs(String fileName) throws Exception {
        this.fileName = fileName;
        properties = new LinkedHashMap();
        readFile();
    }

    private void readFile() throws Exception {
        properties = FileUtility.readPropertiesFileAsMap(fileName, ":");
    }

    private void writeFile() {
        try {
            FileUtility.writePropertiesFile(fileName, properties, ":");
        } catch (IOException ex) {
            Utility.showExceptionError(ex.getMessage()+"\n"+ex.getClass());
        }
    }

    public void put(String key, String value) {
        properties.put(key, String.valueOf(value));
        writeFile();
    }

    public String get(String key, String object) {
        if (properties.containsKey(key)) {
            return (String) properties.get(key);
        } else {
            return object;
        }
    }

}
