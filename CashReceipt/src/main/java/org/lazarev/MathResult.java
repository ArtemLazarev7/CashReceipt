package org.lazarev;

import org.apache.commons.io.output.TeeOutputStream;
import org.lazarev.exception.InvalidFormat;
import org.lazarev.exception.InvalidId;
import org.lazarev.exception.InvalidWeight;
import org.lazarev.list.AbstractLists;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MathResult {
    private final String FILE_NAME_PRODUCTS = "listOfProducts";
    private final String FILE_NAME_PRICES = "listOfPrices";
    private final String FILE_NAME_CARDS = "listOfCards";

    AbstractLists abstractLists;
    private List<String> productsList;
    private List<String> pricesList;
    private List<String> cardsList;
    private List<Integer> listId;
    private List<Double> listWeight;
    private double discount;
    private ByteArrayOutputStream buffer;


    public MathResult() {
        AbstractLists abstractLists = new AbstractLists();
        this.productsList = abstractLists.addList(FILE_NAME_PRODUCTS);
        this.pricesList = abstractLists.addList(FILE_NAME_PRICES);
        this.cardsList = abstractLists.addList(FILE_NAME_CARDS);
    }

    public void translate(String str) {
        if (str.matches("(\\d+\\-\\d+\\.?\\d*\s)+card\s\\d{4}")) {
            listId = new ArrayList<>();
            listWeight = new ArrayList<>();
            String[] idWeightCard = str.trim().split("card");
            String numberCard = idWeightCard[1].trim();
            discount = returnSale(numberCard);
            String stringIdWeight = idWeightCard[0].trim();
            for (int i = 0; i < stringIdWeight.length(); i++) {
                String s = "";
                while (stringIdWeight.charAt(i) != '-') {
                    s += stringIdWeight.charAt(i);
                    i++;
                }
                if (Integer.parseInt(s) >= 0 && Integer.parseInt(s) < productsList.size()) {
                    listId.add(Integer.parseInt(s));
                } else
                    try {
                        throw new InvalidId();
                    } catch (InvalidId e) {
                        throw new RuntimeException(e);
                    }
                s = "";
                i++;
                while (stringIdWeight.charAt(i) != ' ') {
                    s += stringIdWeight.charAt(i);
                    if (i == stringIdWeight.length() - 1) {
                        break;
                    }
                    i++;
                }
                if (Double.parseDouble(s) < 15) {
                    listWeight.add(Double.parseDouble(s));
                } else try {
                    throw new InvalidWeight("Incorrect weight. You can buy up to 15 kg of fruit in one purchase.");
                } catch (InvalidWeight e) {
                    throw new RuntimeException(e);
                }
            }
            print();
        } else try {
            throw new InvalidFormat();
        } catch (InvalidFormat e) {
            throw new RuntimeException(e);
        }
    }

    public double sellOut(int id, double weight) {
        return Double.parseDouble(pricesList.get(id)) * weight;
    }

    public double sellAtDiscount(int id, double weight) {
        if (weight >= 5) {
            return Double.parseDouble(pricesList.get(id)) * weight * (discount - 0.1);
        } else
            return Double.parseDouble(pricesList.get(id)) * weight * discount;
    }

    public double returnSale(String numberCard) {
        if (cardsList.contains(numberCard))
            return (1 - Double.parseDouble(numberCard.substring(0, 2)) / 100);
        else {
            return 1;
        }
    }

    public void print() {

        double taxableTot = 0;
        double total = 0;
        Locale local = new Locale("ru", "Ru");
        Date currentDate = new Date();
        DateFormat df = DateFormat.getDateInstance(DateFormat.DATE_FIELD, local);
        DateFormat tf = DateFormat.getTimeInstance(DateFormat.DEFAULT, local);
        startWriterCheckFail();
        System.out.println("            CASH RECEIPT" + "\n" + "            Fresh Fruit" + "\n" + "        24 Masherova street" + "\n"
                + "           Tel: 688-64-26" + "\n" + "Cashier № 1234         DATE: " + df.format(currentDate) + "\n" + "                       TIME: " + tf.format(currentDate)
                + "\n" + "________________________________________" + "\n" + "QTY    DESCRIPTION        PRICE   TOTAL");
        for (int i = 0; i < listId.size(); i++) {
            System.out.printf("%-7s%-19s%-8s%-5s\n", String.format("%.3f", listWeight.get(i)), productsList.get(listId.get(i))
                    , pricesList.get(listId.get(i)), String.format("%.2f", sellAtDiscount(listId.get(i), listWeight.get(i))));
            taxableTot += sellAtDiscount(listId.get(i), listWeight.get(i));
            total += sellOut(listId.get(i), listWeight.get(i));
        }
        System.out.println("________________________________________" + "\n" + "________________________________________");
        System.out.println("TAXABLE TOT                     " + String.format("%.2f", taxableTot));
        System.out.println("VAT" + Math.round(((total - taxableTot) / total) * 100) + "%" + "                          " + String.format("%.2f", (total - taxableTot)));
        System.out.println("TOTAL                           " + String.format("%.2f", total));
        endWriterCheckFail();
    }

    private void startWriterCheckFail() {
        buffer = new ByteArrayOutputStream();
        OutputStream teeStream = new TeeOutputStream(System.out, buffer);
        System.setOut(new PrintStream(teeStream));
    }

    private void endWriterCheckFail() {
        try (OutputStream fileStream = new FileOutputStream("CashReceipt")) {
            buffer.writeTo(fileStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}

