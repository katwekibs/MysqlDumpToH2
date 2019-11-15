package mysqltoh2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Copyright 2010 Alvin J. Alexander, devdaily.com
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * A copy of the GNU Lesser General Public License is available at this URL:
 * http://www.gnu.org/licenses/lgpl.txt
 *
 */
public class FileUtility {

    /**
     * Opens and reads a file, and returns the contents as one String.
     *
     * @param filename
     * @return
     * @throws java.io.IOException
     */
    public static String readFileAsString(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        reader.close();
        return sb.toString();
    }

    /**
     * Open and read a file, and return the lines in the file as a list of
     * Strings.
     *
     * @param filename
     * @return
     * @throws java.lang.Exception
     */
    public static List<String> readFileAsListOfStrings(String filename) throws Exception {
        List<String> records = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            records.add(line);
        }
        reader.close();
        return records;
    }

    /**
     * Reads a "properties" file, and returns it as a Map (a collection of
     * key/value pairs).
     *
     * @param filename
     * @param delimiter
     * @return
     * @throws Exception
     */
    public static Map<String, String> readPropertiesFileAsMap(String filename, String delimiter)
            throws Exception {
        Map<String, String> map = new HashMap();


        File file = new File(filename);
        if (!file.exists()) {
                   Files.createFile(Paths.get(filename));
        }
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                continue;
            }
            // assumption here is that proper lines are like "String : http://xxx.yyy.zzz/foo/bar",
            // and the ":" is the delimiter
            int delimPosition = line.indexOf(delimiter);
            String key = line.substring(0, delimPosition).trim();
            String value = line.substring(delimPosition + 1).trim();
            map.put(key, value);
        }
        reader.close();
        return map;
    }

    /**
     * Read a Java properties file and return it as a Properties object.
     */
    public static Properties readPropertiesFile(String canonicalFilename)
            throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(canonicalFilename));
        return properties;
    }

    /**
     * Save the given text to the given filename.
     *
     * @param canonicalFilename Like /Users/al/foo/bar.txt
     * @param text All the text you want to save to the file as one String.
     * @throws IOException
     */
    public static void writeFile(String canonicalFilename, String text)
            throws IOException {
        File file = new File(canonicalFilename);
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(text);
        out.close();
    }

    public static void writePropertiesFile(String canonicalFilename, Map properties, String delimiter)
            throws IOException {
        File file = new File(canonicalFilename);
        BufferedWriter out = new BufferedWriter(new FileWriter(file,false));

        Set keys = properties.keySet();
        for (Object key : keys) {
            out.write(key + delimiter + properties.get(key) + "\n");
        }
        out.close();
    }

    /**
     * Write an array of bytes to a file. Presumably this is binary data; for
     * plain text use the writeFile method.
     */
    public static void writeFileAsBytes(String fullPath, byte[] bytes) throws IOException {
        OutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fullPath));
        InputStream inputStream = new ByteArrayInputStream(bytes);
        int token = -1;

        while ((token = inputStream.read()) != -1) {
            bufferedOutputStream.write(token);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        inputStream.close();
    }

    public static void copyFile(File source, File destination) throws IOException {
        //if (!source.isFile() || !dest.isFile()) return false;

        byte[] buffer = new byte[100000];

        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(source));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(destination));
            int size;
            while ((size = bufferedInputStream.read(buffer)) > -1) {
                bufferedOutputStream.write(buffer, 0, size);
            }
        } catch (IOException e) {
            // TODO may want to do something more here
            throw e;
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
            } catch (IOException ioe) {
                // TODO may want to do something more here
                throw ioe;
            }
        }
    }
    
     public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
}
