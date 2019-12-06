import java.util.ArrayList;
import java.util.List;

import TextInputLib.TextStandardInputStream;

/**
 * 
 * @author Chris Wischeropp (u34466), Oliver Lindemann (u33873)
 */

public class Graph extends Environment {

	/** Eingelesene Knoten im eingelesenen Graphen */
	private int anzahlKnoten;
	/** Eingelesener Graph als Matrix in int[][] abgespeichert */
	private int[][] adjazenzmatrix;

	/**
	 * Liest einen Graphen �ber den {@link TextStandardInputStream} ein und
	 * speichert diesen in der Klassenvariable {@link Graph#adjazenzmatrix} ab.
	 */
	public void readGraph() {
		// In der ersten Zeile ist die Anzahl der Knoten im Graphen angegeben
		anzahlKnoten = stdin.readInt();
		adjazenzmatrix = new int[anzahlKnoten][anzahlKnoten];

		// Alle Knoten des Graphens einlesen und in graph-Variable abspeichern
		for (int i = 0; i < anzahlKnoten; i++) {
			for (int j = 0; j < anzahlKnoten; j++) {
				adjazenzmatrix[i][j] = stdin.readInt();
			}
		}

		System.out.println("Graph: ");
		printArray(adjazenzmatrix);
	}

	/**
	 * Kopiert das gegebene Array und liefert die Kopie zur�ck
	 * 
	 * @param arrayToCopy Array, das kopiert werden soll
	 * @return kopiertes Array
	 */
	private int[][] copyArray(int[][] arrayToCopy) {
		int[][] newArray = new int[arrayToCopy.length][arrayToCopy.length];
		for (int i = 0; i < arrayToCopy.length; i++) {
			for (int j = 0; j < arrayToCopy[i].length; j++) {
				newArray[i][j] = arrayToCopy[i][j];
			}
		}
		return newArray;
	}

	public String rang() {
		// Wegematrix der eingelesenen Adjazenzmatrix erstellen
		int[][] wegematrix = bestimmeWegematrix(adjazenzmatrix);

		// Berechne den minimalen Rang
		return Integer.toString(rangGraph(wegematrix));

	}

	private int[][] bestimmeWegematrix(int[][] adj) {
		int[][] wegematrix = copyArray(adj);

		// alle Wege ohne Zwischenknoten sind erfasst
		for (int j = 0; j < wegematrix.length; j++) {
			// alle Wege, deren Zwischenknoten hoechstens den Index
			// j-1 haben, sind erfasst
			for (int i = 0; i < wegematrix.length; i++) {
				if (wegematrix[i][j] == 1) {
					// Die Zwischenknoten des Wegs von vi nach vj haben
					// hoechstens den Index j-1
					for (int k = 0; k < wegematrix.length; k++) {
						if (wegematrix[j][k] == 1) {
							// Die Zwischenknoten des Wegs von vj nach vk haben
							// hoechstens den Index j-1
							wegematrix[i][k] = 1;
							// Die Zwischenknoten des Wegs von vi nach vk haben
							// hoechstens den Index j
						}
					}
				}
			} // alle Wege, deren Zwischenknoten hoechstens den Index j haben, sind erfasst
		}
		return wegematrix;
	}

	private int rangGraph(int[][] wegematrix) {
		// gibt die maximale Anzahl an Schl�gen an,
		// die f�r den gegebenen Graphen ben�tigt wurden
		int maxSektionsSchlaege = 0;

		// Liste mit bereits gepr�ften Listenknoten
		List<Integer> gepruefteKnoten = new ArrayList<>();
		for (int wurzel = 0; wurzel < wegematrix.length; wurzel++) {
			if (wegematrix[wurzel][wurzel] == 1 && !gepruefteKnoten.contains(wurzel)) { // Diagonale
				// Dieser Knoten ist eine Wurzel
				gepruefteKnoten.add(wurzel);

				// Zur Wurzel zugeh�rige Knoten finden
				List<Integer> sektion = findeZugehoerigeKnoten(wegematrix, wurzel);
				gepruefteKnoten.addAll(sektion); // und hinzuf�gen (damit diese nicht erneut gepr�ft weden)

				// minimalen Rang f�r diese Sektion ermitteln
				int minRangFuerSektion = rangSektion(sektion);

				// Maximum aus dem aktuellen Maximum der Schl�ge und dem Minimum der Sektion
				// ermitteln
				maxSektionsSchlaege = Math.max(maxSektionsSchlaege, minRangFuerSektion);
			}
		}

		return maxSektionsSchlaege;
	}

	private int rangSektion(List<Integer> sektion) {
		// mit hohem Wert f�r min-Funktion initialisieren
		int minRang = Integer.MAX_VALUE;

		// Kopie der Adjazenzmatrix erstellen
		int[][] adjKopie = copyArray(adjazenzmatrix);

		// Alle Knoten, die nicht in der gegebenen Sektion vorhanden sind l�schen
		for (int i = 0; i < adjKopie.length; i++) {
			if (!sektion.contains(i)) {
				loescheKnoten(adjKopie, i);
			}
		}

		// Jeden Knoten aus der gegebenen Sektion einmal aus der Matrix l�schen und
		// Minimum suchen
		for (int zugehoerigerKnoten : sektion) {
			// Kopie der vorbereiteten Matrix, damit jeder Knoten einmal gel�scht werden
			// kann
			int[][] tempAdj = copyArray(adjKopie);

			// Knoten aus der Kopie l�schen
			loescheKnoten(tempAdj, zugehoerigerKnoten);

			// Wegematrix nach L�schung des Knoten bestimmen
			int[][] wegematrixNachLoeschung = bestimmeWegematrix(tempAdj);

			// Durch neu erzeugten Graphen laufen und Sektionen suchen
			int rangGraph = rangGraph(wegematrixNachLoeschung);

			// Bestimmen des Minimums aus dem aktuellen Minimum und dem Minimum des
			// rangGraph
			// +1, da ein Knoten gel�scht wurde (+ 1 Schlag)
			minRang = Math.min(minRang, rangGraph + 1);
		}

		return minRang;
	}

	private List<Integer> findeZugehoerigeKnoten(int[][] wegematrix, int wurzel) {
		// Liste mit zugeh�rigen Knoten zu der gegebenen Wurzel
		List<Integer> zugehoerigeKnoten = new ArrayList<>();

		// Knoten zu der gegebenen Wurzel suchen
		for (int i = 0; i < wegematrix.length; i++) {
			// Falls zwischen Knoten an der Stelle i und der gegebenen Wurzel ein starker
			// Zusammenhang besteht, diesen Knoten zur Liste hinzuf�gen
			if (wegematrix[i][wurzel] == 1 && wegematrix[wurzel][i] == 1) {
				zugehoerigeKnoten.add(i);
			}
		}

		return zugehoerigeKnoten;
	}

	private void loescheKnoten(int[][] matrix, int knoten) {
		for (int i = 0; i < matrix.length; i++) {
			matrix[knoten][i] = 0; // Reihe des Knoten loschen
			matrix[i][knoten] = 0; // Verweis auf den Knoten loeschen
		}
	}

	private void printArray(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				System.out.print(array[i][j] + " ");
			}
			System.out.println();
		}
	}

}
