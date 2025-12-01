package psyware.dao;

import psyware.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY nome_produto";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(criarProduto(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Produto> buscarPorNome(String termo) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome_produto LIKE ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(criarProduto(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void salvar(Produto p) {
        if (p.getIdProduto() == 0) {
            inserir(p);
        } else {
            atualizar(p);
        }
    }

    private void inserir(Produto p) {
        String sql = "INSERT INTO produtos (nome_produto, descricao_produto, quantidade_estoque, preco, categoria, imagem) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherStatement(ps, p);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void atualizar(Produto p) {
        String sql = "UPDATE produtos SET nome_produto=?, descricao_produto=?, quantidade_estoque=?, preco=?, categoria=?, imagem=? WHERE id_produto=?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preencherStatement(ps, p);
            ps.setInt(7, p.getIdProduto());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id_produto = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Produto criarProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id_produto"),
                rs.getString("nome_produto"),
                rs.getString("descricao_produto"),
                rs.getInt("quantidade_estoque"),
                rs.getDouble("preco"),
                rs.getString("categoria"),
                rs.getString("imagem")
        );
    }

    private void preencherStatement(PreparedStatement ps, Produto p) throws SQLException {
        ps.setString(1, p.getNomeProduto());
        ps.setString(2, p.getDescricaoProduto());
        ps.setInt(3, p.getQuantidadeEstoque());
        ps.setDouble(4, p.getPreco());
        ps.setString(5, p.getCategoria());
        ps.setString(6, p.getImagem());
    }
}