package hetdict;

import org.openscience.cdk.AtomType;
import org.openscience.cdk.atomtype.IAtomTypeMatcher;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.NoSuchAtomTypeException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class CustomAtomTypeMatcher implements IAtomTypeMatcher {
    
    private IAtomTypeMatcher delegatingMatcher;
    
    private AtomTypeFactory atomTypeFactory;
    
    public CustomAtomTypeMatcher(IAtomTypeMatcher delegatingMatcher) {
        this.delegatingMatcher = delegatingMatcher;
        atomTypeFactory = AtomTypeFactory.getInstance(
                NoNotificationChemObjectBuilder.getInstance());
    }

    @Override
    public IAtomType findMatchingAtomType(
            IAtomContainer container, IAtom atom) throws CDKException {
        IAtomType initialType = delegatingMatcher.findMatchingAtomType(container, atom);
        if (initialType == null) {
            try {
                return findCustomType(container, atom);
            } catch (NoSuchAtomTypeException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return initialType;
        }
    }

    private IAtomType findCustomType(IAtomContainer container, IAtom atom) throws NoSuchAtomTypeException {
        String symbol = atom.getSymbol();
        int charge = atom.getFormalCharge();
        int connectedAtomCount = container.getConnectedAtomsCount(atom);
//        if (symbol.equals("N")) {
//            if (charge == 1) {
////                if (connectedAtomCount == 4) {
//                    return atomTypeFactory.getAtomType("N.sp3");
////                }
//            }
//        }
        return null;
    }

    @Override
    public IAtomType[] findMatchingAtomType(
            IAtomContainer container) throws CDKException {
        // TODO Auto-generated method stub
        return null;
    }

}
