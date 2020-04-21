package com.daisa.tfg;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;

public class AndroidLauncher extends AndroidApplication implements Juego.MiJuegoCallBack{

	ServicioBluetooth servicioBluetooth;
	AndroidLauncher androidLauncher;
	Juego juego;
	public static Array<String> dispositivosConectados = new Array<>();
	ConectarJugadoresScreen conectarJugadoresScreen;

	//TODO Permitir ser descubierto

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		androidLauncher = this;

		juego = new Juego();
		juego.setMyGameCallback(androidLauncher);

		initialize(juego, config);
	}

	@Override
	public void activityForResultBluetooth() {
		servicioBluetooth = new ServicioBluetooth(androidLauncher, juego, androidLauncher);
		servicioBluetooth.activarBluetooth();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			case ConstantesBluetooth.SOLICITAR_BLUETOOTH:
				if(resultCode == Activity.RESULT_OK){
					servicioBluetooth.descubirBluetooth();
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							conectarJugadoresScreen = new ConectarJugadoresScreen(juego);
							juego.setScreen(conectarJugadoresScreen);
						}
					});
				}else{
					Toast.makeText(this, "No se puede jugar sin bluetooth", Toast.LENGTH_LONG).show();
				}
				break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		androidLauncher.registerReceiver(mReceiver, filter);
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (device != null) {
					String direccion = device.getName();
					if(direccion != null) {
						dispositivosConectados.add(direccion);
						conectarJugadoresScreen.refrescarLista(dispositivosConectados);
					}
				}else{
					Toast.makeText(androidLauncher, "ERROR al recibir nombre del dispositivo", Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
}