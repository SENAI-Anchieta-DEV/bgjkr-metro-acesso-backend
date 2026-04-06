package com.senai.bgjkr_metro_acesso_backend.interface_ui.exception;

import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.auth.CredenciaisInvalidasException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.auth.UsuarioNaoAutenticadoException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.estacao.NumeroDeLinhaInvalidaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.*;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.tag_pcd.TagIndisponivelException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handlerConflitosGerais(Exception ex, HttpServletRequest request) {
        log.error("Erro interno não tratado: ", ex);

        return ProblemDetailUtils.buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado. Entre em contato com o suporte.",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.NOT_FOUND,
                "A entidade solicitada não foi encontrada.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TagIndisponivelException.class)
    public ProblemDetail handleTagIndisponivel(TagIndisponivelException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.CONFLICT,
                "Tags indisponíveis.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({AlterarFormularioPcdJaValidadoException.class, FormularioPcdComEmailDeUsuarioAtivoException.class, RemocaoDeFormularioDePcdAtivoException.class})
    public ProblemDetail handleConflitosFormulario(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.CONFLICT,
                "Conflito na operação com o formulário.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({ComprovacaoDeDeficienciaAusenteException.class, MotivoReprovacaoAusenteException.class})
    public ProblemDetail handleDadosFaltantesFormulario(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Dados obrigatórios ausentes.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({LeituraDeComprovacaoDeDeficienciaException.class, RegistroDeComprovacaoDeDeficienciaException.class})
    public ProblemDetail handleErrosDeArquivoFormulario(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro no processamento de arquivos.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NumeroDeLinhaInvalidaException.class)
    public ProblemDetail handleNumeroDeLinhaInvalida(NumeroDeLinhaInvalidaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Linha de estação inválida.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({CredenciaisInvalidasException.class, UsuarioNaoAutenticadoException.class})
    public ProblemDetail handleErroAutenticacao(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.UNAUTHORIZED,
                "Falha na Autenticação.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ProblemDetail handleAcessoNegado(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.FORBIDDEN,
                "Acesso Negado",
                "Você não tem permissão para realizar esta operação.",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail badRequest(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Um ou mais campos são inválidos",
                request.getRequestURI()
        );

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Tipo de parâmetro inválido");
        problem.setDetail(String.format(
                "O parâmetro '%s' deve ser do tipo '%s'. Valor recebido: '%s'",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido",
                ex.getValue()
        ));
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ProblemDetail handleConversionFailed(ConversionFailedException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Falha de conversão de parâmetro");
        problem.setDetail("Um parâmetro não pôde ser convertido para o tipo esperado.");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("error", ex.getMessage());
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Erro de validação nos parâmetros");
        problem.setDetail("Um ou mais parâmetros são inválidos");
        problem.setInstance(URI.create(request.getRequestURI()));

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String campo = violation.getPropertyPath().toString();
            String mensagem = violation.getMessage();
            errors.put(campo, mensagem);
        });
        problem.setProperty("errors", errors);
        return problem;
    }
}