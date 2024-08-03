package sanity.nil.webchat.infrastructure.channels.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

public record CentrifugoResponseDTO(
        ErrorResponse error,
        Result result
) {

    @AllArgsConstructor
    @NoArgsConstructor
    private static class ErrorResponse {
        public int code;
        public String message;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class Result {
        public int offset;
        public int epoch;
    }
}
