package com.daisa.tfg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;

public class ServicioBluetooth {

    //TODO terminar estados
    //TODO conectar ambos dispositivos, enviar mensajes entre dispositivos

    private final BluetoothAdapter bluetoothAdapter;
    private final Activity mCurrentActivity;
    Juego juego;
    AndroidLauncher androidLauncher;
    EstadosBluetooth estado;


    public ServicioBluetooth(Activity activity, Juego juego, AndroidLauncher androidLauncher) {
        mCurrentActivity = activity;
        this.juego = juego;
        this.androidLauncher = androidLauncher;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Si el adaptador es nulo, significa que no se soporta el Bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(mCurrentActivity, "No se puede jugar sin bluetooth", Toast.LENGTH_LONG).show();
            return;
        }

        estado = EstadosBluetooth.NULO;
    }

    public void activarBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            descubirBluetooth();
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    androidLauncher.conectarJugadoresScreen = new ConectarJugadoresScreen(juego);
                    juego.setScreen(androidLauncher.conectarJugadoresScreen);
                }
            });
        }else{
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mCurrentActivity.startActivityForResult(enableIntent, ConstantesBluetooth.SOLICITAR_BLUETOOTH);
        }
    }

    public void descubirBluetooth(){
        estado = EstadosBluetooth.BUSCANDO;
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    public enum EstadosBluetooth{
        CONECTADO, BUSCANDO, ESCUCHANDO, NULO
    }

}
