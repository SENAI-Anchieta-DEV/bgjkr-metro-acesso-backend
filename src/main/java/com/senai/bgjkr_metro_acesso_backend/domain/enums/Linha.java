package com.senai.bgjkr_metro_acesso_backend.domain.enums;

import com.senai.bgjkr_metro_acesso_backend.domain.exception.estacao.NumeroDeLinhaInvalidaException;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum Linha {
    AZUL(1),
    VERDE(2),
    VERMELHA(3),
    AMARELA(4),
    LILAS(5),
    LARANJA(6),
    RUBI(7),
    DIAMANTE(8),
    ESMERALDA(9),
    TURQUESA(10),
    CORAL(11),
    SAFIRA(12),
    JADE(13),
    ONIX(14),
    PRATA(15),
    VIOLETA(16),
    OURO(17),
    BRONZE(18),
    CELESTE(19),
    ROSA(20),
    GRAFITE(21),
    MARROM(22);

    Linha(int numero) {
        this.numero = numero;
    }

    private final int numero;

    public static Set<Linha> fromNumeros(Set<Integer> numerosLinhas) {
        return numerosLinhas.stream()
                .map(Linha::fromNumero)
                .collect(Collectors.toSet());
    }

    private static Linha fromNumero(int numero) {
        for (Linha linha : Linha.values()) {
            if (linha.numero == numero) {
                return linha;
            }
        }
        throw new NumeroDeLinhaInvalidaException(numero);
    }
}
