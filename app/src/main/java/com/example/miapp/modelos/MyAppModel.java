package com.example.miapp.modelos;

public class MyAppModel {

    // Declaro los campos
    private String nombre, precio, stock, img;


    // Campo nombre
    public String getNombre() {
        return nombre;
    }
    // Seteo el nombre
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    // Campo precio
    public String getPrecio() {
        return precio;
    }
    // Seteo el precio
    public void setPrecio(String precio) {
        this.precio = precio;
    }


    // Campo stock
    public String getStock() {
        return stock;
    }
    // Seteo el Stock
    public void setStock(String stock) {
        this.stock = stock;
    }


    // Campo imagen
    public String getImagen(){
        return img;
    }
    // Seteo la imagen
    public void setImagen(String img){
        this.img = img;
    }
    
}
