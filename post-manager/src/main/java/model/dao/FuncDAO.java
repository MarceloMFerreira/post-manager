package model.dao;

import java.util.List;

import model.Funcionario;
import model.ModelException;

public interface FuncDAO {
	boolean save(Funcionario funcionario) throws ModelException;
	boolean update(Funcionario funcionario) throws ModelException;
	boolean delete(Funcionario funcionario) throws ModelException;
	List<Funcionario> listAll() throws ModelException;
	Funcionario findById(int id) throws ModelException;
}
