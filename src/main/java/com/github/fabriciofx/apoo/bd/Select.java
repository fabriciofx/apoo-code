package com.github.fabriciofx.apoo.bd;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Select implements Comando<List<Map<String, Object>>> {
	private final transient Conexao conexao;

	public Select(final Conexao conexao) {
		this.conexao = conexao;
	}

	@Override
	public List<Map<String, Object>> executa(final String sql,
			final Object... args) throws IOException {
		try {
			final PreparedStatement pstmt = conexao.statement(sql);
			prepare(pstmt, args);
			final ResultSet rs = pstmt.executeQuery();
			final List<Map<String, Object>> tabela = tabela(rs);
			conexao.efetiva();
			pstmt.close();
			return tabela;
		} catch (final SQLException e) {
			throw new IOException(e);
		}
	}

	private void prepare(final PreparedStatement stmt, final Object... args)
			throws SQLException {
		int pos = 1;
		for (final Object arg : args) {
			if (arg == null) {
				stmt.setString(pos, null);
			} else if (arg instanceof Long) {
				stmt.setLong(pos, Long.class.cast(arg));
			} else if (arg instanceof Boolean) {
				stmt.setBoolean(pos, Boolean.class.cast(arg));
			} else if (arg instanceof Date) {
				stmt.setDate(pos, Date.class.cast(arg));
			} else if (arg instanceof Integer) {
				stmt.setInt(pos, Integer.class.cast(arg));
			} else if (arg instanceof byte[]) {
				stmt.setBytes(pos, byte[].class.cast(arg));
			} else {
				stmt.setString(pos, arg.toString());
			}
			++pos;
		}
	}

	private List<Map<String, Object>> tabela(final ResultSet rs)
			throws SQLException {
		final ResultSetMetaData rsmd = rs.getMetaData();
		final int colunas = rsmd.getColumnCount();
		final List<Map<String, Object>> tabela = new LinkedList<>();
		while (rs.next()) {
			final Map<String, Object> linhas = new HashMap<>();
			for (int col = 1; col <= colunas; col++) {
				linhas.put(rsmd.getColumnName(col).toLowerCase(),
						rs.getObject(col));
			}
			tabela.add(linhas);
		}
		return tabela;
	}
}