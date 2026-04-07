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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// ... imports

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- ERROS DE SINTAXE E VALIDAÇÃO (400 BAD REQUEST) ---

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Um ou mais campos são inválidos",
                request.getRequestURI()
        );

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Erro de validação nos parâmetros",
                "Um ou mais parâmetros da URL/Path são inválidos",
                request.getRequestURI()
        );

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage()));

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidJson(HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido",
                "O JSON enviado possui erros de sintaxe ou formato.",
                request.getRequestURI()
        );
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            ConversionFailedException.class,
            NumeroDeLinhaInvalidaException.class,
            ComprovacaoDeDeficienciaAusenteException.class,
            MotivoReprovacaoAusenteException.class
    })
    public ProblemDetail handleBadRequestGeral(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // --- ERROS DE RECURSO NÃO ENCONTRADO (404 NOT FOUND) ---

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.NOT_FOUND,
                "A entidade solicitada não foi encontrada.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // --- ERROS DE CONFLITO DE NEGÓCIO (409 CONFLICT) ---

    @ExceptionHandler({
            TagIndisponivelException.class,
            AlterarFormularioPcdJaValidadoException.class,
            FormularioPcdComEmailDeUsuarioAtivoException.class,
            RemocaoDeFormularioDePcdAtivoException.class
    })
    public ProblemDetail handleConflitos(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.CONFLICT,
                "Conflito de regra de negócio.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // --- ERROS DE SEGURANÇA (401 E 403) ---

    @ExceptionHandler({
            CredenciaisInvalidasException.class,
            UsuarioNaoAutenticadoException.class
    })
    public ProblemDetail handleErroAutenticacao(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.UNAUTHORIZED,
                "Falha na Autenticação.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            AuthorizationDeniedException.class
    })
    public ProblemDetail handleAcessoNegado(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.FORBIDDEN,
                "Acesso Negado",
                "Você não tem permissão para realizar esta operação.",
                request.getRequestURI()
        );
    }

    // --- ERROS INTERNOS DO SERVIDOR (500 INTERNAL SERVER ERROR) ---

    @ExceptionHandler({
            LeituraDeComprovacaoDeDeficienciaException.class,
            RegistroDeComprovacaoDeDeficienciaException.class
    })
    public ProblemDetail handleErrosDeArquivo(Exception ex, HttpServletRequest request) {
        return ProblemDetailUtils.buildProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro no processamento de arquivos.",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // CATCH-ALL
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
}