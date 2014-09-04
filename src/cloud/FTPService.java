package cloud;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.File;

public class FTPService {

	FTPClient ftp = new FTPClient();

	public FTPService(String server) throws Exception {
		ftp.connect(server);
		if (!ftp.isConnected())
			throw new Exception("[FTPService] Connection to server '" + server
					+ "' failed");
	}

	public FTPService(String server, Integer port) throws Exception {
		ftp.connect(server, port);
		if (!ftp.isConnected())
			throw new Exception("[FTPService] Connection to server '" + server
					+ "' via port " + port + " failed");
	}

	public FTPService authorize(String USERNAME, String PASSWORD)
			throws Exception {
		ftp.login(USERNAME, PASSWORD);
		if (!ftp.isAuthenticated())
			throw new Exception("[FTPService] Authentication failed");
		return this;
	}

	public FTPService def() {
		ftp.setPassive(true);
		ftp.setType(FTPClient.TYPE_BINARY);
		return this;
	}

	public String currentDirectory() throws Exception {
		return ftp.currentDirectory();
	}

	public FTPFile[] getFiles(String server_directory) throws Exception {
		return ftp.list(server_directory);
	}

	public void download(String local_fullpath, String server_fullpath)
			throws Exception {
		File temp = new File(local_fullpath);
		ftp.download(server_fullpath, temp);
		if (!temp.exists())
			throw new Exception("[FTPService] Download failed");
	}

	public void downloadArray(String local_path, String[] server_fullpath)
			throws Exception {
		for (String fullpath : server_fullpath) {
			File temp = new File(local_path
					+ fullpath.substring(fullpath.lastIndexOf("/"),
							fullpath.length()));
			ftp.download(fullpath, temp);
			if (!temp.exists())
				throw new Exception("[FTPService] Download failed");
		}
	}

	public void upload(String local_fullpath, String server_path)
			throws Exception {
		File temp = new File(local_fullpath);
		ftp.changeDirectory(server_path);
		ftp.upload(temp);
	}

	public void uploadArray(String[] local_fullpath, String server_path)
			throws Exception {
		ftp.changeDirectory(server_path);
		for (String fullpath : local_fullpath) {
			File temp = new File(fullpath);
			ftp.upload(temp);
		}
	}

	public void removeFile(String server_fullpath) throws Exception {
		ftp.deleteFile(server_fullpath);
	}

	public void removeFolder(String server_fullpath) throws Exception {
		ftp.deleteDirectory(server_fullpath);
	}

	public void close() throws Exception {
		ftp.disconnect(true);
	}
}