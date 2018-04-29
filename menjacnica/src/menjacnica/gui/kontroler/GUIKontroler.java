package menjacnica.gui.kontroler;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import menjacnica.Menjacnica;
import menjacnica.MenjacnicaInterface;
import menjacnica.Valuta;
import menjacnica.gui.DodajKursGUI;
import menjacnica.gui.IzvrsiZamenuGUI;
import menjacnica.gui.MenjacnicaGUI;
import menjacnica.gui.ObrisiKursGUI;
import menjacnica.gui.models.MenjacnicaTableModel;

public class GUIKontroler {
	public static MenjacnicaGUI menjacnica;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIKontroler.menjacnica = new MenjacnicaGUI();
					GUIKontroler.menjacnica.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void ugasiAplikaciju() {
		int opcija = JOptionPane.showConfirmDialog(menjacnica.contentPane,
				"Da li ZAISTA zelite da izadjete iz apliacije", "Izlazak", JOptionPane.YES_NO_OPTION);

		if (opcija == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	public static void sacuvajUFajl() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(menjacnica.contentPane);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				menjacnica.sistem.sacuvajUFajl(file.getAbsolutePath());
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnica.contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziAboutProzor() {
		JOptionPane.showMessageDialog(menjacnica.contentPane, "Autor: Bojan Tomic, Verzija 1.0",
				"O programu Menjacnica", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void prikaziSveValute() {
		MenjacnicaTableModel model = (MenjacnicaTableModel) (menjacnica.table.getModel());
		model.staviSveValuteUModel(menjacnica.sistem.vratiKursnuListu());

	}

	public static void ucitajIzFajla() {
		try {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(menjacnica.contentPane);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				menjacnica.sistem.ucitajIzFajla(file.getAbsolutePath());
				prikaziSveValute();
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnica.contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void unesiKurs(String naziv, String skraceniNaziv, int sifra, double prodajni, double kupovni,
			double srednji) {
		try {
			Valuta valuta = new Valuta();

			// Punjenje podataka o valuti
			valuta.setNaziv(naziv);
			valuta.setSkraceniNaziv(skraceniNaziv);
			valuta.setSifra(sifra);
			valuta.setProdajni(prodajni);
			valuta.setKupovni(kupovni);
			valuta.setSrednji(srednji);

			// Dodavanje valute u kursnu listu
			GUIKontroler.menjacnica.sistem.dodajValutu(valuta);

			// Osvezavanje glavnog prozora
			GUIKontroler.prikaziSveValute();

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnica.contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziDodajKursGUI() {
		DodajKursGUI prozor = new DodajKursGUI();
		prozor.setLocationRelativeTo(menjacnica.contentPane);
		prozor.setVisible(true);
	}

	public static void prikaziObrisiKursGUI() {

		if (menjacnica.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (menjacnica.table.getModel());
			ObrisiKursGUI prozor = new ObrisiKursGUI(model.vratiValutu(menjacnica.table.getSelectedRow()));
			prozor.setLocationRelativeTo(menjacnica.contentPane);
			prozor.setVisible(true);
		}
	}

	public static void obrisiValutu(Valuta valuta) {
		try {
			menjacnica.sistem.obrisiValutu(valuta);

			GUIKontroler.prikaziSveValute();

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnica.contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void prikaziIzvrsiZamenuGUI() {
		if (menjacnica.table.getSelectedRow() != -1) {
			MenjacnicaTableModel model = (MenjacnicaTableModel) (menjacnica.table.getModel());
			IzvrsiZamenuGUI prozor = new IzvrsiZamenuGUI(model.vratiValutu(menjacnica.table.getSelectedRow()));
			prozor.setLocationRelativeTo(menjacnica.contentPane);
			prozor.setVisible(true);
		}
	}

	public static double izvrsiZamenu(Valuta valuta, boolean prodaja, double iznos) {
		try {
			double konacniIznos = menjacnica.sistem.izvrsiTransakciju(valuta, prodaja, iznos);

			return konacniIznos;
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(menjacnica.contentPane, e1.getMessage(), "Greska", JOptionPane.ERROR_MESSAGE);

		}
		return -1;
	}

}
