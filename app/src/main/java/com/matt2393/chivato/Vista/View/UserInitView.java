package com.matt2393.chivato.Vista.View;

import com.matt2393.chivato.Modelo.RolUsuario;
import com.matt2393.chivato.Modelo.Usuario;

import java.util.ArrayList;

public interface UserInitView {

    void loadRol(ArrayList<RolUsuario> roles);
    void loadUser(Usuario user);

    void errorRoles(String mess);
    void errorUser(String mess);
}
