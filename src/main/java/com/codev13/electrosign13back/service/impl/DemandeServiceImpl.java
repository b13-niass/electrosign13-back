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
import com.codev13.electrosign13back.exception.demande.DemandeNotFoundException;
import com.codev13.electrosign13back.exception.document.DocumentNotFoundException;
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
     * Approuve une demande et passe au signataire suivant si tous les approbateurs ont approuvé
     */
    @Transactional
    public void approuverDemande(Long demandeId, Long userId) {
        Demande demande = repository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        // Vérifier que l'utilisateur est bien l'approbateur actuel
        DemandeSignature approbation = demandeSignatureRepository.findByDemandeIdAndReceiverIdAndAction(
                        demandeId, userId, DemandeSignatureActions.APPROUVER)
                .orElseThrow(() -> new RuntimeException("Vous n'êtes pas autorisé à approuver cette demande"));

        if (approbation.getDetenant() != 1) {
            throw new RuntimeException("Ce n'est pas votre tour d'approuver cette demande");
        }

        // Marquer l'approbation comme effectuée
        approbation.setDetenant(2); // 2 = approuvé
        demandeSignatureRepository.save(approbation);

        // Mettre à jour le compteur d'approbation
        demande.setCurrentApprobation(demande.getCurrentApprobation() + 1);

        // Vérifier si tous les approbateurs ont approuvé
        if (demande.getCurrentApprobation() >= demande.getNombreApprobation()) {
            // Passer au premier signataire
            List<DemandeSignature> signataires = demandeSignatureRepository.findByDemandeIdAndActionOrderByOrdre(
                    demandeId, DemandeSignatureActions.SIGNER);

            if (!signataires.isEmpty()) {
                DemandeSignature premierSignataire = signataires.get(0);
                premierSignataire.setDetenant(1); // 1 = en attente de signature
                demandeSignatureRepository.save(premierSignataire);

                // Mettre à jour le statut de la demande
                demande.setStatus(StatusDemande.EN_ATTENTE_SIGNATURE);
            } else {
                // Pas de signataires, la demande est considérée comme approuvée
                demande.setStatus(StatusDemande.APPROUVEE);
            }
        }

        repository.save(demande);
    }

    /**
     * Signe une demande et passe au signataire suivant
     */
    @Transactional
    public void signerDemande(Long demandeId, Long userId) {
        Demande demande = repository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        // Vérifier que l'utilisateur est bien le signataire actuel
        DemandeSignature signature = demandeSignatureRepository.findByDemandeIdAndReceiverIdAndAction(
                        demandeId, userId, DemandeSignatureActions.SIGNER)
                .orElseThrow(() -> new RuntimeException("Vous n'êtes pas autorisé à signer cette demande"));

        if (signature.getDetenant() != 1) {
            throw new RuntimeException("Ce n'est pas votre tour de signer cette demande");
        }

        // Marquer la signature comme effectuée
        signature.setDetenant(2); // 2 = signé
        demandeSignatureRepository.save(signature);

        // Mettre à jour le compteur de signature
        demande.setCurrentSignature(demande.getCurrentSignature() + 1);

        // Vérifier s'il y a un signataire suivant
        if (demande.getCurrentSignature() < demande.getNombreSignature()) {
            // Trouver le prochain signataire
            List<DemandeSignature> signataires = demandeSignatureRepository.findByDemandeIdAndActionOrderByOrdre(
                    demandeId, DemandeSignatureActions.SIGNER);

            Optional<DemandeSignature> prochainSignataire = signataires.stream()
                    .filter(s -> s.getDetenant() == 0) // 0 = pas encore signé
                    .min(Comparator.comparing(DemandeSignature::getOrdre));

            if (prochainSignataire.isPresent()) {
                prochainSignataire.get().setDetenant(1); // 1 = en attente de signature
                demandeSignatureRepository.save(prochainSignataire.get());
            }
        } else {
            // Tous les signataires ont signé, la demande est signée
            demande.setStatus(StatusDemande.SIGNEE);

            // Notifier les ampliateurs si nécessaire
            if (demande.getNombreAmpliateur() > 0) {
                List<DemandeSignature> ampliateurs = demandeSignatureRepository.findByDemandeIdAndAction(
                        demandeId, DemandeSignatureActions.AMPLIER);

                for (DemandeSignature ampliateur : ampliateurs) {
                    ampliateur.setDetenant(1); // 1 = en attente d'ampliation
                    demandeSignatureRepository.save(ampliateur);
                }
            }
        }

        repository.save(demande);
    }

    /**
     * Refuse une demande (approbateur ou signataire)
     */
    @Transactional
    public void refuserDemande(Long demandeId, Long userId, String motif) {
        Demande demande = repository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        // Vérifier que l'utilisateur est bien un approbateur ou signataire actuel
        DemandeSignature participation = demandeSignatureRepository.findByDemandeIdAndReceiverId(demandeId, userId)
                .orElseThrow(() -> new RuntimeException("Vous n'êtes pas associé à cette demande"));

        if (participation.getDetenant() != 1) {
            throw new RuntimeException("Ce n'est pas votre tour d'agir sur cette demande");
        }

        // Marquer la demande comme refusée
        demande.setStatus(StatusDemande.REFUSEE);
        repository.save(demande);

        // Enregistrer le motif de refus (à implémenter selon votre modèle de données)
        // ...
    }

    @Override
    public DemandeResponseListDto getDemandeById(Long demandeId) {
        User currentUser = userRepository.findByEmail(tokenProvider.getEmailFromToken())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        Demande demande = repository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée avec l'ID: " + demandeId));

        if (!demande.isActive()) {
            throw new DemandeNotFoundException("Cette demande n'est plus active");
        }

        return mapToDemandeResponseDto(demande, currentUser);
    }

    @Override
    public Document getDocumentByDemandeId(Long demandeId, TypeDocument type) {
        User currentUser = userRepository.findByEmail(tokenProvider.getEmailFromToken())
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));

        Demande demande = repository.findById(demandeId)
                .orElseThrow(() -> new DemandeNotFoundException("Demande non trouvée avec l'ID: " + demandeId));

        if (!demande.isActive()) {
            throw new DemandeNotFoundException("Cette demande n'est plus active");
        }

        boolean hasAccess = demandeSignatureRepository.existsBySenderIdOrReceiverIdAndDemandeId(
                currentUser.getId(), currentUser.getId(), demandeId);

        if (!hasAccess) {
            throw new RuntimeException("Vous n'avez pas accès à ce document");
        }

        Optional<Document> document;
        if (type == null) {
            document = documentRepository.findFirstByDemandeIdAndTypeAndActiveTrue(
                    demandeId, TypeDocument.SIGNATURE);
        } else {
            document = documentRepository.findFirstByDemandeIdAndTypeAndActiveTrue(
                    demandeId, type);
        }

        return document.orElseThrow(() ->
                new DocumentNotFoundException("Document non trouvé pour la demande avec l'ID: " + demandeId));
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
                    participant.setSignature(receiver.getMySignature());
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
