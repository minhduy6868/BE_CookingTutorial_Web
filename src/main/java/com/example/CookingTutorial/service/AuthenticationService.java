package com.example.CookingTutorial.service;

import com.example.CookingTutorial.dto.request.AuthenticationRequest;
import com.example.CookingTutorial.dto.request.IntrospectRequest;
import com.example.CookingTutorial.dto.response.AuthenticationResponse;
import com.example.CookingTutorial.dto.response.IntrospectResponse;
import com.example.CookingTutorial.entity.User;
import com.example.CookingTutorial.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.text.ParseException;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY; // key này tự tạo

    // hàm xác thực
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new RuntimeException("Not find email" + request.getEmail()));

        boolean authenticated = new BCryptPasswordEncoder(10)
                .matches(request.getPassword(), user.getPassword());
        // lấy mật khẩu nhập và mật khẩu trên hệ thống so sánh

        if(!authenticated){
            throw new RuntimeException("Unauthenticated");};

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .access_token(token)
                .authenticated(true)
                .build();

    }

    // hàm tạo token
    private String generateToken(User user){
        JWSHeader header=new JWSHeader(JWSAlgorithm.HS512); //

        JWTClaimsSet jwtClaimsSet= new JWTClaimsSet.Builder() //phần nội dung của token
                .subject(user.getEmail())
                .issuer("CookingTutorial.com")
                .issueTime(new Date())
                .claim("scope",buildScope(user))// thông tin thêm có cũng được k cũng đucợ
                .build();
//        .expirationTime(new Date(
//                Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
//        )) thêm nếu muốn set thời gian
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject =new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token",e);
            throw new RuntimeException(e);
        }
    }

    // Hàm kiểm tra token có đúng hay k có bị thay đổi hay k
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier jwsVerifier=new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT=SignedJWT.parse(token);
//        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime(); kiểm tra thời gian của token

        var verified = signedJWT.verify(jwsVerifier);
        return IntrospectResponse.builder()
                .valid(verified)
                .build();
//        return IntrospectResponse.builder()
//                .valid(verified && expityTime.after(new Date()))
//                .build(); kiểm tra thời gian của token có còn hay k
    }

    private String buildScope(User user){ // tách các cái role ra
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);

        return stringJoiner.toString();
    }
}
