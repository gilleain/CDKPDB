package indexed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openscience.cdk.Bond;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.ElectronContainer;
import org.openscience.cdk.LonePair;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.SingleElectron;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.ICDKObject;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IElectronContainer;
import org.openscience.cdk.interfaces.ILonePair;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.ISingleElectron;

public class IndexedBuilder implements IChemObjectBuilder {
    
    private IChemObjectBuilder defaultBuilder;
    
    public IndexedBuilder() {
        defaultBuilder = DefaultChemObjectBuilder.getInstance();
    }

    @Override
    public <T extends ICDKObject> T newInstance(Class<T> clazz,
            Object... params) throws IllegalArgumentException {
        if (IElectronContainer.class.isAssignableFrom(clazz)) {
            return newElectronContainerInstance(clazz, params);
        } else if (IMolecule.class.isAssignableFrom(clazz)) {
            return newMoleculeInstance(clazz, params);
        } else {
            System.err.println("class " + clazz + " params " + Arrays.toString(params));
            return defaultBuilder.newInstance(clazz, params);
        }
    }
    
    public <T extends ICDKObject>T newMoleculeInstance(
            Class<T> clazz, Object... params) {
        if (params.length == 0) {
            return (T)new Molecule();
        } else if (params.length == 1 &&
            params[0] instanceof IAtomContainer) {
            return (T)new Molecule((IAtomContainer)params[0]);
        } else if (params.length == 4 &&
                params[0] instanceof Integer &&
                params[1] instanceof Integer &&
                params[2] instanceof Integer &&
                params[3] instanceof Integer) {
            return (T)new Molecule(
                (Integer)params[0], (Integer)params[1], (Integer)params[2], (Integer)params[3]
            );
        }
        throw new IllegalArgumentException(
                "No constructor found with the given number of parameters."
        );
    }
    
    @SuppressWarnings("unchecked")
    private <T extends ICDKObject>T newElectronContainerInstance(
            Class<T> clazz, Object... params)
    {
        if (IBond.class.isAssignableFrom(clazz)) {
            if (params.length == 0) {
                return (T)new Bond();
            } else if (params.length == 2 &&
                    params[0] instanceof IAtom &&
                    params[0] instanceof IAtom) {
                return (T)new Bond((IAtom)params[0], (IAtom)params[1]);
            } else if (params.length == 3 &&
                    params[0] instanceof IAtom &&
                    params[1] instanceof IAtom &&
                    params[2] instanceof IBond.Order) {
                return (T)new Bond(
                    (IAtom)params[0], (IAtom)params[1], (IBond.Order)params[2]
                );
            } else if (params.length == 4 &&
                    params[0] instanceof IAtom &&
                    params[1] instanceof IAtom &&
                    params[2] instanceof IBond.Order &&
                    params[3] instanceof IBond.Stereo) {
                return (T)new Bond(
                    (IAtom)params[0], (IAtom)params[1],
                    (IBond.Order)params[2], (IBond.Stereo)params[3]
                );
            } else if (params[params.length-1] instanceof IBond.Order) {
                // the IBond(IAtom[], IBond.Order) constructor
                boolean allIAtom = true;
                int orderIndex = params.length-1;
                List<IAtom> atoms = new ArrayList<IAtom>();
                for (int i=0; i<(orderIndex-1) && allIAtom; i++) {
                    if (!(params[i] instanceof IAtom)) {
                        allIAtom = false;
                        atoms.add((IAtom)params[i]);
                    }
                }
                if (allIAtom) {
                    return (T)new Bond(
                        atoms.toArray(new IAtom[atoms.size()]),
                        (IBond.Order)params[orderIndex]
                    );
                }
            } else {
                // the IBond(IAtom[]) constructor
                boolean allIAtom = true;
                for (int i=0; i<(params.length-1) && allIAtom; i++) {
                    if (!(params[i] instanceof IAtom)) allIAtom = false;
                }
                if (allIAtom) {
                    return (T)new Bond((IAtom[])params);
                }
            }
        } else if (ILonePair.class.isAssignableFrom(clazz)) {
            if (params.length == 0) {
                return (T)new LonePair();
            } else if (params.length == 1 &&
                    params[0] instanceof IAtom) {
                return (T)new LonePair((IAtom)params[0]);
            }
        } else if (ISingleElectron.class.isAssignableFrom(clazz)) {
            if (params.length == 0) {
                return (T)new SingleElectron();
            } else if (params.length == 1 &&
                    params[0] instanceof IAtom) {
                return (T)new SingleElectron((IAtom)params[0]);
            }
        } else {
            if (params.length == 0) return (T)new ElectronContainer();
        }

        throw new IllegalArgumentException(
            "No constructor found with the given number of parameters."
        );
    }

}
