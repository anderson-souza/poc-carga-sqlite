package com.aps.poccargasqlite.services;

import com.aps.poccargasqlite.config.CustomSqliteDatasource;
import com.aps.poccargasqlite.model.Pessoa;
import com.aps.poccargasqlite.repositories.PessoaRepository;
import com.github.javafaker.Faker;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PocService {

    public static final String DROP_TABLE_IF_EXISTS_PESSOA = "DROP TABLE IF EXISTS pessoa";
    public static final String CREATE_TABLE = "CREATE TABLE pessoa (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME STRING)";
    public static final String INSERT_PESSOA = "INSERT INTO pessoa (NAME) VALUES (?)";
    public static final int QUANTIDADE_REGISTROS = 10000;
    static final String DATABASE_NAME = "banco.db";

    @Autowired
    private PessoaRepository pessoaRepository;

    /**
     * Método que gera e popula um banco de dados Sqlite utilizando JDBCTemplate
     *
     * @return
     */
    public File gerarBancoDadosSpringTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(
            new CustomSqliteDatasource().criarDatasource());
        jdbcTemplate.setQueryTimeout(30);
        jdbcTemplate.setLazyInit(false);
        jdbcTemplate.execute(DROP_TABLE_IF_EXISTS_PESSOA);
        jdbcTemplate.execute(CREATE_TABLE);

        List<Pessoa> pessoaList = gerarPessoaList();

        jdbcTemplate.batchUpdate(INSERT_PESSOA, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, pessoaList.get(i).getNome());
            }

            @Override
            public int getBatchSize() {
                return 50;
            }
        });

        return new File(DATABASE_NAME);
    }

    /**
     * Método que gera e popula um banco de dados SQLITE utilizando JDBC Puro
     *
     * @return
     */
    public File gerarBancoDadosJDBC() {
        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {
                statement.setQueryTimeout(30);  // set timeout to 30 sec.
                statement.executeUpdate(DROP_TABLE_IF_EXISTS_PESSOA);
                statement.executeUpdate(CREATE_TABLE);
            }

            List<Pessoa> pessoaList = gerarPessoaList();

            try (PreparedStatement pstmt = connection
                .prepareStatement(INSERT_PESSOA)) {

                for (Pessoa pessoa : pessoaList) {
                    pstmt.setString(1, pessoa.getNome());
                    pstmt.addBatch();
                }

                pstmt.executeBatch();
            }
            connection.commit();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return new File(DATABASE_NAME);
    }

    private List<Pessoa> gerarPessoaList() {
        List<Pessoa> pessoaList = new ArrayList<>();
        Faker faker = new Faker(new Locale("pt-BR"));
        for (int i = 0; i < QUANTIDADE_REGISTROS; i++) {
            pessoaList.add(new Pessoa(faker.name().fullName()));
        }
        return pessoaList;
    }

}
