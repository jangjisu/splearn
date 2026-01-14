package com.clean.splearn.domain;

public class MemberFixture {
    public static MemberRegisterRequest createMemberRegisterRequest(String mail) {
        return new MemberRegisterRequest(mail, "david", "passwordSecret");
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("jsjangdv@gmail.com");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
}
