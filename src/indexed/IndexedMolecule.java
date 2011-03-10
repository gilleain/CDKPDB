package indexed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectChangeEvent;
import org.openscience.cdk.interfaces.IChemObjectListener;
import org.openscience.cdk.interfaces.IElectronContainer;
import org.openscience.cdk.interfaces.ILonePair;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.interfaces.IBond.Order;
import org.openscience.cdk.interfaces.IBond.Stereo;

public class IndexedMolecule implements IMolecule {
    
    private List<IAtom> atoms;
    
    private List<IBond> bonds;
    
    private List<int[]> neighbours;
    
    public IndexedMolecule() {
        this.atoms = new ArrayList<IAtom>();
        this.bonds = new ArrayList<IBond>();
        neighbours = new ArrayList<int[]>();
    }
    
    public Object clone() {
        return null;    // TODO
    }

    @Override
    public IChemObjectBuilder getBuilder() {
        return new IndexedBuilder();
    }

    @Override
    public void setAtoms(IAtom[] atoms) {
        // TODO Auto-generated method stub
    
    }

    @Override
    public void setBonds(IBond[] bonds) {
        // TODO Auto-generated method stub
    
    }

    @Override
    public void setAtom(int number, IAtom atom) {
        // TODO Auto-generated method stub
    
    }

    @Override
    public IAtom getAtom(int number) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IBond getBond(int number) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addAtom(IAtom atom) {
        atoms.add(atom);
    }

    @Override
    public void addBond(IBond bond) {
        
    }

    @Override
    public Iterable<IAtom> atoms() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<IBond> bonds() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAtom getFirstAtom() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAtom getLastAtom() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getAtomNumber(IAtom atom) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getBondNumber(IAtom atom1, IAtom atom2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getBondNumber(IBond bond) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IBond getBond(IAtom atom1, IAtom atom2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addListener(IChemObjectListener col) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getListenerCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void removeListener(IChemObjectListener col) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNotification(boolean bool) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getNotification() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void notifyChanged() {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyChanged(IChemObjectChangeEvent evt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setProperty(Object description, Object property) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeProperty(Object description) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getProperty(Object description) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Object, Object> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setID(String identifier) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFlag(int flag_type, boolean flag_value) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getFlag(int flag_type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setProperties(Map<Object, Object> properties) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFlags(boolean[] flagsNew) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean[] getFlags() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void stateChanged(IChemObjectChangeEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addStereoElement(IStereoElement element) {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterable<IStereoElement> stereoElements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ILonePair getLonePair(int number) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ISingleElectron getSingleElectron(int number) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<ILonePair> lonePairs() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<ISingleElectron> singleElectrons() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<IElectronContainer> electronContainers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLonePairNumber(ILonePair lonePair) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSingleElectronNumber(ISingleElectron singleElectron) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IElectronContainer getElectronContainer(int number) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getAtomCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getBondCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getLonePairCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSingleElectronCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getElectronContainerCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<IAtom> getConnectedAtomsList(IAtom atom) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IBond> getConnectedBondsList(IAtom atom) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ILonePair> getConnectedLonePairsList(IAtom atom) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ISingleElectron> getConnectedSingleElectronsList(IAtom atom) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<IElectronContainer> getConnectedElectronContainersList(
            IAtom atom) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getConnectedAtomsCount(IAtom atom) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getConnectedBondsCount(IAtom atom) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getConnectedBondsCount(int atomnumber) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getConnectedLonePairsCount(IAtom atom) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getConnectedSingleElectronsCount(IAtom atom) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getBondOrderSum(IAtom atom) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Order getMaximumBondOrder(IAtom atom) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Order getMinimumBondOrder(IAtom atom) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void add(IAtomContainer atomContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addLonePair(ILonePair lonePair) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addSingleElectron(ISingleElectron singleElectron) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addElectronContainer(IElectronContainer electronContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(IAtomContainer atomContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAtom(int position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAtom(IAtom atom) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBond removeBond(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IBond removeBond(IAtom atom1, IAtom atom2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeBond(IBond bond) {
        // TODO Auto-generated method stub

    }

    @Override
    public ILonePair removeLonePair(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeLonePair(ILonePair lonePair) {
        // TODO Auto-generated method stub

    }

    @Override
    public ISingleElectron removeSingleElectron(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeSingleElectron(ISingleElectron singleElectron) {
        // TODO Auto-generated method stub

    }

    @Override
    public IElectronContainer removeElectronContainer(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeElectronContainer(IElectronContainer electronContainer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAtomAndConnectedElectronContainers(IAtom atom) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAllElements() {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAllElectronContainers() {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeAllBonds() {
        // TODO Auto-generated method stub

    }

    @Override
    public void addBond(int atom1, int atom2, Order order, Stereo stereo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addBond(int atom1, int atom2, Order order) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addLonePair(int atomID) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addSingleElectron(int atomID) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean contains(IAtom atom) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(IBond bond) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(ILonePair lonePair) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(ISingleElectron singleElectron) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(IElectronContainer electronContainer) {
        // TODO Auto-generated method stub
        return false;
    }

}
