package service;

import dominio.Endereco;
import dominio.Pet;
import dominio.SexoPet;
import dominio.TipoPet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class PetService {

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

        System.out.print("Qual o endereço (rua primeiro): ");
        Endereco endereco = new Endereco("",0,"","");
        endereco.setRua(sc.nextLine());

        System.out.println("Qual o número?");
        endereco.setNumero(sc.nextInt());

        System.out.println("Qual o bairro?");
        endereco.setBairro(sc.nextLine());

        System.out.println("Qual a cidade?");
        endereco.setCidade(sc.nextLine());

        pet.setEndereco(endereco);



        System.out.println("Qual a raça: ");
        pet.setRaca(sc.nextLine());
//        pet.setEndereco(sc.nextLine());


        try {
            Path diretorioBase = Path.of("petsCadastrados");
            Files.createDirectories(diretorioBase);

            Path caminhoFinal = diretorioBase.resolve("petsCadastrados" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + pet.getNome().toUpperCase());


            Files.writeString(caminhoFinal, "1 - " + pet.getNome() + "\n" + "2 - " + pet.getTipopet() + "\n" + "3 - " + pet.getSexoPet() + "\n" +
                    "4 - " + pet.getEndereco().toString().replace("{"," ").replace("}","")
                    + "\n" + "5 - " + pet.getIdade() + " anos" + "\n" +
                    "6 - " + pet.getPeso() + "kg" + "\n" + "7 - " + pet.getTipopet());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Pet: " + pet.getNome() + " cadastrado!");
    }


    public List<Pet> buscarPets(String nome, String raca, Integer idade, SexoPet sexo) {
        List<Pet> todosOsPets = buscarTodos();

        return todosOsPets.stream()
                .filter(p -> p.getTipopet().equals(TipoPet.CACHORRO))
                .filter(p -> nome == null || nome.isBlank() || p.getNome().toUpperCase().contains(nome.toUpperCase())) // Contains para busca parcial [2]
                .filter(p -> raca == null || raca.isBlank() || p.getRaca().equalsIgnoreCase(raca))
                .filter(p -> idade == null || p.getIdade() == idade)
                .filter(p -> sexo == null || p.getSexoPet().equals(sexo))
                .collect(Collectors.toList());
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
        if (s.equals("SIM")) {

        }


        sc.nextLine();
        System.out.print("Qual o endereço: ");
//        pet.setNome(sc.nextLine());

    }

    public static void deletarPet(Scanner sc) {
    }


    public List<Pet> buscarTodos() {
        Path pastaInicial = Paths.get("./petsCadastrados");

        try (Stream<Path> streamDeArquivos = Files.walk(pastaInicial)) {
            return streamDeArquivos
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))

                    .map(this::converterArquivoParaPet)

                    .filter(Objects::nonNull)

                    .collect(Collectors.toList());

        } catch (IOException e) {
            return List.of();
        }
    }

        public Pet converterArquivoParaPet(Path caminhoArquivo){
            try {
                List<String> linhas = Files.readAllLines(caminhoArquivo);


                String nomeCompleto = linhas.get(0).split("-")[1].trim();
                String tipo = linhas.get(1).split("-")[1].trim();
                String sexo = linhas.get(2).split("-")[1].trim();
                String idade = linhas.get(3).split("-")[1].trim();
                String endereco = linhas.get(4).split("-")[1].trim();
                String raca = linhas.get(5).split("-")[1].trim();


                return new Pet(nomeCompleto, tipo, sexo, idade, endereco, raca);

            } catch (IOException e) {
                System.err.println("Erro ao ler arquivo: " + caminhoArquivo);
                return null;
            }
        }
    }

