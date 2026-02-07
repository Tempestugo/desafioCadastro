package service;

import dominio.Endereco;
import dominio.Pet;
import dominio.SexoPet;
import dominio.TipoPet;
import exceptions.IdadeInvalidaException;
import exceptions.NomeInvalidoException;
import exceptions.PesoInvalidoException;
import test.ConnectionFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String regex ="^[\\p{L}]+(\\s+[\\p{L}]+)+$";
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
        if (pet.getIdade() > 20) {
            throw new IdadeInvalidaException("A idade é inválida, maior que 20");
        }
        if (pet.getIdade() < 1) {
            double idadeMeses = pet.getIdade() * 10;
            petIdade = pet.getIdade() + " Meses";

        } else {
            petIdade = pet.getIdade() + " anos";

        }
        sc.nextLine();


        System.out.print("Qual o endereço (rua primeiro): ");
        Endereco endereco = new Endereco("", 0, "", "");
        endereco.setRua(sc.nextLine());
        if (endereco.getRua() == null) {
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
        if (pet.getPeso() > 60 || pet.getPeso() < 0.5) {
            throw new PesoInvalidoException("O peso é maior que 60 ou menor que 0.5");
        }
        sc.nextLine();


        System.out.println("Qual a raça: ");
        pet.setRaca(sc.nextLine());
        if (pet.getRaca() == null) {
            pet.setRaca(constante);
        }


        try {
            Path diretorioBase = Path.of("petsCadastrados");
            Files.createDirectories(diretorioBase);

            Path caminhoFinal = diretorioBase.resolve("petsCadastrados" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + pet.getNome().toUpperCase() + ".txt");


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


        String sql = """ 
                     INSERT INTO anime_store.pets (nome, SexoPet,TipoPet, Raca, Endereco, Idade, PESO) 
                     VALUES (?,?,?,?,?,?,?);
                     """;
        try(Connection connection = ConnectionFactory.getConncection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, pet.getNome());
            preparedStatement.setString(2, pet.getSexoPet().toString());
            preparedStatement.setString(3, pet.getTipopet().toString());
            preparedStatement.setString(4, pet.getRaca());
            preparedStatement.setString(5, pet.getEndereco().getRua()); 
            
            preparedStatement.setDouble(6, pet.getIdade());
            preparedStatement.setDouble(7, pet.getPeso());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        System.out.println("Pet: " + pet.getNome() + " cadastrado!");
    }



    public List<Pet> buscarPets(TipoPet tipoSelecionado, String nome, String raca, Integer idade, SexoPet sexo) {
        List<Pet> petsEncontrados = new ArrayList<>();
        
        String sql = "SELECT * FROM anime_store.pets WHERE 1=1";
        
        if (tipoSelecionado != null) sql += " AND TipoPet = ?";
        if (nome != null) sql += " AND nome LIKE ?";
        if (raca != null) sql += " AND Raca LIKE ?";
        if (idade != null) sql += " AND Idade = ?";
        if (sexo != null) sql += " AND SexoPet = ?";

        try (Connection connection = ConnectionFactory.getConncection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            int index = 1;
            if (tipoSelecionado != null) ps.setString(index++, tipoSelecionado.toString());
            if (nome != null) ps.setString(index++, "%" + nome + "%");
            if (raca != null) ps.setString(index++, "%" + raca + "%");
            if (idade != null) ps.setDouble(index++, idade);
            if (sexo != null) ps.setString(index++, sexo.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setNome(rs.getString("nome"));
                pet.setTipopet(TipoPet.valueOf(rs.getString("TipoPet")));
                pet.setSexoPet(SexoPet.valueOf(rs.getString("SexoPet")));
                pet.setRaca(rs.getString("Raca"));
                pet.setIdade(rs.getDouble("Idade"));
                pet.setPeso(rs.getDouble("PESO"));
                pet.setEndereco(new Endereco(rs.getString("Endereco"), 0, "", ""));
                
                petsEncontrados.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return petsEncontrados;
    }


    public void deletarPet(Scanner sc) {
        Map.Entry<Path, Pet> entry = buscarESelecionarPetParaEdicao(sc);
        if (entry == null) return;

        try {
            Files.delete(entry.getKey());
            System.out.println("Pet deletado com sucesso.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String sql = """ 
                                    DELETE FROM anime_store.pets
                                    WHERE (? IS NULL OR nome = ?)
                                      AND (? IS NULL OR SexoPet = ?)
                                      AND (? IS NULL OR TipoPet = ?)
                                      AND (? IS NULL OR Raca = ?)
                                      AND (? IS NULL OR Endereco = ?)
                                      AND (? IS NULL OR Idade = ?)
                                      AND (? IS NULL OR PESO = ?);
                                                         
                     """;
        try(Connection connection = ConnectionFactory.getConncection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, entry.getValue().getNome());
            preparedStatement.setString(2, entry.getValue().getNome());
            preparedStatement.setString(3, entry.getValue().getSexoPet().toString());
            preparedStatement.setString(4, entry.getValue().getSexoPet().toString());
            preparedStatement.setString(5, entry.getValue().getTipopet().toString());
            preparedStatement.setString(6, entry.getValue().getTipopet().toString());
            preparedStatement.setDouble(7,entry.getValue().getPeso());
            preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public List<Pet> buscarTodos() {
        String sql = "SELECT * FROM anime_store.pets;";
        try(Connection connection = ConnectionFactory.getConncection();
            PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nome = rs.getString("nome");
                String sexo = rs.getString("SexoPet");
                String tipo = rs.getString("TipoPet");
                String raca = rs.getString("Raca");
                double peso = rs.getDouble("PESO");

                System.out.println(String.format("Nome: %s | Tipo: %s | Raça: %s | Peso: %.2fkg",
                        nome, tipo, raca, peso));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

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


            String raca = linhas.get(6).split(" - ", 2)[1].trim();

            return new Pet(nome, idade, peso, tipo, sexo, endereco, raca);

        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo: " + caminhoArquivo);
            return null;
        }
    }


    public List<Map.Entry<Path, Pet>> buscarPetsParaEdicao(
            TipoPet tipoSelecionado,
            String nome,
            String raca,
            Integer idade,
            SexoPet sexo
    ) {

        Map<Path, Pet> todosPets = carregatPetsDoBancoMap();

        return todosPets.entrySet()
                .stream()
                .filter(e -> tipoSelecionado == null
                        || e.getValue().getTipopet() == tipoSelecionado)
                .filter(e -> nome == null || nome.isBlank()
                        || e.getValue().getNome()
                        .toUpperCase()
                        .contains(nome.toUpperCase()))
                .filter(e -> raca == null || raca.isBlank()
                        || e.getValue().getRaca()
                        .equalsIgnoreCase(raca))
                .filter(e -> idade == null || idade <= 0
                        || e.getValue().getIdade() == idade)
                .filter(e -> sexo == null
                        || e.getValue().getSexoPet() == sexo)
                .collect(Collectors.toList());
    }

    public Map.Entry<Path, Pet> buscarESelecionarPetParaEdicao(Scanner sc) {

        List<Map.Entry<Path, Pet>> candidatos;

        do {
            System.out.println("\n--- BUSCA DE PET PARA EDIÇÃO (ARQUIVO) ---");

            System.out.println("Tipo (ou Enter):");
            String tipoStr = sc.nextLine();
            TipoPet tipo = tipoStr.isBlank() ? null
                    : TipoPet.valueOf(tipoStr.toUpperCase());

            System.out.println("Nome (ou Enter):");
            String nome = sc.nextLine();
            if (nome.isBlank()) nome = null;

            System.out.println("Raça (ou Enter):");
            String raca = sc.nextLine();
            if (raca.isBlank()) raca = null;

            System.out.println("Idade (ou Enter):");
            String idadeStr = sc.nextLine();
            Integer idade = idadeStr.isBlank() ? null : Integer.parseInt(idadeStr);

            System.out.println("Sexo (ou Enter):");
            String sexoStr = sc.nextLine();
            SexoPet sexo = sexoStr.isBlank() ? null
                    : SexoPet.valueOf(sexoStr.toUpperCase());

            candidatos = buscarPetsParaEdicao(tipo, nome, raca, idade, sexo);

            if (candidatos.isEmpty()) {
                System.out.println("Nenhum pet encontrado. Tentar novamente? (s/n)");
                if (!sc.nextLine().equalsIgnoreCase("s")) {
                    return null;
                }
            }

        } while (candidatos.isEmpty());

        int opcao;
        do {
            for (int i = 0; i < candidatos.size(); i++) {
                Pet p = candidatos.get(i).getValue();
                System.out.println(i + " - " + p.getNome() + " - " + p.getTipopet());
            }

            System.out.println("Escolha o número:");
            while (!sc.hasNextInt()) {
                System.out.println("Digite um número válido.");
                sc.nextLine();
            }

            opcao = sc.nextInt();
            sc.nextLine();

            if (opcao < 0 || opcao >= candidatos.size()) {
                System.out.println("Número inválido.");
            }

        } while (opcao < 0 || opcao >= candidatos.size());

        return candidatos.get(opcao);
    }

    public Map<Path, Pet> carregatPetsDoBancoMap() {
        Map<Path, Pet> hashMap = new HashMap<>();
        Path pasta = Paths.get("petsCadastrados");

        try (Stream<Path> caminhos = Files.walk(pasta)) {
            caminhos
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(path -> {
                        Pet pet = converterArquivoParaPet(path);
                        if (pet != null) {
                            hashMap.put(path, pet);

                        }

                    });

            return hashMap;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        return hashMap;
    }


    public void alterarPet2(Scanner sc) {
        Map.Entry<Path, Pet> entry = buscarESelecionarPetParaEdicao(sc);
        if (entry == null) return;

        Pet pet = entry.getValue();

        System.out.println("Editando Pet: " + pet.getNome());

        System.out.println("Digite o NOME (ou Enter para ignorar):");
        String nome2 = sc.nextLine();
        if (!nome2.isBlank()){
            pet.setNome(nome2);
        }

        System.out.println("Digite o Tipo (ou Enter para ignorar):");
        String tipoStr2 = sc.nextLine();

        if (!tipoStr2.isBlank()) {
            TipoPet tipoPet = TipoPet.valueOf(tipoStr2.toUpperCase());
            pet.setTipopet(tipoPet);
        }

        System.out.println("Digite o novo sexo do pet (ou Enter para ignorar");
        String sexoPetStr = sc.nextLine();
        if(!sexoPetStr.isBlank()){
            SexoPet sexoPet = SexoPet.valueOf(sexoPetStr.toUpperCase());
            pet.setSexoPet(sexoPet);
        }
        
        String petIdade = null;
        System.out.println("Digite a nova Idade (ou Enter para ignorar):");
        String idadeStr2 = sc.nextLine();
        if (!idadeStr2.isBlank()) {
            pet.setIdade(Integer.parseInt(idadeStr2));
        }

        if (pet.getIdade() > 20) {
            throw new IdadeInvalidaException("A idade é inválida, maior que 20");
        }
        if (pet.getIdade() < 1) {
            petIdade = pet.getIdade() + " Meses";

        } else {
            petIdade = pet.getIdade() + " anos";

        }


        System.out.print("Qual o endereço (rua primeiro): ");
        Endereco endereco = pet.getEndereco();
        String rua = sc.nextLine();
        if (!rua.isBlank()){
            endereco.setRua(rua);
        }

        System.out.println("Qual o número? Ou enter para pular");
        String numStr = sc.nextLine();
        if (!numStr.isBlank()) {
            endereco.setNumero(Integer.parseInt(numStr));
        }


        System.out.println("Qual o bairro?");
        String bairro = sc.nextLine();
        if (!endereco.getBairro().isBlank()) {
            endereco.setBairro(bairro);
        }

        System.out.println("Qual a cidade?");
        String cidade = sc.nextLine();
        if (!endereco.getCidade().isBlank()) {
            endereco.setCidade(cidade);
        }


        pet.setEndereco(endereco);


        System.out.println("Qual o novo peso do pet? ");
        String pesoStr = sc.nextLine();
        if (!pesoStr.isBlank()) {
           pet.setPeso(Double.parseDouble(pesoStr));
        }
        if (pet.getPeso() > 60 || pet.getPeso() < 0.5) {
            throw new PesoInvalidoException("O peso é maior que 60 ou menor que 0.5");
        }


        System.out.println("Qual a raça: ");
        String raca2 = sc.nextLine();
        if(!raca2.isBlank()){
            pet.setRaca(raca2);
        }

        try {
            Files.writeString(
                    entry.getKey(),
                    "1 - " + pet.getNome() + "\n" + "2 - " + pet.getTipopet() + "\n" + "3 - " + pet.getSexoPet() + "\n" +
                            "4 - " + endereco.getRua() + "," +
                            endereco.getNumero() + "," +
                            endereco.getBairro() + "," +
                            endereco.getCidade()

                            + "\n" + "5 - " + petIdade + "\n" +
                            "6 - " + pet.getPeso() + "kg" + "\n" + "7 - " + pet.getRaca(),
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
            System.out.println("Pet atualizado no arquivo com sucesso!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public Pet buscarESelecionarPetSQL(Scanner sc) {
        List<Pet> candidatos;

        do {
            System.out.println("\n--- BUSCA DE PET PARA EDIÇÃO (SQL) ---");

            System.out.println("Tipo (ou Enter):");
            String tipoStr = sc.nextLine();
            TipoPet tipo = tipoStr.isBlank() ? null : TipoPet.valueOf(tipoStr.toUpperCase());

            System.out.println("Nome (ou Enter):");
            String nome = sc.nextLine();
            if (nome.isBlank()) nome = null;

            System.out.println("Raça (ou Enter):");
            String raca = sc.nextLine();
            if (raca.isBlank()) raca = null;

            System.out.println("Idade (ou Enter):");
            String idadeStr = sc.nextLine();
            Integer idade = idadeStr.isBlank() ? null : Integer.parseInt(idadeStr);

            System.out.println("Sexo (ou Enter):");
            String sexoStr = sc.nextLine();
            SexoPet sexo = sexoStr.isBlank() ? null : SexoPet.valueOf(sexoStr.toUpperCase());

            candidatos = buscarPets(tipo, nome, raca, idade, sexo);

            if (candidatos.isEmpty()) {
                System.out.println("Nenhum pet encontrado no banco. Tentar novamente? (s/n)");
                if (!sc.nextLine().equalsIgnoreCase("s")) {
                    return null;
                }
            }

        } while (candidatos.isEmpty());

        int opcao;
        do {
            for (int i = 0; i < candidatos.size(); i++) {
                Pet p = candidatos.get(i);
                System.out.println(i + " - " + p.getNome() + " - " + p.getTipopet() + " - " + p.getRaca());
            }

            System.out.println("Escolha o número:");
            while (!sc.hasNextInt()) {
                System.out.println("Digite um número válido.");
                sc.nextLine();
            }

            opcao = sc.nextInt();
            sc.nextLine();

            if (opcao < 0 || opcao >= candidatos.size()) {
                System.out.println("Número inválido.");
            }

        } while (opcao < 0 || opcao >= candidatos.size());

        return candidatos.get(opcao);
    }

    public void alterarPetSQL(Scanner sc) {

        Pet pet = buscarESelecionarPetSQL(sc);
        if (pet == null) return;
        String nomeAntigo = pet.getNome();

        System.out.println("Editando Pet (SQL): " + pet.getNome());

        System.out.println("Novo NOME (ou Enter para manter '" + pet.getNome() + "'):");
        String nomeNovo = sc.nextLine();
        if (!nomeNovo.isBlank()) pet.setNome(nomeNovo);

        System.out.println("Novo TIPO (ou Enter para manter '" + pet.getTipopet() + "'):");
        String tipoStr = sc.nextLine();
        if (!tipoStr.isBlank()) pet.setTipopet(TipoPet.valueOf(tipoStr.toUpperCase()));

        System.out.println("Novo SEXO (ou Enter para manter '" + pet.getSexoPet() + "'):");
        String sexoStr = sc.nextLine();
        if (!sexoStr.isBlank()) pet.setSexoPet(SexoPet.valueOf(sexoStr.toUpperCase()));

        System.out.println("Nova IDADE (ou Enter para manter '" + pet.getIdade() + "'):");
        String idadeStr = sc.nextLine();
        if (!idadeStr.isBlank()) pet.setIdade(Double.parseDouble(idadeStr));

        System.out.println("Novo PESO (ou Enter para manter '" + pet.getPeso() + "'):");
        String pesoStr = sc.nextLine();
        if (!pesoStr.isBlank()) pet.setPeso(Double.parseDouble(pesoStr));

        System.out.println("Nova RAÇA (ou Enter para manter '" + pet.getRaca() + "'):");
        String racaStr = sc.nextLine();
        if (!racaStr.isBlank()) pet.setRaca(racaStr);

        System.out.println("Novo ENDEREÇO (Rua) (ou Enter para manter):");
        String endStr = sc.nextLine();
        if (!endStr.isBlank()) pet.getEndereco().setRua(endStr);


        String sql = """
                UPDATE anime_store.pets 
                SET nome = ?, TipoPet = ?, SexoPet = ?, Idade = ?, PESO = ?, Raca = ?, Endereco = ?
                WHERE nome = ?
                """;

        try (Connection conn = ConnectionFactory.getConncection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pet.getNome());
            ps.setString(2, pet.getTipopet().toString());
            ps.setString(3, pet.getSexoPet().toString());
            ps.setDouble(4, pet.getIdade());
            ps.setDouble(5, pet.getPeso());
            ps.setString(6, pet.getRaca());
            ps.setString(7, pet.getEndereco().getRua());


            ps.setString(8, nomeAntigo);

            int linhasAfetadas = ps.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Pet atualizado com sucesso no banco de dados!");
            } else {
                System.out.println("Erro: Nenhum pet foi atualizado. Verifique se o nome mudou no banco concorrentemente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao atualizar pet no banco", e);
        }
    }
}
