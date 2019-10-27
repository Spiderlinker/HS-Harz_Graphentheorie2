
/**
 * 
 * @author Chris Wischeropp (u34466), Oliver Lindemann (u33873)
 */
public class Graph extends Environment {

	/** Eingelesene Knoten im eingelesenen Graphen */
	private int anzahlKnoten;
	/** Eingelesener Graph als Matrix in int[][] abgespeichert */
	private int[][] graph;
	/** Array mit Anordnungsnummern aller Knoten im Graphen */
	private int[] knotenanordnungsNummern;

	/**
	 * Liest einen Graphen über den {@link TextStandardInputStream} ein und
	 * speichert diesen in der Klassenvariable {@link Graph#graph} ab.
	 */
	public void readGraph() {
		// In der ersten Zeile ist die Anzahl der Knoten im Graphen angegeben
		anzahlKnoten = stdin.readInt();
		graph = new int[anzahlKnoten][anzahlKnoten];

		// Alle Knoten des Graphens einlesen und in graph-Variable abspeichern
		for (int i = 0; i < anzahlKnoten; i++) {
			for (int j = 0; j < anzahlKnoten; j++) {
				graph[i][j] = stdin.readInt();
			}
		}
	}

	/**
	 * Berechnet für alle Knoten im eingelesenen Graphen eine passende
	 * Anordnungsnummer, sofern kein Kreis existiert. Falls dieser Graph einen Kreis
	 * haben sollte, so werden einige Knoten keine Knotenanordnungsnummer erhalten.
	 */
	public void berechneLineareKnotenanordnung() {
		// Neue Berechnung: Anordnungsnummern werde neu initialisiert
		initialisiereKnotenanordnungNummern();

		// Allen Knoten soll nun eine Anordnungsnummer zugewiesen werden:
		// - Anordnungsnummer soll von 1 beginnend vergeben werden
		// - Die Knoten werden allerdings von hinten nach vorne betrachtet
		berechneLineareKnotenanordnung(1, anzahlKnoten - 1);
	}

	/**
	 * Berechnet rekursiv die Anordnungsnummern von allen Knoten im Graphen.
	 * 
	 * @param anordnungsnummer Aktuelle Knotenanordnungsnummer, die dem nächsten
	 *                         gefundenen Knoten zugewiesen werden soll
	 * @param aktuellerKnoten  Aktuelle Knoten der untersucht werden soll
	 */
	private void berechneLineareKnotenanordnung(int anordnungsnummer, int aktuellerKnoten) {
		// Abbrechen, falls kein gültiger Knoten(index) übergeben wurde
		if (aktuellerKnoten < 0) {
			return;
		}

		// Falls der gegebene Knoten noch keine Anordnungsnummer bekommen hat UND der
		// aktuelle Knoten keinen Vorgänger hat, so erhält dieser Knoten die nächste
		// Anordnungsnummer
		if (knotenanordnungsNummern[aktuellerKnoten] < 0 && !hatKnotenVorgaenger(aktuellerKnoten)) {
			knotenanordnungsNummern[aktuellerKnoten] = anordnungsnummer;
			// Berechnung des nächsten Knoten beginnen mit inkrementierter
			// Knotenanordnungsnummer. (anzahlKnoten - 1: Erneut beim letzten Knoten
			// beginnen)
			berechneLineareKnotenanordnung(anordnungsnummer + 1, anzahlKnoten - 1);
		} else {
			// Diesem Knoten wurde schon eine Anordnungsnummer zugewiesen.
			// Der nächst niedrigere Knoten soll nun untersucht werden
			berechneLineareKnotenanordnung(anordnungsnummer, aktuellerKnoten - 1);
		}
	}

	/**
	 * Das Array {@link Graph#knotenanordnungsNummern} wird initialisiert (Die Größe
	 * des Arrays entspricht der Anzahl der vorhandenen Knoten) und mit dem Wert -1
	 * befüllt. Alle Werte des Arrays werden also auf den Wert -1 gesetzt.
	 */
	private void initialisiereKnotenanordnungNummern() {
		knotenanordnungsNummern = new int[anzahlKnoten];
		befuelleArray(knotenanordnungsNummern, -1);
	}

	/**
	 * Befüllt das gegebene Array mit dem gegebenen Wert. Alle Werte im Array werden
	 * auf den Wert val gesetzt.
	 * 
	 * @param array Array, das mit dem gegebenen Wert befüllt werden soll
	 * @param val   Wert zur Befüllung des Arrays
	 */
	private void befuelleArray(int[] array, int val) {
		for (int i = 0; i < array.length; i++) {
			array[i] = val;
		}
	}

	/**
	 * Prueft, ob in dem gegebenen Graphen ein Kreis existiert. Ein Kreis existiert,
	 * sobald einem Knoten eine Anordnungsnummer zugewiesen werden konnte.
	 * 
	 * @return true, falls ein Kreis existiert; andernfalls false
	 */
	private boolean hatGraphEinenKreis() {
		for (int n : knotenanordnungsNummern) {
			// Falls ein Wert im knotenanordnungNummern-Array noch den default-Wert -1
			// besitzt, dann besitzt dieser Graph einen Kreis
			if (n < 0) {
				return true;
			}
		}
		// Allen Knoten wurde eine Anordnungsnummer zugewiesen. Es existiert kein Kreis.
		return false;
	}

	/**
	 * Liefert einen boolean der angibt, ob der gegebene Knoten einen Vorgaenger
	 * besitzt. Falls also ein beliebiger Knoten im {@link Graph#graph} auf diesen
	 * Knoten(index) verweist, so hat der gegebene Knoten einen Vorgaenger.
	 * 
	 * @param knoten Knoten der geprueft werden soll, ob dieser einen Vorgeanger
	 *               besitzt
	 * @return true, falls der gegebene Knoten einen Vorgaenger besitzt; sonst false
	 */
	private boolean hatKnotenVorgaenger(int knoten) {

		for (int i = 0; i < anzahlKnoten; i++) {
			// Wenn dieser Knoten auf den gegebenen Knoten verweist und dieser Knoten noch
			// keine Anordnungsnummer besitzt, dann hat der gegebene Knoten einen
			// Vorgaenger; und zwar den Knoten an der Stelle i
			if (graph[i][knoten] == 1 && knotenanordnungsNummern[i] == -1) {
				return true;
			}
		}
		// es wurde kein Vorgaenger gefunden
		return false;
	}

	/**
	 * Gibt die berechneten KnotenanordnungNummern aus. Dies geschieht in folgender
	 * Form:<br>
	 * Knotenindex: .... 1 2 3 4 <br>
	 * --------------------------- (Trennstrich) <br>
	 * Anordnungsnummer: 1 3 2 4
	 */
	public void printLineareKnotenanordnungNummern() {

		// Prüfen, ob der Graph einen Kreis hat. Falls der Graph einen Kreis haben
		// sollte, so wird statt den KnotenanordnungNummern eine entsprechende Meldung
		// ausgegeben
		if (hatGraphEinenKreis()) {
			System.out.println("Der Graph hat einen Kreis!");
			return;
		}

		// KnotenanordnungNummern ausgeben
		printKnotenindex();
		printTrennstrich();
		printKnotenanordnung();

	}

	/**
	 * Die Knotenindizes werden der Reihe nach mit einem Tab-Abstand in die Konsole
	 * ausgegeben
	 */
	private void printKnotenindex() {
		StringBuilder knotenIndexBuilder = new StringBuilder("Knotenindex:\t\t");
		for (int i = 1; i <= anzahlKnoten; i++) {
			knotenIndexBuilder.append(i).append('\t');
		}
		System.out.println(knotenIndexBuilder.toString());
	}

	/**
	 * Ein Trennstrich wird in die Konsole ausgegeben
	 */
	private void printTrennstrich() {
		StringBuilder trennStrichBuilder = new StringBuilder();
		for (int i = 0; i < anzahlKnoten; i++) {
			trennStrichBuilder.append("------------");
		}
		System.out.println(trennStrichBuilder.toString());
	}

	/**
	 * Die Anordnungsnummern werden der Reihe nach mit einem Tab-Abstand in die
	 * Konsole ausgegeben
	 */
	private void printKnotenanordnung() {
		StringBuilder anordnungsnummerBuilder = new StringBuilder("Anordnungsnummer:\t");
		for (int k : knotenanordnungsNummern) {
			anordnungsnummerBuilder.append(k).append('\t');
		}
		System.out.println(anordnungsnummerBuilder.toString());
	}

}
