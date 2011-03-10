package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.io.PDBReader;
import org.openscience.cdk.io.listener.PropertiesListener;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smsd.labelling.AtomContainerPrinter;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.SaturationChecker;
import org.openscience.cdk.tools.SmilesValencyChecker;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

public class ReadPDBeChemData {
    
    public static String dataDir = "data";
    
    public void testSmallMolecule(String filename) throws FileNotFoundException, CDKException {
        File file = new File(dataDir, filename + ".pdb");
        PDBReader reader = new PDBReader(new FileReader(file));
        reader.getIOSettings()[2].setSetting("true");
        IChemFile chemFile = (IChemFile) reader.read(new ChemFile());
        List<IAtomContainer> allAtomContainers =
            ChemFileManipulator.getAllAtomContainers(chemFile);
        Assert.assertTrue("Not enough molecules", allAtomContainers.size() > 0);
        IAtomContainer molecule = allAtomContainers.get(0);
        Assert.assertTrue("Not connected", ConnectivityChecker.isConnected(molecule));
//        CDKHydrogenAdder.getInstance(
//                NoNotificationChemObjectBuilder.getInstance()).addImplicitHydrogens(molecule);
        for (IAtom atom : molecule.atoms()) {
            atom.setImplicitHydrogenCount(0);
        }
        for (IAtom atom : molecule.atoms()) {
            System.out.print(atom.getSymbol() + "|" + atom.getAtomTypeName() + "|" + atom.getID() + ", ");
        }
        System.out.println();
//        SaturationChecker saturator = new SaturationChecker();
//        saturator.saturate(molecule);
//        LigandHelper.addMissingBondOrders(molecule);
//        SmilesValencyChecker valenceyChecker = new SmilesValencyChecker();
//        valenceyChecker.saturate(molecule);
        LigandHelper.addMissingBondOrders(molecule);
        SmilesGenerator smilesGenerator = new SmilesGenerator();
        System.out.println(smilesGenerator.createSMILES(molecule));
        System.out.println(new AtomContainerPrinter().toString(molecule));
    }
    
    public void testMacromolecule(String filename) throws FileNotFoundException, CDKException {
        File file = new File(dataDir, filename + ".pdb");
        PDBReader reader = new PDBReader(new FileReader(file));
        IChemFile chemFile = (IChemFile) reader.read(new ChemFile());
        Assert.assertNotNull(chemFile);
        Assert.assertTrue(chemFile.getChemSequenceCount() > 0);
        IChemSequence chemSeq = chemFile.getChemSequence(0);
        Assert.assertNotNull(chemSeq);
        Assert.assertTrue(chemSeq.getChemModelCount() > 0);
        IChemModel chemModel = chemSeq.getChemModel(0);
        Assert.assertNotNull(chemModel);
        IMoleculeSet molSet = chemModel.getMoleculeSet();
        Assert.assertNotNull(molSet);
        Assert.assertTrue("Not enough atom containers!", molSet.getAtomContainerCount() > 0);
    }
    
    @Test
    public void test00C() throws FileNotFoundException, CDKException {
        testSmallMolecule("00C");
    }
    
    @Test
    public void testNAD() throws FileNotFoundException, CDKException {
        testSmallMolecule("NAD");
    }
    
    @Test
    public void testCOB() throws FileNotFoundException, CDKException {
        testSmallMolecule("COB");
    }
    
    @Test
    public void test2MHR() throws FileNotFoundException, CDKException {
        testMacromolecule("2mhr");
    }
    
    @Test
    public void testConnectRecords() throws Exception {
        String data =
            "SEQRES    111111111111111111111111111111111111111111111111111111111111111     \n" +
            "ATOM      1  N   SER A 326     103.777  74.304  20.170  1.00 21.58           N\n" + 
            "ATOM      2  CA  SER A 326     102.613  74.991  20.586  1.00 18.59           C\n" +
            "ATOM      3  C   SER A 326     101.631  74.211  21.431  1.00 17.75           C\n" +  
            "ATOM      4  O   SER A 326     101.653  74.549  22.634  1.00 18.51           O\n" +
            "CONECT    1    4\n" +
            "CONECT    4    1\n" +
            "END    \n";
        
        StringReader stringReader = new StringReader(data); 
        PDBReader reader = new PDBReader(stringReader);
        Properties properties = new Properties();
        properties.setProperty("ReadConnectSection", "true");
        properties.setProperty("UseRebondTool", "false");
        PropertiesListener listener = 
            new PropertiesListener(properties);
        reader.addChemObjectIOListener(listener);
        reader.customizeJob();

        ChemObject object = new ChemFile();
        reader.read(object);
        stringReader.close();
        Assert.assertNotNull(object);
        int bondCount = ((IChemFile)object)
                            .getChemSequence(0)
                                .getChemModel(0)
                                    .getMoleculeSet()
                                        .getMolecule(0)
                                            .getBondCount();
        /*
         * if ReadConnectSection=true and UseRebondTool=false
         * then bondCount == 1 (from just the CONECT)
         * else if ReadConnectSection=false and UseRebondTool=true
         * then bondCount == 3 (just atoms within bonding distance)
         */
        Assert.assertEquals(1, bondCount);
    }

}
