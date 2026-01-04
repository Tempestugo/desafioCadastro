package repository;


import dominio.Pet;
import dominio.SexoPet;
import dominio.TipoPet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
//File, FileWriter, BufferedWrite
// Métodos para escrever o arquivo do Pet (Passo 4),
// ler o arquivo formulario.txt (Passo 1), listar os arquivos da pasta e deletar arquivos.
// ano, mês, dia,T, hora, minuto - NOME+SOBRENOME em maiúsculo.
public class PetRepository {




    public static void exibirMenu() {
        System.out.println("\n--- MENU PET SHOP 2026 ---");
        System.out.println("1. Cadastrar novo pet");
        System.out.println("2. Alterar dados do pet");
        System.out.println("3. Deletar pet");
        System.out.println("4. Listar todos os pets");
        System.out.println("5. Listar por critério");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }
// ano, mês, dia,T, hora, minuto - NOME+SOBRENOME em maiúsculo.

    public static void cadastrarPet(Scanner sc, Pet pet) {
        System.out.print("Digite o nome do pet: ");
        pet.setNome(sc.nextLine());

        System.out.print("Qual o tipo do pet (CACHORRO/GATO)? ");
        pet.setTipopet(TipoPet.valueOf(sc.nextLine()));

        System.out.print("Qual o sexo do pet (FEMEA/MACHO)? ");
        pet.setSexoPet(SexoPet.valueOf(sc.nextLine()));


        System.out.print("Qual a idade: ");
        pet.setIdade(sc.nextInt());
        sc.nextLine();

        System.out.print("Qual o endereço: ");

//        pet.setEndereco(sc.nextLine());


        try {
            Path diretorioBase = Path.of("petsCadastrados");
            Files.createDirectories(diretorioBase);

            Path caminhoFinal = diretorioBase.resolve("petsCadastrados" +  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + pet.getNome().toUpperCase());


            Files.writeString(caminhoFinal,"1 - "+pet.getNome()+"\n"+"2 - "+pet.getTipopet()+"\n"+"3 - "+pet.getSexoPet()+"\n"+
                    "4 - "+pet.getEndereco()+"\n"+"5 - "+pet.getIdade()+" anos"+"\n"+
                    "6 - "+pet.getPeso()+"kg"+"\n"+"7 - "+pet.getTipopet());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Pet: " + pet.getNome() + " cadastrado!");
    }
//1 - Florzinha da Silva
//2 - Gato
//3 - Femea
//4 - Rua 2, 456, Seilandia
//5 - 6 anos
//6 - 5kg
//7 - Siames

    public static class listarTodos extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path arquivo, BasicFileAttributes attrs) {
            // Filtra apenas arquivos .txt
            if (arquivo.toString().endsWith(".txt")) {
                System.out.println("--- Lendo: " + arquivo.getFileName() + " ---");
                try {
                    List<String> linhas = Files.readAllLines(arquivo);
                    linhas.forEach(System.out::println);
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo: " + arquivo);
                }
                System.out.println("-------------------------------\n");
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.err.println("Não foi possível acessar: " + file);
            return FileVisitResult.CONTINUE;
        }

        public static void main(String[] args) throws IOException {

        }
    }




    public static void alterarPet(Scanner sc) {
        System.out.println("O que você gostaria de alterar?");
        System.out.print("1 - NOME");
        System.out.print("2 - ");
        System.out.print("Qual o sexo do pet (FEMEA/MACHO)? ");
        System.out.print("Qual a idade: ");
        sc.nextLine();
        System.out.print("Qual o endereço: ");
//        pet.setNome(sc.nextLine());

    }
    public static void deletarPet(Scanner sc) { /* ... */ }
    public static void buscarPet(Scanner sc) {

    }


}
