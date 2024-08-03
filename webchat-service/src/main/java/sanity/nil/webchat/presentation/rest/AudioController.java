package sanity.nil.webchat.presentation.rest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/audio")
@CrossOrigin
public class AudioController {

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<byte[]>> getAudio() {
//        List<byte[]> bytes = divideFileIntoChunks(165547);
        List<byte[]> bytes = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        return Flux.interval(Duration.ofMillis(500))
                .map(sequence -> ServerSentEvent.<byte[]>builder()
                        .event("audioChunk")
                        .data(bytes.get(i.get()))
                        .build()
                )
                .doOnNext(e -> i.getAndIncrement())
                .subscribeOn(Schedulers.parallel())
                .onErrorComplete();
    }
}
