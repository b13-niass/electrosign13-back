package com.codev13.electrosign13back.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
import com.codev13.electrosign13back.exception.demande.DemandeUnauthorizedException;
import com.codev13.electrosign13back.exception.document.DocumentNotFoundException;
import com.codev13.electrosign13back.exception.user.UserNotFoundException;
import com.codev13.electrosign13back.service.DemandeService;
import com.codev13.electrosign13back.service.MessageService;
import com.codev13.electrosign13back.service.TokenProvider;
import com.codev13.electrosign13back.utils.FIleManageUtil;
import com.codev13.electrosign13back.web.dto.request.DemandeCreateRequestDto;
import com.codev13.electrosign13back.web.dto.request.SignerDemandeRequestDto;
import com.codev13.electrosign13back.web.dto.request.UserDemandeRequestDto;
import com.codev13.electrosign13back.web.dto.response.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DemandeServiceImpl implements DemandeService {
    private final DemandeRepository repository;
    private final DemandeSignatureRepository demandeSignatureRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final FileStorageServiceManager fileStorageService;
    private final TokenProvider tokenProvider;
    private final MessageService messageService;
    @Value("${storage.local.path}")
    private String localPath;
    private final Cloudinary cloudinary;

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
                String absolutePath = fileStorageService.getActiveStorageService().uploadFile(request.file(), "nonsigner");
                String currentStorage = fileStorageService.getCurrentStorageType();
                byte[] docBytes = request.file().getBytes();
                String docBase64 = Base64.getEncoder().encodeToString(docBytes);
                Document docSignature = documentRepository.save(Document.builder()
                                    .demande(demande)
                                    .linkCloud(currentStorage.equals("cloud") ? absolutePath: "" )
                                    .linkLocal(currentStorage.equals("local") ? absolutePath: "")
                                    .nom(request.titre())
                                    .type(TypeDocument.SIGNATURE)
                                    .etatDocument(EtatDocument.DISPONIBLE)
                                    .contenu(docBase64)
                                    .build());
                if (request.approbateurs() != null) {
                    for(UserDemandeRequestDto approbateur : request.approbateurs()){
                        User user = userRepository.findById(approbateur.id()).orElseThrow(() -> new UserNotFoundException("Approbateur inexistant"));
                        envoyerNotificationNouvelleDemande(
                                user.getEmail(),
                                user.getPrenom()+" "+user.getNom(),
                                absolutePath,
                                sender.getPrenom()+" "+sender.getNom()
                                );
                    }
                }
                for(UserDemandeRequestDto signataire : request.signataires()){
                    User user = userRepository.findById(signataire.id()).orElseThrow(() -> new UserNotFoundException("Signataire inexistant"));
                    envoyerNotificationNouvelleDemande(
                            user.getEmail(),
                            user.getPrenom()+" "+user.getNom(),
                            docSignature.getNom(),
                            sender.getPrenom()+" "+sender.getNom()
                    );
                }
                if (request.ampliateurs() != null) {
                    for (UserDemandeRequestDto ampliateur : request.ampliateurs()) {
                        User user = userRepository.findById(ampliateur.id()).orElseThrow(() -> new UserNotFoundException("Ampliateur inexistant"));
                        envoyerNotificationNouvelleDemande(
                                user.getEmail(),
                                user.getPrenom()+" "+user.getNom(),
                                absolutePath,
                                sender.getPrenom()+" "+sender.getNom()
                        );
                    }
                }
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
     * Signe une demande et passe au signataire suivant
     */
    @Transactional
    public DemandeResponseListDto signerDemande(SignerDemandeRequestDto request) {
        try {

            User currentUser = userRepository.findByEmail(tokenProvider.getEmailFromToken()).orElseThrow(
                    () -> new UserNotFoundException("Demandeur inexistant")
            );
            Demande demande = repository.findById(request.demandeId())
                    .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
            System.out.println("OUI" + demande.getId());

            Document document = documentRepository.findFirstByDemandeIdAndTypeAndActiveTrue(request.demandeId(), TypeDocument.SIGNATURE).orElseThrow(
                    () -> new DocumentNotFoundException("Document non trouvé pour cette demande"));

            DemandeSignature signature = demandeSignatureRepository.findByDemandeIdAndReceiverIdAndAction(
                            request.demandeId(), currentUser.getId(), DemandeSignatureActions.SIGNER)
                    .orElseThrow(() -> new DemandeUnauthorizedException("Vous n'êtes pas autorisé à signer cette demande"));

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
                        request.demandeId(), DemandeSignatureActions.SIGNER);

                Optional<DemandeSignature> prochainSignataire = signataires.stream()
                        .filter(s -> s.getDetenant() == 0)
                        .min(Comparator.comparing(DemandeSignature::getOrdre));

                if (prochainSignataire.isPresent()) {
                    prochainSignataire.get().setDetenant(1);
                    demandeSignatureRepository.save(prochainSignataire.get());
                    envoyerInvitationSignature(
                            prochainSignataire.get().getReceiver().getEmail(),
                            prochainSignataire.get().getReceiver().getPrenom() +" "+ prochainSignataire.get().getReceiver().getNom(),
                            document.getNom(),
                            prochainSignataire.get().getSender().getPrenom() +" "+ prochainSignataire.get().getSender().getNom()
                    );
                }
            } else {
                demande.setStatus(StatusDemande.SIGNEE);

                if (demande.getNombreAmpliateur() > 0) {
                    List<DemandeSignature> ampliateurs = demandeSignatureRepository.findByDemandeIdAndAction(
                            request.demandeId(), DemandeSignatureActions.AMPLIER);

                    for (DemandeSignature ampliateur : ampliateurs) {
                        ampliateur.setDetenant(1);
                        demandeSignatureRepository.save(ampliateur);
                    }
                }
            }

            repository.save(demande);
            if (currentUser.getMySignature() == null && request.signature() != null) {
                currentUser.setMySignature(request.signature());
                userRepository.save(currentUser);
            }

            String absolutePath = fileStorageService.getActiveStorageService().uploadFile(request.file(), "signer");
            String currentStorage = fileStorageService.getCurrentStorageType();
            byte[] docBytes = request.file().getBytes();
            String docBase64 = Base64.getEncoder().encodeToString(docBytes);

            boolean isFileDelete = FIleManageUtil.deleteFile(document.getLinkLocal());

            document.setContenu(docBase64);
            document.setLinkCloud(currentStorage.equals("cloud") ? absolutePath: document.getLinkCloud() );
            document.setLinkLocal(currentStorage.equals("local") ? absolutePath: document.getLinkLocal());

            documentRepository.save(document);

            return mapToDemandeResponseDto(demande, currentUser);
        }catch (Exception e){
            throw new DemandeInternalServerException(e.getMessage());
        }
    }


    /**
     * Approuve une demande et passe au signataire suivant si tous les approbateurs ont approuvé
     */
    @Transactional
    public DemandeResponseListDto approuverDemande(Long demandeId) {
        User currentUser = userRepository.findByEmail(tokenProvider.getEmailFromToken()).orElseThrow(
                () -> new UserNotFoundException("Demandeur inexistant")
        );
        Demande demande = repository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        // Vérifier que l'utilisateur est bien l'approbateur actuel
        DemandeSignature approbation = demandeSignatureRepository.findByDemandeIdAndReceiverIdAndAction(
                        demandeId, currentUser.getId(), DemandeSignatureActions.APPROUVER)
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
                Document document = documentRepository.findFirstByDemandeIdAndTypeAndActiveTrue(premierSignataire.getDemande().getId(), TypeDocument.SIGNATURE).get();
                envoyerInvitationSignature(
                        premierSignataire.getReceiver().getEmail(),
                        premierSignataire.getReceiver().getPrenom() +" "+ premierSignataire.getReceiver().getNom(),
                        document.getNom(),
                        premierSignataire.getSender().getPrenom() +" "+ premierSignataire.getSender().getNom()
                );
                // Mettre à jour le statut de la demande
                demande.setStatus(StatusDemande.EN_ATTENTE_SIGNATURE);
            } else {
                // Pas de signataires, la demande est considérée comme approuvée
                demande.setStatus(StatusDemande.APPROUVEE);
            }
        }else{
            List<DemandeSignature> approbateurs = demandeSignatureRepository.findByDemandeIdAndActionOrderByOrdre(
                    demandeId, DemandeSignatureActions.APPROUVER);
            for (DemandeSignature ds: approbateurs) {
               if (ds.getOrdre() == approbation.getOrdre()+1){
                   ds.setDetenant(1);
                   demandeSignatureRepository.save(ds);
                   Document document = documentRepository.findFirstByDemandeIdAndTypeAndActiveTrue(ds.getDemande().getId(), TypeDocument.SIGNATURE).get();
                   envoyerDemandeApprobation(
                           ds.getReceiver().getEmail(),
                           ds.getReceiver().getPrenom()+" "+ds.getReceiver().getNom(),
                           document.getNom(),
                           "Electro-Sign"
                   );
                   break;
               }
            };
        }

        repository.save(demande);
        return mapToDemandeResponseDto(demande, currentUser);
    }

    /**
     * Refuse une demande (approbateur ou signataire)
     */

    @Transactional
    public DemandeResponseListDto rejeterDemande(Long demandeId) {
        User currentUser = userRepository.findByEmail(tokenProvider.getEmailFromToken()).orElseThrow(
                () -> new UserNotFoundException("Demandeur inexistant")
        );
        Demande demande = repository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        // Vérifier que l'utilisateur est bien un approbateur ou signataire actuel
        DemandeSignature participation = demandeSignatureRepository.findByDemandeIdAndReceiverId(demandeId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Vous n'êtes pas associé à cette demande"));

        if (participation.getDetenant() != 1) {
            throw new RuntimeException("Ce n'est pas votre tour d'agir sur cette demande");
        }

        // Marquer la demande comme refusée
        demande.setStatus(StatusDemande.REFUSEE);
        repository.save(demande);
        return mapToDemandeResponseDto(demande, currentUser);
    }

    /**
     * Archive les documents des demandes signées vers Cloudinary
     * @return ArchiveResultDto contenant les résultats de l'archivage
     */
    @Transactional
    public ArchiveResultDto archiveDocuments () {
        log.info("Début du processus d'archivage des documents signés");

        // Récupérer toutes les demandes avec le statut SIGNEE
        List<Demande> signedDemandes = repository.findByStatus(StatusDemande.SIGNEE);

        List<Document> documentsToArchive = signedDemandes.stream()
                .flatMap(demande -> demande.getDocuments().stream())
                .filter(document -> document.getEtatDocument() != EtatDocument.ARCHIVE)
                .collect(Collectors.toList());

        log.info("Nombre de documents à archiver : {}", documentsToArchive.size());

        List<String> successfullyArchived = new ArrayList<>();
        List<String> failedToArchive = new ArrayList<>();

        for (Document document : documentsToArchive) {
            try {
                // Construire le chemin complet du fichier
                String filePath = localPath + File.separator + document.getLinkLocal();
                File documentFile = new File(filePath);

                if (!documentFile.exists()) {
                    log.error("Le fichier n'existe pas : {}", filePath);
                    failedToArchive.add(document.getNom());
                    continue;
                }

                // Upload vers Cloudinary
                Map uploadResult = cloudinary.uploader().upload(documentFile, ObjectUtils.emptyMap());
                String cloudUrl = (String) uploadResult.get("secure_url");

                // Mettre à jour le document
                document.setLinkCloud(cloudUrl);
                document.setEtatDocument(EtatDocument.ARCHIVE);
                documentRepository.save(document);

                successfullyArchived.add(document.getNom());
                log.info("Document archivé avec succès : {}", document.getNom());
            } catch (IOException e) {
                log.error("Erreur lors de l'archivage du document : {}", document.getNom(), e);
                failedToArchive.add(document.getNom());
            }
        }

        // Créer et retourner le DTO de résultat
        ArchiveResultDto result = new ArchiveResultDto();
        result.setTotalDocuments(documentsToArchive.size());
        result.setSuccessfullyArchived(successfullyArchived);
        result.setFailedToArchive(failedToArchive);

        log.info("Fin du processus d'archivage. Documents archivés : {}, Échecs : {}",
                successfullyArchived.size(), failedToArchive.size());

        return result;
    }

    /**
     * Récupère les statistiques d'archivage
     * @return ArchiveStatsDto contenant les statistiques
     */
    public ArchiveStatsDto getArchiveStats() {
        log.info("Récupération des statistiques d'archivage");

        // Compter les documents signés (dans les demandes avec statut SIGNEE)
        List<Demande> signedDemandes = repository.findByStatus(StatusDemande.SIGNEE);
        int signedDocumentsCount = signedDemandes.stream()
                .flatMap(demande -> demande.getDocuments().stream())
                .filter(document -> document.getEtatDocument() != EtatDocument.ARCHIVE)
                .collect(Collectors.toList())
                .size();

        // Compter les documents archivés
        int archivedDocumentsCount = documentRepository.countByEtatDocument(EtatDocument.ARCHIVE);

        log.info("Statistiques d'archivage - Documents signés : {}, Documents archivés : {}",
                signedDocumentsCount, archivedDocumentsCount);

        return ArchiveStatsDto.builder()
                .signedDocuments(signedDocumentsCount)
                .archivedDocuments(archivedDocumentsCount)
                .build();
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

    // Exemple 1: Nouvelle demande de signature
    public void envoyerNotificationNouvelleDemande(String email, String nomDestinataire, String documentName, String senderName) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("salutation", "Bonjour " + nomDestinataire + ",");
        messageData.put("documentName", documentName);
        messageData.put("senderName", senderName);
        messageData.put("sendDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        messageData.put("dueDate", new SimpleDateFormat("dd/MM/yyyy").format(
                new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))); // +7 jours
        messageData.put("actionUrl", "http://localhost:5173/demandes");
        messageData.put("expirationTime", "48 heures");
        messageData.put("logoExists", true);

        try {
            messageService.sendMailWithThymeleaf(email, "nouvelle_demande", messageData);
        } catch (Exception e) {
            // Gérer l'exception
        }
    }

    // Exemple 2: Demande d'approbation
    public void envoyerDemandeApprobation(String email, String nomDestinataire, String documentName, String senderName) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("salutation", "Bonjour " + nomDestinataire + ",");
        messageData.put("documentName", documentName);
        messageData.put("senderName", senderName);
        messageData.put("sendDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        messageData.put("dueDate", new SimpleDateFormat("dd/MM/yyyy").format(
                new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000))); // +5 jours
        messageData.put("actionUrl", "http://localhost:5173/demandes");
        messageData.put("expirationTime", "72 heures");
        messageData.put("logoExists", true);

        try {
            messageService.sendMailWithThymeleaf(email, "approbation", messageData);
        } catch (Exception e) {
            // Gérer l'exception
        }
    }

    // Exemple 3: Invitation à signer
    public void envoyerInvitationSignature(String email, String nomDestinataire, String documentName, String senderName) {
        Map<String, Object> messageData = new HashMap<>();
        messageData.put("salutation", "Bonjour " + nomDestinataire + ",");
        messageData.put("documentName", documentName);
        messageData.put("senderName", senderName);
        messageData.put("sendDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        messageData.put("dueDate", new SimpleDateFormat("dd/MM/yyyy").format(
                new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000))); // +3 jours
        messageData.put("actionUrl", "http://localhost:5173/demandes");
        messageData.put("expirationTime", "24 heures");
        messageData.put("logoExists", true);

        try {
            messageService.sendMailWithThymeleaf(email, "signature", messageData);
        } catch (Exception e) {
            // Gérer l'exception
        }
    }
}
