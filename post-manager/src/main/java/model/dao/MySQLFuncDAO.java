package model.dao;


import java.util.ArrayList;
import java.util.List;
import model.Company;
import model.Funcionario;
import model.ModelException;


public class MySQLFuncDAO implements FuncDAO {
	
	@Override
	public boolean save(Funcionario funcionario) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO funcionarios VALUES (DEFAULT, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, funcionario.getNome());
		db.setString(2, funcionario.getOcupacao());
		db.setString(3, funcionario.getCpf());
		db.setString(4, funcionario.getContato());		
		db.setInt(5, funcionario.getCompany().getId());
		
		return db.executeUpdate() > 0;	
	}

	@Override
	public boolean update(Funcionario funcionario) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE funcionarios "
				+ " SET nome = ?, "
				+ " ocupacao = ?, "
				+ " cpf = ?, "
				+ " contato = ?, "
				+ " company_id = ? "
				+ " WHERE id = ?; "; 
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, funcionario.getNome());
		db.setString(2, funcionario.getOcupacao());
		db.setString(3, funcionario.getCpf());
		db.setString(4, funcionario.getContato());
		db.setInt(5, funcionario.getCompany().getId());
		db.setInt(6, funcionario.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Funcionario funcionario) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM funcionarios "
		         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, funcionario.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Funcionario> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
			
		// Declara uma instrução SQL
		String sqlQuery = " SELECT c.id as 'funcionario_id', c.*, u.* \n"
				+ " FROM funcionarios c \n"
				+ " INNER JOIN companies u \n"
				+ " ON c.company_id = u.id;";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			Company company = new Company(db.getInt("company_id"));
			company.setName(db.getString("name"));
			company.setRole(db.getString("role"));
			company.setStart(db.getDate("start"));
			company.setEnd(db.getDate("end"));
			
			Funcionario funcionario = new Funcionario(db.getInt("funcionario_id"));
			funcionario.setNome(db.getString("nome"));
			funcionario.setOcupacao(db.getString("ocupacao"));
			funcionario.setCpf(db.getString("cpf"));
			funcionario.setContato(db.getString("contato"));
			funcionario.setCompany(company);
			funcionarios.add(funcionario);
		}
		
		return funcionarios;
	}

	@Override
	public Funcionario findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM funcionarios WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Funcionario c = null;
		while (db.next()) {
			c = new Funcionario(id);
			
			c.setContato(db.getString("contato"));
			c.setNome(db.getString("nome"));
			c.setOcupacao(db.getString("ocupacao"));
			c.setCpf(db.getString("cpf"));
			
			
			CompanyDAO companyDAO = DAOFactory.createDAO(CompanyDAO.class); 
			Company company = companyDAO.findById(db.getInt("company_id"));
			c.setCompany(company);
			
			break;
		}
		
		return c;
	}

}
