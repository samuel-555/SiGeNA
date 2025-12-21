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
import sigena.model.common.util.DataConverter;
import sigena.model.common.util.StringUtils;
import sigena.model.domain.Evento;
import sigena.model.service.GestaoEventoService;
import sigena.controller.util.ListOrdener;

@WebServlet(name = "EventoController", urlPatterns = {"/EventoController"})
public class EventoController extends HttpServlet {
    private final GestaoEventoService service = new GestaoEventoService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
                String acao = request.getParameter("acao");
                
                if("listar".equals(acao)) {
                    String di = StringUtils.conferNull(request.getParameter("dataInicio"));
                    String df = StringUtils.conferNull(request.getParameter("dataFim"));

                    LocalDateTime dataInicio = null;
                    LocalDateTime dataFim = null;

                    if(di != null && !di.isBlank())
                        dataInicio = LocalDateTime.parse(di);
                    
                    if(df != null && !df.isBlank())
                        dataFim = LocalDateTime.parse(df);
                        
                    List<Evento> eventos = null;
                    
                    String tipo = StringUtils.conferNull(request.getParameter("tipo"));
                    String busca = StringUtils.conferNull(request.getParameter("busca"));
                    String ordem = StringUtils.conferNull(request.getParameter("ordem"));
                    String filtro = StringUtils.conferNull(request.getParameter("filtro"));
                    
                    if ("ocorridos".equals(tipo) && (ordem == null || ordem.isBlank()))
                        ordem = "decrescente";
    
                    try {
                        eventos = service.listarEventos(busca, filtro, tipo, dataInicio, dataFim);
                    
                        ListOrdener.ordenarBusca(eventos, ordem);
                        request.setAttribute("eventos", eventos);
                    
                        if (dataInicio != null) {
                            request.setAttribute("dataInicio", DataConverter.toHTMLFormat(dataInicio));
                        }

                        if (dataFim != null) {
                            request.setAttribute("dataFim", DataConverter.toHTMLFormat(dataFim));
                        }
                    } catch (IllegalArgumentException e) {
                        eventos = service.listarEventos(busca, filtro, tipo, null, null);
                    
                        ListOrdener.ordenarBusca(eventos, ordem);
                        request.setAttribute("eventos", eventos);
                        
                        HttpSession sessao = request.getSession(false);
                        sessao.setAttribute("erro", e.getMessage());
                        response.sendRedirect(request.getContextPath() + "/EventoController?acao=listar");
                        return;
                    }
                    
                    
                    request.getRequestDispatcher("eventos.jsp").forward(request, response);
                }    
                
                if("exibir".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Evento evento = service.buscarEvento(id);
                    request.setAttribute("evento", evento);
                    boolean expirado = evento.getDataProgramada().isBefore(LocalDateTime.now());
                    request.setAttribute("expirado", expirado);
                    request.getRequestDispatcher("exibir-evento.jsp").forward(request, response);
                }
                
                if("editar".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Evento evento = service.buscarEvento(id);
                    request.setAttribute("evento", evento);
                    request.getRequestDispatcher("editar-evento.jsp").forward(request, response);
                }
                
                if("salvar_alteracoes".equals(acao)) {
                    Long id = Long.valueOf(request.getParameter("id"));
                    Evento evento = service.buscarEvento(id);
                    request.setAttribute("evento", evento);
                    request.getRequestDispatcher("exibir-evento.jsp").forward(request, response);
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
                    response.sendRedirect(request.getContextPath() + "/EventoController?acao=cadastrar");
                    return;
                }
                
                sessao.setAttribute("acaoBemSucedida", "Evento cadastrado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/EventoController?acao=listar");
                return;
            }
            
            if("excluir".equals(acao)) {
                excluir(request, response);
                response.sendRedirect(request.getContextPath() + "/EventoController?acao=listar");
            }
            
            if("cancelar".equals(acao)) {
                cancelar(request, response);
                response.sendRedirect(request.getContextPath() + "/EventoController?acao=listar");
            }
            
            if("ativar".equals(acao)) {
                ativar(request, response);
                response.sendRedirect(request.getContextPath() + "/EventoController?acao=listar&tipo=cancelados");
            }
            
            if("editar".equals(acao)) {
                boolean success = editar(request, response);
                String id = request.getParameter("id");
                HttpSession sessao = request.getSession(false);
                if(!success) {
                    sessao.setAttribute("campoInvalidoErro", "Campo(s) inválido(s) preenchido(s)!");
                    response.sendRedirect(request.getContextPath() + "/EventoController?acao=editar&id=" + id);
                    return;
                }
                sessao.setAttribute("acaoBemSucedida", "Evento editado com sucesso!");
                response.sendRedirect(request.getContextPath() + "/EventoController?acao=exibir&id=" + id);
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
    
    private void excluir(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException{
        Long id = Long.valueOf(request.getParameter("id"));
        service.excluirEvento(id);
    }
    
    private void cancelar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException{
        Long id = Long.valueOf(request.getParameter("id"));
        service.cancelarEvento(id);
    }
    
    private void ativar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException{
        Long id = Long.valueOf(request.getParameter("id"));
        service.ativarEvento(id);
    }
    
    private boolean editar(HttpServletRequest request, HttpServletResponse response) throws PersistenciaException {
        Long id = Long.valueOf(request.getParameter("id"));
        String titulo = request.getParameter("titulo");
        String descricao = request.getParameter("descricao");
        String dataProgramada = request.getParameter("data-programada");

        Evento eventoEditado = new Evento(id, titulo, descricao, dataProgramada);
 
        return service.editarEvento(eventoEditado);
    }
}
