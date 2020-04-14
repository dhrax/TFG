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
    DatabaseReference databaseReference;
    Preferencias preferencias;

    JuegoAssetManager manager = new JuegoAssetManager();

    @Override
    public void create() {
        batch = new SpriteBatch();


        try {
            firebaseDatabase =conectar();
        } catch (IOException e) {
            e.printStackTrace();
        }

        databaseReference = firebaseDatabase.getReference("/Usuario");

        preferencias = new Preferencias();

        setScreen(new LoginScreen(this));
    }

    public FirebaseDatabase conectar() throws IOException {
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
}
