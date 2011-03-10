package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smsd.labelling.AtomContainerPrinter;

public class ReadMolfile {
    
    public void readMolfile(String name) throws FileNotFoundException, CDKException {
        File file = new File("data/" + name + ".mol");
        MDLV2000Reader reader = new MDLV2000Reader(new FileReader(file));
        IMolecule molecule = reader.read(new Molecule());
        IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(builder);
        int i = 0;
        for (IAtom atom : molecule.atoms()) {
            IAtomType type = matcher.findMatchingAtomType(molecule, atom);
            if (type == null) {
                System.out.println(i + " null type " + atom.getSymbol());
            } else {
                System.out.println(i + " " + type.getAtomTypeName() + " type " 
                        + atom.getSymbol());
            }
            i++;
        }
        AtomContainerPrinter acp = new AtomContainerPrinter();
        System.out.println(acp.toString(molecule));
    }
    
    @Test
    public void cobTest() throws FileNotFoundException, CDKException {
        readMolfile("cob");
    }

}
