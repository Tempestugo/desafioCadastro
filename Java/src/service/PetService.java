package service;

import dominio.Pet;
import dominio.SexoPet;
import repositorioo.PetRepository;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

//        ▪ Método para cadastrar: Verifica se o peso está entre 0.5kg e 60kg, se a idade é válida. Se passar, chama o PetRepository.salvar().
//        ▪ Método de busca: Recebe os critérios, pede ao repositório a lista de todos e filtra (lógica do Passo 5).
//        ▪ Lógica para formatar o nome do arquivo (20231101T...) antes de mandar salvar.
public class PetService {

    public List<Pet> buscarPets(String nome, String raca, Integer idade, SexoPet sexo) {
        PetRepository petRepository = new PetRepository();
        List<Pet> todosOsPets = petRepository.buscarTodos();

        return todosOsPets.stream()
                .filter(p -> nome == null || nome.isEmpty() || p.getNome().equalsIgnoreCase(nome))
                .filter(p -> raca == null || raca.isEmpty() || p.getRaca().equalsIgnoreCase(raca))
                .filter(p -> idade == null || idade <= 0)
                .filter(p -> sexo == null || p.getSexoPet().equals(sexo))
                .collect(Collectors.toList());
    }
}
