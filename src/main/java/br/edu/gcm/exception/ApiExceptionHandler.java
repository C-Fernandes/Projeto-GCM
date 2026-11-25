package br.edu.gcm.exception;

import br.edu.gcm.dto.ErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErroDTO> requisicaoInvalida(Exception exception) {
        return erro(HttpStatus.BAD_REQUEST, "Requisição inválida: informe os campos obrigatórios com tipos válidos.");
    }
    @ExceptionHandler(ProdutoDuplicadoException.class)
    public ResponseEntity<ErroDTO> duplicado(ProdutoDuplicadoException exception) {
        return erro(HttpStatus.CONFLICT, exception.getMessage());
    }
    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> naoEncontrado(ProdutoNaoEncontradoException exception) {
        return erro(HttpStatus.NOT_FOUND, exception.getMessage());
    }
    private ResponseEntity<ErroDTO> erro(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroDTO(status.value(), mensagem));
    }
}
