<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="sigena.model.domain.Cargo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    HttpSession sessaoHeader = request.getSession(false);
    Cargo cargoHeader = (Cargo) sessaoHeader.getAttribute("cargoUsuario");
    String nomeUsuarioHeader = sessaoHeader.getAttribute("NomeLogado") != null
            ? sessaoHeader.getAttribute("NomeLogado").toString()
            : String.valueOf(sessaoHeader.getAttribute("CpfLogado"));

    String cargoIconHeader = null;
    if (cargoHeader != null) {
        switch (cargoHeader) {
            case GERENTE -> cargoIconHeader = "IMG's/Identificador-gerente.png";
            case ZOOTECNISTA -> cargoIconHeader = "IMG's/Identificador-zootecnista.png";
            case VETERINARIO -> cargoIconHeader = "IMG's/Identificador-veterinario.png";
            case TRATADOR -> cargoIconHeader = "IMG's/Identificador-tratador.png";
        }
    }
%>

<div class="topbar">
    <div class="left-section">
        <a href="home.jsp"><img src="IMG's/Logo-SiGeNA.png" alt="Logo" class="logo"></a>
        <div class="search-bar">
            <img src="IMG's/Search.png" alt="Busca">
            <input type="text" placeholder="Pesquisar animais, tarefas...">
        </div>
    </div>
    <div class="right-section">
        <% if (cargoIconHeader != null) { %>
            <img src="<%= cargoIconHeader %>" alt="Cargo" class="role-icon">
        <% } %>
        
        <div class="notif-wrapper">
            <div class="notif-icon" onclick="toggleNotifs()">
                <img src="IMG's/Bell.png" alt="Notificações">
                <span class="notif-badge">2</span>
            </div>
            <div class="notif-dropdown" id="notifDropdown">
                <div class="notif-header">Notificações</div>
                <div class="notif-item">Sua tarefa "Limpeza Recinto 4" vence hoje.</div>
                <div class="notif-item">Novo relatório de saúde disponível.</div>
            </div>
        </div>

        <div class="user-profile">
            <div class="user-info">
                <span class="user-name"><%= nomeUsuarioHeader %></span>
                <span class="user-status">Online</span>
            </div>
            <img src="IMG's/User.png" alt="Avatar" class="avatar">
        </div>
    </div>
</div>

<audio id="notifSound" src="sounds/notif.mp3" preload="auto"></audio>
<audio id="clickSound" src="sounds/click.mp3" preload="auto"></audio>