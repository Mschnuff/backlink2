package mschnuff;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.net.*;
import java.text.DecimalFormat;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        String seobilityFilename = "backlinks.csv"; // add additional files in the same way. note: other files may need
                                                    // to be formatted differently
        String seobilityFilename2 = "backlinks_marktplatz.csv"; // add additional files in the same way. note: other
                                                                // files may need to be formatted differently
        // we parse the file to get the backlinks
        ArrayList<Backlink> seobilityBacklinks = getBacklinks(seobilityFilename); // backlinks are stored in a custom
                                                                                   // data type
        // we will store our response codes in here
        HashMap<Integer, Integer> responseCodes = new HashMap<Integer, Integer>(); // second number stands for the
                                                                                   // amount of times a response code
                                                                                   // appeared
        String[] originalDomain = { "internet-sicherheit.de/" }; // domain of our homepage
        String replaceDomain = "internet-sicherheit.de/wp_ifis_neu/"; // current location of our new homepage

        String[] originalDomain2 = { "http://www.it-sicherheit.de/", "https://www.it-sicherheit.de/" }; // domain of our
                                                                                                        // homepage
        String replaceDomain2 = "http://10.0.12.198/"; // current location of our new homepage
        boolean replace = true; // replace the Domain?
        int currentTimeout = 5000; // timeout

        String outputPath = createOutputFile("output.txt");

        // tests backlinks and generates output
        generateOutput(seobilityBacklinks, originalDomain, replaceDomain, responseCodes, replace, currentTimeout,
                outputPath);

    }

    public static String createOutputFile(String outFile) {
        String path = "";
        String user_dir = System.getProperty("user.dir") + "/backlink_checker/";
        path = user_dir + outFile;
        // output file
        try {
            File myObj = new File(path);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                new FileWriter(path, false).close();
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return path;
    }

    public static void handlePrint(String toPrint, String outputPath) {
        System.out.println(toPrint);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath, true));
            //PrintWriter printWriter = new PrintWriter(outputPath);
            bw.append(toPrint);            
            bw.close();
            // System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void generateOutput(
            ArrayList<Backlink> backlinks, // a collection that contains all of the backlinks from the csv-file
            String[] oriDomain, // the original domain from the csv-file
            String replDomain, // the new domain after website migration
            HashMap<Integer, Integer> responseCodes, // we will store our collected repsonceCodes in this file
            boolean replaceDomain, // indicates whether or not the original Domain should be replaced for the
                                   // current measurement
            int currentTimeout, // the amount of time in milliseconds after which a request is aborted
            String outputPath) {
        String oriString = "[";
        for (int b = 0; b < oriDomain.length; b++) {
            if(b==0) {
                oriString = oriString + oriDomain[b];
            } else {
                oriString = oriString + ", " + oriDomain[b];
            }
            
        }
        oriString = oriString + "]";       
        String out1 = "Im folgenden werden die von Seobility.com ermittelten Backlinks für '" + oriString
        + "' --> '" + replDomain + "' getestet."
        + "\n----------------------------------------\n";        
        handlePrint(out1, outputPath);
        System.out.println();
        // we iterate over the previously collected backlinks and test each one
        for (int i = 0; i < backlinks.size(); i++) {
            Backlink cb = backlinks.get(i);
            if (replaceDomain) {
                for (int a = 0; a < oriDomain.length; a++) {
                    cb.target = cb.target.replace(oriDomain[a], replDomain);
                }

            }
            // this output is mainly for testing purposes. it is not used atm
            String outputline = "domain: " + cb.domain + ", domain rank: " + cb.domainrank + ", origin: " + cb.origin
                    + ", target: "
                    + cb.target + ", nofollow: " + cb.nofollow + ", anchor: " + cb.anchor + ", deleted: " + cb.deleted
                    + "\n";
            // System.out.println(outputline);
            String out2 = "Es wird versucht eine Verbindung mit '" + cb.target
            + "' aufzubauen. Der Backlink befindet sich auf: '" + cb.domain
            + "' (Wichtigkeit der Domäne: " + cb.domainrank + ")"
            + "...\n(Die exakte Quell-URL lautet: '" + cb.origin + "')\nakutelles Zeitüberschreitungslimit: "
            + currentTimeout + " Millisekunden\n";
            handlePrint(out2, outputPath);           
            HashMap<String, Integer> testURLMap = testURL(cb.target, currentTimeout);
            for (String testUrlOutput : testURLMap.keySet()) {
                handlePrint(testUrlOutput + "\n-----------------------------\n", outputPath);
                //System.out.println(testUrlOutput);
                Integer responseCode = testURLMap.get(testUrlOutput);
                if (responseCodes.containsKey(responseCode)) {
                    int count = responseCodes.get(responseCode);
                    responseCodes.put(responseCode, count + 1);
                } else {
                    responseCodes.put(responseCode, 1);
                }
            }

            // System.out.println(testURL(cb.target, cT));

        }
        // the total number of backlinks
        float totalNum = backlinks.size();
        String out3 = "Folgende Anwort-Codes wurden gefunden: (Gesamt-Anzahl der Backlinks: " + (int) totalNum + ")\n";
        handlePrint(out3, outputPath);
        // int differentCodes = 0;
        // is there no simpler way to iterate over hashmaps????
        Iterator<HashMap.Entry<Integer, Integer>> it = responseCodes.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry<Integer, Integer> pair = it.next();
            float currentNum = pair.getValue();
            DecimalFormat df = new DecimalFormat("#.##");
            String percentage = df.format(currentNum / totalNum * 100f);
            String out4 =  "Antwort-Code: " + pair.getKey() + ", Anzahl : " + pair.getValue() + " (" + percentage + "%)\n";
            handlePrint(out4, outputPath);
        }

    }

    // with this function we actually test the backlink, throws various errors
    public static HashMap<String, Integer> testURL(String urlAsString, int customTimeout) {
        // String currentURL = "https://internet-sicherheit.de";
        int code = 504;
        String outputMessage = "";

        try {
            URL url = new URL(urlAsString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(customTimeout);
            for (int i = 1; i <= 8; i++) {
                // System.out.println(connection.getHeaderFieldKey(i) + " = " +
                // connection.getHeaderField(i));
                outputMessage = outputMessage + connection.getHeaderFieldKey(i) + " = " + connection.getHeaderField(i)
                        + "\n";
            }
            // connection.setRequestMethod("GET");
            connection.connect();
            // connection.wait(10000, 0);
            code = connection.getResponseCode();
            String mimeType = connection.getContentType();
            String responseMessage = connection.getResponseMessage();
            connection.disconnect();
            outputMessage = outputMessage + "code = " + code + "\n";
            outputMessage = outputMessage + "mimeType = " + mimeType + "\n";
            outputMessage = outputMessage + "responseMessage = " + responseMessage + "\n";
            // System.out.println("code = " + code);
            // System.out.println("mimeType = " + mimeType);
            // System.out.println("responseMessage = " + responseMessage);
        } catch (java.net.SocketTimeoutException ste) {
            System.out.println("Der Verbindungsversuch für '" + urlAsString + "' wurde nach " + customTimeout
                    + " Millisekunden abgebrochen. "
                    + "\nStatuscode = 504 (Zeitüberschreitung)");
            // ste.printStackTrace();

        } catch (java.io.IOException ioe) {
            ioe.printStackTrace();
        }
        HashMap<String, Integer> messageAndCode = new HashMap<String, Integer>();
        messageAndCode.put(outputMessage, code);
        return messageAndCode;
    }

    public static ArrayList<Backlink> getBacklinks(String filename) {

        String user_dir = System.getProperty("user.dir") + "/backlink_checker/";
        String path = user_dir + filename;
        String line = "initial string...";
        BufferedReader reader;
        ArrayList<Backlink> backlinks = new ArrayList<Backlink>();
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        try {
            reader = new BufferedReader(new FileReader(path));
            line = reader.readLine();

            while (line != null) {

                // read next line
                line = reader.readLine();
                if (line != null) {
                    line = line.replaceAll("\",\"", "___");
                    line = line.replaceAll("\"", "");
                    // System.out.println(line);
                    String[] backlink_felder = line.split("___");

                    Backlink backlink = new Backlink();
                    // string "Domain", int "Domainrank", string "Origin", string "Target",
                    // boolean "Nofollow", string "Anchor",boolean "Deleted"
                    backlink.domain = backlink_felder[0];
                    backlink.setDomainRank(backlink_felder[1]);
                    backlink.origin = backlink_felder[2];
                    backlink.target = backlink_felder[3];
                    backlink.setNoFollow(backlink_felder[4]);
                    backlink.anchor = backlink_felder[5];
                    backlink.setDeleted(backlink_felder[6]);
                    backlinks.add(backlink);

                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return backlinks;

    }

}
