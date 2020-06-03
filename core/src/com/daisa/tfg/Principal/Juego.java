package com.daisa.tfg.principal;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.daisa.tfg.personajes.Personaje;
import com.daisa.tfg.screens.ConectarJugadoresScreen;
import com.daisa.tfg.screens.ElegirModoScreen;
import com.daisa.tfg.screens.ElegirPersonaje;
import com.daisa.tfg.screens.LoginScreen;
import com.daisa.tfg.screens.MenuPrincipalScreen;
import com.daisa.tfg.util.JuegoAssetManager;

public class Juego extends Game {

    //TODO Cargar todo los managers aquí
    public SpriteBatch batch;
    public ExtendViewport viewport;
    public OrthographicCamera camera;

    public Preferencias preferencias;

    public JuegoAssetManager manager;

    // Local variable to hold the callback implementation
    private MiJuegoCallBack myGameCallback;
    private FirebaseCallBack firebaseCallBack;
    private Array<String> nombreDispositivosVisibles;
    public ConectarJugadoresScreen conectarJugadoresScreen;
    Juego juego;

    public boolean yoPreparado = false, rivalPreparado = false;
    private int miPuntuacion = 0, rivalPuntuacion = 0;
    private String nombreUsuario = null;

    public Array<String> getNombreDispositivosVisibles() {
        return nombreDispositivosVisibles;
    }


    //TODO igual se podria llamar a metodos del android launcher para firebase

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(720, 1280, camera);

        juego = this;
        preferencias = new Preferencias();

        manager = new JuegoAssetManager();

        setScreen(new ElegirPersonaje(juego));
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
        this.write("true");

    }

    public void mensajeRecibido(String readMessage) {
        Gdx.app.debug("MENSAJE RECIBIDO", readMessage);
        rivalPreparado = Boolean.parseBoolean(readMessage);
    }

    public void balaRecibida(String readMessage) {
        try{
            String[] mensaje = readMessage.split(":");
            final float balaX = Float.parseFloat(mensaje[0]);
            final int idPJRival = Integer.parseInt(mensaje[1]);
            final int tamanoBala = Integer.parseInt(mensaje[2]);
            Gdx.app.debug("DEBUG", "Se ha recibido una bala de rival. Bala: [" + mensaje[0] + ", " + mensaje[1] +  ", " + mensaje[2] +  "]");
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Personaje.anadirBalaRival(balaX, idPJRival, tamanoBala);
                }
            });
        }catch(Exception e){
            Gdx.app.debug("DEBUG", "[ERROR] Error al convertir los datos dela bala rival");
        }


    }

    public void conexionPerdida() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(new ElegirModoScreen(juego));
            }
        });
    }


    public void irAMenuPrincipal() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(new MenuPrincipalScreen(juego));
            }
        });
    }

    public void rivalDesconectado() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(new ElegirModoScreen(juego));
            }
        });
    }
              
    public void crearToast(String mensaje) {
        firebaseCallBack.pintarToast(mensaje);
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

    public interface FirebaseCallBack{
        void comprobacionUsuario(String nombreUsuario, String contrasena);
        void pintarToast(String mensaje);
        void usuarioYaExiste(String nombreUsuario, String contrasena);
    }

    public void comprobacionUsuarioLIBGDX(String nombreUsuario, String contrasena){
        firebaseCallBack.comprobacionUsuario(nombreUsuario, contrasena);
    }
    public void usuarioYaExisteLIBGDX(String nombreUsuario, String contrasena) {
        firebaseCallBack.usuarioYaExiste(nombreUsuario, contrasena);
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

    public FirebaseCallBack getFirebaseCallBack() {
        return firebaseCallBack;
    }

    public void setFirebaseCallBack(FirebaseCallBack firebaseCallBack) {
        this.firebaseCallBack = firebaseCallBack;
    }

    public void elegirPersonajes(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(new ElegirPersonaje(juego));
            }
        });
    }

    public int getMiPuntuacion() {
        return miPuntuacion;
    }

    public void setMiPuntuacion(int miPuntuacion) {
        this.miPuntuacion = miPuntuacion;
    }

    public int getRivalPuntuacion() {
        return rivalPuntuacion;
    }

    public void setRivalPuntuacion(int rivalPuntuacion) {
        this.rivalPuntuacion = rivalPuntuacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
