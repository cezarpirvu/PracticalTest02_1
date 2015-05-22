package ro.pub.cs.systems.pdsd.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.util.Log;
import android.widget.TextView;

public class ClientThread extends Thread {
	
	private String   client_address;
	private int      client_port;
	private String   city;
	private String   information;
	private TextView results;
	
	private Socket   socket;
	
	public ClientThread(
			String client_address,
			int client_port,
			String city,
			String information,
			TextView results) {
		this.client_address = client_address;
		this.client_port = client_port;
		this.city = city;
		this.information = information;
		this.results = results;
	}
	
	@Override
	public void run() {
		try {
			socket = new Socket(client_address, client_port);
			if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(city);
				printWriter.flush();
				printWriter.println(information);
				printWriter.flush();
				String results_obtained;
				while ((results_obtained = bufferedReader.readLine()) != null) {
					final String finalized_results_obtained = results_obtained;
					results.post(new Runnable() {
						@Override
						public void run() {
							results.append(finalized_results_obtained + "\n");
						}
					});
				}
			} else {
				Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
			}
			socket.close();
		} catch (IOException ioException) {
			Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
			if (Constants.DEBUG) {
				ioException.printStackTrace();
			}
		}
	}
}
