package sanity.nil.webchat.infrastructure.channels.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CentrifugoBroadcastPayload(
        @JsonProperty(value = "channels")
        List<String> channels,
        @JsonProperty(value = "data")
        CentrifugoPayloadBase data,
        @JsonProperty(value = "idempotency_key")
        String idempotencyKey
) { }
