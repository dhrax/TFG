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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;

public class ElegirPersonajee implements Screen {
    Texture img;
    Juego juego;

    Array<Image> regionArray = new Array<>();
    int mostrando;

    Stage stage;

    Image img1, img2, img3;

    public ElegirPersonajee(Juego juego) {
        img = new Texture("badlogic.jpg");
        this.juego = juego;
        inicializar();
        mostrando = 0;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        img1 = regionArray.get(mostrando);
        img2 = regionArray.get(mostrando+1);
        img3 = regionArray.get(mostrando+2);


    }

    private void inicializar() {
        for (int i = 1; i < 7; i++) {
            regionArray.add(new Image(new Texture(Gdx.files.internal("nave_prueba_" + i + ".png"))));
        }
    }

    @Override
    public void show() {

        Gdx.app.debug("LLAMADA", "SE LLAMA A SHOW");
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        Image flechaDerecha = new Image(new Texture(Gdx.files.internal("flecha_derecha.png")));

        flechaDerecha.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(mostrando > 0){
                    mostrando--;
                }
                Gdx.app.debug("TOCADO", String.valueOf(mostrando));
            }
        });

        Image flechaIzquierda = new Image(new Texture(Gdx.files.internal("flecha_derecha.png")));

        flechaIzquierda.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(mostrando < regionArray.size-3){
                    mostrando++;
                }
                Gdx.app.debug("TOCADO", String.valueOf(mostrando));

            }
        });

        // Añade filas a la tabla y añade los componentes
        table.add(flechaDerecha);
        table.add(img1);
        table.add(img2);
        table.add(img3);
        table.add(flechaIzquierda);


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        actualizarImagenes();

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

}
