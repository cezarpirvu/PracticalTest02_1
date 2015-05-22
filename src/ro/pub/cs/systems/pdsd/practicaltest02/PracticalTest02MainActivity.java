package ro.pub.cs.systems.pdsd.practicaltest02;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends Activity {

	//server
	private EditText server_port = null;
	private Button server_connect = null;
	//client
	private EditText client_address = null;
	private Button client_information = null;
	private EditText client_port = null;
	private EditText city_desired = null;
	private Spinner information = null;
	private TextView results = null;
	
	//thread-uri
	private ServerThread serverthread = null;
	private ClientThread clientthread = null;
	
	
	private ServerButtonListener ServerConnectButton = new ServerButtonListener();
	private class ServerButtonListener implements Button.OnClickListener {
		
		@Override
		public void onClick (View view) {
			String serverPort = server_port.getText().toString();
			if (serverPort == null || serverPort.isEmpty()) {
				Toast.makeText(getApplicationContext(),"Server port should be filled!",Toast.LENGTH_SHORT).show();
				return;
			}
			
			serverthread = new ServerThread(Integer.parseInt(serverPort));
			if (serverthread.getServerSocket() != null) {
				serverthread.start();
			} else {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
			}
		}
	}
	
	private ClientButtonListener ClientButton = new ClientButtonListener();
	private class ClientButtonListener implements Button.OnClickListener {
		
		@Override
		public void onClick (View view) {
			String clientAddress = client_address.getText().toString();
			String clientPort    = client_port.getText().toString();
			if (clientAddress == null || clientAddress.isEmpty() || clientPort == null || clientPort.isEmpty()) {
				Toast.makeText(getApplicationContext(),"Client connection parameters should be filled!",Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (serverthread == null || !serverthread.isAlive()) {
				Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
				return;
			}
			
			String city = city_desired.getText().toString();
			String informationType = information.getSelectedItem().toString();
			if (city == null || city.isEmpty() || informationType == null || informationType.isEmpty()) {
				Toast.makeText(getApplicationContext(),"Parameters from client (city / information type) should be filled!",Toast.LENGTH_SHORT).show();
				return;
			}
			
			results.setText(Constants.EMPTY_STRING);
			
			clientthread = new ClientThread(clientAddress,Integer.parseInt(clientPort),city,informationType,results);
			clientthread.start();
			
			
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practical_test02_main);
		
		server_port = (EditText)findViewById(R.id.server_port);
		server_connect = (Button)findViewById(R.id.server_button);
		server_connect.setOnClickListener(ServerConnectButton);
		
		client_address = (EditText)findViewById(R.id.client_address);
		client_information = (Button)findViewById(R.id.information);
		client_information.setOnClickListener(ClientButton);
		
		client_port = (EditText)findViewById(R.id.client_port);
		city_desired = (EditText)findViewById(R.id.city);
		results = (TextView)findViewById(R.id.results);
		information = (Spinner)findViewById(R.id.option);
		
	}

	
	@Override
	protected void onDestroy() {
		if (serverthread != null) {
			serverthread.stopThread();
		}
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.practical_test02_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
