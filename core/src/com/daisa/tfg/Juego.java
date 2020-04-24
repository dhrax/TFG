package com.daisa.tfg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Juego extends Game {

    //TODO Cargar todo los managers aquí
    SpriteBatch batch;
    ExtendViewport viewport;
    OrthographicCamera camera;

    //FirebaseDatabase firebaseDatabase;
    //DatabaseReference databaseReference, reference;
    Preferencias preferencias;

    JuegoAssetManager manager = new JuegoAssetManager();

    // Local variable to hold the callback implementation
    private MiJuegoCallBack myGameCallback;
    private Array<String> nombreDispositivosVisibles;
    public ConectarJugadoresScreen conectarJugadoresScreen;
    Juego juego;

    public Array<String> getNombreDispositivosVisibles() {
        return nombreDispositivosVisibles;
    }

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(720, 1280, camera);

        /*try {
            firebaseDatabase = conectarAFirebase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        databaseReference = firebaseDatabase.getReference("Usuario");

        Usuario usuario = new Usuario("Daisa", "root");

        reference = databaseReference.child(usuario.getNombre());
        reference.setValueAsync(usuario);

         */

        juego = this;
        preferencias = new Preferencias();

        setScreen(new LoginScreen(juego));
        //setScreen(new ElegirPersonajee(this));
    }

    public boolean estaBluetoothEncencido() {
        return myGameCallback.bluetoothEncendido();
    }

    public void descubrirDispositivos() {
        myGameCallback.descubrirDispositivosBluetooth();
    }

    public void refrescarListaDispositivos() {
        conectarJugadoresScreen.refrescarLista(nombreDispositivosVisibles);
    }

    public void anadirDispositivo(Array<String> nombreDispositivosVisibles) {
        this.nombreDispositivosVisibles = nombreDispositivosVisibles;
    }

    public void empezarAEscuchar() {
        myGameCallback.empezarAEscucharBluetooth();
    }

    public interface MiJuegoCallBack{
        void activityForResultBluetooth();
        void conectarDispositivosBluetooth(String nombreDispositivo);
        void habilitarSerDescubiertoBluetooth();
        boolean bluetoothEncendido();
        void descubrirDispositivosBluetooth();
        void empezarAEscucharBluetooth();
        void write(String string);
        void stop();
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
    public void write(String string){
        myGameCallback.write(string);
    }

    public void stop(){
        myGameCallback.stop();
    }

    /*public FirebaseDatabase conectarAFirebase() throws IOException {
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(Gdx.files.internal("key.json").read()))
                .setDatabaseUrl("https://trabajo-final-grado.firebaseio.com/")
                .build();

        FirebaseApp defaultApp = FirebaseApp.initializeApp(options);

        return FirebaseDatabase.getInstance(defaultApp);
    }
     */

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


    public void comenzarPartida(){

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

                setScreen(new ElegirPersonajee(juego));
            }
        });

    }
}
