package service;

import dominio.Endereco;
import dominio.Pet;
import dominio.SexoPet;
import dominio.TipoPet;
import exceptions.IdadeInvalidaException;
import exceptions.NomeInvalidoException;
import exceptions.PesoInvalidoException;

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
    private static final String constante = "NÃO_INFORMADO";

    public String getConstante() {
        return constante;
    }

    public static void cadastrarPet(Scanner sc, Pet pet) {


        System.out.print("Digite o nome do pet: ");
        pet.setNome(sc.nextLine());
        String regex = "^[\\p{L}]+(\\s+[\\p{L}]+)+$";
        boolean valido = pet.getNome().matches(regex);
        if(pet.getNome().trim().split("\\s+").length >= 2 ){
            throw new NomeInvalidoException("Precisa ter nome e sobrenome");
        }
        if(valido == true){
            throw new NomeInvalidoException("O nome não pode conter caracteres especiais");
        }
        if(pet.getNome() == null || pet.getNome().isBlank()){
            pet.setNome(constante);
        }

        System.out.print("Qual o tipo do pet (CACHORRO/GATO)? ");
        pet.setTipopet(TipoPet.valueOf(sc.nextLine()));




        System.out.print("Qual o sexo do pet (FEMEA/MACHO)? ");
        pet.setSexoPet(SexoPet.valueOf(sc.nextLine()));



        String petIdade = null;
        System.out.print("Qual a idade: ");
        pet.setIdade(sc.nextInt());
        if(pet.getIdade() > 20){
            throw new IdadeInvalidaException("A idade é inválida, maior que 20");
        }
        if(pet.getIdade() < 1){
            int idadeMeses = pet.getIdade() * 10;
            petIdade = pet.getIdade() + " Meses";

        }else{
             petIdade = pet.getIdade() +" anos";

        }



        System.out.print("Qual o endereço (rua primeiro): ");
        Endereco endereco = new Endereco("",0,"","");
        endereco.setRua(sc.nextLine());
        if(endereco.getRua() == null){
            endereco.setRua(constante);
        }

        System.out.println("Qual o número?");
        endereco.setNumero(sc.nextInt());

        System.out.println("Qual o bairro?");
        endereco.setBairro(sc.nextLine());

        System.out.println("Qual a cidade?");
        endereco.setCidade(sc.nextLine());

        pet.setEndereco(endereco);


        System.out.println("Qual o peso do pet? ");
        pet.setPeso(sc.nextInt());
        if(pet.getPeso() > 60 || pet.getPeso() <0.5){
            throw new PesoInvalidoException("O peso é maior que 60 ou menor que 0.5");
        }


        System.out.println("Qual a raça: ");
        pet.setRaca(sc.nextLine());
        if(pet.getRaca() == null){
            pet.setRaca(constante);
        }


        try {
            Path diretorioBase = Path.of("petsCadastrados");
            Files.createDirectories(diretorioBase);

            Path caminhoFinal = diretorioBase.resolve("petsCadastrados" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + pet.getNome().toUpperCase());


            Files.writeString(caminhoFinal, "1 - " + pet.getNome() + "\n" + "2 - " + pet.getTipopet() + "\n" + "3 - " + pet.getSexoPet() + "\n" +
                    "4 - " + pet.getEndereco().toString().replace("{"," ").replace("}","")
                    + "\n" + "5 - " + petIdade + " anos" + "\n" +
                    "6 - " + pet.getPeso() + "kg" + "\n" + "7 - " + pet.getRaca());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Pet: " + pet.getNome() + " cadastrado!");
    }


    public List<Pet> buscarPets(TipoPet tipoSelecionado, String nome, String raca, Integer idade, SexoPet sexo) {
        List<Pet> todosOsPets = buscarTodos();

        return todosOsPets.stream()
                .filter(p -> p.getTipopet() == tipoSelecionado)

                .filter(p -> nome == null || nome.isBlank() || p.getNome().toUpperCase().contains(nome.toUpperCase()))
                .filter(p -> raca == null || raca.isBlank() || p.getRaca().equalsIgnoreCase(raca))
                .filter(p -> idade == null || idade <= 0 || p.getIdade() == idade)
                .filter(p -> sexo == null || p.getSexoPet() == sexo)

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

    public Pet converterArquivoParaPet(Path caminhoArquivo) {
        try {
            List<String> linhas = Files.readAllLines(caminhoArquivo);

            if (linhas.size() < 7) return null;

            // 1 - Nome (Index 0)
            String nome = linhas.get(0).split("-", 2)[2].trim();

            String tipoStr = linhas.get(1).split("-", 2)[2].trim();
            TipoPet tipo = TipoPet.valueOf(tipoStr);

            String sexoStr = linhas.get(2).split("-", 2)[2].trim();
            SexoPet sexo = SexoPet.valueOf(sexoStr);

            String endereco = linhas.get(3).split("-", 2)[2].trim();
            String[] dadosEndereco = endereco.split(",");
            Endereco enderecoObj;
            if (dadosEndereco.length >= 3) {
                String rua = dadosEndereco.toString().trim();
                String numero = dadosEndereco[3].trim();
                String bairroCidade = dadosEndereco[4].trim();

                enderecoObj = new Endereco(rua, numero, bairroCidade);
            } else {
                enderecoObj = new Endereco(endereco, null, "Não informado");
            }





            String idadeStr = linhas.get(4).split("-", 2)[2]
                    .replace(" anos", "")
                    .trim();
            int idade = Integer.parseInt(idadeStr);

            String pesoStr = linhas.get(5).split("-", 2)[2].replace("kg", "").trim();
            double peso = Double.parseDouble(pesoStr);

            String raca = linhas.get(6).split("-", 2)[2].trim();

            return new Pet(nome,idade,peso,tipo,sexo,enderecoObj,raca);

        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo: " + caminhoArquivo + " | " + e.getMessage());
            return null;
        }
    }
    }

