import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Start extends JPanel implements PropertyChangeListener, ActionListener {

	/**
	 * All rights reserved! Email: mar00m@seznam.cz Actually, my name is mar21, but friends call me Hafis. So, a man with name Hafis_CZ could be me.
	 * ;) Thanks.
	 */

	private static final long serialVersionUID = 1L;

	public static JFrame frame2;

	public static JTextField in1;
	public static JTextField in2;
	public static JTextField in3;
	public static JTextField in4;

	public static boolean time_enabled = false;
	public static boolean cloud_enabled = false;

	public static String filename;

	public static String folder_backup = "";
	public static String folder_save = "";
	public static String folder_game = "";
	public static String timebackup_split = "";

	public static String[] cols = { "Sync", "Name", "Date", "Size" };
	public static JButton browse;
	public static JButton use;
	public static JButton remove;
	public static String backupPath;
	public static JTable table;

	public static JCheckBox m1;

	public static JButton b1;
	public static JButton b2;
	public static JButton b3;

	public static String[] data;
	public static String datas;

	public static JButton createBackup;
	public static JButton loadBackup;
	public static JButton saveData;
	public static JButton playGame;
	public static JButton renameBackup;

	public static JButton uploadCloud; // Upload to cloud
	public static JButton removeCloud; // Remove from cloud
	public static JButton downloadCloud; // Download from cloud
	public static JButton refreshCloud; // Refresh list of files from cloud

	public Start() {
		this.setFocusable(false);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		// Load saved settings
		try {
			if (new File("data.nfo").isFile()) {
				String datas = new String(Files.readAllBytes(Paths.get("data.nfo")));
				data = datas.split("\\|");
				if (data.length >= 1) folder_save = data[0].toString();
				if (data.length >= 2) folder_backup = data[1].toString();
				if (data.length >= 3) folder_game = data[2].toString();
				if (data.length >= 4) timebackup_split = data[3].toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JPanel subPanel = new JPanel(new FlowLayout());
		{
			// Play button will launch the Game if path to .exe file is specified
			playGame = new JButton("Play");
			playGame.setFont(getFont().deriveFont(15.0f));
			playGame.setForeground(Color.BLACK);
			playGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (new File(folder_game).isFile()) {
							new ProcessBuilder(folder_game).start();
						}
					} catch (Exception f) {
						f.printStackTrace();
					}
				}
			});
			subPanel.add(playGame);
			// Create button creates a new backup if path to save file is specified
			createBackup = new JButton("Create Backup");
			createBackup.setFont(getFont().deriveFont(15.0f));
			createBackup.setForeground(new Color(0, 200, 10));
			createBackup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Calendar cal = Calendar.getInstance();
						DateFormat dateFormat = new SimpleDateFormat("yy_MM_dd");
						filename = folder_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + ".zip";
						if (new File(folder_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + ".zip").isFile()) {
							for (int i = 1;; i++) {
								if (new File(folder_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + "_" + i + ".zip").isFile()) {
									continue;
								}
								filename = folder_backup + "\\backup" + "_" + dateFormat.format(cal.getTime()) + "_" + i + ".zip";
								break;
							}
						}
						ZipFile zipFile = new ZipFile(filename);
						ZipParameters parameters = new ZipParameters();
						parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
						parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
						zipFile.createZipFileFromFolder(folder_save, parameters, false, 10485760);
						updateTable();
					} catch (Exception j) {
					} finally {
						JOptionPane.showMessageDialog(null, "Backup file created :\n" + filename, "Backup completed", JOptionPane.PLAIN_MESSAGE);
					}
				}
			});
			subPanel.add(createBackup);
		}
		this.add(subPanel);
		// Creates a Tabbed pane
		JTabbedPane mainWindow = new JTabbedPane();
		mainWindow.addTab("Backup Manager", setupP1());
		mainWindow.setMnemonicAt(0, KeyEvent.VK_NUMPAD1);
		mainWindow.addTab("Settings", setupP2());
		mainWindow.setMnemonicAt(1, KeyEvent.VK_NUMPAD2);
		mainWindow.addTab("Cloud Connection", setupP3());
		mainWindow.setMnemonicAt(2, KeyEvent.VK_NUMPAD3);
		this.add(mainWindow);
	}

	public static void updateTable() {
		DefaultTableModel dm = (DefaultTableModel) table.getModel();
		dm.getDataVector().removeAllElements();
		dm.fireTableDataChanged();
		table.setModel(new DefaultTableModel(getDatabase(folder_backup), cols));
		table.getColumnModel().getColumn(0).setPreferredWidth(1);
	}

	public JPanel setupP3() { // TODO PANE_CLOUD
		JPanel pane = new JPanel();
		JLabel l = new JLabel("Work in Progress - Will be possibly available in future version");
		pane.add(l);
		return pane;
	}

	public JPanel setupP2() { // TODO PANE_SETTINGS
		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

		JPanel pathes = new JPanel();
		pathes.setLayout(new FlowLayout());
		pathes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Paths"));
		{
			JPanel sub1 = new JPanel();
			sub1.setLayout(new BoxLayout(sub1, BoxLayout.PAGE_AXIS));
			{
				JLabel lb1 = new JLabel("Save Folder:");
				lb1.setFont(getFont().deriveFont(17.0f));
				sub1.add(lb1);
				JLabel lb2 = new JLabel("Backup Folder:");
				lb2.setFont(getFont().deriveFont(17.0f));
				sub1.add(lb2);
				JLabel lb3 = new JLabel("Game:");
				lb3.setFont(getFont().deriveFont(17.0f));
				sub1.add(lb3);
			}
			pathes.add(sub1);

			JPanel sub2 = new JPanel();
			sub2.setLayout(new BoxLayout(sub2, BoxLayout.PAGE_AXIS));
			{
				in1 = new JTextField();
				in1.setText(folder_save);
				in1.setColumns(30);
				in1.setFont(getFont().deriveFont(14.0f));
				sub2.add(in1);
				in2 = new JTextField();
				in2.setText(folder_backup);
				in2.setColumns(30);
				in2.setFont(getFont().deriveFont(14.0f));
				sub2.add(in2);
				in3 = new JTextField();
				in3.setText(folder_game);
				in3.setColumns(30);
				in3.setFont(getFont().deriveFont(14.0f));
				sub2.add(in3);
			}
			pathes.add(sub2);

			JPanel sub3 = new JPanel();
			sub3.setLayout(new BoxLayout(sub3, BoxLayout.PAGE_AXIS));
			{
				b1 = new JButton("...");

				sub3.add(b1);

				b2 = new JButton("...");

				sub3.add(b2);

				b3 = new JButton("...");

				sub3.add(b3);
			}
			pathes.add(sub3);

		}
		pane.add(pathes);

		pane.add(new JLabel(""));

		JPanel sub4 = new JPanel();
		sub4.setLayout(new FlowLayout());
		sub4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Auto-Backup"));
		{
			m1 = new JCheckBox();
			m1.setActionCommand("1");
			m1.setText("Enabled");
			m1.setFont(getFont().deriveFont(17.0f));
			m1.setEnabled(time_enabled);
			sub4.add(m1);
			JLabel l1 = new JLabel("Split: ");
			l1.setFont(getFont().deriveFont(17.0f));
			sub4.add(l1);
			in4 = new JTextField();
			in4.setText(timebackup_split);
			in4.setColumns(10);
			in4.setFont(getFont().deriveFont(14.0f));
			in4.setEnabled(time_enabled);
			sub4.add(in4);
		}
		pane.add(sub4);

		in1.setText(folder_save);
		in2.setText(folder_backup);
		in3.setText(folder_game);
		in4.setText(timebackup_split);

		in1.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				update();
			}

			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				update();
			}
		});

		in2.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				update();
			}

			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				update();
			}
		});

		in3.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				update();
			}

			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				update();
			}
		});

		in4.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				update();
			}

			public void removeUpdate(DocumentEvent e) {
				update();
			}

			public void insertUpdate(DocumentEvent e) {
				update();
			}
		});

		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose save folder");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setFileHidingEnabled(false);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						folder_save = (chooser.getSelectedFile()).toString();
						in1.setText(folder_save);
					} else {
					}

				} catch (Exception j) {
				}
			}
		});

		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose folder for backups");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setFileHidingEnabled(false);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						folder_backup = (chooser.getSelectedFile()).toString();
						in2.setText(folder_backup);
					} else {
					}
				} catch (Exception j) {
				}
			}
		});

		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose Minecraft launcher");
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setFileHidingEnabled(false);
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						folder_game = (chooser.getSelectedFile()).toString();
						in3.setText(folder_game);
					} else {
					}
				} catch (Exception j) {
				}
			}
		});

		saveData = new JButton("Save settings");
		saveData.setFont(getFont().deriveFont(15.0f));
		saveData.setForeground(Color.BLACK);
		saveData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					System.out.println("Written");
					BufferedWriter w = new BufferedWriter(new FileWriter("data.nfo"));
					w.write(folder_save + "|" + folder_backup + "|" + folder_game + "|" + timebackup_split);
					w.close();
				} catch (Exception j) {
					j.printStackTrace();
				}
			}
		});
		pane.add(saveData);

		return pane;
	}

	public JPanel setupP1() { // TODO PANE_BACKUPS
		JPanel pane = new JPanel();
		try {
			pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
			JPanel listing = new JPanel();
			pane.add(listing);
			table = new JTable();
			table.setModel(new DefaultTableModel(getDatabase(folder_backup), cols));
			table.setPreferredScrollableViewportSize(new Dimension(570, 150));
			table.setFillsViewportHeight(true);
			table.getColumnModel().getColumn(0).setPreferredWidth(1);
			JScrollPane slr = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			listing.add(slr);
			JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttons.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Local"));
			pane.add(buttons);
			browse = new JButton("...");
			buttons.add(browse);
			use = new JButton("Load");
			buttons.add(use);
			renameBackup = new JButton("Rename");
			buttons.add(renameBackup);
			remove = new JButton("Remove");
			buttons.add(remove);
			JPanel buttons2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttons2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Cloud"));
			pane.add(buttons2);
			refreshCloud = new JButton("Refresh");
			refreshCloud.setEnabled(cloud_enabled);
			buttons2.add(refreshCloud);
			uploadCloud = new JButton("Upload");
			uploadCloud.setEnabled(cloud_enabled);
			buttons2.add(uploadCloud);
			removeCloud = new JButton("Remove");
			removeCloud.setEnabled(cloud_enabled);
			buttons2.add(removeCloud);
			downloadCloud = new JButton("Download");
			downloadCloud.setEnabled(cloud_enabled);
			buttons2.add(downloadCloud);
			browse.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						JFileChooser chooser = new JFileChooser();
						chooser.setCurrentDirectory(new java.io.File("."));
						chooser.setDialogTitle("Choose folder with backups");
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						chooser.setFileHidingEnabled(false);
						chooser.setAcceptAllFileFilterUsed(false);
						if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
							backupPath = (chooser.getSelectedFile()).toString();
							updateTable();
						}
					} catch (Exception f) {
						f.printStackTrace();
					}
				}
			});
			// Removes selected backup file
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
						String deletepath = folder_backup + "\\" + selected;
						delete(new File(deletepath));
						updateTable();
					} catch (Exception j) {
						j.printStackTrace();
					}
				}
			});
			// Loads selected backup file
			use.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
						String path = folder_backup + "\\" + selected;
						if (JOptionPane.showConfirmDialog(null, "Do you really want to retrieve files from backup ?"
								+ "\nThis will remove all current files in specified path" + "\nand place there all files from :\n" + path,
								"Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
							delete(new File(folder_save));
							ZipFile zipFile = new ZipFile(path);
							zipFile.extractAll(folder_save.substring(0, folder_save.lastIndexOf("\\")));
							JOptionPane.showMessageDialog(null, "Backup file loaded :\n" + path, "Load completed", JOptionPane.PLAIN_MESSAGE);
						}
					} catch (Exception j) {
						j.printStackTrace();
					}
				}
			});
			// Changes name of selected file
			renameBackup.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Object selected = table.getModel().getValueAt(table.getSelectedRow(), 1);
						String path = folder_backup + "\\" + selected;
						String newName = JOptionPane.showInputDialog(null, "Enter new name for file: ", "Rename file: " + selected,
								JOptionPane.PLAIN_MESSAGE);
						if (newName != "") {
							File sel = new File(path);
							sel.renameTo(new File(folder_backup + "\\" + newName));
						}
						updateTable();
					} catch (Exception j) {
						j.printStackTrace();
					}
				}
			});
		} catch (Exception j) {
			j.printStackTrace();
		}
		return pane;
	}

	public static void update() {
		folder_save = in1.getText();
		folder_backup = in2.getText();
		folder_game = in3.getText();
		timebackup_split = in4.getText();
		updateTable();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupGui();
			}
		});
	}

	public static Object[][] getDatabase(String location) {
		DateFormat dateFormat = new SimpleDateFormat("dd MM yy HH:mm:ss");
		if (!new File(location).exists()) return new Object[][] { { "", "-", "", "" } };
		File[] files = listFiles(location);
		String[] dates = new String[files.length];
		long[] size = new long[files.length];
		String[] names = nameFiles(location);
		for (int i = 0; i < files.length; i++) {
			dates[i] = dateFormat.format(files[i].lastModified());
			size[i] = files[i].length();
		}
		Object[][] mix = new Object[files.length][4];
		for (int i = 0; i < files.length; i++) {
			mix[i][0] = "Local File";
			mix[i][1] = names[i];
			mix[i][2] = dates[i];
			mix[i][3] = ((size[i] / 1024) < 1) ? size[i] + " B" : (((size[i] / 1048576) < 1) ? size[i] / 1024 + ","
					+ (int) Math.ceil(((size[i] % 1024) * 1000) / 1000) + " kB" : size[i] / 1048576 + ","
					+ (int) Math.ceil(((size[i] / 1024) * 1000) / 1000) + " MB");
		}
		return mix;
	}

	public static void setupGui() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		JFrame Frame = new JFrame("Minecraft Backup Utility | CC-NY-NC-ND by mar21");
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.setResizable(false);
		Start Content = new Start();
		Content.setOpaque(true);
		Frame.setContentPane(Content);
		Frame.pack();
		Frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

	}

	public static boolean delete(File file) {
		File[] flist = null;
		if (file == null) { return false; }
		if (file.isFile()) { return file.delete(); }
		if (!file.isDirectory()) { return false; }
		flist = file.listFiles();
		if (flist != null && flist.length > 0) {
			for (File f : flist) {
				if (!delete(f)) { return false; }
			}
		}
		return file.delete();
	}

	public static String[] nameFiles(String path) {
		return new File(path).isDirectory() ? new File(path).list() : null;
	}

	public static File[] listFiles(String path) {
		File folder = new File(path);
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			return files;
		}
		return null;
	}

}