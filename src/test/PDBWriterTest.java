package test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import javax.vecmath.Point3d;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.Atom;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.PDBWriter;

public class PDBWriterTest {
    
    private IMolecule singleAtomMolecule() {
        return singleAtomMolecule("");    
    }
    
    private IMolecule singleAtomMolecule(String id) {
        return singleAtomMolecule(id, null);
    }
    
    private IMolecule singleAtomMolecule(String id, Integer formalCharge) {
        IMolecule mol = new Molecule();
        IAtom atom = new Atom("C", new Point3d(0.0, 0.0, 0.0)); 
        mol.addAtom(atom);
        mol.setID(id);
        if (formalCharge != null) {
            atom.setFormalCharge(formalCharge);
        }
        return mol;
    }
    
    private IMolecule singleBondMolecule() {
        IMolecule mol = new Molecule();
        mol.addAtom(new Atom("C", new Point3d(0.0, 0.0, 0.0)));
        mol.addAtom(new Atom("O", new Point3d(1.0, 1.0, 1.0)));
        mol.addBond(0, 1, IBond.Order.SINGLE);
        return mol;
    }
    
    private String getAsString(IMolecule mol) throws CDKException, IOException {
        StringWriter stringWriter = new StringWriter();
        PDBWriter writer = new PDBWriter(stringWriter);
        writer.writeMolecule(mol);
        writer.close();
        return stringWriter.toString();
    }
    
    private String[] getAsStringArray(IMolecule mol) throws CDKException, IOException {
        return getAsString(mol).split(System.getProperty("line.separator"));
    }
        
    @Test
    public void writeAsHET() throws CDKException, IOException {
        IMolecule mol = singleAtomMolecule();
        StringWriter stringWriter = new StringWriter();
        PDBWriter writer = new PDBWriter(stringWriter);
        writer.getIOSettings()[0].setSetting("true");
        writer.writeMolecule(mol);
        writer.close();
        String asString = stringWriter.toString();
        Assert.assertTrue(asString.indexOf("HETATM") != -1);
    }
    
    @Test
    public void writeAsATOM() throws CDKException, IOException {
        IMolecule mol = singleAtomMolecule();
        StringWriter stringWriter = new StringWriter();
        PDBWriter writer = new PDBWriter(stringWriter);
        writer.getIOSettings()[0].setSetting("false");
        writer.writeMolecule(mol);
        writer.close();
        String asString = stringWriter.toString();
        Assert.assertTrue(asString.indexOf("ATOM") != -1);
    }
    
    @Test
    public void writeMolID() throws CDKException, IOException {
        IMolecule mol = singleAtomMolecule("ZZZ");
        Assert.assertTrue(getAsString(mol).indexOf("ZZZ") != -1);
    }
    
    @Test
    public void writeNullMolID() throws CDKException, IOException {
        IMolecule mol = singleAtomMolecule(null);
        Assert.assertTrue(getAsString(mol).indexOf("MOL") != -1);
    }
    
    @Test
    public void writeEmptyStringMolID() throws CDKException, IOException {
        IMolecule mol = singleAtomMolecule("");
        Assert.assertTrue(getAsString(mol).indexOf("MOL") != -1);
    }

    @Test
    public void writeChargedAtom() throws CDKException, IOException {
        IMolecule mol = singleAtomMolecule("", 1);
        String[] lines = getAsStringArray(mol);
        Assert.assertTrue(lines[lines.length - 2].endsWith("+1"));
    }
    
    @Test
    public void writeMoleculeWithBond() throws CDKException, IOException {
        IMolecule mol = singleBondMolecule();
        String[] lines = getAsStringArray(mol);
        String lastLineButTwo = lines[lines.length - 3];
        String lastLineButOne = lines[lines.length - 2];
        System.out.println(Arrays.toString(lines));
        Assert.assertEquals("CONECT    1    2", lastLineButTwo);
        Assert.assertEquals("CONECT    2    1", lastLineButOne);
    }
}
