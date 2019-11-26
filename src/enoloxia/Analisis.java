/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enoloxia;

/**
 *
 * @author oracle
 */
public class Analisis {
    public String codigoa;
    public int acidez;
    public int grao;
    public int taninos;
    public String tipo;
    public int cantidade;
    public String dni;

    public Analisis() {
    }

    public Analisis(String codigoa, int acidez, int grao, int taninos, String tipo, int cantidade, String dni) {
        this.codigoa = codigoa;
        this.acidez = acidez;
        this.grao = grao;
        this.taninos = taninos;
        this.tipo = tipo;
        this.cantidade = cantidade;
        this.dni = dni;
    }

    

    public String getCodigoa() {
        return codigoa;
    }

    public void setCodigoa(String codigoa) {
        this.codigoa = codigoa;
    }

    public int getAcidez() {
        return acidez;
    }

    public void setAcidez(int acidez) {
        this.acidez = acidez;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidade() {
        return cantidade;
    }

    public void setCantidade(int cantidade) {
        this.cantidade = cantidade;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Override
    public String toString() {
        return "\ncodigoa: " + codigoa + "\tacidez: " + acidez + "\ttipo: " + tipo + "\tcantidade: " + cantidade + "\tdni: " + dni;
    }
    
    
    
    
    
    
}
