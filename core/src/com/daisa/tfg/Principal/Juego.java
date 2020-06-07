package com.daisa.tfg.Principal;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.daisa.tfg.Personajes.Personaje;
import com.daisa.tfg.Screens.ConectarJugadoresScreen;
import com.daisa.tfg.Screens.ElegirModoScreen;
import com.daisa.tfg.Screens.ElegirPersonajeScreen;
import com.daisa.tfg.Screens.LoginScreen;
import com.daisa.tfg.Screens.MenuPrincipalScreen;
import com.daisa.tfg.Screens.RankingScreen;
import com.daisa.tfg.Util.JuegoAssetManager;

import java.util.ArrayList;

public class Juego extends Game {

    //TODO Cargar todo los managers aquí
    public SpriteBatch batch;
    public ExtendViewport viewport;
    public OrthographicCamera camera;

    public Preferencias preferencias;

    public JuegoAssetManager manager;

    private BluetoothCallBack bluetoothCallBack;
    private FirebaseCallBack firebaseCallBack;
    private Array<String> nombreDispositivosVisibles;
    public ConectarJugadoresScreen conectarJugadoresScreen;
    public RankingScreen rankingScreen;
    Juego juego;

    public boolean yoPreparado = false, rivalPreparado = false;
    private int miPuntuacion = 0, rivalPuntuacion = 0;
    private String nombreUsuario = null;

    private TextureRegion fondoMenu;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(720, 1280, camera);

        juego = this;
        preferencias = new Preferencias();

        manager = new JuegoAssetManager();

        fondoMenu = new TextureRegion(new Sprite(new Texture("Fondos/fondoMenus.jpg")));

        setScreen(new LoginScreen(juego));
    }

    public boolean estaBluetoothEncencido() {
        return bluetoothCallBack.bluetoothEncendido();
    }

    public void descubrirDispositivos() {
        bluetoothCallBack.descubrirDispositivosBluetooth();
    }

    public void refrescarListaDispositivos() {
        conectarJugadoresScreen.refrescarLista(nombreDispositivosVisibles);
    }

    public void anadirDispositivo(Array<String> nombreDispositivosVisibles) {
        this.nombreDispositivosVisibles = nombreDispositivosVisibles;
    }

    public void empezarAEscuchar() {
        bluetoothCallBack.empezarAEscucharBluetooth();
    }

    public void comenzarPartida() {
        this.write("true");
    }

    public void rivalPreparado(String readMessage) {
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

    public void recogerPuntuacionesLibGDX() {
        firebaseCallBack.recogerPuntuaciones();
    }

    public void refrescarListaRanking(Array<String> ranking) {
        for(String nombre : ranking){
            Gdx.app.debug("DEBUG", nombre);
        }
        rankingScreen.refrescarLista(ranking);
    }

    public interface BluetoothCallBack {
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
        bluetoothCallBack.activityForResultBluetooth();
    }

    public void conectarBluetooth(String nombreDispositivo){
        bluetoothCallBack.conectarDispositivosBluetooth(nombreDispositivo);
    }

    public void habilitarSerDescubierto(){
        bluetoothCallBack.habilitarSerDescubiertoBluetooth();
    }

    public void write(String string){
        bluetoothCallBack.write(string);
    }

    public void stop(){
        bluetoothCallBack.stop();
    }

    public interface FirebaseCallBack{
        void comprobacionUsuario(String nombreUsuario, String contrasena);
        void pintarToast(String mensaje);
        void usuarioYaExiste(String nombreUsuario, String contrasena);
        void recogerPuntuaciones();
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

    public void setBluetoothCallBack(BluetoothCallBack callback) {
        bluetoothCallBack = callback;
    }

    public void setFirebaseCallBack(FirebaseCallBack firebaseCallBack) {
        this.firebaseCallBack = firebaseCallBack;
    }

    public void elegirPersonajes(){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setScreen(new ElegirPersonajeScreen(juego));
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

    public TextureRegion getFondoMenu() {
        return fondoMenu;
    }
}
