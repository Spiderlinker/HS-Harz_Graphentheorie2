
/**
 * Datenstruktur: gerichteter Graph Testprogramm
 *
 * @author Bernhard Zimmermann
 * @version 1.0, 23.06.2001
 *
 */
public class TestGraph extends Environment {

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
				System.setOut(new java.io.PrintStream(new java.io.FileOutputStream(args[1])));
			} catch (java.io.FileNotFoundException e) {
			}
		}
		TestGraph pn = new TestGraph();
		pn.mainProgram(args);
	}

	void mainProgram(String[] args) {

		System.out.println("Operationen auf Graphen");
		System.out.println("=======================");

		Graph g = new Graph(); // leerer Graph
		g.readGraph();
		g.berechneLineareKnotenanordnung();
		g.printLineareKnotenanordnungNummern();

	} // mainProgram

} // TestGraph