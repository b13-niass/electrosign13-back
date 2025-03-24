package com.codev13.electrosign13back.service.impl;

import com.codev13.electrosign13back.data.entity.Demande;
import com.codev13.electrosign13back.data.entity.DemandeSignature;
import com.codev13.electrosign13back.data.entity.Document;
import com.codev13.electrosign13back.data.entity.User;
import com.codev13.electrosign13back.data.repository.DemandeRepository;
import com.codev13.electrosign13back.data.repository.DemandeSignatureRepository;
import com.codev13.electrosign13back.data.repository.DocumentRepository;
import com.codev13.electrosign13back.data.repository.UserRepository;
import com.codev13.electrosign13back.enums.DemandeSignatureActions;
import com.codev13.electrosign13back.enums.EtatDocument;
import com.codev13.electrosign13back.enums.StatusDemande;
import com.codev13.electrosign13back.enums.TypeDocument;
import com.codev13.electrosign13back.exception.demande.DemandeInternalServerException;
import com.codev13.electrosign13back.exception.user.UserNotFoundException;
import com.codev13.electrosign13back.service.DemandeService;
import com.codev13.electrosign13back.service.TokenProvider;
import com.codev13.electrosign13back.utils.DateUtils;
import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.request.UserDemandeRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DemandeServiceImpl implements DemandeService {
    private final DemandeRepository repository;
    private final DemandeSignatureRepository demandeSignatureRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final FileStorageServiceManager fileStorageService;
    private final TokenProvider tokenProvider;
    @Override
    @Transactional
    public DemandeResponseDto create(DemandeCreateRequestDto request) {
        try{
            User sender = userRepository.findByEmail(tokenProvider.getEmailFromToken()).orElseThrow(
                    () -> new UserNotFoundException("Demandeur inexistant")
            );
                // Save demande
                Demande demande = repository.save(Demande.builder()
                                .titre(request.titre())
                                .description(request.description())
                                .dateLimite(request.dateLimite())
                                .priority(request.priority())
                                .status(request.approbateurs()!=null && !request.approbateurs().isEmpty()? StatusDemande.EN_ATTENTE_APPROBATION : StatusDemande.EN_ATTENTE_SIGNATURE)
                                .nombreApprobation(Optional.ofNullable(request.approbateurs()).map(List::size).orElse(0))
                                .nombreSignature(request.signataires().size())
                                .nombreAmpliateur(Optional.ofNullable(request.ampliateurs()).map(List::size).orElse(0))
                                .currentApprobation(0)
                                .currentSignature(0)
                                .currentAmpliateur(0)
                                .build());
                // Save Signataires
                for(UserDemandeRequestDto signataire : request.signataires()){
                    User user = userRepository.findById(signataire.id()).orElseThrow(() -> new UserNotFoundException("Signataire inexistant"));
                    demandeSignatureRepository.save(DemandeSignature.builder()
                                    .demande(demande)
                                    .sender(sender)
                                    .receiver(user)
                                    .action(DemandeSignatureActions.SIGNER)
                                    .ordre(1)
                                    .detenant(0)
                                    .build());
                }
                // Save approbateurs
            if (request.approbateurs() != null) {
                for(UserDemandeRequestDto approbateur : request.approbateurs()){
                    User user = userRepository.findById(approbateur.id()).orElseThrow(() -> new UserNotFoundException("Approbateur inexistant"));
                    demandeSignatureRepository.save(DemandeSignature.builder()
                                    .demande(demande)
                                    .sender(sender)
                                    .receiver(user)
                                    .action(DemandeSignatureActions.APPROUVER)
                                    .ordre(2)
                                    .detenant(0)
                                    .build());
                }
            }
                // Save ampliateurs
            if (request.ampliateurs() != null) {
                for (UserDemandeRequestDto ampliateur : request.ampliateurs()) {
                    User user = userRepository.findById(ampliateur.id()).orElseThrow(() -> new UserNotFoundException("Ampliateur inexistant"));
                    demandeSignatureRepository.save(DemandeSignature.builder()
                            .demande(demande)
                            .sender(sender)
                            .receiver(user)
                            .action(DemandeSignatureActions.AMPLIER)
                            .ordre(3)
                            .detenant(0)
                            .build());
                }
            }
                // Save document
                String absolutePath = fileStorageService.getActiveStorageService().uploadFile(request.file(), "signatures");
                String currentStorage = fileStorageService.getCurrentStorageType();
                byte[] docBytes = request.file().getBytes();
                String docBase64 = Base64.getEncoder().encodeToString(docBytes);
                documentRepository.save(Document.builder()
                                    .demande(demande)
                                    .linkCloud(currentStorage.equals("cloud") ? absolutePath: "" )
                                    .linkLocal(currentStorage.equals("local") ? absolutePath: "")
                                    .nom(request.titre())
                                    .type(TypeDocument.SIGNATURE)
                                    .etatDocument(EtatDocument.DISPONIBLE)
                                    .contenu(docBase64)
                                    .build());

                if (request.fileAttachment() != null) {
                    for (MultipartFile file : request.fileAttachment()) {
                        absolutePath = fileStorageService.getActiveStorageService().uploadFile(file, "attachments");
                        currentStorage = fileStorageService.getCurrentStorageType();
                        docBytes = file.getBytes();
                        docBase64 = Base64.getEncoder().encodeToString(docBytes);
                        documentRepository.save(Document.builder()
                                .demande(demande)
                                .linkCloud(currentStorage.equals("cloud") ? absolutePath : "")
                                .linkLocal(currentStorage.equals("local") ? absolutePath : "")
                                .nom(file.getOriginalFilename())
                                .type(TypeDocument.ATTACHMENT)
                                .etatDocument(EtatDocument.DISPONIBLE)
                                .contenu(docBase64)
                                .build());
                    }
                }

                return DemandeResponseDto.builder()
                        .id(demande.getId())
                        .build();
        }catch (Exception e){
            throw new DemandeInternalServerException(e.getMessage());
        }
    }
}
