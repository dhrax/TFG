package com.daisa.tfg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ServicioBluetooth {

    //TODO terminar estados
    //TODO conectar ambos dispositivos, enviar mensajes entre dispositivos

    private final BluetoothAdapter bluetoothAdapter;
    private final Activity mCurrentActivity;
    Juego juego;
    AndroidLauncher androidLauncher;
    EstadosBluetooth estado;
    HiloConectar hiloConectar;
    HiloConectado hiloConectado;


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
            descubirDispositivos();
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

    public void descubirDispositivos(){
        estado = EstadosBluetooth.CONECTANDO;
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    public synchronized void conectarDispositivos(BluetoothDevice bluetoothDevice){

        hiloConectar = new HiloConectar(bluetoothDevice);
        hiloConectar.start();
        setEstado(EstadosBluetooth.CONECTANDO);
    }

    public void serDescubierto(){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
        mCurrentActivity.startActivity(intent);
    }

    public EstadosBluetooth getEstado() {
        return estado;
    }

    public void setEstado(EstadosBluetooth estado) {
        this.estado = estado;
    }

    private class HiloConectar extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public HiloConectar(BluetoothDevice bluetoothDevice) {
            mmDevice = bluetoothDevice;
            BluetoothSocket tmpSocket = null;

            try {
                tmpSocket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("DDD59690-4FBA-11E2-BCFD-0800200C9A66"));
            } catch (IOException e) {
                Log.d("ERROR", e.getMessage());
            }
            mmSocket = tmpSocket;
        }

        @Override
        public void run() {
            //Se deja de buscar dispositivos para mejorar el rendimiento
            bluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    Log.d("ERROR", "No se ha podido realizar la conexion Bluetooth");
                    mmSocket.close();
                } catch (IOException ex) {
                    Log.d("ERROR", "No se ha podido cerrar el socket");
                }
                estado = EstadosBluetooth.NULO;
                return;
            }
            hiloConectar = null;

            conectado(mmSocket, mmDevice);

        }
    }

    private void conectado(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {

        hiloConectado = new HiloConectado(mmSocket);
        hiloConectado.start();

        CharSequence connectedDevice = "Connected to " + mmDevice.getName();
        Toast.makeText(mCurrentActivity, connectedDevice, Toast.LENGTH_SHORT).show();

    }


    private class HiloConectado extends Thread{
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public HiloConectado(BluetoothSocket mmSocket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                Log.d("ERROR", "No se han podido generar los I/O streams");
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);

                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e("ERROR", "Excepcion al escribir el buffer", e);
            }
        }

    }

    public enum EstadosBluetooth{
        CONECTADO, CONECTANDO, ESCUCHANDO, NULO
    }

}
