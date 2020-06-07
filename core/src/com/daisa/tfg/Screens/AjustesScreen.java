package com.daisa.tfg.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.daisa.tfg.Constantes.ConstantesJuego;
import com.daisa.tfg.Principal.Juego;

public class AjustesScreen implements Screen {

    Juego juego;
    Stage stage;

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

        juego.reproducirMusica(juego.manager.getManagerJuego().get(ConstantesJuego.MUSICA_MENU, Music.class));

        stage = new Stage(juego.viewport);

        Table tabla = new Table();
        tabla.setFillParent(true);
        stage.addActor(tabla);

        tabla.setBackground(new TiledDrawable(juego.getFondoMenu()));

        titleLabel = new Label( "Preferencias", juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class) );
        titleLabel.setFontScale(4);
        volumeMusicLabel = new Label( "Volumen Musica", juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class) );
        volumeMusicLabel.setFontScale(4);
        volumeSoundLabel = new Label( "Volumen Sonido", juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class) );
        volumeSoundLabel.setFontScale(4);
        musicOnOffLabel = new Label( "Musica", juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class) );
        musicOnOffLabel.setFontScale(4);
        soundOnOffLabel = new Label( "Sonido", juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class) );
        soundOnOffLabel.setFontScale(4);

        final Slider volumeMusicSlider = new Slider(0f, 1f, 0.1f,false, juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class));
        volumeMusicSlider.scaleBy(1, 3  );
        volumeMusicSlider.setValue( juego.getPreferencias().getMusicVolume() );
        volumeMusicSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Slider slider = (Slider) actor;

                float value = slider.getValue();
                juego.getPreferencias().setMusicVolume( value );
                juego.manager.getManagerJuego().get(ConstantesJuego.MUSICA_MENU, Music.class).setVolume(juego.preferencias.getMusicVolume());
            }

        });

        Container<Slider> container = new Container<>(volumeMusicSlider);
        container.setTransform(true);
        container.size(400, 100);

        final CheckBox musicCheckbox = new CheckBox(null, juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class));
        musicCheckbox.setChecked( juego.getPreferencias().isMusicEnabled() );
        musicCheckbox.addListener( new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean enabled = musicCheckbox.isChecked();
                juego.getPreferencias().setMusicEnabled( enabled );
                if(!enabled)
                    juego.manager.getManagerJuego().get(ConstantesJuego.MUSICA_MENU, Music.class).stop();
                else
                    juego.manager.getManagerJuego().get(ConstantesJuego.MUSICA_MENU, Music.class).play();

            }
        });






        final Slider soundMusicSlider = new Slider(0f, 1f, 0.1f,false, juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class));
        soundMusicSlider.setValue( juego.getPreferencias().getSoundVolume() );
        soundMusicSlider.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                Slider slider = (Slider) actor;

                float value = slider.getValue();
                juego.getPreferencias().setSoundVolume( value );
            }
        });
        Container<Slider> container2 = new Container<>(soundMusicSlider);
        container2.setTransform(true);
        container2.size(400, 100);

        final CheckBox soundEffectsCheckbox = new CheckBox(null, juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class));
        soundEffectsCheckbox.setChecked( juego.getPreferencias().isSoundEffectsEnabled() );
        soundEffectsCheckbox.addListener( new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                juego.getPreferencias().setSoundEffectsEnabled( enabled );
            }
        });




        final TextButton backButton = new TextButton("Volver", juego.manager.managerJuego.get(ConstantesJuego.NOMBRE_JSON_SKIN, Skin.class));
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                juego.reproducirSonido(juego.manager.getManagerJuego().get(ConstantesJuego.SONIDO_PULSAR_BOTON, Sound.class));
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
