package repositorioo;


import dominio.Pet;
import dominio.SexoPet;
import dominio.TipoPet;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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

        System.out.println("Qual a raça: ");
        pet.setRaca(sc.nextLine());
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
        String opcao = "";
//        do {
            System.out.println("O que você gostaria de alterar?");
            System.out.print("1 - NOME");
            System.out.print("2 - TIPO");
            System.out.print("3- SEXO (FEMEA/MACHO)? ");
            System.out.print("4 - IDADE");
            System.out.println("5 - ENDERECO");

//
//            switch(opcao){
//                case 1:
//                    pet.setNome(sc.nextLine());
//
//
//            }
//        }


        System.out.println("Qual o próximo item?");

        String s = sc.nextLine().toUpperCase();
        if(s.equals("SIM")){

        }



        sc.nextLine();
        System.out.print("Qual o endereço: ");
//        pet.setNome(sc.nextLine());

    }
    public static void deletarPet(Scanner sc) { }



//    Nome ou sobrenome
//            Sexo
//    Idade
//            Peso
//    Raça
//            Endereço

//    Nome e / ou sobrenome E IDADE
//    Idade E peso



//
//    O usuário PRIMEIRAMENTE e SEMPRE deverá escolher o critério TIPO DE ANIMAL.
//    O formato de resposta, deverá exibir SEMPRE uma lista de possiveis resultados, por exemplo:
//            1.  Rex - Cachorro - Macho  - Rua 1, 123 - Cidade 1 - 2 anos - 5kg - Vira-lata
//2.  Florzinha da Silva - Gato - Femea - Rua 2, 456 - Seilandia - 6 anos - 5kg - Siames
//    Caso o usuário escolha por exemplo, NOME, os resultados da busca devem trazer PARTES do nome, por exemplo, caso ele pesquise por FLOR, deverá trazer o caso 2 citado anteriormente.
//    Toda busca deverá ser case-sensitive, ou seja, ignorar maiuscula e minuscula (tratando como iguais) e acentos.
    public static void buscarPet(Scanner sc) {
        String opcao = "";


//        do{
//            System.out.println("Você gostaria de buscar por qual critério?");
//            System.out.print("1 - NOME");
//            System.out.print("2 - TIPO");
//            System.out.print("3- SEXO (FEMEA/MACHO)? ");
//            System.out.print("4 - IDADE");
//            System.out.println("5 - ENDERECO");
//
//                        switch(opcao){
//                            case 1:
//
//
//
//            }
//
//
//        }
        }
    public static void buscarPets(List<Pet> listaOriginal, String nome, String raca, Integer idade, SexoPet sexo) {
        List<Pet> listaDePets;





        List<Pet> resultado = listaOriginal.stream()
                .filter(p -> nome == null || nome.isEmpty() || p.getNome().equalsIgnoreCase(nome))

                .filter(p -> raca == null || raca.isEmpty() || p.getRaca().equalsIgnoreCase(raca))

                .filter(p -> idade == null || idade <= 0 || p.getIdade() == idade)

                .filter(p -> sexo == null || sexo.describeConstable().isEmpty()  || !p.getSexoPet().equals(sexo))

                .toList();

        if (resultado.isEmpty()) {
            System.out.println("Nenhum pet encontrado com esses critérios.");
        } else {
            resultado.forEach(p -> System.out.println("Encontrado: " + p.getNome() + " - " + p.getRaca()));
        }
    }


    }



