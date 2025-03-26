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
import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.request.UserDemandeRequestDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseDto;
import com.codev13.electrosign13back.web.dto.response.DemandeResponseListDto;
import com.codev13.electrosign13back.web.dto.response.ParticipantDto;
import com.codev13.electrosign13back.web.dto.response.UserInfoDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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
            int i = 0;
                // Save demande
            assert request.file() != null;
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
                                .initSize(request.file().getSize())
                                .updateSize(0)
                                .build());

                // Save approbateurs
                if (request.approbateurs() != null) {
                    for(UserDemandeRequestDto approbateur : request.approbateurs()){
                        User user = userRepository.findById(approbateur.id()).orElseThrow(() -> new UserNotFoundException("Approbateur inexistant"));
                        demandeSignatureRepository.save(DemandeSignature.builder()
                                .demande(demande)
                                .sender(sender)
                                .receiver(user)
                                .action(DemandeSignatureActions.APPROUVER)
                                .ordre(approbateur.ordre())
                                .detenant(approbateur.ordre() == 1 ? 1 : 0)
                                .build());
                        i++;
                    }
                }
                // Save Signataires
                for(UserDemandeRequestDto signataire : request.signataires()){
                    User user = userRepository.findById(signataire.id()).orElseThrow(() -> new UserNotFoundException("Signataire inexistant"));
                    demandeSignatureRepository.save(DemandeSignature.builder()
                                    .demande(demande)
                                    .sender(sender)
                                    .receiver(user)
                                    .action(DemandeSignatureActions.SIGNER)
                                    .ordre(i + signataire.ordre())
                                    .detenant(i + signataire.ordre() == 1 ? 1 : 0)
                                    .build());
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
                                .ordre(0)
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

    /**
     * Récupère les demandes envoyées par l'utilisateur connecté
     */
    public List<DemandeResponseListDto> getDemandesEnvoyees() {
        User currentUser = userRepository.findByEmail(tokenProvider.getEmailFromToken()).orElseThrow(
                () -> new UserNotFoundException("Demandeur inexistant")
        );
        List<DemandeSignature> demandeSignatures = demandeSignatureRepository.findBySenderId(currentUser.getId());

        // Regrouper par demande pour éviter les doublons
        Map<Long, List<DemandeSignature>> demandeMap = demandeSignatures.stream()
                .collect(Collectors.groupingBy(ds -> ds.getDemande().getId()));

        List<DemandeResponseListDto> result = new ArrayList<>();

        for (Map.Entry<Long, List<DemandeSignature>> entry : demandeMap.entrySet()) {
            Demande demande = entry.getValue().get(0).getDemande();
            result.add(mapToDemandeResponseDto(demande, currentUser));
        }

        return result;
    }

    /**
     * Récupère les demandes reçues par l'utilisateur connecté
     */
    public List<DemandeResponseListDto> getDemandesRecues() {
        User currentUser = userRepository.findByEmail(tokenProvider.getEmailFromToken()).orElseThrow(
                () -> new UserNotFoundException("Demandeur inexistant")
        );
        List<DemandeSignature> demandeSignatures = demandeSignatureRepository.findByReceiverId(currentUser.getId());

        // Regrouper par demande pour éviter les doublons
        Map<Long, List<DemandeSignature>> demandeMap = demandeSignatures.stream()
                .collect(Collectors.groupingBy(ds -> ds.getDemande().getId()));

        List<DemandeResponseListDto> result = new ArrayList<>();

        for (Map.Entry<Long, List<DemandeSignature>> entry : demandeMap.entrySet()) {
            Demande demande = entry.getValue().get(0).getDemande();
            result.add(mapToDemandeResponseDto(demande, currentUser));
        }

        return result;
    }

    /**
     * Convertit une entité Demande en DTO avec les informations sur les participants
     */
    private DemandeResponseListDto mapToDemandeResponseDto(Demande demande, User currentUser) {
        List<DemandeSignature> allSignatures = demande.getDemandeSignatures();

        // Trouver l'expéditeur (créateur de la demande)
        User sender = allSignatures.stream()
                .filter(ds -> ds.getSender() != null)
                .map(DemandeSignature::getSender)
                .findFirst()
                .orElse(null);

        // Récupérer l'URL du document principal
        String documentUrl = "";
        if (demande.getDocuments() != null && !demande.getDocuments().isEmpty()) {
            Document mainDoc = demande.getDocuments().stream()
                    .findFirst()
                    .orElse(null);
            if (mainDoc != null) {
                documentUrl = mainDoc.getLinkCloud();
            }
        }

        // Vérifier si l'utilisateur courant est le signataire actuel
        boolean isCurrentUserSigner = false;
        boolean isCurrentUserApprobateur = false;

        // Grouper les participants par rôle
        List<ParticipantDto> signataires = new ArrayList<>();
        List<ParticipantDto> approbateurs = new ArrayList<>();
        List<ParticipantDto> ampliateurs = new ArrayList<>();

        for (DemandeSignature signature : allSignatures) {
            User receiver = signature.getReceiver();
            if (receiver != null) {
                boolean isCurrentUser = receiver.getId().equals(currentUser.getId());
                boolean isCurrentSigner = signature.getDetenant() == 1;

                // Vérifier si l'utilisateur courant est le signataire ou approbateur actuel
                if (isCurrentUser && isCurrentSigner) {
                    if (signature.getAction() == DemandeSignatureActions.SIGNER) {
                        isCurrentUserSigner = true;
                    } else if (signature.getAction() == DemandeSignatureActions.APPROUVER) {
                        isCurrentUserApprobateur = true;
                    }
                }

                ParticipantDto participant = ParticipantDto.builder()
                        .id(receiver.getId())
                        .name(receiver.getPrenom() + " " + receiver.getNom())
                        .email(receiver.getEmail())
                        .role(signature.getAction().toString())
                        .hasSigned(signature.getDetenant() == 2) // 2 = déjà signé
                        .isCurrentSigner(isCurrentSigner) // 1 = en attente de signature
                        .ordre(signature.getOrdre())
                        .action(signature.getAction().toString())
                        .build();

                if (signature.getAction() == DemandeSignatureActions.SIGNER) {
                    signataires.add(participant);
                } else if (signature.getAction() == DemandeSignatureActions.APPROUVER) {
                    approbateurs.add(participant);
                } else if (signature.getAction() == DemandeSignatureActions.AMPLIER) {
                    ampliateurs.add(participant);
                }
            }
        }

        // Trier les participants par ordre
        signataires.sort((p1, p2) -> Integer.compare(p1.getOrdre(), p2.getOrdre()));
        approbateurs.sort((p1, p2) -> Integer.compare(p1.getOrdre(), p2.getOrdre()));
        ampliateurs.sort((p1, p2) -> Integer.compare(p1.getOrdre(), p2.getOrdre()));

        // Créer le DTO de l'expéditeur
        UserInfoDto senderDto = null;
        if (sender != null) {
            senderDto = UserInfoDto.builder()
                    .id(sender.getId())
                    .nom(sender.getNom())
                    .prenom(sender.getPrenom())
                    .email(sender.getEmail())
                    .fonction(sender.getFonction() != null ? sender.getFonction().getLibelle() : "")
                    .acronyme(sender.getFonction() != null ? sender.getFonction().getAcronyme() : "")
                    .build();
        }

        return DemandeResponseListDto.builder()
                .id(demande.getId())
                .titre(demande.getTitre())
                .description(demande.getDescription())
                .dateCreated(demande.getCreatedAt())
                .dateLimite(demande.getDateLimite())
                .status(demande.getStatus())
                .priority(demande.getPriority())
                .documentUrl(documentUrl)
                .signataires(signataires)
                .approbateurs(approbateurs)
                .ampliateurs(ampliateurs)
                .sender(senderDto)
                .isCurrentUserSigner(isCurrentUserSigner)
                .isCurrentUserApprobateur(isCurrentUserApprobateur)
                .build();
    }
}
