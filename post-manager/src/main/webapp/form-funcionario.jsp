<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<%@include file="base-head.jsp"%>
	</head>
	
		<body>
		<%@include file="nav-menu.jsp"%>
		
		<div id="container" class="container-fluid">
			<h3 class="page-header">Adicionar Funcionário</h3>

			<form action="${pageContext.request.contextPath}/funcionario/${action}" method="POST">
				<input type="hidden" value="${funcionario.getId()}" name="funcionarioId">
				<div class="row">
					<div class="form-group col-md-4">
						<label for="content">Nome Funcionário</label>
						<input type="text" class="form-control" id="nome" name="nome" 
							   autofocus="autofocus" placeholder="Nome do Funcionário" 
							   required oninvalid="this.setCustomValidity('Por favor, informe o nome do Funcionário.')"
							   oninput="setCustomValidity('')"
							   value="${funcionario.getNome()}">
					</div>
	
					<div class="form-group col-md-4">
						<label for="content">Ocupação</label>
							<input type="text" class="form-control" id="ocupacao" name="ocupacao" 
								   placeholder="Ocupação na empresa" 
								   required oninvalid="this.setCustomValidity('Por favor, informe a ocupação na empresa.')"
								   oninput="setCustomValidity('')"
								   value="${funcionario.getOcupacao()}">
					</div>
					
			<div class="form-group col-md-4">
						<label for="company">Empresa</label>
						<select id="company" class="form-control selectpicker" name="company" 
							    required oninvalid="this.setCustomValidity('Por favor, informe a empresa')"
							    oninput="setCustomValidity('')">
						  <option value="" disabled ${not empty funcionario ? "" : "selected"}>Selecione uma empresa</option>
						  <c:forEach var="company" items="${companies}">
						  	<option value="${company.getId()}"  ${funcionario.getCompany().getId() == company.getId() ? "selected" : ""}>
						  		${company.getName()}
						  	</option>	
						  </c:forEach>
						</select>
					</div>
				</div>
				<hr/>
				<div class="row">
					<div class="form-group col-md-4">
						<label for="content">CPF</label>
							<input type="text" class="form-control" id="cpf" name="cpf" 
								   placeholder="Cadastro de Pessoa Física" 
								   required oninvalid="this.setCustomValidity('Por favor, informe o CPF do funcionário.')"
								   oninput="setCustomValidity('')"
								   value="${funcionario.getCpf()}">
					</div>
					
					<div class="form-group col-md-4">
						<label for="content">Contato</label>
							<input type="text" class="form-control" id="contato" name="contato" 
								   placeholder="Contato" 
								   required oninvalid="this.setCustomValidity('Por favor, informe o telefone')"
								   oninput="setCustomValidity('')"
								   value="${funcionario.getContato()}">
					</div>
				</div>
				
				<hr />
				<div id="actions" class="row pull-right">
					<div class="col-md-12">
						<a href="${pageContext.request.contextPath}/funcionario" class="btn btn-default">Cancelar</a>
						<button type="submit" 
								class="btn btn-primary">${not empty funcionario ? "Alterar Funcionário" : "Criar Funcionário"}
						</button>
					</div>
				</div>
			</form>
		</div>
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>

		</body>
</html>