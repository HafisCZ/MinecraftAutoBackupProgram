import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPService {

	FTPClient ftp = null;
	boolean debug_enabled = false;

	public FTPService(String server) throws Exception {
		ftp = new FTPClient();
		if (debug_enabled) ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		ftp.connect(server);
		int id = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(id)) {
			ftp.disconnect();
			throw new Exception("FTPService cannot connect to specified FTP Server");
		}
	}

	public FTPService(String server, int port) throws Exception {
		ftp = new FTPClient();
		if (debug_enabled) ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		ftp.connect(server, port);
		int id = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(id)) {
			ftp.disconnect();
			throw new Exception("FTPService cannot connect to specified FTP Server");
		}
	}

	public void DebugMode(boolean enabled) {
		debug_enabled = enabled;
	}

	public void Authorize(String USERNAME, String PASSWORD) throws Exception {
		ftp.login(USERNAME, PASSWORD);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
	}

	public boolean Download(String local_path, String server_path) throws Exception {
		OutputStream dataflow = new BufferedOutputStream(new FileOutputStream(new File(local_path)));
		boolean return_id = ftp.retrieveFile(server_path, dataflow);
		dataflow.close();
		return return_id;
	}

	public boolean Upload(String local_path, String server_name, String server_folder) throws Exception {
		try {
			InputStream dataflow = new FileInputStream(new File(local_path));
			ftp.storeFile(server_folder + server_name, dataflow);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void FTP_close() {
		if (ftp.isConnected()) {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException f) {

			}
		}
	}
}
