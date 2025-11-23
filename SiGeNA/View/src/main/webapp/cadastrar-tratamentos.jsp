<%@page import="sigena.model.domain.Animal"%>
<%@page import="java.util.List"%>
<%@page import="sigena.model.service.GestaoAnimalService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession" %>
<%@page import="sigena.model.domain.util.TipoTratamento"%>
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
    String paginaHome = "GERENTE".equals(String.valueOf(session.getAttribute("cargoUsuario"))) ? "home-gerente.jsp" : "home.jsp";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Novo Tratamento</title>
        <link rel="stylesheet" href="CSS/styletratamentos.css">
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <header>
            <div class="titulo"><a href="<%= paginaHome %>">SiGeNA</a></div>
        </header>
        <div class="container">
            <div class="botoes-acoes">
                <button class="btn"><a href="tratamentos.jsp">Ver Histórico de Tratamentos</a></button>
            </div>
            <div class="formulario">
                <h2>Registrar Novo Tratamento</h2>
                <form action="TratamentosServlet" method="POST">
                    <label for="tipoTratamento" >Tipo do Tratamento</label>
                    <select id="tipoTratamento" name="tipoTratamento">
                        <c:forEach var="tipo" items="${tiposTratamento}">
                        <option value="${tipo}">${tipo.tipo}</option>
                        </c:forEach>
                    </select>
                    <label for="animal">Animal:</label>
                    <select id="animal" name="animal">
                        <c:forEach var="animal" items="${animais}">
                        <option value="${animal.id}">${animal.nome}</option>
                        </c:forEach>
                    </select>
                    
                    <label for="diagnostico">Diagnostico:</label>
                    <input type="text" id="diagnostico" name="diagnostico" placeholder="Ex: Doença X">    
                    
                    <label for="medicacao">Medicação:</label>
                    <input type="text" id="medicacao" name="medicacao" placeholder="Ex: Antibiótico X">
                    
                    <div id="temFrequencia" style="display: block;">
                    <label for="frequencia">Frequência:</label>
                    <input type="number" step="1" id="frequencia" name="frequencia" placeholder="Ex: 8 em 8h" >
                    </div>
                    
                    <label for="data">Data:</label>
                    <input type="date" id="data" name="data" >
                    
                    <div id="temHora" style="display: none;">
                    <label for="horario">Horario:</label>
                    <input type="time" id="horario" name="horario" placeholder="Horaio da consulta: 10:30">
                    </div>
                    
                    <label for="observacoes">Observações:</label>
                    <textarea id="observacoes" name="observacoes" rows="3" placeholder="Observações adicionais"></textarea>
                    
                    <input type="hidden" name="acao" value="salvar">
                    <button type="submit" class="btn-enviar">Salvar Tratamento</button>
                </form>
            </div>
            
        </div>
    </body>
</html>

<script>
    document.getElementById("tipoTratamento").addEventListener("change", function(){
        let valor = this.value;
        

        if(valor === "CIRURGIA" || valor === "PREVENTIVO"){
            document.getElementById("temFrequencia").style.display = "none";
            document.getElementById("temHora").style.display = "block";
        }else{
            document.getElementById("temFrequencia").style.display = "block";
            document.getElementById("temHora").style.display = "none";
        }

            
    });
    
</script>