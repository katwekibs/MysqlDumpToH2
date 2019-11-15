/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltoh2;

import java.awt.Dimension;
//import mySystem.model.BaseTableModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.format.*;
import java.time.*;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;

/**
 *
 * @author muhamedkakembo
 */
public class Utility {

    public Utility() {
    }

    public static String upperCaseFirst(String text) {
        String s1 = text.substring(0, 1).toUpperCase();
        String s2 = text.substring(1);

        StringBuilder res = new StringBuilder();
        res.append(s1);
        res.append(s2);
        return res.toString();
    }

    public static String lowerCaseFirst(String text) {
        String s1 = text.substring(0, 1).toLowerCase();
        String s2 = text.substring(1);

        StringBuilder res = new StringBuilder();
        res.append(s1);
        res.append(s2);
        return res.toString();
    }

    public static String formatDouble(double num) {
        return MessageFormat.format("{0,number,#.##}", num);
    }

    public static String getPercentFromForm(double num) {
        String res = "0";
        try {
            num = num / 100;
            res = MessageFormat.format("{0,number,#.#####}", num);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return res;
    }

    public static String getPercentFromForm(String num) {
        String result = "0";
        if (num.equals("")) {
            return result;
        }

        // remove % from num
        String percent = num.substring(num.length() - 1);
        result = num;
        if (percent.equals("%")) {
            result = num.substring(0, num.length() - 1);
        }
        return getPercentFromForm(Double.parseDouble(result));
    }

    public static String showPercentOnForm(int num) {
        return showPercentOnForm(String.valueOf(num));
    }

    public static String showPercentOnForm(double num) {
        return MessageFormat.format("{0,number,##%}", num);

    }

    public static String showPercentOnForm(String num) {
        return showPercentOnForm(Double.parseDouble(num));
    }

    public static int getMoniesFromForm(String formMoney) {
        int result = 0;
        if (formMoney.equals("")) {
            return result;
        }
        // remove R from money
        String currency = formMoney.substring(0, 1);
        if (currency.equals("R") || currency.equals("r")) {
            formMoney = formMoney.substring(1);
        }
        int pointIndex = formMoney.indexOf('.');
        if (pointIndex > -1) {
            // to avoid index out of range when a user inters one decimal degit,
            // i will add 3 zero to the end of each number after all we only need two decimal 
            // degits.
            formMoney = formMoney + "000";
            String dec = formMoney.substring(pointIndex + 1, pointIndex + 3);
            String money = formMoney.substring(0, pointIndex);
            result = Integer.parseInt(money + dec);
        } else {
            result = Integer.parseInt(formMoney) * 100;
        }
        return result;
    }

    public static String showMoneyOnForm(int dbMoney) {
        String money = String.valueOf(dbMoney).trim();
        String result = "0";
        if (money.length() > 1) {
            String moneyString = money.substring(0, money.length() - 2);
            String decimal = money.substring(money.length() - 2);
            result = moneyString + "." + decimal;
        }
        return MessageFormat.format("{0,number,R#,##0.00}", Double.parseDouble(result));
    }

    public static String showMoneyOnForm(String dbMoney) {
        return MessageFormat.format("{0,number,R#,##0.00}", Double.parseDouble(dbMoney));
    }

    public static String showMoneyOnForm(double num) {
        String money = String.valueOf(num).trim();
        String result = "";
        if (money.length() > 1) {
            String moneyString = money.substring(0, money.length() - 2);
            String decimal = money.substring(money.length() - 2);
            result = moneyString + "." + decimal;
        }
        return result;
    }

    public static String[] separateString(String string, String subStr) {
        String[] result = new String[2];
        int index = string.indexOf(subStr);
        if (index > 0) {
            result[0] = string.substring(0, index);
            result[1] = string.substring(index + 1);
        } else {
            result[0] = string;
        }
        return result;
    }

    public static String formartLongDate(String dateString) {
        String res;
        LocalDateTime dateTimeObj = extractLocalDateTimeFromString(dateString);
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("MMM dd, YYYY - HH:mm a");
        return dateTimeObj.format(formater);
    }

    public static String formartDate(LocalDateTime dateTimeObj) {
        String res;
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("MMM dd, YYYY");
        return dateTimeObj.format(formater);
    }

    public static LocalDateTime extractLocalDateTimeFromString(String string) {
        LocalDateTime formatedDate;
        if (string.contains("T")) {
            formatedDate = LocalDateTime.parse(string);
        } else {
            Timestamp tm = Timestamp.valueOf(string);
            formatedDate = tm.toLocalDateTime();
        }
        return formatedDate;
    }

    public static String formartDate(String dateString) {
        return formartDate(extractLocalDateTimeFromString(dateString));
    }

    public static Object getObjectFromString(String classPath, String name) {
        Object cont = null;
        String className = Utility.upperCaseFirst(name);
        try {
            cont = (Object) Class.forName(classPath + "." + className).newInstance();
        } catch (ClassNotFoundException ex) {
            Utility.showExceptionError(classPath + "." + className + " class could not be found happened in " + Utility.class.getName());
        } catch (InstantiationException ex) {
            Utility.showExceptionError(classPath + "." + className + " class could not be instantiated happened in " + Utility.class.getName());
        } catch (IllegalAccessException ex) {
            Utility.showExceptionError(classPath + "." + className + " something happened in " + Utility.class.getName());
        }
        return cont;
    }

    public static void showExceptionError(String message) {

        JTextArea textArea = new JTextArea(message);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(500, 500));

        JOptionPane.showMessageDialog(null, scrollPane,
                "Exception",
                JOptionPane.WARNING_MESSAGE);
    }

    public static String extractClassNameFromPackage(String packageName) {
        String[] pieces = packageName.split("\\.");
        return pieces[pieces.length - 1];
    }

    public static String extractMoneyFromForm(String formMoney) {
        String result;
        if (formMoney == null || formMoney.equals("")) {
            result = "0";
        } else {
            // remove R from money
            result = formMoney;
            String currency = formMoney.substring(0, 1);
            if (currency.equals("R") || currency.equals("r")) {
                result = formMoney.substring(1);
            }
        }
        return result;
    }

    public static String MyStringRound(String num) {
        return new BigDecimal(num).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public static String calculateMoneyInc(String excluding, String rate) {
        String res;
        BigDecimal subTotal, taxRate, tax, total;
        subTotal = new BigDecimal(excluding);
        taxRate = new BigDecimal(rate);
        taxRate = taxRate.divide(new BigDecimal("100")).add(new BigDecimal("1"));
        total = subTotal.multiply(taxRate);
        total = total.setScale(2, RoundingMode.HALF_UP);

        res = total.toString();
        return res;
    }

    public static String calculateMoneyExc(String including, String rate) {
        String res;
        BigDecimal subTotal, taxRate, tax, total;
        subTotal = new BigDecimal(including);
        taxRate = new BigDecimal(rate);
        taxRate = taxRate.divide(new BigDecimal("100")).add(new BigDecimal("1"));
        total = subTotal.divide(taxRate, 2, RoundingMode.HALF_UP);

        res = total.toString();
        return res;
    }

    public static String calculatePercentage(String salePrice, String costPrice) {
        BigDecimal bigDnum, sale, cost, markup, total;
        sale = new BigDecimal(salePrice);
        cost = new BigDecimal(costPrice);
        markup = sale.subtract(cost);
        markup = markup.multiply(new BigDecimal("100"));
        total = markup.divide(cost, 2, RoundingMode.HALF_UP);
        return total.toString();
    }

    public static String calculateTax(String withTax, String rate) {
        BigDecimal bigWithTax, bigWithoutTax, bigTotal;
        bigWithTax = new BigDecimal(withTax);
        bigWithoutTax = new BigDecimal(Utility.calculateMoneyExc(withTax, rate));

        bigTotal = bigWithTax.subtract(bigWithoutTax);
        return bigTotal.toString();
    }

    public static String calculateChange(String tendered, String dueTotal) {
        BigDecimal bigTendered, Bigdue, balance;
        bigTendered = new BigDecimal(tendered);
        Bigdue = new BigDecimal("-" + dueTotal);
        balance = Bigdue.add(bigTendered);
        return balance.toString();
    }

//    public static ArrayList getTableSelectedItems(BaseTableModel model, JTable table) {
//        ArrayList items = new ArrayList();
//
//        int[] row_indexes = table.getSelectedRows();
//        for (int i = 0; i < row_indexes.length; i++) {
//            Object o = model.getObjectAt(row_indexes[i]);
//            items.add(o);
//        }
//        return items;
//    }
    public static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    public static void listSelectAll(JList list) {
        int start = 0;
        int end = list.getModel().getSize() - 1;
        if (end >= 0) {
            list.setSelectionInterval(start, end);
        }
    }

    public static void treeSelectAll(JTree list) {
        int start = 0;
        int end = list.getRowCount();
        if (end >= 0) {
            list.setSelectionInterval(start, end);
        }
    }

    public static void treeDeSelectAll(JTree list) {
        list.setSelectionInterval(0, 0);
    }

    public static void treeExpandAll(JTree list) {
        for (int i = 0; i < list.getRowCount(); i++) {
            list.expandRow(i);
        }
    }
}
