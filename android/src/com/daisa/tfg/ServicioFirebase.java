package com.daisa.tfg;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.daisa.tfg.constantes.ConstantesBluetooth;
import com.daisa.tfg.principal.Juego;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ServicioFirebase implements Juego.FirebaseCallBack {

    FirebaseFirestore db;
    Activity activity;
    Juego juego;
    Handler handler;
	AndroidLauncher androidLauncher;

    public ServicioFirebase(Activity activity, Juego juego, Handler handler, AndroidLauncher androidLauncher) {
        this.activity = activity;
        this.juego = juego;
        this.handler = handler;
        db = FirebaseFirestore.getInstance();
        juego.setFirebaseCallBack(this);
		this.androidLauncher = androidLauncher;
    }

    @Override
    public void comprobacionUsuario(final String nombreUsuario, final String contrasena) {
        db.collection("usuarios")
                .whereEqualTo("nombre", nombreUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() >= 1) {
                                comprobarUsuarioContrasena(nombreUsuario, contrasena);
                            } else {
                                pintarToast("Usuario no encontrado");
                            }
                        } else {
                            Log.d("DEBUG", "[ERROR] Tarea comprobacionUsuario no finalizada");
                            Log.d("DEBUG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void comprobarUsuarioContrasena(final String nombreUsuario, String contrasena) {
        db.collection("usuarios")
                .whereEqualTo("nombre", nombreUsuario)
                .whereEqualTo("contrasena", contrasena)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() >= 1) {
                                juego.irAMenuPrincipal();
                                juego.setNombreUsuario(nombreUsuario);
                            } else {
                                pintarToast("Contraseña incorrecta");
                                //todo llamar al show de LoginScreen
                            }
                        } else {
                            Log.d("DEBUG", "[ERROR] Tarea comprobarUsuarioContrasena no finalizada");
                            Log.d("DEBUG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void pintarToast(String mensaje) {
        Message msg = handler.obtainMessage(ConstantesBluetooth.MENSAJE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("toast", mensaje);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }


    @Override
    public void usuarioYaExiste(final String nombreUsuario, final String contrasena) {

        db.collection("usuarios")
                .whereEqualTo("nombre", nombreUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() >= 1) {
                                pintarToast("El nombre de usuario ya existe");
                            } else {
                                if (controlesNombreUsuario(nombreUsuario) && !controlesContrasena(contrasena)) {
                                    registrarUsuario(nombreUsuario, contrasena);
                                }
                            }
                        } else {
                            Log.d("DEBUG", "[ERROR] Tarea usuarioYaExiste no finalizada");
                            Log.d("DEBUG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private boolean controlesNombreUsuario(String nombreUsuario) {
        boolean correcto = true;
        if (nombreUsuario.length() < 5 || nombreUsuario.length() > 15) {
            correcto = false;
            pintarToast("El nombre de usuario tiene que tener entre 5 y 15 caracteres");
        } else if (nombreUsuario.indexOf("@") > 0 || nombreUsuario.indexOf("$") > 0 || nombreUsuario.indexOf("€") > 0 || nombreUsuario.indexOf("&") > 0) {
            correcto = false;
            pintarToast("El nombre de usuario no puede contener ninguno de los siguientes caracteres especiales (@, $, €, &)");
        } else if (nombreUsuario.toUpperCase().equals(nombreUsuario)) {
            correcto = true;
            pintarToast("La contraseña tiene que contener al menos una minúscula");
        } else if (nombreUsuario.toLowerCase().equals(nombreUsuario)) {
            correcto = true;
            pintarToast("La contraseña tiene que contener al menos una mayúscula");
        }

        return correcto;
    }

    private boolean controlesContrasena(String contrasena) {
        boolean hayError = false;
        if (contrasena.length() < 5 || contrasena.length() > 10) {
            hayError = true;
            pintarToast("La contraseña tiene que tener entre 5 y 10 caracteres");
        } else if (contrasena.indexOf("@") > 0 || contrasena.indexOf("$") > 0 || contrasena.indexOf("€") > 0 || contrasena.indexOf("&") > 0) {
            hayError = true;
            pintarToast("La contraseña no puede contener ninguno de los siguientes caracteres especiales (@, $, €, &)");
        } else if (contrasena.toUpperCase().equals(contrasena)) {
            hayError = true;
            pintarToast("La contraseña tiene que contener al menos una minúscula");
        } else if (contrasena.toLowerCase().equals(contrasena)) {
            hayError = true;
            pintarToast("La contraseña tiene que contener al menos una mayúscula");
        } else if (androidLauncher.utilAndroid.hayNumerosEnString(contrasena)) {
            hayError = true;
            pintarToast("La contraseña tiene que contener al menos un número");
        }

        return hayError;
    }

    private void registrarUsuario(final String nombreUsuario, String contrasena) {

        Map<String, Object> usuarioNuevo = new HashMap<>();
        usuarioNuevo.put("nombre", nombreUsuario);
        usuarioNuevo.put("contrasena", contrasena);

        db.collection("usuarios")
                .add(usuarioNuevo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DEBUG", "Se ha añadido el usuario correctamente con el ID: " + documentReference.getId());
                        juego.irAMenuPrincipal();
                        juego.setNombreUsuario(nombreUsuario);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG", "[ERROR] Al añadir el nuevo usuario", e);
                    }
                });
    }
}
