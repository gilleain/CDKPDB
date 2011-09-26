package hetdict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.atomtype.IAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.AtomSignature;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.BondManipulator;

public class MakeBondDictionary {
    
    public static List<String[]> typeMolecule(IAtomTypeMatcher typeMatcher, IMolecule mol) {
        List<String[]> nulls = new ArrayList<String[]>();
        try {
            CDKHueckelAromaticityDetector.detectAromaticity(mol);
            int signatureHeight = 1;
            for (IAtom atom : mol.atoms()) {
                IAtomType type = typeMatcher.findMatchingAtomType(mol, atom);
                if (type == null) {
                    AtomSignature atomSignature = 
                        new AtomSignature(atom, signatureHeight, mol);
//                    System.out.println(
//                            "Null type for " + mol.getID() + " " + atom.getID() + " " + atomSignature.toCanonicalString());
                    nulls.add(new String[]{ mol.getID(), atom.getID(), atom.getSymbol(), atomSignature.toCanonicalString()});
                } else {
                    AtomTypeManipulator.configure(atom, type);
                }
            }
        } catch (CDKException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return nulls;
    }
    
    public static void printBonds(IMolecule mol) {
        String molName = mol.getID();
        for (IBond bond : mol.bonds()) {
            IBond.Order order = bond.getOrder(); 
            if (order != IBond.Order.SINGLE) {
                String atomName0 = bond.getAtom(0).getID(); 
                String atomName1 = bond.getAtom(1).getID();
                int orderNumeral = ((int)BondManipulator.destroyBondOrder(order)) + 1;
                System.out.println(
                        molName + ":" + atomName0 + ":" + atomName1 + ":" + orderNumeral);
            }
        }
    }
    
    public static void main(String[] args) {
//        String defaultLocation = "tbd.cif";
        String defaultLocation = "components.cif";
        String location;
        if (args.length > 0) {
            location = args[0];
        } else {
            location = defaultLocation;
        }
        
        IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
        IAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(builder);
        try {
            FileReader fileReader = new FileReader(new File(location));
            IteratingCIFReader reader = new IteratingCIFReader(fileReader, builder);
            while (reader.hasNext()) {
                IMolecule molecule = (IMolecule) reader.next();
                List<String[]> nulls = typeMolecule(matcher, molecule);
//                printBonds(molecule);
                for (String[] nullStringArr : nulls) {
                    for (String s : nullStringArr) {
                        System.out.print(s);
                        System.out.print("\t");
                    }
                    System.out.println();
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
