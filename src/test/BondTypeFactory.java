package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.openscience.cdk.interfaces.IBond;

public class BondTypeFactory {
    
    private class Dictionary {
        
        private HashMap<String, Map<String, Map<String, Integer>>> map;
        
        public Dictionary() {
            map = new HashMap<String, Map<String, Map<String, Integer>>>();
        }
        
        public int getValue(String keyA, String keyB, String keyC) {
            if (map.containsKey(keyA)) {
                Map<String, Map<String, Integer>> innerMap = map.get(keyA); 
                if (innerMap.containsKey(keyB)) {
                    if (innerMap.get(keyB).containsKey(keyC)) {
                        return innerMap.get(keyB).get(keyC);
                    }
                } else if (innerMap.containsKey(keyC)) {
                    if (innerMap.get(keyC).containsKey(keyB)) {
                        return innerMap.get(keyC).get(keyB);
                    }
                }
            }
            return -1;
        }
        
        public void add(String line) {
            String[] parts = line.split(":");
            String keyA = parts[0];
            String keyB = parts[1];
            String keyC = parts[2];
            int value = Integer.parseInt(parts[3]);
            
            if (map.containsKey(keyA)) {
                Map<String, Map<String, Integer>> innerMap = map.get(keyA);
                if (innerMap.containsKey(keyB)) {
                    Map<String, Integer> innerInnerMap = innerMap.get(keyB);
                    if (innerInnerMap.containsKey(keyC)) {
                        // throw exception?
                    } else {
                        innerInnerMap.put(keyC, value);
                    }
                } else {
                    innerMap.put(keyB, makeInnerInner(keyB, keyC, value));
                }
            } else {
                Map<String, Map<String, Integer>> innerMap = 
                    new HashMap<String, Map<String, Integer>>();
                innerMap.put(keyB, makeInnerInner(keyB, keyC, value));
                map.put(keyA, innerMap);
            }
        }
        
        private Map<String, Integer> makeInnerInner( 
                                    String keyB, String keyC, int value) {
            Map<String, Integer> innerInnerMap = 
                new HashMap<String, Integer>();
            innerInnerMap.put(keyC, value);
            return innerInnerMap;
        }
        
        public void toStream(PrintStream stream) {
            for (String keyA : map.keySet()) {
                Map<String, Map<String, Integer>> innerMap = map.get(keyA);
                for (String keyB : innerMap.keySet()) {
                    for (String keyC : innerMap.get(keyB).keySet()) {
                        int value = innerMap.get(keyB).get(keyC);
                        stream.println(String.format("%s:%s:%s:%s", keyA, keyB, keyC, value));
                    }
                }
            }
        }
    }
    
    public static final String DICTIONARY = "bond_dictionary.txt";
    
    private static BondTypeFactory instance;
    
    private Dictionary bondMap;
    
    private BondTypeFactory() throws IOException {
        File file = new File(DICTIONARY);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        bondMap = new Dictionary();
        String line;
        while ((line = reader.readLine()) != null) {
            bondMap.add(line);
        }
        reader.close();
    }
    
    public static BondTypeFactory getInstance() throws IOException {
        if (instance == null) {
            instance = new BondTypeFactory();
        }
        return instance;
    }
    
    public IBond.Order getBondOrder(String resName, String atomNameA, String atomNameB) {
        int value = bondMap.getValue(resName, atomNameA, atomNameB);
        switch (value) {
            case 2 : return IBond.Order.DOUBLE;
            case 3 : return IBond.Order.TRIPLE;
            default: return IBond.Order.SINGLE; 
        }
    }
    
    public void printDictionaryToStream(PrintStream stream) {
        bondMap.toStream(stream);
    }

}
