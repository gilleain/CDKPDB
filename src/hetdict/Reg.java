package hetdict;

import java.util.ArrayList;
import java.util.List;


public class Reg {
    
    private static List<String> partition(String s) {
        List<String> parts = new ArrayList<String>();
        int ptr = 0;
        String current = "";
        boolean inSQ = false;
        boolean inDQ = false;
        boolean inW = false;
        while (ptr < s.length()) {
            char c = s.charAt(ptr);
            System.out.println("c " + c + " inSQ " + inSQ + " inDQ " + inDQ + " inW " + inW + " " + parts);
            // if its whitespace, skip it, unless we're in quotes
            if (c == ' ') {
                if (inSQ || inDQ) {
                    current += c;
                    inW = false;
                } else {
                    inW = true;
                }
            } else if (c == '"') {
                if (inDQ) {
                    inDQ = false;
                } else {
                    if (inSQ) {
                        current += c;
                    } else {
                        if (inW) {
                            current = startNew(current, parts);
                            inW = false;
                        }
                        inDQ = true;
                    }
                }
            } else if (c == '\'') {
                if (inSQ) {
                    inSQ = false;
                } else {
                    if (inDQ) {
                        current += c;
                    } else {
                        if (inW) {
                            current = startNew(current, parts);
                            inW = false;
                        }
                        inSQ = true;
                    }
                }
            } else {
                if (inW) {
                    current = startNew(c, current, parts);
                } else {
                    current += c;
                }
                inW = false;
            }
            ptr++;
        }
        parts.add(new String(current));
        return parts;
    }
    
    private static String startNew(char c, String current, List<String> parts) {
        if (current != null) parts.add(new String(current));
        return new String(new char[] {c});
    }
    
    private static String startNew(String current, List<String> parts) {
        if (current != null) parts.add(new String(current));
        return new String();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String sDS = "hello \"world 'or' worlds\" er world";
        String sSD = "hello 'world \"or\" worlds' er world";
        String l = "2FL \"HO1'\" \"'HO1\" H 0 0 N N N 25.619 82.487 130.272 -0.476 -0.766 5.690  \"HO1'\" 2FL 41";
//        System.out.println(partition(sDS));
//        System.out.println(partition(sSD));
        System.out.println(partition(l));
    }

}
