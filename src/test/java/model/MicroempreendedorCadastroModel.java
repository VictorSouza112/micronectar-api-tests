package model;

import lombok.Data;

// @Data do Lombok gera Getters, Setters, toString, etc. automaticamente.
@Data
public class MicroempreendedorCadastroModel {
    private String nome;
    private String email;
    private String senha;
    private String nrCnpj;
    private String nmFundador;
    private String dsCategoria;
    private String dsHistoria;
    private String dsHistoriaFundador;
    private String pitchUrl;
}