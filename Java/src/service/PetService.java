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
import java.util.*;
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
        if (pet.getNome() == null || pet.getNome().isBlank()) {
            pet.setNome(constante);
        }

        if (!pet.getNome().matches(regex)) {
            throw new NomeInvalidoException("O nome não pode conter caracteres especiais");
        }

        if (pet.getNome().trim().split("\\s+").length < 2) {
            throw new NomeInvalidoException("Precisa ter nome e sobrenome");
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
            double idadeMeses = pet.getIdade() * 10;
            petIdade = pet.getIdade() + " Meses";

        }else{
             petIdade = pet.getIdade() +" anos";

        }
        sc.nextLine();




        System.out.print("Qual o endereço (rua primeiro): ");
        Endereco endereco = new Endereco("",0,"","");
        endereco.setRua(sc.nextLine());
        if(endereco.getRua() == null){
            endereco.setRua(constante);
        }

        System.out.println("Qual o número?");
        endereco.setNumero(sc.nextInt());
        sc.nextLine();


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
        sc.nextLine();



        System.out.println("Qual a raça: ");
        pet.setRaca(sc.nextLine());
        if(pet.getRaca() == null){
            pet.setRaca(constante);
        }


        try {
            Path diretorioBase = Path.of("petsCadastrados");
            Files.createDirectories(diretorioBase);

            Path caminhoFinal = diretorioBase.resolve("petsCadastrados" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + pet.getNome().toUpperCase()+".txt");


            Files.writeString(caminhoFinal, "1 - " + pet.getNome() + "\n" + "2 - " + pet.getTipopet() + "\n" + "3 - " + pet.getSexoPet() + "\n" +
                    "4 - " + endereco.getRua() + "," +
                    endereco.getNumero() + "," +
                    endereco.getBairro() + "," +
                    endereco.getCidade()

                    + "\n" + "5 - " + petIdade + "\n" +
                    "6 - " + pet.getPeso() + "kg" + "\n" + "7 - " + pet.getRaca());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Pet: " + pet.getNome() + " cadastrado!");
    }


    public List<Pet> buscarPets(TipoPet tipoSelecionado, String nome, String raca, Integer idade, SexoPet sexo) {
        List<Pet> todosOsPets = carregarPetsDoBanco();

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

    public String deletarPet(TipoPet tipoSelecionado, String nome, String raca, Integer idade, SexoPet sexo) {
        List<Pet> todosOsPets = carregarPetsDoBanco();

        List<Pet> collect = todosOsPets.stream()
                .filter(p -> p.getTipopet() == tipoSelecionado)

                .filter(p -> nome == null || nome.isBlank() || p.getNome().toUpperCase().contains(nome.toUpperCase()))
                .filter(p -> raca == null || raca.isBlank() || p.getRaca().equalsIgnoreCase(raca))
                .filter(p -> idade == null || idade <= 0 || p.getIdade() == idade)
                .filter(p -> sexo == null || p.getSexoPet() == sexo)

                .collect(Collectors.toList());


        try {
            Files.delete(Path.of("./petsCadastrados"+collect));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return "Arquivo Apagado";
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

            String nome = linhas.get(0).split(" - ", 2)[1].trim();

            TipoPet tipo = TipoPet.valueOf(
                    linhas.get(1).split(" - ", 2)[1].trim()
            );

            SexoPet sexo = SexoPet.valueOf(
                    linhas.get(2).split(" - ", 2)[1].trim()
            );

            String enderecoLinha = linhas.get(3).split(" - ", 2)[1].trim();
            String[] dadosEndereco = enderecoLinha.split(",");

            Endereco endereco;
            if (dadosEndereco.length >= 4) {
                String rua = dadosEndereco[0].trim();
                int numero = Integer.parseInt(dadosEndereco[1].trim());
                String bairro = dadosEndereco[2].trim();
                String cidade = dadosEndereco[3].trim();

                endereco = new Endereco(rua, numero, bairro, cidade);
            } else {
                endereco = new Endereco(enderecoLinha, 0, "Não informado", "Não informado");
            }

            String idadeStr = linhas.get(4).split(" - ", 2)[1]
                    .replaceAll("[^0-9]", "")
                    .trim();

            double idade = Double.parseDouble(idadeStr);



            String pesoStr = linhas.get(5)
                    .split(" - ", 2)[1]
                    .toLowerCase()
                    .replace("kg", "")
                    .replace(",", ".")
                    .trim();

            double peso = Double.parseDouble(pesoStr);


            // Raça
            String raca = linhas.get(6).split(" - ", 2)[1].trim();

            return new Pet(nome, idade, peso, tipo, sexo, endereco, raca);

        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo: " + caminhoArquivo);
            return null;
        }
    }


    public List<Pet> carregarPetsDoBanco() {
        List<Pet> pets = new ArrayList<>();

        Path pasta = Paths.get("petsCadastrados");

        try (Stream<Path> caminhos = Files.walk(pasta)) {
            pets = caminhos
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .map(this::converterArquivoParaPet)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivos: " + e.getMessage());
        }
        return pets;
    }

    public Map<Path,Pet> carregatPetsDoBancoMap(){
        Map<Path, Pet> hashMap = new HashMap<>();
        Path pasta = Paths.get("petsCadastrados");

        try(Stream<Path> caminhos = Files.walk(pasta)){

        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }


    return hashMap;
    }
    }

