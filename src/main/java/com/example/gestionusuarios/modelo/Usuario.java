package com.example.gestionusuarios.modelo;

import java.sql.Date;

public class Usuario {
    private int IdUsuario;
    private String nombre;
    private String correo;
    private Date fechaNacimiento;
    private String contrasenia;
    private Boolean estado;
    //Constructores
    public Usuario() {}
    public Usuario(int idUsuario, String nombre, String correo, Date fechaNacimiento, String contrasenia, Boolean estado) {
        IdUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.contrasenia = contrasenia;
        this.estado = estado;
    }
    //ID
    public int getIdUsuario() {
        return IdUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        IdUsuario = idUsuario;
    }
    //Nombre
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    //Correo
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    //Fecha de nacimiento
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    //Contraseña
    public String getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
    //Estado
    public Boolean getEstado() {
        return estado;
    }
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
