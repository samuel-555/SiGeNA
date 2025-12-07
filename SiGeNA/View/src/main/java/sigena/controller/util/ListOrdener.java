package sigena.controller.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ListOrdener {
        public static <T> void ordenarBusca(List<T> lista, String sequencia, String ordem, Function<T, String> getNome){
        if("adicionado".equals(sequencia) || "".equals(sequencia)) {
            if("crescente".equals(ordem) || "".equals(ordem))
                Collections.reverse(lista);
        } else if("alfabetica".equals(sequencia)) {
            lista.sort(Comparator.comparing(getNome));
            if("decrescente".equals(ordem))
                Collections.reverse(lista);
        }
    }
}
