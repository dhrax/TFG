package com.daisa.tfg;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Array;
import com.daisa.tfg.Constantes.ConstantesBluetooth;
import com.daisa.tfg.Principal.Juego;
import com.daisa.tfg.Screens.ConectarJugadoresScreen;

import java.util.LinkedHashSet;
import java.util.Set;

public class AndroidLauncher extends AndroidApplication {

    ServicioBluetooth servicioBluetooth;
    ServicioFirebase servicioFirebase;
    IntentFilter filtroEncontradoDispositivo = new IntentFilter(BluetoothDevice.ACTION_FOUND);

    AndroidLauncher androidLauncher;
    Juego juego;
    UtilAndroid utilAndroid;
    public static Array<String> nombreDispositivosVisibles = new Array<>();
    Set<BluetoothDevice> SetDispositivosVisibles = new LinkedHashSet<>();

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                ocultarBotonesVirtuales();
            }
        }
    }

    //Se quitan los botnes y la barra de navegación en dispositivos superiores a Android KITKAT
    @TargetApi(19)
    private void ocultarBotonesVirtuales() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        View decorView = getWindow().getDecorView();
        // Oculta la barra de navegación y la de estado
        int opciones = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(opciones);

        utilAndroid = new UtilAndroid();

        androidLauncher = this;
        juego = new Juego();
        servicioFirebase = new ServicioFirebase(androidLauncher, juego, handler, androidLauncher);

        servicioBluetooth = new ServicioBluetooth(androidLauncher, juego, androidLauncher, handler);
        androidLauncher.registerReceiver(mReceiver, filtroEncontradoDispositivo);

        initialize(juego, config);
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case ConstantesBluetooth.LEER_MENSAJE:
                    byte[] bufferLeido = (byte[]) msg.obj;
                    //Se construye el mensaje a partir de buffer
                    String mensajeRecibido = new String(bufferLeido, 0, msg.arg1);

                    switch (mensajeRecibido) {
                        case "true":
                            Toast.makeText(androidLauncher, "El rival ha elegido", Toast.LENGTH_SHORT).show();
                            juego.rivalPreparado(mensajeRecibido);
                            break;
                        case "fin":
                            juego.setMiPuntuacion(juego.getMiPuntuacion() + 1);
                            servicioFirebase.guardarPuntuacionBBDD(juego.getNombreUsuario() , 1);
                            juego.elegirPersonajes();
                            break;
                        default:
                            juego.balaRecibida(mensajeRecibido);
                            break;
                    }
                    break;

                case ConstantesBluetooth.MENSAJE_NOMBRE_DISPOSITIVO:
                    CharSequence dispositivoConectado = "Conectado con " + msg.getData().getString(UtilAndroid.NOMBRE_DISPOSITIVO);
                    Toast.makeText(androidLauncher, dispositivoConectado, Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG", "Se llama a la SrcreenElegirPersonajee");
                    juego.elegirPersonajes();
                    break;

                case ConstantesBluetooth.MENSAJE_TOAST:
                    CharSequence content = msg.getData().getString("toast");
                    Toast.makeText(androidLauncher, content, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



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
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.d("DEBUG", "AndroidLauncher::Se ha encontrado un dispositivo");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device != null) {
                    String nombreDispositivo = device.getName();
                    if (nombreDispositivo != null) {
                        if (SetDispositivosVisibles.add(device)) {
                            Log.d("DEBUG", "AndroidLauncher::Dispositivo añadido a la lista: " + nombreDispositivo);
                            nombreDispositivosVisibles.add(nombreDispositivo);

                            juego.anadirDispositivo(nombreDispositivosVisibles);
                            juego.refrescarListaDispositivos();
                        }
                    } else {
                        Log.d("DEBUG", "AndroidLauncher::Dispositivo sin nombre, no se muestra en la lista");
                    }
                } else {
                    Log.d("DEBUG", "AndroidLauncher::[ERROR] al recibir nombre del dispositivo");
                }
            }
        }
    };
}