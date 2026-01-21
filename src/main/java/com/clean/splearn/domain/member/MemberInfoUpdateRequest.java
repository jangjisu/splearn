package com.clean.splearn.domain.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdateRequest(
        @Size(min = 5, max = 20) String nickname,
        @Size(max = 15) String profileAddress,
        @NotNull String introduction
) {
}
