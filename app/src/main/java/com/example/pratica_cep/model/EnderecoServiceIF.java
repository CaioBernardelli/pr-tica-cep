package com.example.pratica_cep.model;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

// Interface que define as chamadas de API para buscar informações do CEP
public interface EnderecoServiceIF {
    // O método buscarEndereco usa a anotação @GET para realizar a requisição GET ao endpoint da API
    @GET("/ws/{cep}/json/")
    Call<Endereco> buscarEndereco(@Path("cep") String cep);
}
