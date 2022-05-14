package controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Company;
import model.Funcionario;
import model.ModelException;
import model.User;
import model.dao.CompanyDAO;
import model.dao.DAOFactory;
import model.dao.FuncDAO;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/funcionario", "/funcionario/form", "/funcionario/delete", "/funcionario/insert", "/funcionario/update"})
public class FuncionariosController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/post-manager/funcionario/form": {
			CommonsController.listCompanies(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-funcionario.jsp");
			break;
		}
		case "/post-manager/funcionario/update":{
			CommonsController.listCompanies(req);
			req.setAttribute("action", "update");
			Funcionario f = loadFuncionario(req);
			req.setAttribute("funcionario", f);
			ControllerUtil.forward(req, resp, "/form-funcionario.jsp");
			break;
		}
		default:
			listFuncionarios(req);
			
			ControllerUtil.transferSessionMessagesToRequest(req);
			
			ControllerUtil.forward(req, resp, "/funcionarios.jsp");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/post-manager/funcionario/insert": {
			insertFuncionario(req, resp);
			break;
		}
		case "/post-manager/funcionario/update":{
			updateFuncionario(req, resp);
			break;
		}
		case "/post-manager/funcionario/delete":{
			deleteFuncionario(req, resp);
			break;
		}
		default:
			System.out.println("URL invÃ¡lida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/funcionario");
	}

	private void deleteFuncionario(HttpServletRequest req, HttpServletResponse resp) {
		String funcionarioIdParameter = req.getParameter("id");
		
		int funcionarioId = Integer.parseInt(funcionarioIdParameter);
		
		FuncDAO dao = DAOFactory.createDAO(FuncDAO.class);
		
		try {
			Funcionario funcionario = dao.findById(funcionarioId);
			
			if (funcionario == null)
				throw new ModelException("Funcionário não encontrado para deletar.");
			
			if (dao.delete(funcionario)) {
				ControllerUtil.sucessMessage(req, "Funcionário '" + funcionario.getNome() + "' deletado com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Funcionário '" + funcionario.getNome() + "' não pode ser deletado.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
	}

	private void updateFuncionario(HttpServletRequest req, HttpServletResponse resp) {
		String funcNome = req.getParameter("nome");
		String funcOcupacao = req.getParameter("ocupacao");
		String cpf = req.getParameter("cpf");
		String contato = req.getParameter("contato");
		Integer companyId = Integer.parseInt(req.getParameter("company"));
		
		Funcionario funcionario = loadFuncionario(req);
		funcionario.setNome(funcNome);
		funcionario.setContato(contato);
		funcionario.setOcupacao(funcOcupacao);
		funcionario.setCpf(cpf);
		Company company = new Company(companyId);
		funcionario.setCompany(company);
		
		FuncDAO dao = DAOFactory.createDAO(FuncDAO.class);
		
		try {
			if (dao.update(funcionario)) {
				ControllerUtil.sucessMessage(req, "Funcionário '" + funcionario.getNome() + "' atualizado com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Funcionário '" + funcionario.getNome()+ "' não pode ser atualizado.");
			}				
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}		
		
	}

	private void insertFuncionario(HttpServletRequest req, HttpServletResponse resp) {
		String funcionarioNome = req.getParameter("nome");
		String ocupacao = req.getParameter("ocupacao");
		String contato = req.getParameter("contato");
		Integer companyId = Integer.parseInt(req.getParameter("company"));
		String cpf = req.getParameter("cpf");
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(funcionarioNome);
		funcionario.setOcupacao(ocupacao);
		funcionario.setCpf(cpf);
		funcionario.setContato(contato);
		
		Company company = new Company(companyId);
		funcionario.setCompany(company);

		FuncDAO dao = DAOFactory.createDAO(FuncDAO.class);

		try {
			if (dao.save(funcionario)) {
				ControllerUtil.sucessMessage(req, 
						"Funcionário '" + funcionario.getNome() + "' salvo com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, 
						"Funcionário '" + funcionario.getNome() + "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
	}

	private void listFuncionarios(HttpServletRequest req) {
		FuncDAO dao = DAOFactory.createDAO(FuncDAO.class);
		
		List<Funcionario> funcionarios = null;
		
		try {
			funcionarios = dao.listAll();
		} catch (ModelException e) {
			
			e.printStackTrace();
		}
		
		if (funcionarios != null)
			req.setAttribute("funcionarios", funcionarios);
		
	}

	private Funcionario loadFuncionario(HttpServletRequest req) {
		String funcionarioIdParameter = req.getParameter("funcionarioId");
		
		int funcionarioId = Integer.parseInt(funcionarioIdParameter);
		
		FuncDAO dao = DAOFactory.createDAO(FuncDAO.class);
		
		try {
			Funcionario f = dao.findById(funcionarioId);
			
			if (f == null)
				throw new ModelException("Funcionário não encontrado para alteração");
			
			return f;
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
		return null;
	}	
}
