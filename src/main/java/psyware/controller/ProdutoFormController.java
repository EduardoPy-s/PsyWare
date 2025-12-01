package psyware.controller;

import psyware.dao.ProdutoDAO;
import psyware.model.Produto;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProdutoFormController {
    @FXML private TextField txtNome, txtQuantidade, txtPreco, txtCategoria, txtImagem;
    @FXML private TextArea txtDescricao;

    private Produto produto;
    private DashboardController dashboardController;
    private ProdutoDAO dao = new ProdutoDAO();

    public void setProduto(Produto p) {
        this.produto = p;
        if (p != null) {
            txtNome.setText(p.getNomeProduto());
            txtDescricao.setText(p.getDescricaoProduto());
            txtQuantidade.setText(String.valueOf(p.getQuantidadeEstoque()));
            txtPreco.setText(String.valueOf(p.getPreco()));
            txtCategoria.setText(p.getCategoria());
            txtImagem.setText(p.getImagem());
        }
    }

    public void setDashboardController(DashboardController controller) {
        this.dashboardController = controller;
    }

    @FXML private void salvar() {
        Produto p = produto != null ? produto : new Produto();
        p.setNomeProduto(txtNome.getText());
        p.setDescricaoProduto(txtDescricao.getText());
        p.setQuantidadeEstoque(Integer.parseInt(txtQuantidade.getText()));
        p.setPreco(Double.parseDouble(txtPreco.getText().replace(",", ".")));
        p.setCategoria(txtCategoria.getText());
        p.setImagem(txtImagem.getText());

        dao.salvar(p);
        if (dashboardController != null) dashboardController.atualizarTabela();
        fechar();
    }

    @FXML private void cancelar() {
        fechar();
    }

    private void fechar() {
        ((Stage) txtNome.getScene().getWindow()).close();
    }
}