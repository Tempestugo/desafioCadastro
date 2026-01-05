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

public class SistemaPet {

    public static void main(String[] args) {




        PetService petService = new PetService();
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
                try {
                    Files.walkFileTree(pastaInicial, new PetRepository.listarTodos());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                break;
            case 5:
                System.out.println("\n--- FILTROS DE BUSCA (Aperte ENTER para pular um filtro) ---");

                System.out.print("Filtrar por Nome: ");
                String nomeFiltro = leitor.nextLine();

                System.out.print("Filtrar por Raça: ");
                String racaFiltro = leitor.nextLine();

                System.out.print("Filtrar por Idade (ou 0 para pular): ");
                String idadeInput = leitor.nextLine();
                int idadeFiltro = idadeInput.isEmpty() ? 0 : Integer.parseInt(idadeInput);

//                List<Pet> todosOsPets = carregarPetsDosArquivos();
//                aplicarFiltros(todosOsPets, nomeFiltro, racaFiltro, idadeFiltro);

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


}
