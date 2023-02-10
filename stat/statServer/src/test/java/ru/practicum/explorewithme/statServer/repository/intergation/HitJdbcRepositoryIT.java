package ru.practicum.explorewithme.statServer.repository.intergation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.stat.dto.StatDto;
import ru.practicum.explorewithme.statServer.model.Hit;
import ru.practicum.explorewithme.statServer.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HitJdbcRepositoryIT {

    @Autowired
    private HitRepository repo;

    @Test
    public void shouldReturnStatWitH1Hit_WhenItIsBetweenSomeDates() {
        repo.save(Hit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        repo.save(Hit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .created(LocalDateTime.now().plusDays(10))
                .build());


        List<StatDto> stats = repo.getStats(
                "app",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                false,
                null);

        assertThat(stats).hasSize(1).contains(StatDto.builder()
                .app("app")
                .uri("uri")
                .hits(1L)
                .build());
    }

    @Test
    public void shouldReturnStatWitH1Hits_WhenItIsFromOneUniqIp() {
        repo.save(Hit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        repo.save(Hit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());


        List<StatDto> stats = repo.getStats(
                "app",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                true,
                null);

        assertThat(stats).hasSize(1).contains(StatDto.builder()
                .app("app")
                .uri("uri")
                .hits(1L)
                .build());
    }

    @Test
    public void shouldReturnStatWitH1Hits_WhenItIsFromUriList() {
        repo.save(Hit.builder()
                .app("app")
                .uri("uri1")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        repo.save(Hit.builder()
                .app("app")
                .uri("uri2")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());


        List<StatDto> stats = repo.getStats(
                "app",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                false,
                List.of("uri1"));

        assertThat(stats).hasSize(1).contains(StatDto.builder()
                .app("app")
                .uri("uri1")
                .hits(1L)
                .build());
    }

    @Test
    public void shouldReturnStatWitH1Hits_WhenItIsFromOneUniqIpAndUriListAndBetweenSomeDates() {
        repo.save(Hit.builder()
                .app("app")
                .uri("uri1")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        repo.save(Hit.builder()
                .app("app")
                .uri("uri1")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        repo.save(Hit.builder()
                .app("app")
                .uri("uri1")
                .ip("ip")
                .created(LocalDateTime.now().plusDays(10))
                .build());

        repo.save(Hit.builder()
                .app("app")
                .uri("uri2")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());


        List<StatDto> stats = repo.getStats(
                "app",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                true,
                List.of("uri1"));

        assertThat(stats).hasSize(1).contains(StatDto.builder()
                .app("app")
                .uri("uri1")
                .hits(1L)
                .build());
    }
}