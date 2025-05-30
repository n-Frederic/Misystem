package M15.Ex1;

import java.io.*;

public class ReadFamilies {
    public static void main(String[] args) {
        Family family1;
        Family family2;
        Family family3;
        Person pat_willcutt;
        Person bryan_basham;

        try {
            /***
             **** Step 8: declare and initialize input stream objects
             ***/

            FileInputStream fis = new FileInputStream( "Families.ser");

            /***
             **** Step 9: read the three families from the object stream
             **** Don't forget to close the top-level stream when done.
             ***/

            ObjectInputStream ois = new ObjectInputStream(fis);
            family1 = (Family) ois.readObject();
            family2 = (Family) ois.readObject();
            family3 = (Family) ois.readObject();
            ois.close();
            fis.close();

            pat_willcutt = family1.findChild("Pat");
            bryan_basham = family3.findChild("Bryan");

            // queries
            System.out.println("Pat Willcutt has " + pat_willcutt.getNumberOfSiblings() + " siblings.");
            System.out.println("Bryan Basham has " + bryan_basham.getNumberOfSiblings() + " siblings.");

            // Handle exceptions
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
        }
    }
}
