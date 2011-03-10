package test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.PDBReader;
import org.openscience.cdk.io.PDBWriter;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.templates.MoleculeFactory;

public class RoundTripLigandTest {
    
    private void convert2DTo3D(IMolecule mol) {
        for (IAtom atom : mol.atoms()) {
            Point2d p = atom.getPoint2d();
            atom.setPoint3d(new Point3d(p.x, p.y, 0));
        }
    }
    
    @Test
    public void molfactoryTest() throws Exception {
        IMolecule mol = MoleculeFactory.makePyrrole();
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(mol, false);
        sdg.generateCoordinates();
        convert2DTo3D(mol);
        StringWriter stringWriter = new StringWriter();
        PDBWriter writer = new PDBWriter(stringWriter);
        writer.writeMolecule(mol);
        writer.close();
        String output = stringWriter.toString();
        System.out.println(output);
        PDBReader reader = new PDBReader(new StringReader(output));
        IChemFile chemFile = (IChemFile) reader.read(new ChemFile());
        IMolecule molecule = 
            chemFile.getChemSequence(0).getChemModel(0).getMoleculeSet().getMolecule(0);
        Assert.assertEquals(mol.getAtomCount(), molecule.getAtomCount());
    }

}
