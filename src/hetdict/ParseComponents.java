package hetdict;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.aromaticity.AromaticityCalculator;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.atomtype.IAtomTypeMatcher;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.AtomSignature;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

//import com.hp.hpl.jena.rdf.model.Model;
//import com.hp.hpl.jena.rdf.model.ModelFactory;
//import com.hp.hpl.jena.rdf.model.Property;
//import com.hp.hpl.jena.rdf.model.Resource;

public class ParseComponents {
    
    public static void printMol(IMolecule mol) {
        System.out.print(mol.getID() + " {");
        for (IAtom atom : mol.atoms()) {
            System.out.print("[" + atom.getID() + ":" + atom.getSymbol() + ":" + atom.getAtomTypeName() + "]");
        }
        System.out.print("} {");
        for (IBond bond : mol.bonds()) {
            System.out.print(bond.getAtom(0).getID() + "-" + bond.getAtom(1).getID() + ",");
        }
        System.out.print("}\n");
    }
    
    public static void printAtoms(IMolecule mol) {
        for (IAtom atom : mol.atoms()) {
            System.out.println(mol.getID() + "." + atom.getID() + ":" + atom.getAtomTypeName());
        }
    }
    
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
                    nulls.add(new String[]{ atom.getID(), atom.getSymbol(), atomSignature.toCanonicalString()});
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
    
//    public static void addToModel(Model model, Resource hetgroup, 
//            IMolecule molecule, List<String[]> nulls, 
//            String resourceURI,
//            String atomResourceURI,
//            Property hasAtom,
//            Property hasAtomID, 
//            Property hasAtomSymbol, 
//            Property hasHeight2Sig,
//            Property inHetgroup) {
//        for (String[] nullTriple : nulls) {
//            String atomID     = nullTriple[0];
//            String atomSymbol = nullTriple[1];
//            String atomSig    = nullTriple[2];
//            Resource atomResource = model.createResource(atomResourceURI + atomID);
////            Resource atomResource = model.createResource("atom/" + atomID);
////            Resource atomResource = model.createBag();
//            atomResource.addProperty(hasAtomID, atomID);
//            atomResource.addProperty(hasAtomSymbol, atomSymbol);
//            atomResource.addProperty(hasHeight2Sig, atomSig);
//            hetgroup.addProperty(hasAtom, atomResource);
//        }
//    }
//    
//    public static Resource addHetgroupToModel(
//            IMolecule molecule, Model model, String resourceURI) {
//        String molID = molecule.getID();
//        Resource hetgroup = model.createResource(resourceURI + molID);
//        return hetgroup;
//    }
    
//    public static void printModel(Model model) {
////        model.write(System.out);
//        model.write(System.out, "N-TRIPLE");
//    }
//    
    public static void main(String[] args) {
//        String defaultLocation = "components.cif";
//        String defaultLocation = "077.cif";
        String defaultLocation = "tbd.cif";
        String location;
        if (args.length > 0) {
            location = args[0];
        } else {
            location = defaultLocation;
        }
        
        try {
            // CDK stuff
            IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
            IAtomTypeMatcher matcher = 
                new CustomAtomTypeMatcher(CDKAtomTypeMatcher.getInstance(builder));
            FileReader fileReader = new FileReader(new File(location));
            IteratingCIFReader reader = new IteratingCIFReader(fileReader, builder);
            
            // rdf-a stuff
//            String resourceURI = "http://cpt/r/";
//            String resourceURI = "het/";
//            String atomResourceURI = "http://cpt/ar/";
//            String atomResourceURI = "atom/";
//            String propertyURI = "http://cpt/p/";
//            String propertyURI = "prop/";
//            Model model = ModelFactory.createDefaultModel();
//            Resource root = model.createResource(resourceURI);
//            Property hasAtom = model.createProperty(propertyURI + "hasAtom");
//            Property hasAtomID = model.createProperty(propertyURI + "hasAtomID");
//            Property hasAtomSymbol = model.createProperty(propertyURI + "hasAtomSymbol");
//            Property hasHeight2Sig = model.createProperty(propertyURI + "hasH2Sig");
//            Property inHetgroup = model.createProperty(propertyURI + "isHET");
            
            while (reader.hasNext()) {
                IMolecule molecule = (IMolecule) reader.next();
                List<String[]> nulls = typeMolecule(matcher, molecule);
//                printMol(molecule);
                printAtoms(molecule);
                if (nulls.size() > 0) {
//                    Resource hetgroup = addHetgroupToModel(molecule, model, resourceURI);
//                    root.addProperty(isHetgroupProperty, hetgroup);
//                    addToModel(model, hetgroup, molecule, nulls, resourceURI, atomResourceURI,
//                            hasAtom, hasAtomID, hasAtomSymbol, hasHeight2Sig, inHetgroup);
                }
            }
//            printModel(model);
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }

}
