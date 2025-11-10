package co.com.capacidad.r2dbc;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.gateways.CapacidadRepository;
import co.com.capacidad.model.capacidad.page.CustomPage;
import co.com.capacidad.r2dbc.entities.CapacidadEntity;
import co.com.capacidad.r2dbc.helper.CapacidadEntityMapper;
import co.com.capacidad.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class CapacidadRepositoryAdapter extends ReactiveAdapterOperations<
        Capacidad,
        CapacidadEntity,
        Long,
        CapacidadReactiveRepository
> implements CapacidadRepository {
    
    private final CapacidadEntityMapper entityMapper;
    private final DatabaseClient databaseClient;
    
    public CapacidadRepositoryAdapter(CapacidadReactiveRepository repository, 
                                     ObjectMapper reactiveMapper,
                                     CapacidadEntityMapper entityMapper,
                                     DatabaseClient databaseClient) {
        super(repository, reactiveMapper, entityMapper::toDomain);
        this.entityMapper = entityMapper;
        this.databaseClient = databaseClient;
    }

    @Override
    protected CapacidadEntity toData(Capacidad entity) {
        return entityMapper.toEntity(entity);
    }

    @Override
    public Mono<Capacidad> guardarCapacidad(Capacidad capacidad) {
        return super.save(capacidad);
    }

    @Override
    public Mono<CustomPage<Capacidad>> listarCapacidades(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    ) {
        String orderBy = buildOrderByClause(sortBy, sortDirection);
        int offset = page * size;

        Mono<Long> countMono = databaseClient.sql("SELECT COUNT(*) FROM capacidades")
                .map(row -> row.get(0, Long.class))
                .one();

        Mono<List<CapacidadEntity>> dataMono = databaseClient.sql(
                "SELECT * FROM capacidades ORDER BY " + orderBy + " LIMIT :limit OFFSET :offset"
        )
                .bind("limit", size)
                .bind("offset", offset)
                .map((row, metadata) -> {
                    CapacidadEntity entity = new CapacidadEntity();
                    entity.setId(row.get("id", Long.class));
                    entity.setNombre(row.get("nombre", String.class));
                    entity.setDescripcion(row.get("descripcion", String.class));
                    entity.setTecnologiasIds(row.get("tecnologias_ids", String.class));
                    return entity;
                })
                .all()
                .collectList();

        return Mono.zip(countMono, dataMono)
                .map(tuple -> {
                    Long totalRows = tuple.getT1();
                    List<CapacidadEntity> entities = tuple.getT2();
                    
                    List<Capacidad> capacidades = entities.stream()
                            .map(entityMapper::toDomain)
                            .toList();

                    boolean hasNext = (offset + size) < totalRows;
                    String sort = sortBy + " " + sortDirection;

                    return CustomPage.<Capacidad>builder()
                            .data(capacidades)
                            .totalRows(totalRows)
                            .pageSize(size)
                            .pageNum(page)
                            .hasNext(hasNext)
                            .sort(sort)
                            .build();
                });
    }

    private String buildOrderByClause(String sortBy, String sortDirection) {
        String direction = "ASC".equalsIgnoreCase(sortDirection) ? "ASC" : "DESC";
        
        return switch (sortBy.toLowerCase()) {
            case "cantidadtecnologias", "cantidad_tecnologias" -> 
                "JSON_LENGTH(tecnologias_ids) " + direction;
            case "nombre" -> 
                "nombre " + direction;
            default -> 
                "nombre " + direction;
        };
    }
}
