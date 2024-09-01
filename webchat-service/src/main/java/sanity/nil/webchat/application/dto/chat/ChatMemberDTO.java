package sanity.nil.webchat.application.dto.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberDTO {
        @JsonProperty(value = "member_id")
        public UUID memberID;
        @JsonProperty(value = "member_name")
        public String memberName;
        @JsonProperty(value = "role")
        public String role;
        @JsonProperty(value = "functions")
        public List<Integer> functions;

        public ChatMemberDTO(UUID memberID, String memberName, String role, String functions) {
                this.memberID = memberID;
                this.memberName = memberName;
                this.role = role;
                this.functions = Arrays.stream(functions.split(","))
                        .map(Integer::valueOf).toList();
        }
}
