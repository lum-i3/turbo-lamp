package com.example.gestionusuarios.modelo.dao;

import com.example.gestionusuarios.modelo.Usuario;
import oracle.ucp.proxy.annotation.Pre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.gestionusuarios.utils.OracleDatabaseConnectionManager.getConnection;

public class UsuarioDao {
    //Metodo para obtener el próximo ID
    public int obtenerProximoId() {
        String query = "SELECT NVL(MAX(IDUSUARIO), 0) + 1 AS NEXT_ID FROM USUARIOS";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("NEXT_ID");
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; //Si hay error, retorna 1 como valor por defecto
    }

    //Funcion de crear (C) del CRUD
    public boolean createUsuario(Usuario u) {
        String query = "INSERT INTO USUARIOS (IDUSUARIO, NOMBRE, CORREO, FECHANACIMIENTO, CONTRASENIA, ESTADO) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, u.getIdUsuario());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setDate(4, u.getFechaNacimiento());
            ps.setString(5, u.getContrasenia());
            ps.setInt(6, u.getEstado() ? 1 : 0); // Convertir Boolean a NUMBER
            int resultado = ps.executeUpdate();
            conn.close();
            return resultado > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Funcion de lectura (R) del CRUD
    public List<Usuario> readUsuarios(){
        String query = "SELECT IDUSUARIO, NOMBRE, CORREO, FECHANACIMIENTO, CONTRASENIA, ESTADO FROM USUARIOS ORDER BY NOMBRE ASC";
        List<Usuario> lista = new ArrayList<>();
        try{
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("IDUSUARIO"));
                u.setNombre(rs.getString("NOMBRE"));
                u.setCorreo(rs.getString("CORREO"));
                u.setFechaNacimiento(rs.getDate("FECHANACIMIENTO"));
                u.setContrasenia(rs.getString("CONTRASENIA"));
                int estadoNum = rs.getInt("ESTADO");
                u.setEstado(estadoNum == 1);
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
    public boolean updateUsuario(Usuario u){
        String query = "UPDATE USUARIOS SET NOMBRE=?, CORREO=?, FECHANACIMIENTO=?, CONTRASENIA=?, ESTADO=? WHERE IDUSUARIO=?";
        try{
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setDate(3, u.getFechaNacimiento());
            ps.setString(4, u.getContrasenia());
            ps.setInt(5, u.getEstado() ? 1 : 0);
            ps.setInt(6, u.getIdUsuario());
            int resultado = ps.executeUpdate();
            conn.close();
            return resultado > 0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Función de eliminar (D) del CRUD - CORREGIDA
    public boolean deleteUsuario(int IdUsuario){
        String query = "DELETE FROM USUARIOS WHERE IDUSUARIO=?";
        try{
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, IdUsuario);
            int filasAfectadas = ps.executeUpdate();
            conn.close();
            return filasAfectadas > 0;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}
