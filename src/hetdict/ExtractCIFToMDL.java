package hetdict;

import java.io.File;
import java.io.FileReader;

import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class ExtractCIFToMDL {

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Two args : filename and ID");
            System.exit(0);
        } 
        String location = args[0];
        try {
            IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
            FileReader fileReader = new FileReader(new File(location));
            IteratingCIFReader reader = new IteratingCIFReader(fileReader, builder);
            while (reader.hasNext()) {
                IMolecule molecule = (IMolecule) reader.next();
                if (molecule.getID().equals(args[1])) {
                    MDLV2000Writer writer = new MDLV2000Writer(System.out);
                    writer.writeMolecule(molecule);
                    writer.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
