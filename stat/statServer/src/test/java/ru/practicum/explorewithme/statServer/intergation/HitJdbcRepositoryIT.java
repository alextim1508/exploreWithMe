package ru.practicum.explorewithme.statServer.intergation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.core.dto.StatDto;
import ru.practicum.explorewithme.statServer.model.Hit;
import ru.practicum.explorewithme.statServer.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HitJdbcRepositoryIT {

    @Autowired
    private StatRepository statRepository;

    @Test
    public void shouldReturnStatWitH1Hit_WhenItIsBetweenSomeDates() {
        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .created(LocalDateTime.now().plusDays(10))
                .build());

        List<StatDto> stats = statRepository.findByCreated(
                "app",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));

        assertThat(stats).hasSize(1).contains(StatDto.builder()
                .app("app")
                .uri("uri")
                .hits(1L)
                .build());
    }

    @Test
    public void shouldReturnStatWitH1Hits_WhenItIsFromOneUniqIp() {
        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());


        List<StatDto> stats = statRepository.findByCreatedWithUniqIp(
                "app",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1));

        assertThat(stats).hasSize(1).contains(StatDto.builder()
                .app("app")
                .uri("uri")
                .hits(1L)
                .build());
    }

    @Test
    public void shouldReturnStatWitH1Hits_WhenItIsFromUriList() {
        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri1")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri2")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());


        List<StatDto> stats = statRepository.findByCreatedAndUri(
                "app",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                List.of("uri1"));

        assertThat(stats).hasSize(1).contains(StatDto.builder()
                .app("app")
                .uri("uri1")
                .hits(1L)
                .build());
    }

    @Test
    public void shouldReturnStatWitH1Hits_WhenItIsFromOneUniqIpAndUriListAndBetweenSomeDates() {
        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri1")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri1")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());

        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri1")
                .ip("ip")
                .created(LocalDateTime.now().plusDays(10))
                .build());

        statRepository.save(Hit.builder()
                .app("app")
                .uri("uri2")
                .ip("ip")
                .created(LocalDateTime.now())
                .build());


        List<StatDto> stats = statRepository.findByCreatedAndUriWithUniqIp(
                "app",
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                List.of("uri1"));

        assertThat(stats).hasSize(1).contains(StatDto.builder()
                .app("app")
                .uri("uri1")
                .hits(1L)
                .build());
    }


    @Test
    public void shouldReturnStatWitH1Hits_WhenItIsFromTwoUniqIpsAndUriListAndBetweenSomeDates() {
        statRepository.save(Hit.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("238.148.205.3")
                .created(LocalDateTime.now())
                .build());

        statRepository.save(Hit.builder()
                .app("ewm-main-service")
                .uri("/events/2")
                .ip("151.105.17.238")
                .created(LocalDateTime.now().plusDays(10))
                .build());

        statRepository.save(Hit.builder()
                .app("ewm-main-service")
                .uri("/events/3")
                .ip("151.105.17.238")
                .created(LocalDateTime.now().plusDays(10))
                .build());

        List<StatDto> stats = statRepository.findByCreatedAndUriWithUniqIp(
                "ewm-main-service",
                LocalDateTime.now().minusDays(100),
                LocalDateTime.now().plusDays(100),
                List.of("/events/1", "/events/2"));

        assertThat(stats).hasSize(2);

    }
}