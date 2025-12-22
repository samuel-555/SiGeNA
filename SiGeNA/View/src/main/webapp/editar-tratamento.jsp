<%@page import="sigena.model.domain.Tratamento"%>
<%@page import="sigena.model.domain.Animal"%>
<%@page import="java.util.List"%>
<%@page import="sigena.model.service.GestaoAnimalService"%>
<%@page import="sigena.model.service.GestaoAnimalService"%>
<%@page import="sigena.model.domain.util.TipoTratamento"%>
<%@page import="sigena.model.domain.util.TipoTratamento"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%
    HttpSession sessao = request.getSession(false);
    if (sessao == null || sessao.getAttribute("CpfLogado") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    pageContext.setAttribute("tiposTratamento", TipoTratamento.values());
    GestaoAnimalService service = new GestaoAnimalService();
    List<Animal> animais = service.listarAnimais();
    pageContext.setAttribute("animais", animais);
    Tratamento tratamento = (Tratamento) request.getAttribute("tratamento");
    if (tratamento == null) {
        out.println("<h2 style='color:red'>❌ Erro: Nenhum tratamento recebido!</h2>");
    }
    pageContext.setAttribute("tratamento", tratamento);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Tratamento</title>
        <link rel="stylesheet" href="CSS/styletratamentos.css">
        <link rel="stylesheet" href="CSS/style.css">
        <link rel="stylesheet" href="CSS/stylehome.css">
    <link rel="stylesheet" href="CSS/stylefuncionalidades.css">
    </head>
    <body>
        <header class="topbar">
            <a href="HomeController" class="titulo">
                <img src="IMG's/logoSiGeNA-COR2.png" alt="Logo" class="brand-logo">
                <span>SiGeNA</span>
            </a>
            <div class="user-area">
                <a href="LogoutServlet" class="btn-sair">Sair</a>
            </div>
        </header>
        <div class="container">
            <h1>Editar Tratamento</h1>

            <div class="formulario">
                <h2>Atualizar Informações</h2>

                <form action="TratamentosController" method="POST">

                    <input type="hidden" name="id" value="${tratamento.id}">

                    <label for="tipoTratamento" >Tipo do Tratamento</label>
                    <select id="tipoTratamento" name="tipoTratamento">
                        <c:forEach var="tipo" items="${tiposTratamento}">
                            <option value="${tipo.name()}" <c:if test="${tipo.tipo == tratamento.tipoTratamento}">selected</c:if>>${tipo.tipo}</option>
                        </c:forEach>
                    </select>
                    <label for="animal">Animal:</label>
                    <select id="animal" name="animal">
                        <c:forEach var="animal" items="${animais}">
                            <option value="${animal.id}" <c:if test="${animal.id == tratamento.animal.id}">selected</c:if> >${animal.nome}</option>
                        </c:forEach>
                    </select>

                    <label for="diagnostico">Diagnostico:</label>
                    <input type="text" id="diagnostico" name="diagnostico" value="${tratamento.diagnostico}" placeholder="Ex: Doença X">    

                    <label for="medicacao">Medicação:</label>
                    <input type="text" id="medicacao" name="medicacao" value="${tratamento.medicacao}" placeholder="Ex: Antibiótico X">

                    <div id="temFrequencia" style="display: block;">
                        <label for="frequencia">Frequência:</label>
                        <input type="number" step="1" id="frequencia" name="frequencia" value="${tratamento.frequencia != null ? tratamento.frequencia : ''}" placeholder="Ex: 8 em 8h" >
                    </div>

                    <label for="data">Data:</label>
                    <input type="date" id="data" name="data" value="${tratamento.dataFinal}">

                    <div id="temHora" style="display: none;">
                        <label for="horario">Horario:</label>
                        <input type="time" id="horario" name="horario" value="${tratamento.horario != null ? tratamento.horario : ''}" placeholder="Horaio da consulta: 10:30">
                    </div>

                    <label for="observacoes">Observações:</label>
                    <textarea id="observacoes" name="observacoes" rows="3" placeholder="Observações adicionais"> ${tratamento.observacao} </textarea>

                    <input type="hidden" name="acao" value="editar">
                    <button type="submit" class="btn-enviar">Salvar Tratamento</button>
                </form>
            </div>

        </div>
    </body>
</html>

<script>
    document.getElementById("tipoTratamento").addEventListener("change", function () {
        let valor = this.value;


        if (valor === "CIRURGIA" || valor === "PREVENTIVO") {
            document.getElementById("temFrequencia").style.display = "none";
            document.getElementById("temHora").style.display = "block";
        } else {
            document.getElementById("temFrequencia").style.display = "block";
            document.getElementById("temHora").style.display = "none";
        }


    });

</script>