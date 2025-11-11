package co.com.capacidad.model.capacidad.page;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomPageTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        List<String> data = Arrays.asList("item1", "item2", "item3");
        CustomPage<String> page = CustomPage.<String>builder()
                .data(data)
                .totalRows(100L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(true)
                .sort("nombre ASC")
                .build();

        assertEquals(data, page.getData());
        assertEquals(100L, page.getTotalRows());
        assertEquals(10, page.getPageSize());
        assertEquals(0, page.getPageNum());
        assertTrue(page.getHasNext());
        assertEquals("nombre ASC", page.getSort());
    }


    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        CustomPage<String> page = new CustomPage<>();

        assertNotNull(page.getData());
        assertTrue(page.getData().isEmpty());
        assertNull(page.getTotalRows());
        assertNull(page.getPageSize());
        assertNull(page.getPageNum());
        assertNull(page.getHasNext());
        assertNull(page.getSort());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        List<String> data = Arrays.asList("item1", "item2");
        CustomPage<String> page = new CustomPage<>(data, 50L, 5, 1, "nombre DESC", false);

        assertEquals(data, page.getData());
        assertEquals(50L, page.getTotalRows());
        assertEquals(5, page.getPageSize());
        assertEquals(1, page.getPageNum());
        assertEquals("nombre DESC", page.getSort());
        assertFalse(page.getHasNext());
    }

    @Test
    void add_DeberiaAgregarItemALaLista() {
        CustomPage<String> page = new CustomPage<>();
        page.add("item1");
        page.add("item2");

        assertEquals(2, page.getData().size());
        assertTrue(page.getData().contains("item1"));
        assertTrue(page.getData().contains("item2"));
    }

    @Test
    void iterator_DeberiaRetornarIteratorDeLaLista() {
        List<String> data = Arrays.asList("item1", "item2", "item3");
        CustomPage<String> page = new CustomPage<>();
        page.setData(data);

        Iterator<String> iterator = page.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("item1", iterator.next());
        assertEquals("item2", iterator.next());
        assertEquals("item3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void builder_ConDataVacia_DeberiaFuncionarCorrectamente() {
        CustomPage<String> page = CustomPage.<String>builder()
                .data(Collections.emptyList())
                .totalRows(0L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .build();

        assertNotNull(page.getData());
        assertTrue(page.getData().isEmpty());
        assertEquals(0L, page.getTotalRows());
        assertFalse(page.getHasNext());
    }

}

