package Servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.mysql.jdbc.PreparedStatement;

/**
 * Servlet implementation class prova
 */
@WebServlet("/prova")
public class prova extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public prova() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
		final String sql = "SELECT * FROM db_ristorante_1.piano";
		InitialContext ic = new InitialContext();
		DataSource ds = (DataSource) ic.lookup("java:jboss/datasources/MySqlDS2");
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
			    response.getWriter().println("Query '" + sql + "' returned " + rs.getString(1)+ " - " +rs.getString(2)+ " - " +rs.getString(3));
			}
		} finally {
		if(rs != null) rs.close();
		if(stmt != null) stmt.close();
		if(con != null) con.close();
		}
		}catch(Exception e){
			response.getWriter().println("FAIL EXCEPTION");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
