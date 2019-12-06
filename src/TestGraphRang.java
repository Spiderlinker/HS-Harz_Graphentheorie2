/* 
 * Hochschule Harz
 * Fachbereich Automatisierung und Informatik
 * Prof. Dr. Bernhard Zimmermann
 * 
 * LV "Graphentheorie" SS 2003
 *
 * Datenstruktur: gerichteter Graph
 * Testprogramm fuer die Funktion "Rang eines Graphen"
 *
 *
 * @author Bernhard Zimmermann
 * @version 1.0
 * 
 */

/**
 * Datenstruktur: gerichteter Graph
 * Testprogramm fuer die Funktion "Rang eines Graphen"
 *
 * @author Bernhard Zimmermann
 * @version 1.0, 28.06.2003
 *
 */
public class TestGraphRang extends Environment {

  public static void main(String[] args) {
    if (args.length > 0) {
      try {
        System.setIn(new java.io.FileInputStream(args[0]));
      } catch (java.io.FileNotFoundException e) {
        System.out.println("*** Eingabedatei nicht gefunden ***");
        System.exit(1);
      }
    }
    if (args.length > 1) {
      try {
        System.setOut(
          new java.io.PrintStream(
            new java.io.FileOutputStream(args[1])));
      } catch (java.io.FileNotFoundException e) {}
    }
    TestGraphRang pn = new TestGraphRang();
    pn.mainProgram(args);
  }

  void mainProgram(String[] args) {

    System.out.println("Operationen auf Graphen");
    System.out.println("=======================");

    Graph g = new Graph(); // leerer Graph
    g.readGraph();
    System.out.println("Rang des Graphen: " + g.rang());

  } // mainProgram

} // TestGraphRang