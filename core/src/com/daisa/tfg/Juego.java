package com.daisa.tfg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.Arrays;

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

    boolean yoPreparado = false, rivalPreparado = false;

    public Array<String> getNombreDispositivosVisibles() {
        return nombreDispositivosVisibles;
    }


    //TODO igual se podria llamar a metodos del android launcher para firebase
    //FirebaseFirestore db;

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

    public void comenzarPartida() {
        yoPreparado = true;
        this.write(yoPreparado + ":" + Gdx.graphics.getWidth() + ":" + Gdx.graphics.getHeight());

    }

    public void mensajeRecibido(String readMessage) {
        Gdx.app.debug("MENSAJE RECIBIDO", readMessage);
        Gdx.app.debug("MENSAJE RECIBIDO", Arrays.toString(readMessage.split(":")));
        String[] datosComienzoPartida = readMessage.split(":");

        rivalPreparado = Boolean.parseBoolean(datosComienzoPartida[0]);
        ConstantesJuego.ALTO_PANTALLA_RIVAL = Integer.parseInt(datosComienzoPartida[1]);
        ConstantesJuego.ANCHO_PANTALLA_RIVAL = Integer.parseInt(datosComienzoPartida[2]);

        if(ConstantesJuego.ANCHO_PANTALLA < ConstantesJuego.ANCHO_PANTALLA_RIVAL){
            ConstantesJuego.ANCHO_PANTALLA_MULTI = ConstantesJuego.ANCHO_PANTALLA;
        }else{
            ConstantesJuego.ANCHO_PANTALLA_MULTI = ConstantesJuego.ANCHO_PANTALLA_RIVAL;
        }

        if(ConstantesJuego.ALTO_PANTALLA < ConstantesJuego.ALTO_PANTALLA_RIVAL){
            ConstantesJuego.ALTO_PANTALLA_MULTI = ConstantesJuego.ALTO_PANTALLA;
        }else{
            ConstantesJuego.ALTO_PANTALLA_MULTI = ConstantesJuego.ALTO_PANTALLA_RIVAL;
        }
    }

    public void balaRecibida(String readMessage) {
        String[] mensaje = readMessage.split(":");
        float balaX = Float.parseFloat(mensaje[0]);
        Gdx.app.debug("DEBUG", "Se ha recibido una bala de rival. Bala: [" + mensaje[0] + ", " + mensaje[1] + "]");
        Personaje.anadirBalaRival(balaX);
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


    public void elegirPersonajes(){

        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {

                setScreen(new ElegirPersonajee(juego));
            }
        });

    }
}
