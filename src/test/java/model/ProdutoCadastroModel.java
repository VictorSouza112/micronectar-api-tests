package model;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoCadastroModel {
    private String nmProduto;
    private BigDecimal vlProduto;
    private Integer qtProduto;
    private String dsProduto;
    private String fotoUrl;
}