package psyware.model;

import javafx.beans.property.*;

public class Produto {
    private final IntegerProperty idProduto = new SimpleIntegerProperty();
    private final StringProperty nomeProduto = new SimpleStringProperty();
    private final StringProperty descricaoProduto = new SimpleStringProperty();
    private final IntegerProperty quantidadeEstoque = new SimpleIntegerProperty();
    private final DoubleProperty preco = new SimpleDoubleProperty();
    private final StringProperty categoria = new SimpleStringProperty();
    private final StringProperty imagem = new SimpleStringProperty();

    public Produto() {}

    public Produto(int id, String nome, String descricao, int estoque, double preco, String categoria, String imagem) {
        setIdProduto(id);
        setNomeProduto(nome);
        setDescricaoProduto(descricao);
        setQuantidadeEstoque(estoque);
        setPreco(preco);
        setCategoria(categoria);
        setImagem(imagem);
    }

    public int getIdProduto() { return idProduto.get(); }
    public IntegerProperty idProdutoProperty() { return idProduto; }
    public void setIdProduto(int id) { this.idProduto.set(id); }

    public String getNomeProduto() { return nomeProduto.get(); }
    public StringProperty nomeProdutoProperty() { return nomeProduto; }
    public void setNomeProduto(String nome) { this.nomeProduto.set(nome); }

    public String getDescricaoProduto() { return descricaoProduto.get(); }
    public StringProperty descricaoProdutoProperty() { return descricaoProduto; }
    public void setDescricaoProduto(String desc) { this.descricaoProduto.set(desc); }

    public int getQuantidadeEstoque() { return quantidadeEstoque.get(); }
    public IntegerProperty quantidadeEstoqueProperty() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int qtd) { this.quantidadeEstoque.set(qtd); }

    public double getPreco() { return preco.get(); }
    public DoubleProperty precoProperty() { return preco; }
    public void setPreco(double p) { this.preco.set(p); }

    public String getCategoria() { return categoria.get(); }
    public StringProperty categoriaProperty() { return categoria; }
    public void setCategoria(String cat) { this.categoria.set(cat); }

    public String getImagem() { return imagem.get(); }
    public StringProperty imagemProperty() { return imagem; }
    public void setImagem(String img) { this.imagem.set(img); }
}