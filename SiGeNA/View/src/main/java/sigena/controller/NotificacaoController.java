package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import sigena.model.domain.Notificacao;
import sigena.model.domain.Usuario;
import sigena.model.service.GestaoNotificacaoService;

@WebServlet(name = "NotificacaoController", urlPatterns = {"/NotificacaoController"})
public class NotificacaoController extends HttpServlet {
    
    private GestaoNotificacaoService service = new GestaoNotificacaoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("UsuarioLogado");

        List<Notificacao> notificacoes = service.listarPorUsuario(usuario.getId());

        request.setAttribute("notificacoes", notificacoes);
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        System.out.println("ID da notificacao: " + id);
        Notificacao n = service.buscarPorId(id);
        service.marcarComoLida(n);
        response.sendRedirect("NotificacaoController");
    }

}
