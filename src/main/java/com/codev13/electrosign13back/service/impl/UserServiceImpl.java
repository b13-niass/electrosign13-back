package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.client.keycloak.*;
import com.codev13.electrosign13back.data.entity.Fonction;
import com.codev13.electrosign13back.data.entity.Role;
import com.codev13.electrosign13back.data.entity.User;
import com.codev13.electrosign13back.data.repository.FonctionRepository;
import com.codev13.electrosign13back.data.repository.RoleRepository;
import com.codev13.electrosign13back.data.repository.UserRepository;
import com.codev13.electrosign13back.exception.fonction.FonctionNotFoundException;
import com.codev13.electrosign13back.exception.role.RoleNotFoundException;
import com.codev13.electrosign13back.service.FileStorageService;
import com.codev13.electrosign13back.service.TokenProvider;
import com.codev13.electrosign13back.service.UserService;
import com.codev13.electrosign13back.utils.AESUtil;
import com.codev13.electrosign13back.utils.KeyGeneratorUtil;
import com.codev13.electrosign13back.web.dto.request.UserRequestDto;
import com.codev13.electrosign13back.web.dto.response.UserResponseDto;
import com.core.communs.service.MapperService;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final KeycloakClient keycloakClient;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FonctionRepository fonctionRepository;
    private final TokenProvider tokenProvider;
    @Value("${keySecret}")
    private String SECRET;

    private final FileStorageService fileStorageService;

    @Transactional
    public UserResponseDto createUser(UserRequestDto request) {
        try {
            Set<Role> roles = request.roles().stream()
                    .map(roleName -> roleRepository.findRoleByLibelle(roleName)
                            .orElseThrow(() -> new RoleNotFoundException("Role " + roleName + " non trouvé")))
                    .collect(Collectors.toSet());

            List<KeycloakRoleAssign> keycloakRoles = roles.stream()
                    .map(roleName -> {
                        try {
                            return keycloakClient.getRoleByName(tokenProvider.getToken(), roleName.getLibelle());
                        } catch (FeignException.NotFound e) {
                            throw new RoleNotFoundException("Role " + roleName + " not found in Keycloak");
                        }
                    })
                    .collect(Collectors.toList());

            Fonction fonction = fonctionRepository.findFonctionById(request.fonctionId()).orElseThrow(
                    () -> new FonctionNotFoundException("Fonction non trouvée avec l'id " + request.fonctionId())
            );
            KeyPair keyPair = KeyGeneratorUtil.getKeyPair();
            //Chiffrer la clé privée avec AES
            String encryptedPrivateKey = AESUtil.encrypt(SECRET, keyPair.getPrivate().getEncoded());
//            String fileName = fileStorageService.uploadFile(request.photo());
            byte[] fileBytes = request.photo().getBytes();
            String fileBase64 = "data:image/png;base64,"+Base64.getEncoder().encodeToString(fileBytes);
            User user = User.builder()
                    .prenom(request.prenom())
                    .nom(request.nom())
                    .email(request.email())
                    .cni(request.cni())
                    .fonction(fonction)
                    .photo(fileBase64)
                    .privateKey(encryptedPrivateKey)
                    .publicKey(KeyGeneratorUtil.encodeKey(keyPair.getPublic().getEncoded()))
                    .password("")
                    .telephone(request.telephone())
                    .roles(roles)
                    .build();

            user = userRepository.save(user);

            KeycloakUserRequest keycloakUser = KeycloakUserRequest.builder()
                    .username(request.email())
                    .firstName(request.prenom())
                    .lastName(request.nom())
                    .email(request.email())
                    .enabled(true)
                    .credentials(List.of(new KeycloakUserRequest.Credential("password", request.password(), false)))
//                    .roles(new ArrayList<>(request.roles()))
                    .build();

            ResponseEntity<Void> response = keycloakClient.createUser(tokenProvider.getToken(), keycloakUser);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                List<KeycloakUserResponse> keycloakUserResponse = keycloakClient.getUserByUsername(tokenProvider.getToken(), request.email());
                if (!keycloakUserResponse.isEmpty()) {
                    String ID = keycloakUserResponse.getFirst().getId();
                     keycloakClient.assignRolesToUser(tokenProvider.getToken(),ID, keycloakRoles);
                }
            }
            return MapperService.mapToEntity(user, UserResponseDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
