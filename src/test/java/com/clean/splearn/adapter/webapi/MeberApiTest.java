package com.clean.splearn.adapter.webapi;

import com.clean.splearn.adapter.webapi.dto.MemberRegisterResponse;
import com.clean.splearn.application.member.provided.MemberRegister;
import com.clean.splearn.application.member.required.MemberRepository;
import com.clean.splearn.domain.member.Member;
import com.clean.splearn.domain.member.MemberFixture;
import com.clean.splearn.domain.member.MemberRegisterRequest;
import com.clean.splearn.domain.member.MemberStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.io.UnsupportedEncodingException;

import static com.clean.splearn.AssertThatUtils.equalsToString;
import static com.clean.splearn.AssertThatUtils.notNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@RequiredArgsConstructor
class MeberApiTest {
    final MockMvcTester mvcTester;
    final ObjectMapper objectMapper;
    final MemberRepository memberRepository;
    final MemberRegister memberRegister;

    @DisplayName("회원을 등록한다.")
    @Test
    void register() throws JsonProcessingException, UnsupportedEncodingException {
        // given
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).exchange();

        // when // then
        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.memberId", notNull())
                .hasPathSatisfying("$.email", equalsToString(request.email()));

        MemberRegisterResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);

        Member member = memberRepository.findById(response.memberId()).orElseThrow();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @DisplayName("중복된 이메일을 가진 회원은 등록될 수 없다.")
    @Test
    void duplicateEmail() throws JsonProcessingException {
        // given
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).exchange();

        // when // then
        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.CONFLICT);
    }
}
