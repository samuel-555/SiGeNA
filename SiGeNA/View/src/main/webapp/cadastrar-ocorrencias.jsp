<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="sigena.model.domain.Ocorrencia" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    Ocorrencia oc = (Ocorrencia) request.getAttribute("ocorrencia");
%>

<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="UTF-8">
        <title><%= oc != null ? "Editar Ocorrência" : "Registrar Ocorrência"%></title>
        <link rel="stylesheet" href="CSS/style.css">
        <link rel="stylesheet" href="CSS/stylefuncionario.css">
        <link rel="stylesheet" href="CSS/stylehome.css">
        <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
    </head>
    <body>

        <header class="topbar">
            <a href="TarefaController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>

        <div class="container">
            <h1><%= oc != null ? "Editar Ocorrência" : "Registrar Ocorrência"%></h1>
            <a href="ocorrencias.jsp" class="btn-sair" style="background: var(--zoo-mint); color: var(--zoo-dark-green); margin-right: 10px;">Voltar</a>
            <form action="ocorrencias" method="post">

                <c:choose>
                    <c:when test="${oc != null}">
                        <input type="hidden" name="acao" value="atualizar" />
                        <input type="hidden" name="id" value="${oc.id}" />
                    </c:when>

                    <c:otherwise>
                        <input type="hidden" name="acao" value="cadastrar" />
                    </c:otherwise>
                </c:choose>

                <label>Tipo da Ocorrência:</label>
                <select name="tipo" required>
                    <option value="">-- selecione --</option>
                    <option value="FUGA"    ${oc != null && oc.tipo == 'FUGA' ? 'selected' : ''}>Fuga</option>
                    <option value="ACIDENTE" ${oc != null && oc.tipo == 'ACIDENTE' ? 'selected' : ''}>Acidente</option>
                    <option value="TECNICA" ${oc != null && oc.tipo == 'TECNICA' ? 'selected' : ''}>Tecnica</option>
                    <option value="OUTRO"   ${oc != null && oc.tipo == 'OUTRO' ? 'selected' : ''}>Outro</option>
                </select>

                <label>Data:</label>
                <input type="date" name="data" max="<%= java.time.LocalDate.now() %>" value="${oc != null ? oc.data.toLocalDate() : ''}" required>

                <label>Hora:</label>
                <input type="time" name="hora"
       value="<%= oc != null && oc.getData() != null ? oc.getData().toLocalTime() : "" %>"
       required />


                <label>Descrição:</label>
                <textarea name="descricao" rows="4">${oc.descricao}</textarea>

                <button type="submit" class="btn">Salvar</button>
                <a href="ocorrencias" class="btn cancelar">Cancelar</a>
            </form>
        </div>

    </body>
</html>
