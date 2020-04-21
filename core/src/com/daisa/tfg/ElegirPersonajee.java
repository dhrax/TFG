package com.daisa.tfg;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

public class ElegirPersonajee implements Screen, InputProcessor {
    Texture img;
    Juego juego;

    Array<Image> regionArray = new Array<>();
    int mostrando;

    Stage stage;

    Image img1, img2, img3;

    Image flechaIzquierda, flechaDerecha;

    Rectangle rectFlechaDerecha, rectFlechaIzquierda;

    public ElegirPersonajee(Juego juego) {
        img = new Texture("badlogic.jpg");
        this.juego = juego;
        inicializar();
        mostrando = 0;

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        actualizarImagenes();

        rectFlechaDerecha = new Rectangle(350, 400, 100, 1000);
        rectFlechaIzquierda = new Rectangle(950, 400, 100, 1000);
    }

    private void inicializar() {
        for (int i = 1; i < 7; i++) {
            regionArray.add(new Image(new Texture(Gdx.files.internal("nave_prueba_" + i + ".png"))));
        }
    }

    @Override
    public void show() {

        Gdx.app.debug("LLAMADA", "SE LLAMA A SHOW");

        stage = new Stage();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        flechaDerecha = new Image(new Texture(Gdx.files.internal("flecha_derecha_prueba.png")));

        flechaIzquierda = new Image(new Texture(Gdx.files.internal("flecha_derecha_prueba.png")));

        // Añade filas a la tabla y añade los componentes
        table.add(flechaDerecha);
        table.add(img1);
        table.add(img2);
        table.add(img3);
        table.add(flechaIzquierda);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla
        stage.act(delta);
        stage.draw();
    }

    private void actualizarImagenes() {
        Gdx.app.debug("LLAMADA", "SE ACTUALIZAN LAS IMAGENES");

        img1 = regionArray.get(mostrando);
        img2 = regionArray.get(mostrando+1);
        img3 = regionArray.get(mostrando+2);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        img.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 tmp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        //cam.unproject(tmp);

        /*Gdx.app.debug("X", String.valueOf(screenX));
        Gdx.app.debug("Y", String.valueOf(screenY));
        Gdx.app.debug("INPUT X", String.valueOf(Gdx.input.getX()));
        Gdx.app.debug("INPUT Y", String.valueOf(Gdx.input.getY()));

        Gdx.app.debug("Y REAL", String.valueOf(Gdx.graphics.getHeight() - tmp.y));*/


        if (rectFlechaDerecha.contains(tmp.x, Gdx.graphics.getHeight() - tmp.y)) {

            if(mostrando > 0){
                mostrando--;
            }
            Gdx.app.debug("TOCADO", String.valueOf(mostrando));
            actualizarImagenes();
            show();

        }else if(rectFlechaIzquierda.contains(tmp.x, Gdx.graphics.getHeight() - tmp.y)){

            if(mostrando < regionArray.size-1){
                mostrando++;
            }
            Gdx.app.debug("TOCADO", String.valueOf(mostrando));
            actualizarImagenes();
            show();

        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
