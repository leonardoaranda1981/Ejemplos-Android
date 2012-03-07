package com.anon.xml;

public class GeoPunto {
	private double latitud;
	private double longitud;
	private String tiempo;
	private String nombre;
	
	public void setLatitud(double lat){
		this.latitud = lat;
	
	}
	public void setLongitud(double lon){
		this.latitud = lon;
	
	}
	public void setTiempo(String t){
		this.tiempo = t;
	
	}
	public void setNombre(String n){
		this.nombre = n;
	}
	public double[] getCoords(){
		double [] coordenadas = {this.latitud,this.longitud};
		return coordenadas;
	}
	public String getName(){
		return nombre;
	}
	public String getTime(){
		return tiempo;
	}
	
}
