package psyware.controller;

import psyware.dao.ProdutoDAO;
import psyware.model.Produto;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.InputStream;
import java.util.List;

public class DashboardController {

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TextField txtBusca;
    @FXML private ComboBox<String> cbCategoria;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @FXML
    private void initialize() {
        cbCategoria.getItems().addAll("Todos", "Mouse", "Teclado", "Monitor", "Headset", "Outro");
        cbCategoria.setValue("Todos"); // Valor padrão

        // Filtro automático ao mudar categoria ou digitar busca
        cbCategoria.valueProperty().addListener((obs, oldV, newV) -> buscar());
        txtBusca.textProperty().addListener((obs, oldV, newV) -> buscar());

        configurarColunas();
        carregarProdutos();
    }

    private void configurarColunas() {
        TableColumn<Produto, Void> colAcao = new TableColumn<>("Ação");
        colAcao.setPrefWidth(100);
        colAcao.setCellFactory(param -> new TableCell<Produto, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            {
                btnEditar.setStyle("-fx-background-color:#4e73df;-fx-text-fill:white;-fx-background-radius:4;-fx-padding:5 10;");
                btnExcluir.setStyle("-fx-background-color:#e74a3b;-fx-text-fill:white;-fx-background-radius:4;-fx-padding:5 10;");
                btnEditar.setOnAction(e -> {
                    Produto p = getTableView().getItems().get(getIndex());
                    if (p != null) editar(p);
                });
                btnExcluir.setOnAction(e -> {
                    Produto p = getTableView().getItems().get(getIndex());
                    if (p != null) excluir(p);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(8, btnEditar, btnExcluir));
                }
            }
        });

        TableColumn<Produto, ImageView> colImagem = new TableColumn<>("Imagem");
        colImagem.setPrefWidth(80);
        colImagem.setCellValueFactory(cellData -> {
            Produto p = cellData.getValue();
            ImageView iv = new ImageView();
            iv.setFitWidth(60);
            iv.setFitHeight(60);
            iv.setPreserveRatio(true);
            String imagemNome = p.getImagem();
            InputStream is = null;
            if (imagemNome != null && !imagemNome.trim().isEmpty()) {
                is = getClass().getResourceAsStream("/images/" + imagemNome.trim());
            }
            if (is == null) {
                is = getClass().getResourceAsStream("/images/placeholder.jpg");
            }
            if (is != null) {
                iv.setImage(new Image(is));
            }
            return new SimpleObjectProperty<>(iv);
        });

        TableColumn<Produto, String> colNome = new TableColumn<>("Produto");
        colNome.setPrefWidth(220);
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));

        TableColumn<Produto, Integer> colEstoque = new TableColumn<>("Estoque Atual");
        colEstoque.setPrefWidth(100);
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));

        TableColumn<Produto, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setPrefWidth(100);
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colPreco.setCellFactory(tc -> new TableCell<Produto, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("R$ %.2f", price));
            }
        });

        TableColumn<Produto, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setPrefWidth(100);
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Produto, Integer> colCodigo = new TableColumn<>("Código");
        colCodigo.setPrefWidth(80);
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("idProduto"));

        tabelaProdutos.getColumns().setAll(colAcao, colImagem, colNome, colEstoque, colPreco, colCategoria, colCodigo);
    }

    private void carregarProdutos() {
        tabelaProdutos.setItems(FXCollections.observableArrayList(produtoDAO.listarTodos()));
    }

    @FXML
    private void buscar() {
        String termo = txtBusca.getText().trim().toLowerCase();
        String categoriaSelecionada = cbCategoria.getValue();

        List<Produto> lista = produtoDAO.listarTodos();

        
        if (!termo.isEmpty()) {
            lista.removeIf(p -> !p.getNomeProduto().toLowerCase().contains(termo));
        }


        if (categoriaSelecionada != null && !categoriaSelecionada.equals("Todos")) {
            lista.removeIf(p -> !p.getCategoria().equalsIgnoreCase(categoriaSelecionada));
        }

        tabelaProdutos.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML private void novoProduto() throws Exception {
        abrirFormulario(null);
    }

    private void editar(Produto p) {
        try {
            abrirFormulario(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void excluir(Produto p) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Excluir \"" + p.getNomeProduto() + "\"?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                produtoDAO.excluir(p.getIdProduto());
                buscar();
            }
        });
    }

    private void abrirFormulario(Produto produto) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProdutoForm.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loader.load()));
        ProdutoFormController controller = loader.getController();
        controller.setProduto(produto);
        controller.setDashboardController(this);
        stage.setTitle(produto == null ? "Novo Produto" : "Editar Produto");
        stage.setResizable(false);
        stage.showAndWait();
        buscar();
    }

    public void atualizarTabela() {
        buscar();
    }

    @FXML private void abrirRelatorios() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Relatorios.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Relatórios - PsyWare");
        stage.show();
    }

    @FXML private void sair() throws Exception {
        Stage stage = (Stage) tabelaProdutos.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 660);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        stage.setScene(scene);
    }
}