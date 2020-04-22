package com.daisa.tfg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class Juego extends Game {

    //TODO Cargar todo los managers aquí
    SpriteBatch batch;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, reference;
    Preferencias preferencias;

    JuegoAssetManager manager = new JuegoAssetManager();

    // Local variable to hold the callback implementation
    private MiJuegoCallBack myGameCallback;

    public interface MiJuegoCallBack{
        void activityForResultBluetooth();
        void conectarDispositivosBluetooth(String nombreDispositivo);
        void habilitarSerDescubiertoBluetooth();
    }

    public void activarBluetooth(){
        myGameCallback.activityForResultBluetooth();
    }
    public void conectarBluetooth(String nombreDispositivo){
        myGameCallback.conectarDispositivosBluetooth(nombreDispositivo);
    }
    public void habilitarSerDescubierto(){
        myGameCallback.habilitarSerDescubiertoBluetooth();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        try {
            firebaseDatabase = conectarAFirebase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        databaseReference = firebaseDatabase.getReference("Usuario");

        Usuario usuario = new Usuario("Daisa", "root");

        reference = databaseReference.child(usuario.getNombre());
        reference.setValueAsync(usuario);

        preferencias = new Preferencias();

        setScreen(new LoginScreen(this));
    }

    public FirebaseDatabase conectarAFirebase() throws IOException {
        Gdx.app.log("ENVIADO", "Listo para enviar");
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(Gdx.files.internal("key.json").read()))
                .setDatabaseUrl("https://trabajo-final-grado.firebaseio.com/")
                .build();

        FirebaseApp defaultApp = FirebaseApp.initializeApp(options);

        return FirebaseDatabase.getInstance(defaultApp);
    }

    public Preferencias getPreferencias(){
        return this.preferencias;
    }

    @Override
    public void dispose() {
        manager.managerJuego.dispose();
    }

    public void setMyGameCallback(MiJuegoCallBack callback) {
        myGameCallback = callback;
    }

    public MiJuegoCallBack getMyGameCallback() {
        return myGameCallback;
    }
}
