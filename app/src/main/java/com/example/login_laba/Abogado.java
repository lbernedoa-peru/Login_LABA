package com.example.login_laba;

public class Abogado {

    int id;
    String nombre;
    String especialidad;
    String telefono;
    String correo;

    public Abogado(String nombre, String especialidad, String telefono, String correo) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.correo = correo;
    }

    public int getId()           { return id; }
    public String getNombre()    { return nombre; }
    public String getEspecialidad() { return especialidad; }
    public String getTelefono()  { return telefono; }
    public String getCorreo()    { return correo; }
}
