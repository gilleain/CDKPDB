package test;

import indexed.IndexedMolecule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLReader;

public class TestIndexedMolecule {
    
    public static String dataDir = "data";

    @Test
    public void readTest() throws FileNotFoundException, CDKException {
        String filename = "10";
        File file = new File(dataDir, filename + ".mol");
        MDLReader reader = new MDLReader(new FileReader(file));
        IndexedMolecule molecule = reader.read(new IndexedMolecule());
//        IMolecule molecule = reader.read(new Molecule());
    }
}
