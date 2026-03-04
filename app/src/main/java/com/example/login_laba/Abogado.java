package com.example.login_laba;

public class Abogado {

    int id;
    String dni;
    String nombre;
    String especialidad;
    String telefono;
    String correo;
    String biografia;
    byte[] imagen;

    public Abogado(String dni, String nombre, String especialidad,
                   String telefono, String correo, String biografia, byte[] imagen) {
        this.dni = dni;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.correo = correo;
        this.biografia = biografia;
        this.imagen = imagen;
    }

    public int getId()              { return id; }
    public String getDni()          { return dni; }
    public String getNombre()       { return nombre; }
    public String getEspecialidad() { return especialidad; }
    public String getTelefono()     { return telefono; }
    public String getCorreo()       { return correo; }
    public String getBiografia()    { return biografia; }
    public byte[] getImagen()       { return imagen; }
}