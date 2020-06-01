package com.daisa.tfg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.daisa.tfg.constantes.ConstantesBluetooth;
import com.daisa.tfg.principal.Juego;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static com.daisa.tfg.ServicioBluetooth.EstadosBluetooth.*;

public class ServicioBluetooth {

    //TODO terminar estados
    //TODO conectar ambos dispositivos, enviar mensajes entre dispositivos

    final BluetoothAdapter bluetoothAdapter;
    private final Activity mCurrentActivity;
    private final Handler mHandler;
    Juego juego;
    AndroidLauncher androidLauncher;
    EstadosBluetooth estado;
    HiloAceptar hiloAceptar;
    HiloConectar hiloConectar;
    HiloConectado hiloConectado;

    boolean esHost;


    public ServicioBluetooth(Activity activity, Juego juego, AndroidLauncher androidLauncher, Handler handler) {
        mCurrentActivity = activity;
        this.juego = juego;
        this.androidLauncher = androidLauncher;
        mHandler = handler;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //Si el adaptador es nulo, significa que no se soporta el Bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(mCurrentActivity, "No se puede jugar sin bluetooth", Toast.LENGTH_LONG).show();
            return;
        }

        estado = NULO;
    }

    public void activarBluetooth() {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mCurrentActivity.startActivityForResult(enableIntent, ConstantesBluetooth.SOLICITAR_BLUETOOTH);
    }

    public void descubirDispositivos(){
        Log.d("DEBUG", "ServicioBluetooth::Se comienza a buscar dispositivos");
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        Log.d("DEBUG", String.valueOf(bluetoothAdapter.isEnabled()));
        if(bluetoothAdapter.startDiscovery()){
            Log.d("DEBUG", "ServicioBluetooth::Busqueda de dispositivos empezada correctamente");
        }else{
            Log.d("DEBUG", "ServicioBluetooth::[ERROR] al comenzar la busqueda de dispositivos");

        }
    }

    public synchronized void conectarDispositivos(BluetoothDevice bluetoothDevice){

        esHost = false;

        if (estado == CONECTANDO) {
            if (hiloConectar != null) {
                hiloConectar.cancel();
                hiloConectar = null;
            }
        }

        if (hiloConectado != null) {
            hiloConectado.cancel();
            hiloConectado = null;
        }

        hiloConectar = new HiloConectar(bluetoothDevice);
        hiloConectar.start();
        estado = CONECTANDO;
    }

    public void serDescubierto(){
        Log.d("DEBUG", "ServicioBluetooth::Se permite que se descubra al dispositivo");
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

    public void escuchar() {
        esHost = true;

        // Cancel any thread attempting to make a connection
        if (hiloConectar != null) {
            hiloConectar.cancel();
            hiloConectar = null;
        }

        // Cancel any thread currently running a connection
        if (hiloConectado != null) {
            hiloConectado.cancel();
            hiloConectado = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (hiloAceptar == null) {
            hiloAceptar = new HiloAceptar();
            hiloAceptar.start();
        }

        estado = ESCUCHANDO;
    }

    private class HiloAceptar extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        public HiloAceptar() {
            BluetoothServerSocket tmp = null;
            try {

                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("TFG", UUID.fromString("DDD59690-4FBA-11E2-BCFD-0800200C9A66"));

            } catch (IOException e) {
                Log.d("DEBUG", "accept() ha fallado");
            }
            mmServerSocket = tmp;
        }

        @Override
        public void run() {
            BluetoothSocket socket;
            while (estado != CONECTADO) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                if (socket != null)
                {
                    Log.d("DEBUG", "Connection accepted :" + estado);

                    synchronized (ServicioBluetooth.this) {
                        switch (estado) {
                            case ESCUCHANDO:
                            case CONECTANDO:
                                // Situation normal. Start the connected thread.
                                Log.d("DEBUG", "Accept Connecting");
                                conectado(socket, socket.getRemoteDevice());
                                break;
                            case NULO:
                            case CONECTADO:
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            Log.d("DEBUG", "END mAcceptThread");

        }

        public void cancel() {
            Log.d("DEBUG", "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class HiloConectar extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public HiloConectar(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("DDD59690-4FBA-11E2-BCFD-0800200C9A66"));
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i("DEBUG", "BEGIN mConnectThread");

            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("DEBUG", "unable to close() socket during connection failure", closeException);
                }
                connectionFailed();
                return;
            }

            synchronized (ServicioBluetooth.this) {
                hiloConectar = null;
            }

            conectado(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class HiloConectado extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public HiloConectado(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                Log.d("DEBUG", "HiloConectado::Se crea el input y el output");
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i("DEBUG", "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);

                    mHandler.obtainMessage(ConstantesBluetooth.LEER_MENSAJE, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e("DEBUG", "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("DEBUG", "close() of connect socket failed", e);
            }
        }
    }

    private synchronized void conectado(BluetoothSocket socket,
                                        BluetoothDevice device) {
        if (hiloConectar != null) {
            hiloConectar.cancel();
            hiloConectar = null;
        }

        if (hiloConectado != null) {
            hiloConectado.cancel();
            hiloConectado = null;
        }

        if (hiloAceptar != null) {
            hiloAceptar.cancel();
            hiloAceptar = null;
        }

        Log.d("DEBUG", "ServicioBluetooth::Dentro de la función conectado");

        Log.d("DEBUG", "ServicioBluetooth::Se crea HiloConectado");
        hiloConectado = new HiloConectado(socket);
        hiloConectado.start();

        Log.d("DEBUG", "ServicioBluetooth::Se envia mensaje del dispositivo al que se ha conectado");
        Message msg = mHandler.obtainMessage(ConstantesBluetooth.MENSAJE_NOMBRE_DISPOSITIVO);
        Bundle bundle = new Bundle();
        bundle.putString("nombre de dispositivo", device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);


        estado = CONECTADO;
    }

    private void connectionLost() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(ConstantesBluetooth.MENSAJE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "El rival se ha desconectado");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        estado = NULO;
        juego.rivalDesconectado();
    }

    public void write(byte[] out) {
        // Create temporary object
        HiloConectado r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (estado != EstadosBluetooth.CONECTADO) return;
            r = hiloConectado;
        }
        // Perform the write unsynchronized
        r.write(out);
    }

    private void connectionFailed() {

        Message msg = mHandler.obtainMessage(ConstantesBluetooth.MENSAJE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        estado = NULO;

        juego.conexionPerdida();
    }

    //TODO LLAMARLO EN EL SCREEN CADA VEZ QUE SE PULSA UN BOTON
    public synchronized void
    stop() {
        if (hiloConectar != null) {
            hiloConectar.cancel();
            hiloConectar = null;
        }
        if (hiloConectado != null) {
            hiloConectado.cancel();
            hiloConectado = null;
        }
        if (hiloAceptar != null) {
            hiloAceptar.cancel();
            hiloAceptar = null;
        }
        estado = NULO;
    }

    public enum EstadosBluetooth{
        CONECTADO, CONECTANDO, ESCUCHANDO, NULO
    }
}
