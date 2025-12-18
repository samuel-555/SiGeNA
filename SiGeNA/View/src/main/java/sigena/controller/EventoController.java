package sigena.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import sigena.model.common.util.StringUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import sigena.model.common.exception.PersistenciaException;
import sigena.model.common.util.StringUtils;
import sigena.model.domain.Evento;
import sigena.model.service.GestaoEventoService;

@WebServlet(name = "EventoController", urlPatterns = {"/EventoController"})
public class EventoController extends HttpServlet {
    private final GestaoEventoService service = new GestaoEventoService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
                String acao = request.getParameter("acao");
                
                if("listar".equals(acao)) {
                    /*String di = StringUtils.conferNull(request.getParameter("dataInicio"));
                    String df = StringUtils.conferNull(request.getParameter("dataFim"));

                    LocalDate dataInicio = null;
                    LocalDate dataFim = null;

                    LocalDateTime inicio = dataInicio.atStartOfDay();
                    LocalDateTime fim = dataFim.plusDays(1).atStartOfDay();*/
                    List<Evento> eventos = null;
                    
                    /*if (di != null && !di.isBlank()) {
                        dataInicio = LocalDate.parse(di);
                    }

                    if (df != null && !df.isBlank()) {
                        dataFim = LocalDate.parse(df);
                    }*/
                    
                    eventos = service.listarEventos();
                    
                    request.setAttribute("eventos", eventos);
                    request.getRequestDispatcher("eventos.jsp").forward(request, response);
                }    
                    
                if("cadastrar".equals(acao)) {
                    request.getRequestDispatcher("cadastrar-evento.jsp").forward(request, response);
                }
            } catch(PersistenciaException e) {
                System.out.println(e.getMessage());
            }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String acao = request.getParameter("acao");
            
            if(acao == null)
                throw new NullPointerException();
                
            if("salvar".equals(acao)) {
                boolean success = cadastrar(request, response);
                HttpSession sessao = request.getSession(false);
                if(!success) {
                    sessao.setAttribute("campoInvalidoErro", "Campo(s) inválido(s) preenchido(s)!");
                    response.sendRedirect(request.getContextPath() + "/AnimalController?acao=cadastrar");
                    return;
                }
                
                sessao.setAttribute("acaoBemSucedida", "Evento cadastrado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/EventoController?acao=listar");
                return;
            }
        } catch(PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private boolean cadastrar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException, IOException, ServletException {
        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        String dataPrevista = request.getParameter("data-programada");
        
        Evento evento = new Evento(titulo, descricao, dataPrevista);
        
        return service.cadastrarEvento(evento);
    }
}
