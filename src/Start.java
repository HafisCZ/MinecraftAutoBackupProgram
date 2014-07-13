import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
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

	public static String filename;

	public static String[] cols = { "Backup", "Date", "Size" };// TODO
	public static Object[][] previousData;
	public static JButton browse;
	public static JButton use;
	public static JButton remove;
	public static JButton close;
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

	public Start() {

		this.setFocusable(false);
		this.add(setupP1(), BorderLayout.CENTER);
	}

	public JPanel setupP1() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		in1 = new JTextField();
		in1.setText(null);
		in1.setColumns(20);
		JLabel lb1 = new JLabel("Save Folder:");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		lb1.setFont(getFont().deriveFont(17.0f));
		pane.add(lb1, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.weightx = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		in1.setFont(getFont().deriveFont(14.0f));
		pane.add(in1, c);

		in2 = new JTextField();
		in2.setText(null);
		in2.setColumns(20);
		JLabel lb2 = new JLabel("Backup Folder:");
		c.fill = GridBagConstraints.HORIZONTAL;
		lb2.setFont(getFont().deriveFont(17.0f));
		c.ipady = 10;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		in2.setFont(getFont().deriveFont(14.0f));
		pane.add(lb2, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridy = 1;
		c.gridwidth = 3;
		pane.add(in2, c);

		in3 = new JTextField();
		in3.setText(null);
		in3.setColumns(20);
		JLabel lb3 = new JLabel("Minecraft:");
		c.fill = GridBagConstraints.HORIZONTAL;
		lb3.setFont(getFont().deriveFont(17.0f));
		c.ipady = 10;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		in3.setFont(getFont().deriveFont(14.0f));
		pane.add(lb3, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridy = 2;
		c.gridwidth = 3;
		pane.add(in3, c);

		b1 = new JButton("...");
		b2 = new JButton("...");
		b3 = new JButton("...");

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 15;
		c.gridx = 4;
		c.gridy = 0;
		c.gridwidth = 1;
		b1.setFont(getFont().deriveFont(10.0f));
		pane.add(b1, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 15;
		c.gridx = 4;
		c.gridy = 1;
		c.gridwidth = 1;
		b1.setFont(getFont().deriveFont(10.0f));
		pane.add(b2, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 15;
		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 1;
		b1.setFont(getFont().deriveFont(10.0f));
		pane.add(b3, c);

		m1 = new JCheckBox();
		m1.setActionCommand("1");
		m1.setText("AutoBackup");
		m1.setFont(getFont().deriveFont(14.0f));
		m1.setEnabled(false);

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
						in1.setText((chooser.getSelectedFile()).toString());
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
						in2.setText((chooser.getSelectedFile()).toString());
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
						in3.setText((chooser.getSelectedFile()).toString());
					} else {
					}
				} catch (Exception j) {
				}
			}
		});

		JPanel subp = new JPanel();
		subp.setLayout(new FlowLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 15;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 5;
		subp.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		saveData = new JButton("Set");
		saveData.setFont(getFont().deriveFont(15.0f));
		saveData.setForeground(Color.BLACK);

		loadBackup = new JButton("Backlist");
		loadBackup.setFont(getFont().deriveFont(15.0f));
		loadBackup.setForeground(Color.red);

		createBackup = new JButton("Backup");
		createBackup.setFont(getFont().deriveFont(15.0f));
		createBackup.setForeground(new Color(0, 200, 10));

		playGame = new JButton("Play");
		playGame.setFont(getFont().deriveFont(15.0f));
		playGame.setForeground(Color.BLACK);

		subp.add(playGame);
		subp.add(m1);

		playGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new ProcessBuilder(in3.getText()).start();
				} catch (Exception f) {
					f.printStackTrace();
				}
			}
		});

		loadBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// TODO
				try {
					frame2 = new JFrame("Backlist");
					frame2.setVisible(true);
					frame2.setSize(600, 300);
					frame2.setResizable(false);
					frame2.setLayout(new BorderLayout());
					JPanel listing = new JPanel();
					frame2.add(listing, BorderLayout.CENTER);
					table = new JTable();
					table.setModel(new DefaultTableModel(getDatabase(in2.getText()), cols));
					table.setPreferredScrollableViewportSize(new Dimension(570, 200));
					table.setFillsViewportHeight(true);
					JScrollPane slr = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					listing.add(slr);
					JPanel buttons = new JPanel(new FlowLayout());
					previousData = getDatabase(in2.getText());
					frame2.add(buttons, BorderLayout.PAGE_END);
					browse = new JButton("...");
					buttons.add(browse);
					use = new JButton("Load");
					buttons.add(use);
					remove = new JButton("Remove");
					buttons.add(remove);
					close = new JButton("Close");
					buttons.add(close);
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
									DefaultTableModel dm = (DefaultTableModel) table.getModel();
									dm.getDataVector().removeAllElements();
									dm.fireTableDataChanged();
									table.setModel(new DefaultTableModel(getDatabase(backupPath), cols));
								}
							} catch (Exception f) {
								f.printStackTrace();
							}
						}
					});

					remove.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								Object selected = table.getModel().getValueAt(table.getSelectedRow(), 0);
								String deletepath = in2.getText() + "\\" + selected;
								delete(new File(deletepath));
								DefaultTableModel dm = (DefaultTableModel) table.getModel();
								dm.getDataVector().removeAllElements();
								dm.fireTableDataChanged();
								table.setModel(new DefaultTableModel(getDatabase(in2.getText()), cols));
							} catch (Exception j) {
								j.printStackTrace();
							}
						}
					});

					use.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								Object selected = table.getModel().getValueAt(table.getSelectedRow(), 0);
								String path = in2.getText() + "\\" + selected;
								if (JOptionPane.showConfirmDialog(null, "Do you really want to retrieve files from backup ?"
										+ "\nThis will remove all current files in specified path" + "\nand place there all files from :\n" + path,
										"Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
									delete(new File(in1.getText()));
									ZipFile zipFile = new ZipFile(path);
									zipFile.extractAll(in1.getText().substring(0, in1.getText().lastIndexOf("\\")));
								}
							} catch (Exception j) {
								j.printStackTrace();
							}
						}
					});

					close.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								frame2.dispatchEvent(new WindowEvent(frame2, WindowEvent.WINDOW_CLOSING));
							} catch (Exception j) {
								j.printStackTrace();
							}
						}
					});

				} catch (Exception j) {
					j.printStackTrace();
				}
			}
		});

		createBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Calendar cal = Calendar.getInstance();
					DateFormat dateFormat = new SimpleDateFormat("yy_MM_dd");
					filename = in2.getText() + "\\backup" + "_" + dateFormat.format(cal.getTime()) + ".zip";
					if (new File(in2.getText() + "\\backup" + "_" + dateFormat.format(cal.getTime()) + ".zip").isFile()) {
						for (int i = 1;; i++) {
							if (new File(in2.getText() + "\\backup" + "_" + dateFormat.format(cal.getTime()) + "_" + i + ".zip").isFile()) {
								continue;
							}
							filename = in2.getText() + "\\backup" + "_" + dateFormat.format(cal.getTime()) + "_" + i + ".zip";
							break;
						}
					}
					ZipFile zipFile = new ZipFile(filename);
					ZipParameters parameters = new ZipParameters();
					parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
					parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
					zipFile.createZipFileFromFolder(in1.getText(), parameters, false, 10485760);
				} catch (Exception j) {
				} finally {
					JOptionPane.showMessageDialog(null, "Backup file created :\n" + filename, "Backup completed", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});

		saveData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BufferedWriter w = new BufferedWriter(new FileWriter("data.nfo"));
					w.write(in1.getText() + "|" + in2.getText() + "|" + in3.getText());
					w.close();
				} catch (Exception j) {
					j.printStackTrace();
				}
			}
		});

		subp.add(loadBackup);
		subp.add(createBackup);
		subp.add(saveData);

		pane.add(subp, c);

		try {
			if (new File("data.nfo").isFile()) {
				String datas = new String(Files.readAllBytes(Paths.get("data.nfo")));
				data = datas.split("\\|");
				if (data.length == 3) {
					in1.setText(data[0].toString());
					in2.setText(data[1].toString());
					in3.setText(data[2].toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pane;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setupGui();
			}
		});
	}

	public Object[][] getDatabase(String location) {
		DateFormat dateFormat = new SimpleDateFormat("dd MM yy HH:mm:ss");
		File[] files = listFiles(location);
		String[] dates = new String[files.length];
		long[] size = new long[files.length];
		String[] names = nameFiles(location);
		for (int i = 0; i < files.length; i++) {
			dates[i] = dateFormat.format(files[i].lastModified());
			size[i] = files[i].length();
		}
		Object[][] mix = new Object[files.length][3];
		for (int i = 0; i < files.length; i++) {
			mix[i][0] = names[i];
			mix[i][1] = dates[i];
			mix[i][2] = ((size[i] / 1024) < 1) ? size[i] + " B" : (((size[i] / 1048576) < 1) ? size[i] / 1024 + " kB" : size[i] / 1048576 + " MB");
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