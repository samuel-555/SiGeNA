<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@taglib uri="jakarta.tags.xml" prefix="x" %>
<%@taglib uri="jakarta.tags.sql" prefix="sql"%>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    LocalDate data = LocalDate.now();
    String hoje = data.toString();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SiGeNA - Gestão de Fornecedores</title>
        <link rel="stylesheet" href="CSS\styleanimais.css">
        <link rel="stylesheet" href="CSS\style.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= request.getContextPath() + ("GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "/home-gerente.jsp" : "/home.jsp") %>">SiGeNA</a></div>
        </header>
        
        <div class="botoes-acoes">
            <a href="FornecedorController?acao=listar" class="btn">Voltar</a>
        </div>
        
        <div class="container">
            <h1>Cadastrar Novo Fornecedor</h1>
        <div class="formulario">
            <form action="FornecedorController" method="post">
                <label for="nome">Nome do Fornecedor:*</label>
                <input type="text" id="nome" name="nome" class="obrigatorio" required>
                
                <label for="telefone">Telefone:</label>
                <input type="text" id="telefone" name="telefone">
                
                <label for="email">Email:</label>
                <input type="email" name="email">
                
                <label for="nome">Endereço:</label>
                <input type="text" id="endereco" name="endereco">

                
                
                <label for="tipo">Tipo:*</label>
                <select name="tipo" id="tipo" class="obrigatorio">
                    <option value="">Selecione o tipo</option>
                    <option value="ALIMENTO">ALIMENTO</option>
                    <option value="MEDICAMENTO">MEDICAMENTO</option>
                    <option value="EQUIPAMENTO">EQUIPAMENTO</option>
                    <option value="HIGIENE E LIMPEZA">HIGIENE E LIMPEZA</option>
                    <option value="ACESSORIOS">ACESSÓRIOS</option>
                    <option value="SERVICOS">SERVIÇOS</option>
                    <option value="VARIADOS">VARIADOS</option>
                    <option value="OUTROS">OUTROS</option>
                </select><br>
                
                <label for="descricao">Descrição:</label>
                <textarea name="descricao"></textarea>
                    
                <input type="hidden" name="acao" value="salvar">
                <button type="submit" class="btn-enviar" onclick="return confirm('Salvar fornecedor?')">Salvar Fornecedor</button>
            </form>
        </div>
        </div>
        <script src="JS/verificar-campos.js"></script>
    </body>
</html>
