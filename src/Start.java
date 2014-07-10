import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class Start extends JPanel implements PropertyChangeListener, ActionListener {

	/**
	 * All rights reserved! Email: mar00m@seznam.cz Actually, my name is mar21, but friends call me Hafis. So, a man with name Hafis_CZ could be me.
	 * ;) Thanks.
	 */

	private static final long serialVersionUID = 1L;

	public static JTable table;

	public static JTextField in1;
	public static JTextField in2;

	public static JRadioButton m1;
	public static JRadioButton m2;

	public static ButtonGroup group;
	public static JButton b1;
	public static JButton b2;

	public static String[] data;
	public static String datas;

	public static JButton createBackup;
	public static JButton loadBackup;
	public static JButton saveData;

	public Start() {

		this.setFocusable(false);
		this.add(setupP1(), BorderLayout.CENTER);
	}

	public JPanel setupP1() { // TODO
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

		b1 = new JButton("...");
		b2 = new JButton("...");

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

		m1 = new JRadioButton();
		m1.setActionCommand("1");
		m1.setText("Yes");
		m1.setFont(getFont().deriveFont(16.0f));
		m2 = new JRadioButton();
		m2.setActionCommand("2");
		m2.setText("No");
		m2.setFont(getFont().deriveFont(16.0f));

		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose save folder");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						in2.setText((chooser.getSelectedFile()).toString());
					} else {
					}
				} catch (Exception j) {
				}
			}
		});

		group = new ButtonGroup();
		group.add(m1);
		group.add(m2);

		JPanel subp = new JPanel();
		subp.setLayout(new FlowLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 15;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 5;

		JLabel choose = new JLabel("AutoBackup :");
		choose.setFont(getFont().deriveFont(17.0f));
		subp.add(choose);

		subp.add(m1);
		subp.add(m2);

		saveData = new JButton("Set");
		saveData.setFont(getFont().deriveFont(15.0f));
		saveData.setForeground(Color.BLACK);

		loadBackup = new JButton("Load");
		loadBackup.setFont(getFont().deriveFont(15.0f));
		loadBackup.setForeground(Color.red);

		createBackup = new JButton("Backup");
		createBackup.setFont(getFont().deriveFont(15.0f));
		createBackup.setForeground(new Color(0, 200, 10));

		loadBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose backup file");
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					chooser.setFileFilter(new FileNameExtensionFilter(".zip Files", "zip", ".zip Files"));
					chooser.setAcceptAllFileFilterUsed(false);
					if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						if (JOptionPane.showConfirmDialog(null, "Do you really want to retrieve files from backup ?"
								+ "\nThis will remove all current files in specified path"
								+ "\nand place there all files from :\n"+chooser.getSelectedFile().getName().toString(), "Warning", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
							delete(new File(in1.getText()));
							ZipFile zipFile = new ZipFile((chooser.getSelectedFile()).toString());
							zipFile.extractAll(in1.getText().substring(0, in1.getText().lastIndexOf("\\")));
						}
					} else {
					}
				} catch (Exception j) {
				}
			}
		});

		createBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Calendar cal = Calendar.getInstance();
					long timestamp = cal.getTimeInMillis();
					ZipFile zipFile = new ZipFile(in2.getText() + "\\backup" + "_" + timestamp + ".zip");
					ZipParameters parameters = new ZipParameters();
					parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
					parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
					zipFile.createZipFileFromFolder(in1.getText(), parameters, true, 10485760);
				} catch (Exception j) {
				}
			}
		});

		saveData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BufferedWriter w = new BufferedWriter(new FileWriter("data.nfo"));
					w.write(in1.getText() + "|" + in2.getText());
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
			String datas = new String(Files.readAllBytes(Paths.get("data.nfo")));
			data = datas.split("\\|");
			in1.setText(data[0].toString());
			in2.setText(data[1].toString());
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
}