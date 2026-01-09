package test;

import dominio.Endereco;
import dominio.Pet;
import dominio.SexoPet;
import dominio.TipoPet;
import exceptions.IdadeInvalidaException;
import repositorioo.PetRepository;
import service.PetService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static repositorioo.PetRepository.*;
import static service.PetService.*;

public class SistemaPet {

    public static void main(String[] args) {

    }

    private static void deletar(Scanner leitor) {
    }

    public static void menuBusca(Scanner scanner, PetService petService) {
        System.out.println("\n--- BUSCA DE PETS ---");

        System.out.println("Qual o tipo de animal? (1-Cachorro, 2-Gato)");
        int inputTipo = scanner.nextInt();
        scanner.nextLine();

        TipoPet tipoSelecionado = null;
        switch (inputTipo) {
            case 1:
                tipoSelecionado = TipoPet.CACHORRO;
                break;
            case 2:
                tipoSelecionado = TipoPet.GATO;
                break;
            default:
                System.out.println("Opção inválida. A busca pode não trazer resultados exatos.");
        }

        System.out.println("Digite o NOME (ou Enter para ignorar):");
        String nome = scanner.nextLine();
        if (nome.isBlank()) nome = null;

        System.out.println("Digite a RAÇA (ou Enter para ignorar):");
        String raca = scanner.nextLine();
        if (raca.isBlank()) raca = null;

        System.out.println("Digite a IDADE (ou Enter para ignorar):");
        String idadeStr = scanner.nextLine();
        Integer idade = null;
        if (!idadeStr.isBlank()) {
            try {
                idade = Integer.parseInt(idadeStr);
                if (idade == 0) idade = null;
            } catch (IdadeInvalidaException e) {
                System.out.println("Idade inválida, ignorando filtro de idade.");
            }
        }

        SexoPet sexoPet = null;
        System.out.println("Qual o sexo? (1-Macho, 2-Fêmea, ou Enter para ignorar):");
        String sexoStr = scanner.nextLine();

        if (!sexoStr.isBlank()) {
            try {
                int inputSexo = Integer.parseInt(sexoStr);
                switch (inputSexo) {
                    case 1:
                        sexoPet = SexoPet.MACHO;
                        break;
                    case 2:
                        sexoPet = SexoPet.FEMEA;
                        break;
                    default:
                        System.out.println("Opção de sexo inválida, ignorando este filtro.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, ignorando filtro de sexo.");
            }
        }

        List<Pet> petsEncontrados = petService.buscarPets(tipoSelecionado, nome, raca, idade, sexoPet);

        if (petsEncontrados.isEmpty()) {
            System.out.println("Nenhum pet encontrado com esses critérios.");
        } else {
            System.out.println("\nResultados Encontrados:");
            petsEncontrados.forEach(System.out::println);
        }
    }

}
