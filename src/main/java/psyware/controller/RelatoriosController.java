package psyware.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import psyware.dao.Conexao;
import psyware.dao.ProdutoDAO;
import psyware.model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class RelatoriosController {

    @FXML private Label lblValorTotal;
    @FXML private Label lblTotalProdutos;
    @FXML private Label lblBaixoEstoque;
    @FXML private PieChart graficoPizza;

    private final ProdutoDAO dao = new ProdutoDAO();

    @FXML
    private void initialize() {
        carregarResumo();
        carregarGrafico();
    }

    private void carregarResumo() {
        try (Connection conn = Conexao.getConnection()) {

            String sqlValor = "SELECT SUM(preco * quantidade_estoque) as total FROM produtos";
            PreparedStatement ps = conn.prepareStatement(sqlValor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double total = rs.getDouble("total");
                lblValorTotal.setText(String.format("R$ %.2f", total));
            }

            String sqlBaixo = "SELECT COUNT(*) as qtd FROM produtos WHERE quantidade_estoque <= 10";
            ps = conn.prepareStatement(sqlBaixo);
            rs = ps.executeQuery();
            if (rs.next()) {
                lblBaixoEstoque.setText(rs.getInt("qtd") + "");
            }

            lblTotalProdutos.setText(dao.listarTodos().size() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarGrafico() {
        List<Produto> lista = dao.listarTodos();

        long mouse = lista.stream().filter(p -> p.getCategoria().equalsIgnoreCase("mouse")).count();
        long teclado = lista.stream().filter(p -> p.getCategoria().equalsIgnoreCase("teclado")).count();
        long monitor = lista.stream().filter(p -> p.getCategoria().equalsIgnoreCase("monitor")).count();
        long headset = lista.stream().filter(p -> p.getCategoria().equalsIgnoreCase("headset")).count();
        long outro = lista.stream().filter(p -> p.getCategoria().equalsIgnoreCase("outro")).count();

        graficoPizza.setData(FXCollections.observableArrayList(
                new PieChart.Data("Mouse", mouse),
                new PieChart.Data("Teclado", teclado),
                new PieChart.Data("Monitor", monitor),
                new PieChart.Data("Headset", headset),
                new PieChart.Data("Outro", outro)
        ));
    }

    @FXML
    private void abrirProdutosBaixa() {

        Stage stage = new Stage();
        stage.setTitle("Produtos com Estoque Baixo");

        TableView<Produto> tabela = new TableView<>();

        TableColumn<Produto, String> colNome = new TableColumn<>("Produto");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));

        TableColumn<Produto, Integer> colQtd = new TableColumn<>("Estoque");
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));

        tabela.getColumns().addAll(colNome, colQtd);

        List<Produto> baixos = dao.listarTodos();
        baixos.removeIf(p -> p.getQuantidadeEstoque() > 10);

        tabela.setItems(FXCollections.observableArrayList(baixos));

        stage.setScene(new Scene(tabela, 400, 300));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    @FXML
    private void fechar() {
        ((Stage) lblValorTotal.getScene().getWindow()).close();
    }
}
