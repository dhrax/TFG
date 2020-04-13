package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class MenuPrincipalScreen implements Screen {

    Juego juego;
    Stage stage;

    Skin skin;

    TextButton btJugar;
    TextButton btRanking;
    TextButton btAjustes;

    public MenuPrincipalScreen(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        stage = new Stage();

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);

        skin = juego.manager.managerJuego.get("skin/glassy-ui.json");

        Image imgLogo = new Image(new Texture(Gdx.files.internal("badlogic.jpg")));

        btJugar = new TextButton("Jugar", skin);
        btJugar.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //juego.setScreen(new ElegirPersonaje(juego));
            }
        });


        btRanking = new TextButton("Ranking", skin);
        btRanking.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {


            }
        });

        btAjustes = new TextButton("Ajustes", skin);
        btAjustes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new AjustesScreen(juego));
            }
        });


        tabla.row().padBottom(30).width(700).height(120);
        tabla.add();
        tabla.add(btJugar).width(700).height(120);

        tabla.row().padBottom(30).width(700).height(120);
        tabla.add(imgLogo).width(400).height(400);
        tabla.add(btRanking);

        tabla.row().padBottom(30).width(700).height(120);
        tabla.add();
        tabla.add(btAjustes);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
    }
}
