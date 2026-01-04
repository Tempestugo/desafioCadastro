package test;

import dominio.Endereco;
import dominio.Pet;
import dominio.SexoPet;
import dominio.TipoPet;
import repository.PetRepository;
import service.PetService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class SistemaPet {

    public static void main(String[] args) {
        PetService petService = new PetService();
        PetRepository petRepository = new PetRepository();
        Endereco endereco = new Endereco("Limao",10,"MG","Xique-xique");
        Pet pet = new Pet("Pedro",20,10, TipoPet.CACHORRO, SexoPet.FEMEA,endereco);
        Scanner leitor = new Scanner(System.in);

        PetRepository.cadastrarPet(pet);





        Path pastaInicial = Paths.get("./petsCadastrados");




        try {
            Files.walkFileTree(pastaInicial, new PetRepository.LeitorArquivos());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        try {
//            petService.executarCadastro(leitor);
//        } catch (RegraDeNegocioException e) {
//            System.out.println("Erro ao cadastrar: " + e.getMessage());
//
//        }
    }


}
