package org.alopsalv.novabank.model;

/*
    Clase que implementa el patrón de diseño Builder para la clase Cliente.
    Permite la creación de objetos Cliente de forma fluida y legible, facilitando el desarrollo de tests.
 */
public class ClienteBuilder {
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;

    public ClienteBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ClienteBuilder apellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public ClienteBuilder dni(String dni) {
        this.dni = dni;
        return this;
    }

    public ClienteBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ClienteBuilder telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public Cliente build() {

        return new Cliente(this.nombre, this.apellidos, this.dni, this.email, this.telefono);
    }
}