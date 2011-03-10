package test;

import java.io.IOException;

import org.junit.Test;

public class BondTypeFactoryTest {
    
    @Test
    public void make() {
        try {
            BondTypeFactory factory = BondTypeFactory.getInstance();
        } catch (IOException ioe) {
            
        }
    }

}
