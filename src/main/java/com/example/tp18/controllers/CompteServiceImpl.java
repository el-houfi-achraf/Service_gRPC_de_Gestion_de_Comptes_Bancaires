package com.example.tp18.controllers;

import com.example.tp18.entities.Compte;
import com.example.tp18.services.CompteService;
import io.grpc.stub.StreamObserver;
import ma.projet.grpc.stubs.*;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;
import java.util.stream.Collectors;

@GrpcService
public class CompteServiceImpl extends CompteServiceGrpc.CompteServiceImplBase {
    private final CompteService compteService;

    public CompteServiceImpl(CompteService compteService) {
        this.compteService = compteService;
    }

    @Override
    public void allComptes(GetAllComptesRequest request, StreamObserver<GetAllComptesResponse> responseObserver) {
        var comptes = compteService.findAllComptes().stream()
                .map(compte -> ma.projet.grpc.stubs.Compte.newBuilder()
                        .setId(compte.getId())
                        .setSolde(compte.getSolde())
                        .setDateCreation(compte.getDateCreation())
                        .setType(TypeCompte.valueOf(compte.getType()))
                        .build())
                .collect(Collectors.toList());

        responseObserver.onNext(GetAllComptesResponse.newBuilder()
                .addAllComptes(comptes).build());
        responseObserver.onCompleted();
    }

    @Override
    public void compteById(GetCompteByIdRequest request, StreamObserver<GetCompteByIdResponse> responseObserver) {
        Compte compte = compteService.findCompteById(request.getId());
        if (compte != null) {
            var grpcCompte = ma.projet.grpc.stubs.Compte.newBuilder()
                    .setId(compte.getId())
                    .setSolde(compte.getSolde())
                    .setDateCreation(compte.getDateCreation())
                    .setType(TypeCompte.valueOf(compte.getType()))
                    .build();
            responseObserver.onNext(GetCompteByIdResponse.newBuilder()
                    .setCompte(grpcCompte).build());
        } else {
            responseObserver.onError(new Throwable("Compte non trouvé"));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void totalSolde(GetTotalSoldeRequest request, StreamObserver<GetTotalSoldeResponse> responseObserver) {
        var comptes = compteService.findAllComptes();
        int count = comptes.size();
        float sum = 0;
        for (Compte compte : comptes) {
            sum += compte.getSolde();
        }
        float average = count > 0 ? sum / count : 0;

        SoldeStats stats = SoldeStats.newBuilder()
                .setCount(count)
                .setSum(sum)
                .setAverage(average)
                .build();

        responseObserver.onNext(GetTotalSoldeResponse.newBuilder().setStats(stats).build());
        responseObserver.onCompleted();
    }

    @Override
    public void saveCompte(SaveCompteRequest request, StreamObserver<SaveCompteResponse> responseObserver) {
        CompteRequest compteReq = request.getCompte();

        var compte = new Compte();
        compte.setId(UUID.randomUUID().toString());
        compte.setSolde(compteReq.getSolde());
        compte.setDateCreation(compteReq.getDateCreation());
        compte.setType(compteReq.getType().name());

        var savedCompte = compteService.saveCompte(compte);

        var grpcCompte = ma.projet.grpc.stubs.Compte.newBuilder()
                .setId(savedCompte.getId())
                .setSolde(savedCompte.getSolde())
                .setDateCreation(savedCompte.getDateCreation())
                .setType(TypeCompte.valueOf(savedCompte.getType()))
                .build();

        responseObserver.onNext(SaveCompteResponse.newBuilder()
                .setCompte(grpcCompte).build());
        responseObserver.onCompleted();
    }
}

