package test;

import dominio.Endereco;
import dominio.Pet;
import dominio.SexoPet;
import dominio.TipoPet;
import repositorioo.PetRepository;
import service.PetService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static repositorioo.PetRepository.*;
import static service.PetService.*;

public class SistemaPet {

    public static void main(String[] args) {


        PetRepository petRepository = new PetRepository();
        Endereco endereco = new Endereco("Limao",10,"MG","Xique-xique");
        Pet pet = new Pet("Pedro",20,10, TipoPet.CACHORRO, SexoPet.FEMEA,endereco,"Golden");
        Path pastaInicial = Paths.get("./petsCadastrados");


    Scanner leitor = new Scanner(System.in);
    int opcao = 0;

        do {
        exibirMenu();
        opcao = leitor.nextInt();
        leitor.nextLine();

        switch (opcao) {
            case 1:
                Pet p = new Pet();
                cadastrarPet(leitor, p);
                break;
            case 2:
                alterarPet(leitor);
                break;
            case 3:
                deletarPet(leitor);
                break;
            case 4:
                PetService petService1 = new PetService();
                System.out.println("--- Lista de Pets ---");
                List<Pet> pets = petService1.buscarTodos();

                if (pets.isEmpty()) {
                    System.out.println("Nenhum pet cadastrado.");
                } else {
                    pets.forEach(pe -> System.out.println(pe));
                }
                break;
            case 5:
                PetService petService = new PetService();

                menuBusca(leitor, petService);

                break;
            case 0:
                System.out.println("Encerrando o sistema...");
                break;
            default:
                System.out.println("Opção inválida! Tente novamente.");
        }
    } while (opcao != 0);

        leitor.close();


//        try {
//            petService.executarCadastro(leitor);
//        } catch (RegraDeNegocioException e) {
//            System.out.println("Erro ao cadastrar: " + e.getMessage());
//
//        }
    }
    public static void menuBusca(Scanner scanner, PetService petService) {
        System.out.println("\n--- BUSCA DE PETS ---");

        System.out.println("Qual o tipo de animal? (1-Cachorro, 2-Gato)");
        int inputTipo = scanner.nextInt();
        scanner.nextLine();

        TipoPet tipoSelecionado = null;

        switch (inputTipo) {
            case 1:
                tipoSelecionado = TipoPet.GATO;
                break;
            case 2:
                tipoSelecionado = TipoPet.CACHORRO;
                break;
            default:
                System.out.println("Opção inválida. O tipo será ignorado na busca.");
        }

        System.out.println("Digite o NOME (ou Enter para ignorar):");
        String nome = scanner.nextLine();
        if (nome.isBlank()) nome = null;

        System.out.println("Digite a RAÇA (ou Enter para ignorar):");
        String raca = scanner.nextLine();
        if (raca.isBlank()) raca = null;

        System.out.println("Digite a IDADE (ou 0 para ignorar):");
        String idadeStr = scanner.nextLine();
        Integer idade = null;
        if (!idadeStr.isBlank()) {
            idade = Integer.parseInt(idadeStr);
            if (idade == 0) idade = null;
        }

        SexoPet sexoPet = null;

        switch (inputTipo) {
            case 1:
                sexoPet = SexoPet.FEMEA;
                break;
            case 2:
                sexoPet = SexoPet.MACHO;
                break;
            default:
                System.out.println("Opção inválida. O tipo será ignorado na busca.");
        }



        List<Pet> petsEncontrados = petService.buscarPets(nome,raca,idade,sexoPet);

        if (petsEncontrados.isEmpty()) {
            System.out.println("Nenhum pet encontrado com esses critérios.");
        } else {
            System.out.println("\nResultados Encontrados:");
            petsEncontrados.forEach(System.out::println);
        }
    }

}
