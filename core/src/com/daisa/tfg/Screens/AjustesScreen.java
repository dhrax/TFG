package com.daisa.tfg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.daisa.tfg.principal.Juego;

public class AjustesScreen implements Screen {

    Juego juego;
    Stage stage;
    Skin skin;

    private Label titleLabel;
    private Label volumeMusicLabel;
    private Label volumeSoundLabel;
    private Label musicOnOffLabel;
    private Label soundOnOffLabel;

    public AjustesScreen(Juego juego) {
        this.juego = juego;
    }

    @Override
    public void show() {

        stage = new Stage(juego.viewport);

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);

        juego.manager.cargaSkin();
        juego.manager.managerJuego.finishLoading();

        skin = juego.manager.managerJuego.get("skin/glassy-ui.json");

        titleLabel = new Label( "Preferencias", skin );
        titleLabel.setFontScale(4);
        volumeMusicLabel = new Label( "Volumen Musica", skin );
        volumeMusicLabel.setFontScale(4);
        volumeSoundLabel = new Label( "Volumen Sonido", skin );
        volumeSoundLabel.setFontScale(4);
        musicOnOffLabel = new Label( "Musica", skin );
        musicOnOffLabel.setFontScale(4);
        soundOnOffLabel = new Label( "Sonido", skin );
        soundOnOffLabel.setFontScale(4);

        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.1f,false, skin);
        volumeMusicSlider.scaleBy(1, 3  );
        volumeMusicSlider.setValue( juego.getPreferencias().getMusicVolume() );
        volumeMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                juego.getPreferencias().setMusicVolume( volumeMusicSlider.getValue() );
                return false;
            }
        });

        Container<Slider> container = new Container<>(volumeMusicSlider);
        container.setTransform(true);
        container.size(400, 100);

        final Slider soundMusicSlider = new Slider(0f, 1f, 0.1f,false, skin);
        volumeMusicSlider.setValue( juego.getPreferencias().getSoundVolume() );
        volumeMusicSlider.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                juego.getPreferencias().setSoundVolume( soundMusicSlider.getValue() );
                return false;
            }
        });

        Container<Slider> container2 = new Container<>(soundMusicSlider);
        container2.setTransform(true);
        container2.size(400, 100);

        final CheckBox musicCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked( juego.getPreferencias().isMusicEnabled() );
        musicCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                juego.getPreferencias().setMusicEnabled( enabled );
                return false;
            }
        });

        final CheckBox soundEffectsCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked( juego.getPreferencias().isSoundEffectsEnabled() );
        musicCheckbox.addListener( new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                juego.getPreferencias().setSoundEffectsEnabled( enabled );
                return false;
            }
        });

        final TextButton backButton = new TextButton("Volver", skin); // the extra argument here "small" is used to set the button to the smaller version instead of the big default version
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                juego.setScreen(new MenuPrincipalScreen(juego));
            }
        });

        tabla.add(titleLabel).colspan(2);
        tabla.row().pad(10,0,0,10).width(800).height(100);
        tabla.add(volumeMusicLabel);
        tabla.add(container);
        tabla.row().pad(10,0,0,10).width(800).height(100);
        tabla.add(musicOnOffLabel);
        tabla.add(musicCheckbox);
        tabla.row().pad(10,0,0,10).width(800).height(100);
        tabla.add(volumeSoundLabel).left();
        tabla.add(container2);
        tabla.row().pad(10,0,0,10).width(800).height(100);
        tabla.add(soundOnOffLabel).left();
        tabla.add(soundEffectsCheckbox);
        tabla.row().pad(10,0,0,10).width(800).height(100);
        tabla.add(backButton).colspan(2);

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
