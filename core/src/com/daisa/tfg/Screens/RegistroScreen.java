package com.daisa.tfg.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.daisa.tfg.principal.Juego;

public class RegistroScreen implements Screen {

    Juego juego;
    Stage stage;
    TextureRegion fondo;
    Label lbNombre;
    Label lbContra;
    Label lbContraRepe;
    TextField tfNombUsuario;
    TextField tfContraUsuario;
    TextField tfContraUsuarioRepe;

    String nombAlmacenado = "";
    String contraAlmacenada = "";
    String contraAlmacenadaRepe = "";

    TextButton btRegistrame;
    TextButton btVolver;

    boolean nombIntroducido = true;
    boolean contraIntroducida = true;
    boolean contraRepeIntroducida = true;

    public RegistroScreen(Juego juego) {
        this.juego = juego;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("DEBUG", "Ancho: " + Gdx.graphics.getWidth());
        Gdx.app.debug("DEBUG", "Alto: " + Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        stage = new Stage(juego.viewport);

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);

        //TODO cambiar imagen de fondo
        fondo = new TextureRegion(new Texture("fondo.jpg"));
        tabla.setBackground(new TiledDrawable(fondo));

        Image imgExclamacion = new Image(new Texture(Gdx.files.internal("Signos/signoExclamacion.png")));
        Image imgExclamacion2 = new Image(new Texture(Gdx.files.internal("Signos/signoExclamacion.png")));
        Image imgExclamacion3 = new Image(new Texture(Gdx.files.internal("Signos/signoExclamacion.png")));



        CharSequence nombUsuario = "Nombre:";
        lbNombre = new Label(nombUsuario, juego.manager.getEstiloLabel());
        lbNombre.setFontScale(2);

        CharSequence nombContra = "Contrasena";
        lbContra = new Label(nombContra, juego.manager.getEstiloLabel());
        lbContra.setFontScale(2);

        CharSequence nombContraRepe = "Repetir contrasena";
        lbContraRepe = new Label(nombContraRepe, juego.manager.getEstiloLabel());
        lbContraRepe.setFontScale(2);


        tfNombUsuario = new TextField(nombAlmacenado, juego.manager.getEstiloTextField());
        nombAlmacenado = "";

        tfNombUsuario.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Input.TextInputListener textInputListener = new Input.TextInputListener() {
                    @Override
                    public void input(String input) {
                        tfNombUsuario.setText(input);
                    }

                    @Override
                    public void canceled() {
                        tfNombUsuario.setText("");
                    }
                };
                Gdx.input.getTextInput(textInputListener, "Nombre: ", tfNombUsuario.getText(), "Introduce el nombre de usuario");
            }
        });

        tfContraUsuario = new TextField(contraAlmacenada, juego.manager.getEstiloTextField());
        contraAlmacenada = "";


        tfContraUsuario.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Input.TextInputListener textInputListener = new Input.TextInputListener() {
                    @Override
                    public void input(String input) {
                        tfContraUsuario.setText(input);
                    }

                    @Override
                    public void canceled() {
                        tfContraUsuario.setText("");
                    }
                };
                Gdx.input.getTextInput(textInputListener, "Contrasena: ", tfContraUsuario.getText(), "Introduce la contraseña");
            }
        });

        tfContraUsuarioRepe = new TextField(contraAlmacenadaRepe, juego.manager.getEstiloTextField());
        contraAlmacenadaRepe = "";


        tfContraUsuarioRepe.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Input.TextInputListener textInputListener = new Input.TextInputListener() {
                    @Override
                    public void input(String input) {
                        tfContraUsuarioRepe.setText(input);
                    }

                    @Override
                    public void canceled() {
                        tfContraUsuarioRepe.setText("");
                    }
                };
                Gdx.input.getTextInput(textInputListener, "Repita la contrasena: ", tfContraUsuarioRepe.getText(), "Introduce la contraseña de nuevo");
            }
        });

        btRegistrame = new TextButton("Registrame", juego.manager.getSkin());
        btRegistrame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nombIntroducido = !tfNombUsuario.getText().equals("");
                contraIntroducida = !tfContraUsuario.getText().equals("");
                contraRepeIntroducida = !tfContraUsuarioRepe.getText().equals("");

                if (!(nombIntroducido && contraIntroducida && contraRepeIntroducida)){
                    nombAlmacenado = tfNombUsuario.getText();
                    contraAlmacenada = tfContraUsuario.getText();
                    contraAlmacenadaRepe = tfContraUsuarioRepe.getText();
                    show();
                }

                if (!tfContraUsuario.getText().equals(tfContraUsuarioRepe.getText())){
                    juego.crearToast("Las contraseñas no coinciden");
                    nombAlmacenado = tfNombUsuario.getText();
                    contraAlmacenada = tfContraUsuario.getText();
                    contraAlmacenadaRepe = tfContraUsuarioRepe.getText();
                    show();
                }else{
                    juego.usuarioYaExisteLIBGDX(tfNombUsuario.getText(), tfContraUsuario.getText());
                }
            }
        });

        btVolver = new TextButton("Volver", juego.manager.getSkin());
        btVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new LoginScreen(juego));
            }
        });

        tabla.row().padBottom(20).padTop(20);
        tabla.add(lbNombre).height(100);
        tabla.add(tfNombUsuario).width(920).height(150).colspan(2);
        if (!nombIntroducido) {
            tabla.add(imgExclamacion).width(150).height(152);
        } else {
            tabla.add();
        }

        tabla.row().padBottom(20).padTop(20);
        tabla.add(lbContra).height(100);
        tabla.add(tfContraUsuario).width(920).height(150).colspan(2);
        if (!contraIntroducida) {
            tabla.add(imgExclamacion2).width(150).height(152);
        } else {
            tabla.add();
        }

        tabla.row().padBottom(20).padTop(20);
        tabla.add(lbContraRepe).height(100);
        tabla.add(tfContraUsuarioRepe).width(920).height(150).colspan(2);
        if (!contraRepeIntroducida) {
            tabla.add(imgExclamacion3).width(150).height(152);
        } else {
            tabla.add();
        }

        tabla.row().padTop(120);
        tabla.add(btRegistrame).width(700).height(120);
        tabla.add().width(200);
        tabla.add(btVolver).width(700).height(120);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        juego.viewport.update(width, height);
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
