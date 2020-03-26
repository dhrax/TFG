package com.daisa.tfg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

//TODO buscar un tipo de fuente
//TODO Cuando se tenga el tipo de fuente y la skin, cambiar VisTextField por TextField
//TODO modularizar
public class LoginScreen implements Screen {

	Juego juego;
	Stage stage;

	public LoginScreen (Juego juego) {
		this.juego = juego;
	}

	@Override
	public void show() {

		if (!VisUI.isLoaded())
			VisUI.load();

		stage = new Stage();

		VisTable tabla = new VisTable(true);
		tabla.setFillParent(true);
		stage.addActor(tabla);

		Image imgLogo = new Image(new Texture(Gdx.files.internal("badlogic.jpg")));

		//El font scale no debría hacer falta una vez que se haya escogido el tipo de letra
		VisLabel lbNombreJuego = new VisLabel("NOMBRE_JUEGO");
		lbNombreJuego.setFontScale(5, 5);

		//TODO hacer mas grande el tamaño de la letro dentro de los VisTextField
		final VisTextField tfNombreUsuario  = new VisTextField();

		/*
		Cuando se pulse el VisTextField, aparece una ventana que permite introducir los datos necesarios.
		Este WorkAround se ha hecho porque, por defecto, si se pulsa en el VisTextField, por la posicion en la que estos se encuentran en esta UI,
		el teclado tapa el VisTextField por lo que no se ve lo que se esta escribiendo. De esta forma se arregla ese problema.
		El inconveniente es que hay que pulsar demasiadas veces para rellenar los datos.

		TODO investigar hacer hacer que la camara permita que el VisTextField se vea:
			https://stackoverflow.com/questions/32788428/libgdx-textfield-show-keyboard-input-field
		 */
		tfNombreUsuario.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Input.TextInputListener textInputListener = new Input.TextInputListener()
				{
					@Override
					public void input(String input)
					{
						tfNombreUsuario.setText(input);
					}

					@Override
					public void canceled()
					{
						tfNombreUsuario.setText("");
					}
				};
				Gdx.input.getTextInput(textInputListener, "Usuario: ", "", "Nombre de Usuario");
			}
		});

		final VisTextField tfContraseñaUsuario  = new VisTextField();
		tfContraseñaUsuario.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Input.TextInputListener textInputListener = new Input.TextInputListener()
				{
					@Override
					public void input(String input)
					{
						tfContraseñaUsuario.setText(input);
					}

					@Override
					public void canceled()
					{
						tfContraseñaUsuario.setText("");
					}
				};
				Gdx.input.getTextInput(textInputListener, "Usuario: ", "", "Nombre de Usuario");
			}
		});

		VisLabel lbOlvidarContraseña = new VisLabel("Olvidé mi contraseña");
		lbOlvidarContraseña.setFontScale(2, 2);
		lbOlvidarContraseña.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {

				juego.setScreen(new ResetContrasena(juego));
				VisUI.dispose();

			}
		});


		VisTextButton btRegistro = new VisTextButton("Registrarse");
		btRegistro.getLabel().setFontScale(4, 4);
		btRegistro.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {

				juego.setScreen(new RegistroScreen(juego));
				VisUI.dispose();

			}
		});

		VisTextButton btInicioSesion = new VisTextButton("Iniciar de Sesion");
		btInicioSesion.getLabel().setFontScale(4, 4);
		btInicioSesion.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {

				//TODO logica del login
				juego.setScreen(new MenuPrincipalScreen(juego));
				VisUI.dispose();

			}
		});


		tabla.row().padBottom(100).padTop(30);
		tabla.add(imgLogo).width(200).height(200);
		tabla.add(lbNombreJuego).colspan(2).left();
		tabla.add();

		tabla.row().padBottom(50).padTop(50);

		tabla.add(tfNombreUsuario).width(1200).height(150).colspan(3);
		tabla.add();
		tabla.add();

		tabla.row().padBottom(20).padTop(20);
		tabla.add(tfContraseñaUsuario).width(1200).height(150).colspan(3);
		tabla.add();
		tabla.add();

		tabla.row().padBottom(20).padTop(20);
		tabla.add(lbOlvidarContraseña).height(80).colspan(3);
		tabla.add();
		tabla.add();

		tabla.row().padBottom(30);
		tabla.add(btRegistro).width(700).height(120);
		tabla.add().width(200);
		tabla.add(btInicioSesion).width(700).height(120);


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
	public void dispose () {

	}
}
