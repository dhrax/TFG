package com.daisa.tfg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class AndroidLauncher extends AndroidApplication implements Juego.MiJuegoCallBack{

	ServicioBluetooth servicioBluetooth;
	IntentFilter filtroEncontradoDispositivo = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	IntentFilter filtroModoScan = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);

	AndroidLauncher androidLauncher;
	Juego juego;
	public static Array<String> nombreDispositivosVisibles = new Array<>();
	//TODO intentar cambiar ArrayList por Conjunto
	ArrayList<BluetoothDevice> dispositivosVisibles = new ArrayList<>();

	//TODO Permitir ser descubierto (si se tiene que ser host)

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		androidLauncher = this;

		juego = new Juego();
		juego.setMyGameCallback(androidLauncher);

		servicioBluetooth = new ServicioBluetooth(androidLauncher, juego, androidLauncher, handler);
		androidLauncher.registerReceiver(mReceiver, filtroEncontradoDispositivo);
		androidLauncher.registerReceiver(scanRecibidor, filtroModoScan);

		initialize(juego, config);
	}

	private Handler handler  = new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {

				case ConstantesBluetooth.LEER_MENSAJE:
					byte[] readBuf = (byte[]) msg.obj;
					// construct a string from the valid bytes in the buffer
					String readMessage = new String(readBuf, 0, msg.arg1);
					Toast.makeText(androidLauncher, readMessage, Toast.LENGTH_SHORT).show();
					//gdxFragment.getGameInstance().incomingMessage(readMessage);
					break;

				case ConstantesBluetooth.MENSAJE_NOMBRE_DISPOSITIVO:
					// save the connected device's name
					CharSequence connectedDevice = "Connected to " + msg.getData().getString("nombre de dispositivo");
					Toast.makeText(androidLauncher, connectedDevice, Toast.LENGTH_SHORT).show();
					//Se elige el personaje
					juego.comenzarPartida();
					break;

				case ConstantesBluetooth.MENSAJE_ESTADO_CAMBIA:
					if(servicioBluetooth.getEstado() == ServicioBluetooth.EstadosBluetooth.NULO )
					{
						//TODO terminar partida
					}
					break;

				case ConstantesBluetooth.MENSAJE_TOAST:
					CharSequence content = msg.getData().getString(" toast");
					Toast.makeText(androidLauncher, content , Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	@Override
	public void activityForResultBluetooth() {
		servicioBluetooth.activarBluetooth();
	}

	@Override
	public void conectarDispositivosBluetooth(String nombreDispositivo) {

		Log.d("DEBUG", "AndroidLauncher::Se está buscando el dispositivo pulsado");
		int pos = -1;
		for (BluetoothDevice device : dispositivosVisibles){
			if(device.getName().equals(nombreDispositivo)){
				pos = dispositivosVisibles.indexOf(device);
			}
		}
		servicioBluetooth.conectarDispositivos(dispositivosVisibles.get(pos));
	}

	@Override
	public void habilitarSerDescubiertoBluetooth() {
		servicioBluetooth.serDescubierto();
	}

	@Override
	public boolean bluetoothEncendido() {
		Log.d("DEBUG", "¿Bluetooth Encencido? " + servicioBluetooth.bluetoothAdapter.isEnabled());
		return servicioBluetooth.bluetoothAdapter.isEnabled();
	}

	@Override
	public void descubrirDispositivosBluetooth() {
		servicioBluetooth.descubirDispositivos();
	}

	@Override
	public void empezarAEscucharBluetooth() {
		servicioBluetooth.escuchar();
	}

	@Override
	public void write(String string) {
		servicioBluetooth.write(string.getBytes());
	}

	@Override
	public void stop() {
		dispositivosVisibles.clear();
		nombreDispositivosVisibles.clear();
		servicioBluetooth.stop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ConstantesBluetooth.SOLICITAR_BLUETOOTH) {
			if (resultCode == Activity.RESULT_OK) {
				Log.d("DEBUG", "AndroidLauncher::Se permite el Bluetooth");
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						juego.conectarJugadoresScreen = new ConectarJugadoresScreen(juego);
						juego.setScreen(juego.conectarJugadoresScreen);
					}
				});
			} else {
				Log.d("DEBUG", "AndroidLauncher::Bluetooth no activado por lo que no se puede jugar al cooperativo");
				Toast.makeText(this, "No se puede jugar sin bluetooth", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				Log.d("DEBUG", "AndroidLauncher::Se ha encontrado un dispositivo");
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (device != null) {
					String nombreDispositivo = device.getName();
					if(nombreDispositivo != null) {
						Log.d("DEBUG", "AndroidLauncher::Dispositivo añadido a la lista: " + nombreDispositivo);
						nombreDispositivosVisibles.add(nombreDispositivo);
						dispositivosVisibles.add(device);
						juego.anadirDispositivo(nombreDispositivosVisibles);
						juego.refrescarListaDispositivos();
					}else{
						Log.d("DEBUG", "AndroidLauncher::Dispositivo sin nombre, no se muestra en la lista");
					}
				}else{
					Log.d("DEBUG", "AndroidLauncher::[ERROR] al recibir nombre del dispositivo");
				}
			}
		}
	};

	BroadcastReceiver scanRecibidor = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action))
			{
				int modoScaneo = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);
				if(modoScaneo == BluetoothAdapter.SCAN_MODE_CONNECTABLE){
					Log.d("DEBUG", "AndroidLauncher::El dispositivo no se puede descubrir pero puede recibir conexiones");
				}else if(modoScaneo == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
					Log.d("DEBUG", "AndroidLauncher::El dispositivo se puede descubrir");
				}else if(modoScaneo == BluetoothAdapter.SCAN_MODE_NONE){
					Log.d("DEBUG", "AndroidLauncher::El dispositivo no se puede descubrir ni recibir conexiones");
				}else if(modoScaneo == BluetoothAdapter.ERROR){
					Log.d("DEBUG", "AndroidLauncher::[ERROR] Ha habido un fallo al recoger el modo en el que se encuentra el dispositivo");
				}else {
					Log.d("DEBUG", "AndroidLauncher::Opcion no contemplada, valor del modo de scaneo: " + modoScaneo);
				}
			}
		}
	};



}