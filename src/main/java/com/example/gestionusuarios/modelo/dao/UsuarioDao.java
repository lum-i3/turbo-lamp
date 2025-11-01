package com.example.gestionusuarios.modelo.dao;

import com.example.gestionusuarios.modelo.Usuario;
import oracle.ucp.proxy.annotation.Pre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.gestionusuarios.utils.OracleDatabaseConnectionManager.getConnection;

public class UsuarioDao {
    //Funcion de crear usuarios (C) del CRUD
    public boolean createUsuario(Usuario u){
        //Obtener la conexion
        //Preparar el statement
        String query = "INSERT INTO USUARIOS (IDUSUARIO, NOMBRE, CORREO, FECHANACIMIENTO, CONTRASENIA, ESTADO) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, u.getIdUsuario());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setDate(4, u.getFechaNacimiento());
            ps.setString(5, u.getContrasenia());
            ps.setBoolean(6, u.getEstado());
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                conn.close();
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    //Funcion de Lectura (R) del CRUD
    public List<Usuario> readUsuarios(){
        String query = "SELECT NOMBRE, CORREO, FECHANACIMIENTO, ESTADO FROM USUARIOS ORDER BY NOMBRE ASC";
        List<Usuario> lista = new ArrayList<>();
        try{
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Usuario u = new Usuario();
                u.setNombre(rs.getString("NOMBRE"));
                u.setCorreo(rs.getString("CORREO"));
                u.setFechaNacimiento(rs.getDate("FECHANACIMIENTO"));
                u.setEstado(rs.getBoolean("ESTADO"));
                lista.add(u);
            }
            rs.close();
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return lista;
    }

    //Funcion de actualizar (U) del CRUD
    public boolean updateUsuario(int IdUsuario, Usuario u){
        //Obtener la conexion
        //Preparar el sql statement
        String query = "UPDATE USUARIOS SET NOMBRE=?, CORREO=?, FECHANACIMIENTO=?, CONTRASENIA=?, ESTADO=? WHERE IDUSUARIO=?";
        try{
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setDate(3, u.getFechaNacimiento());
            ps.setString(4, u.getContrasenia());
            ps.setBoolean(5, u.getEstado());
            ps.setInt(6, u.getIdUsuario()); //Este es el WHERE IDUSUARIO = ?
            int resultado = ps.executeUpdate();
            if (resultado > 0) {
                conn.close();
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    //Funcion de eliminar (D) del CRUD
    public boolean deleteUsuario(int IdUsuario){
        String query = "DELETE FROM USUARIOS WHERE IDUSUARIO=?";
        boolean seBorro = false;
        try{
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, IdUsuario);
            if(ps.executeUpdate()>0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return seBorro;
    }
}
