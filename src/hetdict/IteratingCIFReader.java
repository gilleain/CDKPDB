package hetdict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.vecmath.Point3d;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.formats.IResourceFormat;
import org.openscience.cdk.io.iterator.DefaultIteratingChemObjectReader;
import org.openscience.cdk.io.setting.IOSetting;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

public class IteratingCIFReader extends DefaultIteratingChemObjectReader {
    
    private BufferedReader input;
    
    private IChemObjectBuilder builder;
    
    private IMolecule currentMol;
    
    private boolean parsedNext;
    
    private boolean started;
    
    private boolean finished;
    
    private boolean unterminatedLoop;
    
    private String line;
    
    public IteratingCIFReader(Reader in, IChemObjectBuilder builder) {
        this.builder = builder;
        input = new BufferedReader(in);
        setup();
    }
    
    public IteratingCIFReader(InputStream in, IChemObjectBuilder builder) {
        this(new InputStreamReader(in), builder);
    }
    
    private void setup() {
        currentMol = null;
        parsedNext = false;
        started = false;
        finished = false;
        unterminatedLoop = false;
        line = null;
    }

    @Override
    public void setReader(Reader reader) throws CDKException {
        if (reader instanceof BufferedReader) {
            input = (BufferedReader)reader;
        } else {
            input = new BufferedReader(reader);
        }
        setup();
    }

    @Override
    public void setReader(InputStream reader) throws CDKException {
        setReader(new InputStreamReader(reader));
    }

    @Override
    public IResourceFormat getFormat() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    @Override
    public IOSetting[] getIOSettings() {
        // TODO Auto-generated method stub
        return new IOSetting[] {};
    }

    @Override
    public boolean hasNext() {
        if (currentMol != null && !finished) {
            return true;
        } else if (started && line == null) {
            return false;
        } else {
            try {
                currentMol = parseNext();
                return currentMol != null;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return false;
            }
        }
    }
    
    @Override
    public IChemObject next() {
        if (!parsedNext) {
            try {
                currentMol = parseNext();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        
        if (currentMol == null) {
            throw new NoSuchElementException();
        }
        parsedNext = false;
        return currentMol;
    }

    private IMolecule parseNext() throws IOException {
        boolean inNext = false;
        IMolecule molecule = null;
        if (line == null && !started) {
            line = input.readLine();
            started = true;
        }
        while (line != null) {
            if (line.startsWith("data")) {
                if (inNext) {
                    parsedNext = true;
                    return molecule;
                } else {
                    molecule = builder.newInstance(IMolecule.class);
                    inNext = true;
                }
            } else if (line.startsWith("loop_")) {
                processLoop(molecule);
            } else if (line.startsWith("_chem_comp.three_letter_code")) {
                molecule.setID(line.substring(30).trim());
            }
            if (unterminatedLoop) {
                unterminatedLoop = false;
            } else {
                line = input.readLine();
            }
        }
        if (line == null) {
            input.close();
            finished = true;
        }
        return molecule;
    }

    private void processLoop(IMolecule molecule) throws IOException {
        line = input.readLine();
        if (line != null && line.startsWith("_chem_comp_atom")) {
            skipTags();
            while (line != null) {
                if (line.startsWith("#")) break;
//                String[] parts = line.split("\\s+");
                List<String> parts = partition(line);
                try {
                    String atom_id = strip(parts.get(1));
                    String type_symbol = strip(parts.get(3));
                    if (type_symbol.length() > 1) {
                        type_symbol = type_symbol.substring(0, 1) 
                                    + type_symbol.substring(1, 2).toLowerCase(); 
                    }
                    int charge = parseInt(parts.get(4));
                    Point3d point = new Point3d(
                                        parseFloat(parts.get(9)),
                                        parseFloat(parts.get(10)),
                                        parseFloat(parts.get(11)));
                    IAtom atom = builder.newInstance(IAtom.class);
                    atom.setID(atom_id);
                    atom.setSymbol(type_symbol);
                    atom.setPoint3d(point);
                    molecule.addAtom(atom);
                    atom.setFormalCharge(new Integer(charge));
                } catch (IndexOutOfBoundsException iiobe) {
                    System.err.println("IIOBE" + line);
                }
                line = input.readLine();
            }
        } else if (line != null && line.startsWith("_chem_comp_bond")) {
            skipTags();
            while (line != null) {
                if (line.startsWith("#")) break;
//                String[] parts = line.split("\\s+");
                List<String> parts = partition(line);
                String id1 = strip(parts.get(1));
                String id2 = strip(parts.get(2));
                String order = parts.get(3);
                try {
                    IAtom atom1 = AtomContainerManipulator.getAtomById(molecule, id1);
                    IAtom atom2 = AtomContainerManipulator.getAtomById(molecule, id2);
                    if (order.equals("SING")) {
                        molecule.addBond(builder.newInstance(IBond.class, atom1, atom2, IBond.Order.SINGLE));
                    } else if (order.equals("DOUB")) {
                        molecule.addBond(builder.newInstance(IBond.class, atom1, atom2, IBond.Order.DOUBLE));
                    } else {
                        molecule.addBond(builder.newInstance(IBond.class, atom1, atom2, IBond.Order.TRIPLE));
                    }
                } catch (CDKException cdke) {
//                    System.err.print(cdke.getMessage() + " " + id1 + " " + id2 + " " + line);
                    System.err.print(cdke.getMessage() + " [" + id1 + "] [" + id2 + "] IN ");
                    for (IAtom atom : molecule.atoms()) {
                        System.err.print("[" + atom.getID() + "] ");
                    }
                    System.err.println(" " + line);
                }
                line = input.readLine();
            }
        } else {
            while (line != null && !(line.startsWith("#") || line.startsWith("data"))) {
                line = input.readLine();
            }
        }
        
        if (!line.startsWith("#")) {
            unterminatedLoop = true;
        }
    }
    
    private List<String> partition(String s) {
        List<String> parts = new ArrayList<String>();
        int ptr = 0;
        String current = "";
        boolean inSQ = false;
        boolean inDQ = false;
        boolean inW = false;
        while (ptr < s.length()) {
            char c = s.charAt(ptr);
//            System.out.println("c " + c + " inSQ " + inSQ + " inDQ " + inDQ + " inW " + inW + " " + parts);
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
    
    private String strip(String s) {
        return s;
    }
    
    private float parseFloat(String floatString) {
        if (floatString.equals("?")) {
            return 0.0f;
        } else {
            try {
                return Float.parseFloat(floatString);
            } catch (NumberFormatException nfe) {
                System.err.println(nfe.getMessage() + " " + line);
                return 0.0f;
            }
        }
    }
    
    private int parseInt(String intString) {
        if (intString.equals("?")) {
            return 0;
        } else {
            try {
                return Integer.parseInt(intString);
            } catch (NumberFormatException nfe) {
                System.err.println(nfe.getMessage() + " " + line);
                return 0;
            }
        }
    }
    
    private void skipTags() throws IOException {
        while ((line = input.readLine()) != null) {
            if (!(line.startsWith("_"))) return;
        }
    }

}
