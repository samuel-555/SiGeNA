package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.domain.Evento;
import sigena.model.domain.Notificacao;
import sigena.model.domain.Usuario;
import sigena.model.service.GestaoEventoService;
import sigena.model.service.GestaoNotificacaoService;

@WebServlet(name = "NotificacaoController", urlPatterns = {"/NotificacaoController"})
public class NotificacaoController extends HttpServlet {

    private GestaoNotificacaoService service = new GestaoNotificacaoService();
    private GestaoEventoService serviceE = new GestaoEventoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = (Usuario) request.getSession().getAttribute("UsuarioLogado");

        LocalDate hoje = LocalDate.now();
        LocalDate ultimaExecucao = (LocalDate) request.getSession()
            .getAttribute("notificacaoExecutada");

        if (ultimaExecucao == null || !ultimaExecucao.equals(hoje)) {
            try {
                notificarEventos(usuario);
                request.getSession().setAttribute("notificacaoExecutada", hoje);
            } catch (PersistenciaException e) {
                e.printStackTrace();
            }
        }
        
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

    public void notificarEventos(Usuario usuario) throws PersistenciaException, ServletException, IOException {
        LocalDateTime amanha = LocalDate.now().plusDays(1).atStartOfDay();

        List<Evento> eventos = serviceE.listarEventos(amanha, amanha.with(LocalTime.MAX));
        for (Evento e : eventos) {
            if (!service.eventoJaNotificado(usuario.getId(), "Amanhã você tem o evento " + e.getTitulo() + "", amanha)) {
                Notificacao n = new Notificacao(usuario.getId(), "Amanhã você tem o evento " + e.getTitulo() + "");
                service.salvar(n);
        }}
    }

}
