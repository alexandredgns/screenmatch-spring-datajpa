package br.com.alura.screenmatch_spring_datajpa.service;

public interface IDataConversion {

    <T> T getData(String json, Class<T> tClass );

}
