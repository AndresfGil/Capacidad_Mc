package co.com.capacidad.schedule;

import co.com.capacidad.model.capacidad.gateways.CapacidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class PurgeService {

    private static final Logger logger = Logger.getLogger(PurgeService.class.getName());
    private final CapacidadRepository capacidadRepository;

    @Scheduled(cron = "0 0 3 * * *") // todos los días a las 3AM
    public void purgeSoftDeleted() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(7);
        logger.log(Level.INFO, "Iniciando purga de capacidades inactivas anteriores a: {0}", fechaLimite);
        
        capacidadRepository.eliminarInactivasAntiguas(fechaLimite)
                .doOnSuccess(v -> logger.info("Purga completada exitosamente"))
                .doOnError(error -> logger.log(Level.SEVERE, "Error en la purga: {0}", error.getMessage()))
                .subscribe();
    }
}

